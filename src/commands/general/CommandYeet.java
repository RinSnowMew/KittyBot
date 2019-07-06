package commands.general;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import core.Command;
import core.Constants;
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

public class CommandYeet extends Command
{
	// Required constructor
	public CommandYeet(KittyRole level, KittyRating rating) { super(level, rating); }
	
	private static Long num = 0l;
	
	@Override
	public String getHelpText() { return LocStrings.stub("YeetInfo"); }
	
	// Called when the command is run!
	@Override 
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
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
				person = user; 
			else
				person = input.mentions[0];
				
			String yeeteeFilename = ImageUtils.downloadFromURL(person.avatarID, ".png");
			if(yeeteeFilename == null)
				return;
			
			yeeteeFile = new File(yeeteeFilename);
			ImageOverlayBuilder builder = new ImageOverlayBuilder(Constants.AssetDirectory + "yeet/frames/", "yeet ", 24, 18);
			builder.overlay(ImageIO.read(yeeteeFile), name);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		yeetFile = new File (name);
		res.sendFile(yeetFile, "gif");

		// Thread cleanup...
		ImageUtils.blockingFileDelete(yeetFile);
		ImageUtils.blockingFileDelete(yeeteeFile);
	}
}
