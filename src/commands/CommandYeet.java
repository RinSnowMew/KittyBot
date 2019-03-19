package commands;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import core.Command;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.ImageUtils;

public class CommandYeet extends Command
{
	
	// Required constructor
	public CommandYeet(KittyRole level, KittyRating rating) { super(level, rating); }
	
	private static Long num = 0l;
	
	@Override
	public String HelpText() { return "Yeet yourself or yeet a friend with @!"; }
	
	// Called when the command is run!
	@Override 
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String name = null;
		File yeetFile = null;
		File yeeteeFile = null;
		
		synchronized(num)
		{
			name = "yeet_" + num + ".gif";
			++num;
		}
		
		try 
		{
			KittyUser person = null;
			
			if(input.mentions == null)
			{
				person = user; 
			}
			else
			{
				person = input.mentions[0];
			}
				
			String yeeteeFilename = ImageUtils.DownloadFromURL(person.avatarID, ".png");
			if(yeeteeFilename == null)
				return;
			
			yeeteeFile = new File(yeeteeFilename);
			YEET(ImageIO.read(yeeteeFile), name);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		yeetFile = new File (name);
		res.CallFile(yeetFile, "gif");

		// Thread cleanup...
		ImageUtils.BlockingFileDelete(yeetFile);
		ImageUtils.BlockingFileDelete(yeeteeFile);
	}
	
	// Lets get some trash established for the yeet
	public static final boolean VERBOSE = false;
	public static final int NONE = -1;
	public static final int PIXEL_BYTE_LENGTH = 4; // 32-bit PNG, RGBA
	public static final String YEET_BASE_PATH = "assets/yeet/frames/";
	public static final int YEET_BASE_SIZE = 24;
	public static final int YEET_FPS = 18;
	
	// Some small logging functions... cause we're professionals...
	public static void Verbose(String str) { if(VERBOSE) Log("[Verbose] " + str); }
	public static void Log(String str) { System.out.println("[Log] " + str); }
	public static void Warn(String str) { System.out.println("[Warn] " + str); }
	public static void Error(String str) { System.out.println("[Error] " + str); }

