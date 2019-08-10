package commands.general;

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
	public String getHelpText() { return LocStrings.stub("BlurryInfo"); };
	
	private static Long num = 0l;
	private static float [] weights = {.00390625f, .0156625f, .0234475f, .0156625f, .00390625f};
	
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
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
				filename = ImageUtils.downloadFromURL(input.args.split(" ")[0], ".png");
				preProcessed = new File(filename);
			}
			catch(Exception e)
			{
				KittyUser person = user;
				if(input.mentions != null)
					person = input.mentions[0];
				
				filename = ImageUtils.downloadFromURL(person.avatarID, ".png");
				if(filename == null)
					return;
			}
			preProcessed = new File(filename);
			applyBlur(ImageIO.read(preProcessed), name);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		postProcessed = new File(name);
		res.sendFile(postProcessed, "png");
		
		ImageUtils.blockingFileDelete(preProcessed);
		ImageUtils.blockingFileDelete(postProcessed);
	}
	
	private static void applyBlur(BufferedImage image, String name) throws IOException
	{
		BufferedImage blurred = ImageUtils.copyImage(image);
		// Iterate over each column left to right and touch up each pixel
		for(int i = 0; i < 10; i++)
		{
			for(int x = 0; x < image.getWidth(); ++x)
			{
				for(int y = 0; y < image.getHeight(); ++y)
				{
					blurred.setRGB(x, y, getColorAvg(image, x, y).getRGB());
				}
			}
			image = blurred;
		}
		

		File outputfile = new File(name);
		ImageIO.write(blurred, "png", outputfile);
	}
	
	private static Color getColorAvg(BufferedImage image, int x, int y)
	{
		Color current;
		float r = 0; 
		float g = 0; 
		float b = 0;
		
		for(int xCor = -2; xCor <= 2; xCor++)
		{
			for(int yCor = -2; yCor <= 2; yCor++)
			{
				int curX = clamp(x + xCor, 0, image.getWidth() - 1);
				int curY = clamp(y + yCor, 0, image.getHeight() - 1);
				
				
				current = new Color(image.getRGB(curX, curY));
				
				r += getGaus(current.getRed(), xCor, yCor);
				g += getGaus(current.getGreen(), xCor, yCor);
				b += getGaus(current.getBlue(), xCor, yCor);
			}
		}
		int red = (int)r;
		int green = (int)g;
		int blue = (int)b;
		return new Color(red, green, blue);
	}
	
	private static int clamp(int num, int min, int max)
	{
		if(num >= max)
			return max;
		if(num < min)
			return min;
		return num;
	}
	
	private static float getGaus(int col, int x, int y)
	{
		x += 2; 
		y+= 2;
		float result = weights[x];
		if(y == 2 || y == 4)
			result *= 4; 
		if(y==3)
			result *= 6;
		
		result *= col; 
		
		return result;
	}
	
}