package commands.benchmark;

import java.util.List;

import core.benchmark.*;

public class BenchmarkCommandFind extends BenchmarkCommand 
{
	private final String lineDelimiter = "\n";
	private final int listMax = 30;
	
	public BenchmarkFormattable onRun(BenchmarkManager manager, BenchmarkInput input)
	{
		String output = "";
		String searchString = input.value.trim();
		
		List<BenchmarkEntry> entries = manager.findModel(searchString);
		
		if(entries.size() > 0)
		{
			output += "```" + lineDelimiter;
			int max = entries.size();
			
			boolean fencepost = true;
			for(int i = 0; i < entries.size() && i < listMax; ++i)
			{
				BenchmarkEntry entry = entries.get(i);
				if(!fencepost)
					output += lineDelimiter;
				
				fencepost = false;
				output += entry.type + ": "+ entry.model;
			}
			
			if(max > listMax)
				output += lineDelimiter + lineDelimiter + "Plus " + (max - listMax) + " more items...";
			
			output += "```";
		}
		else
		{
			output = "Couldn't find any models matching the search `" + searchString + "`!";
		}
		
		return new BenchmarkFormattable(output);
	}
}
