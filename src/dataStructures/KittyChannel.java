package dataStructures;

//import core.DatabaseTrackedObject;

public class KittyChannel //extends DatabaseTrackedObject
{
	public String uniqueID;
	public KittyGuild guild;
	
	//public KittyRating rating;
	//public bool isVoice;
	
	public KittyChannel(String uniqueID, KittyGuild guild)
	{
		//super(uniqueID);
		this.uniqueID = uniqueID;
		this.guild = guild;
	}

//	@Override
//	public String serialize() 
//	{
//		return "";
//	}
//
//	@Override
//	public void deSerialzie(String string) 
//	{
//		
//	}
}
