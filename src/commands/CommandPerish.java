package commands;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.Command;
import core.LocStrings;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.ImageUtils;

public class CommandPerish  extends Command
{
	public CommandPerish(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("PerishInfo"); };
	
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
			name = "perish_" + num + ".png";
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
			ApplyTintEffect(ImageIO.read(preProcessed), name);
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
	
	private static void ApplyTintEffect(BufferedImage image, String name) throws IOException
	{
		// Iterate over each column left to right and touch up each pixel
		for(int x = 0; x < image.getWidth(); ++x)
		{
			for(int y = 0; y < image.getHeight(); ++y)
			{
				Color c = new Color(image.getRGB(x, y), true);
				
				int red = c.getRed();
				red += 100;
				if(red > 255)
					red = 255;
				
				int green = c.getGreen();
				
				int blue = c.getBlue();
				blue += 20;
				if(blue > 255)
					blue = 255;
				
				Color tinted = new Color(red, green, blue, c.getAlpha());
				image.setRGB(x, y, tinted.getRGB());
			}
		}
		
		File outputfile = new File(name);
		ImageIO.write(image, "png", outputfile);
	}
}
