package commands.benchmark;

import java.util.List;

import core.benchmark.BenchmarkCommand;
import core.benchmark.BenchmarkEntry;
import core.benchmark.BenchmarkFormattable;
import core.benchmark.BenchmarkInput;
import core.benchmark.BenchmarkManager;

public class BenchmarkCommandCompare extends BenchmarkCommand 
{
	public BenchmarkFormattable onRun(BenchmarkManager manager, BenchmarkInput input)
	{
		final String lineDelimiter = "\n";
		String output = "";
		String[] inputSplit = input.value.trim().split("\\s+");

		if(inputSplit.length < 2)
			return new BenchmarkFormattable("You must to specify 2 models to compare!");
		
		for(int i = 0; i < inputSplit.length; ++i)
		{
			if(i != 0)
				output += lineDelimiter;
			
			List<BenchmarkEntry> entries = manager.findModel(inputSplit[i]);
			
			if(entries.size() < 1)
				output += "Couldn't find any models containing `" + inputSplit[i] + "`!";
			
			output += BenchmarkCommandInfo.formatInfo(entries.get(0));
			output += lineDelimiter;
			
			if(entries.size() > 1)
				output += "_chose " + entries.get(0).model + " from " + entries.size() + " potential components. To see others, try the command `benchmark find " + inputSplit[i] +"`_)";
			
			output += lineDelimiter;
			output += "----------";
		}

		return new BenchmarkFormattable(output);
	}
}