	// Performs the Y E E T (image overlay per frame specified, catches IO errors as a note.)
	public static void YEET(BufferedImage overlay, String outpath)
	{
		try 
		{
			long start = Calendar.getInstance().getTimeInMillis();
			
			ProcessYeet(overlay, outpath);
			
			long end = Calendar.getInstance().getTimeInMillis();
			Log("Took " + (end - start) + "ms");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Processing the image frames to construct a gif. Relies on (0,255,0) pixel for image center specification.
	public static void ProcessYeet(BufferedImage overlay, String outfileName) throws IOException
	{
		BufferedImage[] frames = new BufferedImage[YEET_BASE_SIZE];

		for(int i = 0; i < YEET_BASE_SIZE; ++i)
		{
			BufferedImage out = CombineImages(YEET_BASE_PATH + "yeet " + i + ".png", overlay);
			
			if(out != null)
				frames[i] = out;
			else
				Error("There was an issue with frame " + i);
		}
		
		Verbose("Processed " + YEET_BASE_SIZE + " frames, writing...");
		ImageOutputStream output = new FileImageOutputStream(new File(outfileName));
		GifSequenceWriter writer = new GifSequenceWriter(output, frames[0].getType(), YEET_FPS, true);
				
		for(int i = 0; i < YEET_BASE_SIZE; ++i) 
			writer.writeToSequence(frames[i]);
		
		writer.close();
		output.close();
	}
	
	// Performs an overlay at a pixel location. We're making some assumptions here, mostly that there is going
	// to be an RGBA-32-bit encoded PNG image read in for our parsing purposes. We then look for a 0, 255, 0
	// green pixel on the base frame to apply the overlay buffer to, centered.
	public static BufferedImage CombineImages(String pathBase, BufferedImage overlay) throws IOException
	{
		// Acquire images
		BufferedImage imageBase = null;
		BufferedImage imageOverlay = overlay;
		
		imageBase = ImageIO.read(new File(pathBase));		
		
		// Grab data we know won't change. As a side note, I discovered you can get specific pixels a bit
		// differently later, but it was too late, I wrote the pixel specific code already.
		final byte[] pixelsBase = ((DataBufferByte) imageBase.getRaster().getDataBuffer()).getData();
		final int baseWidth = imageBase.getWidth();
		final int overlayWidth = imageOverlay.getWidth();
		final int baseHeight = imageBase.getHeight();
		final int overlayHeight = imageOverlay.getHeight();
		
		// Warn about odd sizing when applicable
		if(overlayHeight > baseHeight || overlayWidth > baseWidth)
			Warn("Size mismatch, overlay is larger. May function, but not supported.");
		
		// Skim base picture for solid green pixel to use as target center 
		int targetX = NONE;
		int targetY = NONE;
		for(int i = 0; i < pixelsBase.length; i += PIXEL_BYTE_LENGTH)
		{
			final int x = ((i / PIXEL_BYTE_LENGTH) % baseWidth);
			final int y = ((i / PIXEL_BYTE_LENGTH) / baseWidth);
			
			final int r = (pixelsBase[i]);
			final int g = (pixelsBase[i + 1]);
			final int b = (pixelsBase[i + 2]);
			//final int a = (pixelsBase[i + 3]);

			// Max contrast in 32-bit PNG for green is no red no blue, max green (ratio), max alpha (implied 0 here)
			if(r == -1 && g == 0 && b == -1)
			{
				//Log("Found target pixel at x: " + x + " y: " + y);
				targetX = x;
				targetY = y;
				break;
			}
		}
		
		// Skip overlay if we have no target location
		if(targetX == NONE || targetY == NONE)
		{
			Verbose("No overlay required for this frame.");
	    	return imageBase;
	    }
	    
	    // Re-create a new byte array and establish overlay bounds
	    final int left = targetX - overlayWidth / 2;
	    final int right = targetX + overlayWidth / 2;
	    final int top = targetY - overlayHeight / 2;
	    final int bottom = targetY + overlayHeight / 2;
	    
	    // Apply overlay when appropriate
	    for(int i = 0; i < pixelsBase.length; i += PIXEL_BYTE_LENGTH)
	    {
			final int x = ((i / PIXEL_BYTE_LENGTH) % baseWidth);
			final int y = ((i / PIXEL_BYTE_LENGTH) / baseWidth);
			
			// If we're within the bounds of where the overlay picture should go...
			if(x > left && x < right && y > top && y < bottom)
			{
				final int overlayX = x - left;
				final int overlayY = y - top;
				
				// Skip out of bounds pixels
				if(overlayX < 0 || overlayY < 0 || overlayX >= baseWidth || overlayY >= baseHeight)
					continue;
				
				// Re-write bytes as overlay
				imageBase.setRGB(x, y, imageOverlay.getRGB(overlayX, overlayY));
			}
		}
		
	    // Give the frame back
		return imageBase;
	}
	
	
	// GifSequenceWriter.java
	//  
	// Created by Elliot Kroo on 2009-04-25, 
	// (Small modifications by wisp in 2018)
	//
	// This work is licensed under the Creative Commons Attribution 3.0 Unported License. 
	// To view a copy of this license visit http://creativecommons.org/licenses/by/3.0/
	// NOTE(wisp): This is Elliot's original license
	public static class GifSequenceWriter 
	{
		protected ImageWriter gifWriter;
		protected ImageWriteParam imageWriteParam;
		protected IIOMetadata imageMetaData;
		  
		// NOTE: This is limited to things that divide into 1000 cleanly as multiples of 10.
		public GifSequenceWriter(ImageOutputStream outputStream, int imageType, int framesPerSecond, boolean isLooping) throws IIOException, IOException 
		{
			gifWriter = getWriter(); 
			imageWriteParam = gifWriter.getDefaultWriteParam();
			ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
			
			imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);
			
			String metaFormatName = imageMetaData.getNativeMetadataFormatName();
			
			IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
			IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
			graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
			graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
			graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
			graphicsControlExtensionNode.setAttribute("delayTime", "" + (int) ((1000.0 / framesPerSecond) / 10.0)); // THis line isn't super well honored
			graphicsControlExtensionNode.setAttribute("transparentColorIndex","0");
						
			IIOMetadataNode appEntensionsNode = getNode( root, "ApplicationExtensions");
			IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
			child.setAttribute("applicationID", "NETSCAPE");
			child.setAttribute("authenticationCode", "2.0");
			
			int loopValue = isLooping ? 0 : 1;
			child.setUserObject(new byte[]{ 0x1, (byte) (loopValue & 0xFF), (byte) ((loopValue >> 8) & 0xFF)});
			appEntensionsNode.appendChild(child);
			
			imageMetaData.setFromTree(metaFormatName, root);
			
			gifWriter.setOutput(outputStream);
			gifWriter.prepareWriteSequence(null);
		}
		
		public void writeToSequence(RenderedImage img) throws IOException 
		{
			gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam); 
		}
		
		/**
		 * Close this GifSequenceWriter object. This does not close the underlying
		 * stream, just finishes off the GIF.
		 */
		public void close() throws IOException 
		{
			gifWriter.endWriteSequence();
		}
		
		/**
		 * Returns the first available GIF ImageWriter using 
		 * ImageIO.getImageWritersBySuffix("gif").
		 * 
		 * @return a GIF ImageWriter object
		 * @throws IIOException if no GIF image writers are returned
		 */
		private static ImageWriter getWriter() throws IIOException 
		{
			Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
		
			if(!iter.hasNext()) 
				throw new IIOException("No GIF Image Writers Exist");
			else
				return iter.next();
		}
		
		/**
		 * Returns an existing child node, or creates and returns a new child node (if 
		 * the requested node does not exist).
		 * 
		 * @param rootNode the <tt>IIOMetadataNode</tt> to search for the child node.
		 * @param nodeName the name of the child node.
		 * 
		 * @return the child node, if found or a new node created with the given name.
		 */
		private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName)
		{
			int nodeCount = rootNode.getLength();
			for (int i = 0; i < nodeCount; i++)
			{
				if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0)
					return((IIOMetadataNode) rootNode.item(i));
			}
			
			IIOMetadataNode node = new IIOMetadataNode(nodeName);
			rootNode.appendChild(node);
			
			return(node);
		}
	}
}
