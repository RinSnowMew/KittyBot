package commands.general;

import core.Command;
import core.LocStrings;
import core.benchmark.BenchmarkFormattable;
import core.benchmark.BenchmarkFramework;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandBenchmark extends Command
{
	// Variables
	private BenchmarkFramework framework;
	
	// Constructor
	public CommandBenchmark(KittyRole level, KittyRating rating)
	{ 
		super(level, rating);
		framework = new BenchmarkFramework();
	}
	
	@Override
	public String getHelpText() { return LocStrings.stub("BenchmarkInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		framework.update();
		
		if(input.args == null || input.args.length() == 0)
		{
			String output = getHelpText();
			res.send(output);
			return;
		}
		
		BenchmarkFormattable commandOutput = null;
		synchronized(framework)
		{
			commandOutput = framework.run(input.args.trim());
		}
		
		if(commandOutput == null)
		{
			res.send(LocStrings.stub("BenchmarkInvalid"));
			return;
		}
		
		commandOutput.call(res);
	}
}