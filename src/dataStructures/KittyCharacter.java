package dataStructures;

public class KittyCharacter 
{
	private KittyUser owner; 
	private String name;
	private String bio; 
	private String refImage; 
	private long uniqueID;
	
	public KittyCharacter(KittyUser owner, String name, String bio, String refImage, long uniqueID)
	{
		this.owner = owner;
		this.name = name;
		this.bio = bio;
		this.refImage = refImage; 
		this.uniqueID = uniqueID;
	}
	
	public void editBio(String bio)
	{
		this.bio = bio;
	}
	
	public void editRefImage(String refImage)
	{
		this.refImage = refImage;
	}
	
	public void editName(String name) 
	{
		this.name = name;	
	}
	
	public KittyUser getOwner()
	{
		return owner;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getBio()
	{
		return bio;
	}
	
	public String getRefImage()
	{
		return refImage; 
	}
	
	public long getUID()
	{
		return uniqueID;
	}
	
	public String toString()
	{
		return name + " " + bio + " " + refImage;
	}
}
