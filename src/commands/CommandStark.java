package commands;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.Command;
import core.LocStrings;
import dataStructures.*;
import utils.ImageUtils;

public class CommandStark extends Command
{
	public CommandStark(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("StarkInfo"); };
	
	private static Long num = 0l;
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String name = null;
		String filename = null;
		File preProcessed = null;
		File postProcessed = null;
		
		synchronized(num)
		{
			name = "snapped_" + num + ".png";
			++num;
		}
		
		try 
		{
			try
			{
				filename = ImageUtils.DownloadFromURL(input.args.split(" ")[0], ".png");
				preProcessed = new File(filename);
			}
			catch(Exception e)
			{
				KittyUser person = user;
				if(input.mentions != null)
					person = input.mentions[0];
				
				filename = ImageUtils.DownloadFromURL(person.avatarID, ".png");
				if(filename == null)
					return;
			}
			preProcessed = new File(filename);
			ApplySnap(ImageIO.read(preProcessed), name);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		postProcessed = new File(name);
		res.CallFile(postProcessed, "png");
		
		ImageUtils.BlockingFileDelete(preProcessed);
		ImageUtils.BlockingFileDelete(postProcessed);
	}
	
	private static void ApplySnap(BufferedImage image, String name) throws IOException
	{
		BufferedImage snap = ImageUtils.copyImage(image);
		// Iterate over each column left to right and touch up each pixel
		for(int x = 0; x < snap.getWidth(); ++x)
		{
			for(int y = 0; y < snap.getHeight(); ++y)
			{
				Color c = new Color(snap.getRGB(x, y), true);
				
				int alpha = c.getAlpha();
				if(x * Math.random() > 25)
					alpha = (int)((1 - (x / ((float)snap.getWidth()))) * 255);
				
				
				Color snapped = new Color(c.getRed(),c.getGreen(), c.getBlue(), alpha);
				snap.setRGB(x, y, snapped.getRGB());
			}
		}
		
		File outputfile = new File(name);
		ImageIO.write(snap, "png", outputfile);
	}
}
