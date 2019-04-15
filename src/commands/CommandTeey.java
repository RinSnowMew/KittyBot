package commands;

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
import utils.ImageOverlayBuilder;
import utils.ImageUtils;

public class CommandTeey extends Command
{
	// Required constructor
	public CommandTeey(KittyRole level, KittyRating rating) { super(level, rating); }
	
	private static Long num = 0l;
	
	@Override
	public String HelpText() { return LocStrings.Stub("TeeyInfo"); }
	
	// Called when the command is run!
	@Override 
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String name = null;
		File teeyFile = null;
		File teeyeeFile = null;
		
		synchronized(num)
		{
			name = "teey_" + num + ".gif";
			++num;
		}
		
		try 
		{
			KittyUser person = null;
			
			if(input.mentions == null)
				person = user; 
			else
				person = input.mentions[0];
				
			String yeeteeFilename = ImageUtils.DownloadFromURL(person.avatarID, ".png");
			if(yeeteeFilename == null)
				return;
			
			teeyeeFile = new File(yeeteeFilename);
			ImageOverlayBuilder builder = new ImageOverlayBuilder("assets/teey/frames/", "teey ", 24, 18);
			builder.Overlay(ImageIO.read(teeyeeFile), name);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		teeyFile = new File (name);
		res.CallFile(teeyFile, "gif");

		// Thread cleanup...
		ImageUtils.BlockingFileDelete(teeyFile);
		ImageUtils.BlockingFileDelete(teeyeeFile);
	}
}
