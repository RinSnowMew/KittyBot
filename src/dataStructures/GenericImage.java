package dataStructures;

import java.awt.Color;

// This is a structure representing an image response for handing around.
public class GenericImage 
{
	private KittyEmbed response= new KittyEmbed(); 
	
	public GenericImage(String artist, String postURL, String imageURL)
	{
		response.authorText = null;
		response.color = new Color(0,0,0);
	}
	
	public void editArtist(String artist)
	{
		response.authorText = artist;
	}
	
	public void editAuthorImage(String URL)
	{
		response.authorImage = URL; 
	}
	
	public void editPostURL(String postURL)
	{
		response.authorLink = postURL;
	}
	
	public void editImageURL(String imageURL)
	{
		response.imageURL = imageURL; 
	}
	
	public void editDescriptionText(String score) 
	{
		response.descriptionText = score;
	}
	
	public void editFooterText(String tags) 
	{
		response.footerText = tags; 
	}
	
	public KittyEmbed output()
	{
		return response; 
	}

	public boolean isValid() 
	{
		if(response.title != null)
			return true;
		return false;
	}

	
}
