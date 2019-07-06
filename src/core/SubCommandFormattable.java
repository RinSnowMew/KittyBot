package core;

import dataStructures.KittyEmbed;
import dataStructures.Response;

// Only one isn't null!
public class SubCommandFormattable
{
	public final KittyEmbed resEmbed;
	public final String resString;
	
	// Default constructor is hidden and disabled.
	// If somehow it is called, defaults to an empty string and no embed.
	@SuppressWarnings("unused")
	private SubCommandFormattable()
	{ 
		this.resEmbed = null;
		this.resString = "";
	}
	
	public SubCommandFormattable(String res)
	{
		this.resEmbed = null;
		this.resString = res;
	}
	
	public SubCommandFormattable(KittyEmbed embed)
	{
		this.resEmbed = embed;
		this.resString = null;
	}
	
	public void Call(Response res)
	{
		if(resEmbed == null)
			res.send(resString);
		else
			res.send(resEmbed);
	}
}
