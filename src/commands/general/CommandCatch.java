package commands.general;

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

public class CommandCatch extends Command
{
	// Required constructor
	public CommandCatch(KittyRole level, KittyRating rating) { super(level, rating); }
	
	private static Long num = 0l;
	
	@Override
	public String getHelpText() { return LocStrings.stub("CatchInfo"); }
	
	// Called when the command is run!
	@Override 
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String name = null;
		File catchFile = null;
		File catcheeFile = null;
		
		synchronized(num)
		{
			name = "catch_" + num + ".gif";
			++num;
		}
		
		try 
		{
			KittyUser person = null;
			
			if(input.mentions == null)
				person = user; 
			else
				person = input.mentions[0];
				
			String catchFilename = ImageUtils.downloadFromURL(person.avatarID, ".png");
			if(catchFilename == null)
				return;
			
			catcheeFile = new File(catchFilename);
			ImageOverlayBuilder builder = new ImageOverlayBuilder("assets/catch/frames/", "catch ", 92, 18);
			builder.overlay(ImageIO.read(catcheeFile), name);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		catchFile = new File (name);
		res.sendFile(catchFile, "gif");

		// Thread cleanup...
		ImageUtils.blockingFileDelete(catchFile);
		ImageUtils.blockingFileDelete(catcheeFile);
	}
}
