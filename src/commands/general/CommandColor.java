package commands.general;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import core.Command;
import core.LocStrings;
import dataStructures.KittyChannel;
import dataStructures.KittyEmbed;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import network.NetworkTheColorAPI;
import network.NetworkTheColorAPI.ColorData;
import utils.ImageUtils;

public class CommandColor extends Command
{
	private NetworkTheColorAPI theColorAPI = new NetworkTheColorAPI();
	
	public CommandColor(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("ColorInfo"); }

	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		// First, try and parse out the color to make sure we can even get it.
		ColorData colorData = theColorAPI.lookupHex(input.args.trim());
		
		// Verify the color was even found
		if(colorData == null)
		{
			res.send(LocStrings.stub("ColorNotSearchable"));
			return;
		}
		
		// Set up the variables we'll be using based on the color, and get the color itself.
		int sideLength = 80; // It's 80x80 because as far as I know, that's the size of a thumbnail in discord.
		int r = colorData.rgb.r;
		int g = colorData.rgb.g;
		int b = colorData.rgb.b;
		int a = 255; // Alpha not supported at this time
		Color parsed = new Color(r, g, b, a);
		
		// Construct the image example file that we intend to display locally
		BufferedImage img = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(parsed);
		graphics.fillRect(0, 0, sideLength, sideLength);
		graphics.dispose();
		
		String tempFileName = ImageUtils.writeTempImageData(img, ".png");
		File file = new File(tempFileName);

		// Build the response
		KittyEmbed response = new KittyEmbed();
		String postEntry = ", ";
		response.title = colorData.name.value + (colorData.name.exact_match_name ? "" : " (ish)");
		response.color = parsed;
		response.descriptionText = "```";
		response.descriptionText += "\n hex:  #" + colorData.hex.clean;
		response.descriptionText += "\n rgb:  " + String.format("%1$03d", r) + postEntry + String.format("%1$03d", g) + postEntry + String.format("%1$03d", b);
		response.descriptionText += "\n hsl:  " + String.format("%1$03d", colorData.hsl.h) + postEntry + String.format("%1$02d", colorData.hsl.s) + "%" + postEntry + String.format("%1$02d", colorData.hsl.l) + "%";
		response.descriptionText += "\n hsv:  " + String.format("%1$03d", colorData.hsv.h) + postEntry + String.format("%1$02d", colorData.hsv.s) + "%" + postEntry + String.format("%1$02d", colorData.hsv.v) + "%";
		response.descriptionText += "\n xyz:  " + String.format("%1$03d", colorData.XYZ.X) + postEntry + String.format("%1$03d", colorData.XYZ.Y) + postEntry + String.format("%1$03d", colorData.XYZ.Z);
		response.descriptionText += "\ncmyk:  " + String.format("%1$03d", colorData.cmyk.c) + postEntry + String.format("%1$03d", colorData.cmyk.m) + postEntry + String.format("%1$03d", colorData.cmyk.y) + postEntry + String.format("%1$03d", colorData.cmyk.k);
		response.descriptionText += "```";
		response.thumbnailURL = "attachment://" + tempFileName;
		response.footerText = "All percentages and values are rounded to the nearest whole number!" + (colorData.name.exact_match_name ? "" : " '" + colorData.name.value + "' is actually " + colorData.name.closest_named_hex + ".");
		
		// Send then delete the temp local files
		res.send(response);
		ImageUtils.blockingFileDelete(file);
	};
	
}
