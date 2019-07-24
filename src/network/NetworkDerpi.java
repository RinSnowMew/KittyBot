package network;

import com.google.gson.Gson;
import dataStructures.GenericImage;
import offline.*;
import utils.*;

public class NetworkDerpi
{
	private final String mainURL = "https://derpibooru.org/search.json?q=";
	private static final Gson jsonParser_ = new Gson();
	
	private class DerpiResponseObject
    {
        public String image;
        public String tags;
        public String score; 
    }
    
    private class InitialRequest
    {
        public int id;
    }

    public GenericImage getDerpi(String query) 
	{
    	GenericImage image = new GenericImage(" ", " ", " ");
    	query = query.trim();
		query = query.replace(" ", ",");
        String res = HTTPUtils.sendGETRequest(mainURL + query + "&random_image=1&key=" + Ref.derpiKey);
        if(res != null)
        {
        	InitialRequest obj;
            // Use class evaluation on an array of the response object to be able to hold multiple.
        	try 
        	{
        		 obj = jsonParser_.fromJson(res, InitialRequest.class);
        	}
        	catch(Exception e)
        	{
        		return null;
        	}
            
            if(res != null)
            {
            	
                String res2 = HTTPUtils.sendGETRequest("https://derpibooru.org/" + obj.id + ".json");
                image.editPostURL("<https://derpibooru.org/" + obj.id +">");
                DerpiResponseObject imageObj = jsonParser_.fromJson(res2, DerpiResponseObject.class);
                
                image.editImageURL("https:" + imageObj.image.substring(0, imageObj.image.indexOf('_')) + 
                		imageObj.image.substring(imageObj.image.lastIndexOf('.')));
                if(imageObj.tags.contains("artist:"))
                {
                	String [] sepTags = imageObj.tags.split(",");
                	String artists = "";
                	for(int i = 0; i < sepTags.length; i++)
                	{
                		if(sepTags[i].contains("artist:"))
                		{
                			if(!artists.equals(""))
                			{
                				artists += " and ";
                			}
                			artists += sepTags[i].substring(sepTags[i].indexOf(":")+1);
                		}
                	}
                	image.editAuthorImage("https://derpicdn.net/favicon.ico");

        			image.editArtist(artists);
        			String tags = "";
        			for(int i = 0; i < sepTags.length && i < 15; i++)
                	{
                		if(!sepTags[i].contains("artist:"))
                		{
                			tags += sepTags[i];
                		}
                	}
        			
        			image.editFooterText("[Tags]" + tags);
        			image.editDescriptionText("Score: " + imageObj.score);
                }
                else
                {
                	image.editArtist("Artist Unknown!");
                }
            }
        }
        
		return image;
	}
    
    public GenericImage getDerpiByID(String id)
    {
    	GenericImage image = new GenericImage(" ", " ", " ");
    	String artists = "";
    	image.editAuthorImage("https://derpicdn.net/favicon.ico");
    	String res;
    	try
    	{
    		 res = HTTPUtils.sendGETRequest("https://derpibooru.org/" + id + ".json");
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    	DerpiResponseObject imageObj = jsonParser_.fromJson(res, DerpiResponseObject.class);
    	if(imageObj.tags.contains("artist:"))
        {
        	String [] sepTags = imageObj.tags.split(",");
        	for(int i = 0; i < sepTags.length; i++)
        	{
        		if(sepTags[i].contains("artist:"))
        		{
        			if(!artists.equals(""))
        			{
        				artists += " and ";
        			}
        			artists += sepTags[i].substring(sepTags[i].indexOf(":")+1);
        		}
        	}
        }
    	if(artists != "")
    		image.editArtist(artists);
    	else
    		image.editArtist("Artist Unknown!");
    	
    	image.editFooterText("[Tags]" + imageObj.tags);
		image.editDescriptionText("Score: " + imageObj.score);
    	image.editImageURL("https:" + imageObj.image.substring(0, imageObj.image.indexOf('_')) + 
        		imageObj.image.substring(imageObj.image.lastIndexOf('.')));
    	return image;
    }
}