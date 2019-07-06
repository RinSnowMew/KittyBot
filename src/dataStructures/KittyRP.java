package dataStructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class KittyRP 
{
	ArrayList <Long> users = new ArrayList<Long>();
	ArrayList <String> log = new ArrayList <String> ();
	KittyChannel channel;
	long timer;
	
	public KittyRP(ArrayList <KittyUser> users, KittyChannel channel)
	{
		for(int i = 0; i < users.size(); i++)
		{
			this.users.add(Long.parseLong(users.get(i).discordID));
		}
		this.channel = channel; 
		timer = System.currentTimeMillis();
	}
	
	public void addLine(KittyUser user, String line)
	{
		if(users.contains(Long.parseLong(user.discordID)))
		{
			log.add(user.name + " " + formatString(line));
			resetTimer();
		}
	}
	
	private String formatString(String line)
	{
		if((line.charAt(0) == '_' && line.charAt(line.length()-1) == '_') 
				||(line.charAt(0) == '*' && line.charAt(line.length()-1) == '*'))
		{
			line = line.substring(1,line.length()-1);
		}
		
		return line;
	}
	
	public File endRP(KittyUser user) throws FileNotFoundException, UnsupportedEncodingException
	{
		if(!users.contains(Long.parseLong(user.discordID)) && user.getRole().getValue() < 2)
		{
			return null;
		}
		PrintWriter writer = new PrintWriter("RP.txt", "UTF-8");
		while(!log.isEmpty())
		{
			 writer.println(log.get(0));
			 writer.flush();
			 log.remove(0);
		}
		writer.close();
		return new File("RP.txt"); 
	}
	
	public void resetTimer()
	{
		timer = System.currentTimeMillis();
	}
	
	public long getTimer()
	{
		return timer;
	}
	
	public KittyChannel getChannel()
	{
		return channel; 
	}
	
	public ArrayList <Long> getUsers()
	{
		return users;
	}
}