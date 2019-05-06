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

public class CommandBlurry extends Command
{
	public CommandBlurry(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("BlurryInfo"); };
	
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
			name = "blurred_" + num + ".png";
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
		BufferedImage blurred = ImageUtils.copyImage(image);
		// Iterate over each column left to right and touch up each pixel
		for(int i = 0; i < 50; ++i)
		{
			for(int x = 1; x < image.getWidth() - 1; ++x)
			{
				for(int y = 1; y < image.getHeight() - 1; ++y)
				{
					blurred.setRGB(x, y, getColorAvg(image, x, y).getRGB());
				}
			}
			image = ImageUtils.copyImage(blurred);
		}
		
		
		File outputfile = new File(name);
		ImageIO.write(blurred, "png", outputfile);
	}
	
	private static Color getColorAvg(BufferedImage image, int x, int y)
	{
		Color center = new Color(image.getRGB(x, y), true);
		Color one = new Color(image.getRGB(x - 1, y), true);
		Color two = new Color(image.getRGB(x - 1, y - 1), true);
		Color three = new Color(image.getRGB(x, y - 1), true);
		Color four = new Color(image.getRGB(x + 1, y - 1), true);
		Color five = new Color(image.getRGB(x + 1, y), true);
		Color six = new Color(image.getRGB(x + 1, y + 1), true);
		Color seven = new Color(image.getRGB(x, y + 1), true);
		Color eight = new Color(image.getRGB(x -1, y + 1), true);
		
		int blue = center.getBlue() + one.getBlue() + two.getBlue() + three.getBlue() + four.getBlue() 
			+ five.getBlue() + six.getBlue() + seven.getBlue() + eight.getBlue();
		int red = center.getRed() + one.getRed() + two.getRed() + three.getRed() + four.getRed() 
			+ five.getRed() + six.getRed() + seven.getRed() + eight.getRed();
		int green = center.getGreen() + one.getGreen() + two.getGreen() + three.getGreen()
			+ four.getGreen() + five.getGreen() + six.getGreen() + seven.getGreen() + eight.getGreen();
		int alpha = center.getAlpha() + one.getAlpha() + two.getAlpha() + three.getAlpha() 
			+ four.getAlpha() + five.getAlpha() + six.getAlpha() + seven.getAlpha() + eight.getAlpha();
		
		blue /= 9;
		red /= 9;
		green /= 9;
		alpha /= 9;
		
		Color blur = new Color(red, green, blue, alpha);
		
		return blur;
	}	
}