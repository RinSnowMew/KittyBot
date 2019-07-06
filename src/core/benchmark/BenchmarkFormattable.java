package core.benchmark;

import dataStructures.KittyEmbed;
import dataStructures.Response;

// Only one isn't null!
public class BenchmarkFormattable
{
	public final KittyEmbed resEmbed;
	public final String resString;
	
	// Default constructor is hidden and disabled.
	// If somehow it is called, defaults to an empty string and no embed.
	@SuppressWarnings("unused")
	private BenchmarkFormattable()
	{ 
		this.resEmbed = null;
		this.resString = "";
	}
	
	public BenchmarkFormattable(String res)
	{
		this.resEmbed = null;
		this.resString = res;
	}
	
	public BenchmarkFormattable(KittyEmbed embed)
	{
		this.resEmbed = embed;
		this.resString = null;
	}
	
	public void call(Response res)
	{
		if(resEmbed == null)
			res.send(resString);
		else
			res.send(resEmbed);
	}
}
