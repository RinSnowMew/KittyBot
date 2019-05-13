package dataStructures;

import java.awt.Color;

// This is a structure representing an image response for handing around.
public class GenericImage 
{
	private KittyEmbed response= new KittyEmbed(); 
	
	public GenericImage(String artist, String postURL, String imageURL)
	{
		response.authorText = artist;
		response.bodyImageURL = imageURL;
		response.authorLink = postURL;
		response.title = "BUTTS"; 
		response.color = new Color(0,0,0);
	}
	
	public void editArtist(String artist)
	{
		response.authorText = artist;
	}
	
	public void editPostURL(String postURL)
	{
		response.authorLink = postURL;
	}
	
	public void editImageURL(String imageURL)
	{
		response.bodyImageURL = imageURL; 
	}
	
	public KittyEmbed output()
	{
		System.out.print("HERE NOWWWWWWWWWWWWWW");
		return response; 
	}
}
