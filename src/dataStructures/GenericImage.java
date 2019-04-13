package dataStructures;

// This is a structure representing an image reponse for handing around.
public class GenericImage 
{
	private String artist; 
	private String postURL; 
	private String imageURL;
	
	public GenericImage(String artist, String postURL, String imageURL)
	{
		this.artist = artist; 
		this.postURL = postURL; 
		this.imageURL = imageURL; 
	}
	
	public void editArtist(String artist)
	{
		this.artist = artist; 
	}
	
	public void editPostURL(String postURL)
	{
		this.postURL = postURL; 
	}
	
	public void editImageURL(String imageURL)
	{
		this.imageURL = imageURL; 
	}
	
	public String toString()
	{
		if(imageURL.isEmpty())
		{
			return "I couldn't find anything! Please try again!"; 
		}
		return "Artist: " + artist + "\n<" + postURL.trim()  + ">\n" + imageURL; 
	}
}
