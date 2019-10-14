package network;

import com.google.gson.Gson;

import dataStructures.GenericImage;
import offline.Ref;
import utils.HTTPUtils;

/**
 * This is the e621 request class, designed for form and parse requests that
 * use the e621 API, and is entirely static. 
 * 
 * If you ever find this class randomly not working,
 * it may be a good idea to make sure the user agent string is set in 
 * HTTPUtils to something other than a browser emulating string, or the default
 * java one.
 * 
 * @author Wisp
 * Edited by Rin
 */
public class NetworkE621 
{   
	  ///////////////////////////////////////
	 // Internal JSON class and variables //
	//////////////////////////////////.////
	private static final Gson jsonParser_ = new Gson();
	private static final String API_ROOT = "https://e621.net/post/index.json?";
	private static int maxSearchResults_ = 10;
	private static String[] blacklist = Ref.e621Blacklist;
	
	private class E621ResponseObject
	{
		// public varaibles matching the case and the type we want for JSON.
		// There are many more fields, but if we don't provide some it just
		// doesn't bother parsing them.
		public String file_url;
		public String id;
		public String tags;
		public String [] artist;
		public String [] sources;
		public String score; 
	}

	
	
	  ////////////////////
	 // Static methods //
	////////////////////
	// Requests a specific image, then returns a few.
	public GenericImage getE621(String input)
	{
		GenericImage image = new GenericImage("","","");
		boolean blacklisted;
		// Clean up request and replace problematic characters for the query string.
		input = input.trim();
		input = input.replace("+", "%2B");
		input = input.replace(" ", "%20");
		
		// Configure and send request. Note: Random ordering added as first
		// tag by default. User-provided tags, therefore, will override it. 
		// If order:score is provided, that will be honored over order:random.
		String res = HTTPUtils.SendPOSTRequest(API_ROOT
			, "tags=order:random%20" + input + "&limit=" + maxSearchResults_);
		
		
		
		if(res != null)
		{
			// Use class evaluation on an array of the response imageObject to be able to hold multiple.
			E621ResponseObject[] imageObj = jsonParser_.fromJson(res, E621ResponseObject[].class);
			
			// For now, we really just wanna display images and their source. 
			// Append them all separately to a response string w/ some flavor text.
			if(imageObj.length < 1)
			{
				
			}
			else
			{
				for(int i = 0; i < imageObj.length; ++i)
				{
					blacklisted = false;
					for(int j = 0; j < blacklist.length; j++)
					{
						if(imageObj[i].tags.contains(blacklist[j]))
						{
							System.out.println("BLACKLISTED " + blacklist[j]);
							blacklisted = true;
						}
					}
					
					if(blacklisted)
					{
						return null;
					}
					// We will always have a file URL. That's a given.
					image.editImageURL(imageObj[i].file_url);
					image.editPostURL("https://e621.net/post/show/" + imageObj[i].id);
					if(imageObj[i].artist.length > 0)
						image.editArtist(imageObj[i].artist[0]);
					else
						image.editArtist("Artist Not Found!");
					
					image.editAuthorImage("https://e621.net/favicon.ico");
					
					String sources = "";
					
					if(imageObj[i].sources != null)
					{
						for(int k = 0; k < imageObj[i].sources.length; k ++)
						{
							if(!imageObj[i].sources[k].endsWith(imageObj[i].file_url.substring(imageObj[i].file_url.lastIndexOf('.'))))
							{
								String source = imageObj[i].sources[k];
								if(source.contains("//www."))
								{
									source = source.substring(source.indexOf(".") + 1);
									source = source.substring(0, source.indexOf('.'));
								}
								else 
								{
									source = source.substring(source.indexOf("/") + 2);
									source = source.substring(0, source.indexOf('.'));
								}
								sources += " [" + source + "](" + imageObj[i].sources[k] + ") "; 
							}
						}
						image.editDescriptionText("Score: " + imageObj[i].score + "\n**Source:**" + sources);
					}
					else
					{
						image.editDescriptionText("Score: " + imageObj[i].score);
					}
					
					
					
					if(imageObj[i].tags.split(" ").length > 15)
					{
						String [] splitTags = imageObj[i].tags.split(" ");
						String descTags = "";
						for(int o = 0; o < 15; o++)
						{
							descTags += splitTags[o] + ", ";
						}
						image.editFooterText("[Tags] " + descTags + " etc.");
					}
					else
					{
						image.editFooterText("[Tags] " + imageObj[i].tags.replace(" ", ", "));
					}
					
					if(image.output().authorText != null)
					{
						return image; 
					}
				}
			}
			
		}
		return image;
	}
}