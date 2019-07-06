package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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

// A class that overlays an image based on a series of frames that will be converted into a gif.
// The class targets a pixel with max Green value and no red or blue that's fully opaque (0, 255, 0)
// and then places the center of the overlay image on the specified location. Must be 32-bit PNGs with RGBA format.
public class ImageOverlayBuilder 
{
	// Lets get some trash established for the yeet
	public static final boolean VERBOSE = false;
	public static final int NONE = -1;
	public static final int PIXEL_BYTE_LENGTH = 4; // 32-bit PNG, RGBA
	public static final int DEFAULT_FPS = 18;
	public static final String FRAME_FILE_EXTENSION = ".png";
	public static final String LOG_HEADER = "[Overlay] ";
	
	// Logging intermediate functions
	private static void verbose(String str) { if(VERBOSE) log("[Verbose] " + str); }
	private static void log(String str) { GlobalLog.log(LogFilter.Util, LOG_HEADER + str); }
	private static void warn(String str) { GlobalLog.warn(LogFilter.Util, LOG_HEADER + str); }
	private static void error(String str) { GlobalLog.error(LogFilter.Util, LOG_HEADER + str); }

	// Local variables
	private final String basePath; // The folder location of the files
	private final String baseName; // This is the consistent part of the filename of each frame, for example "image "
	private final int frameCount;  // The number of frames in the image
	private final int fps;         // This doesn't quite line up, multiples of 10 are as specific as it gets.
	
	// Required minimal constructor
	public ImageOverlayBuilder(String basePath, String baseName, int frameCount)
	{
		this(basePath, baseName, frameCount, DEFAULT_FPS);
	}
	
	// Constructor with framerate specification
	public ImageOverlayBuilder(String basePath, String baseName, int frameCount, int fps)
	{
		this.basePath = basePath;
		this.baseName = baseName;
		this.frameCount = frameCount;
		this.fps = fps;
	}
	
	// Performs a per-frame overlay, catches IO errors as a note.)
	public void overlay(BufferedImage overlay, String outpath)
	{
		try 
		{
			long start = Calendar.getInstance().getTimeInMillis();
			
			processOverlay(overlay, outpath);
			
			long end = Calendar.getInstance().getTimeInMillis();
			log("Took " + (end - start) + "ms");
		} 
		catch (IOException e) 
		{
			error(e.getMessage());
		}
	}
	
	// Processing the image frames to construct a gif. Relies on (0,255,0) pixel for image center specification.
	private void processOverlay(BufferedImage overlay, String outfileName) throws IOException
	{
		ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>(frameCount);
		//BufferedImage[] frames = new BufferedImage[frameCount];
		
		for(int i = 0; i < frameCount; ++i)
		{
			BufferedImage out = combineImages(basePath + baseName + i + FRAME_FILE_EXTENSION , overlay);
			
			if(out != null)
				frames.add(out);// = out;
			else
				warn("Skipping frame " + i + " of base " + basePath + baseName);
		}
		
		if(frames.isEmpty())
		{
			error("No frames were found at all, so the gif could not be made!");
			return;
		}
		
		verbose("Processed " + frameCount + " frames, found " + frames.size() + " valid, writing...");
		ImageOutputStream output = new FileImageOutputStream(new File(outfileName));
		GifSequenceWriter writer = new GifSequenceWriter(output, frames.get(0).getType(), fps, true);
		
		Iterator<BufferedImage> buffIter = frames.iterator();
		while(buffIter.hasNext())
			writer.writeToSequence(buffIter.next());
		
		writer.close();
		output.close();
	}
	
	// Performs an overlay at a pixel location. We're making some assumptions here, mostly that there is going
	// to be an RGBA-32-bit encoded PNG image read in for our parsing purposes. We then look for a 0, 255, 0
	// green pixel on the base frame to apply the overlay buffer to, centered.
	private BufferedImage combineImages(String pathBase, BufferedImage overlay) throws IOException
	{
		// Acquire images
		BufferedImage imageBase = null;
		BufferedImage imageOverlay = overlay;
		
		File potentialImage = new File(pathBase);
		if(!potentialImage.exists())
			return null;
		
		imageBase = ImageIO.read(potentialImage);		
		
		// Grab data we know won't change. As a side note, I discovered you can get specific pixels a bit
		// differently later, but it was too late, I wrote the pixel specific code already.
		final byte[] pixelsBase = ((DataBufferByte) imageBase.getRaster().getDataBuffer()).getData();
		final int baseWidth = imageBase.getWidth();
		final int overlayWidth = imageOverlay.getWidth();
		final int baseHeight = imageBase.getHeight();
		final int overlayHeight = imageOverlay.getHeight();
		
		// Warn about odd sizing when applicable
		if(overlayHeight > baseHeight || overlayWidth > baseWidth)
			warn("Size mismatch, overlay is larger. May function, but not supported.");
		
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
			verbose("No overlay required for this frame.");
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
			// ...then get bytes and manually apply alpha overlay.
			if(x > left && x < right && y > top && y < bottom)
			{
				final int overlayX = x - left;
				final int overlayY = y - top;
				
				// Skip out of bounds pixels
				if(overlayX < 0 || overlayY < 0 || overlayX >= baseWidth || overlayY >= baseHeight)
					continue;
				
				byte[] overlayBytes = ByteBuffer.allocate(4).putInt(imageOverlay.getRGB(overlayX, overlayY)).array();
				byte[] baseBytes = ByteBuffer.allocate(4).putInt(imageBase.getRGB(x, y)).array();
				float overlayT = (overlayBytes[0] & 0xFF) / 255.0f;
				
				// Java has a restriction where we can't actually manupulate unsigned bytes apparently(?) so 
				// what this does is convert the specific bytes to ints in order to manipuate them, then takes the
				// byte buffer and parses the far right byte out of the int after manipulating the value.
				byte[] output = new byte[4];
				output[0] = (byte)0b11111111;
				output[1] = ByteBuffer.allocate(4).putInt((int) ((baseBytes[1] & 0xFF) - (overlayT * ((baseBytes[1] & 0xFF) - (overlayBytes[1]  & 0xFF))))).array()[3];
				output[2] = ByteBuffer.allocate(4).putInt((int) ((baseBytes[2] & 0xFF) - (overlayT * ((baseBytes[2] & 0xFF) - (overlayBytes[2]  & 0xFF))))).array()[3]; 
				output[3] = ByteBuffer.allocate(4).putInt((int) ((baseBytes[3] & 0xFF) - (overlayT * ((baseBytes[3] & 0xFF) - (overlayBytes[3]  & 0xFF))))).array()[3];
				
				// Re-write bytes as overlay
				imageBase.setRGB(x, y, ByteBuffer.wrap(output).getInt());
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
	// NOTE: ^ This is Elliot's original license
	private static class GifSequenceWriter 
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
