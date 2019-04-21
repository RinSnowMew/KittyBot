package commands;

import core.Command;
import core.LocStrings;
import core.CommandManager.ThreadData;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import core.Stats;

public class CommandStats extends Command
{
	public CommandStats(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("StatsInfo"); }
	
	// Called when the command is run!
	@Override 
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String out = "```\n";
		Stats stats = Stats.instance;
		
		long seen = stats.GetMessagesSeen();
		long processed = stats.GetCommandsProcessed();
		
		out += "----- [General] -----\n";
		out += " Messages observed: " + seen + "\n";
		out += "Commands processed: " + processed + "\n";
		out += "  Command invoke %: " + ((int)((processed / (float)seen) * 1000)) / 10.0f + "%\n";
		out += "   KittyBot uptime: " + stats.GetFormattedUptime() + "\n";
		out += "\n";
		out += "----- [Health] -----\n";
		out += " SMT cores: " + stats.GetCPUAvailable() + "\n";
		
		// If CPU load works on this OS, list it.
		double CPULoad = stats.GetSystemCPULoad();
		if(CPULoad > -0.9) // -1.0 is the error state 
			out += "System CPU Load: " + (CPULoad * 100) + "%\n";
		
		ThreadData data = stats.GetThreadData();
		Integer terminated = data.states.get(Thread.State.TERMINATED);
		Integer runnable = data.states.get(Thread.State.RUNNABLE);
		Integer blocked = data.states.get(Thread.State.BLOCKED);
		Integer timed_waiting = data.states.get(Thread.State.TIMED_WAITING);
		Integer waiting = data.states.get(Thread.State.WAITING);
		
		out += "   Threads:"
			+ " run-" + (runnable == null ? 0 : runnable)
			+ " block-" + (blocked == null ? 0 : blocked)
			+ " sleep-" + (timed_waiting == null ? 0 : timed_waiting)
			+ " wait-" + (waiting == null ? 0 : waiting)
			+ " term-" + (terminated == null ? 0 : terminated)
			+ "\n";
		
		Integer guildCount = stats.GetGuildCount();
		Integer userCount = stats.GetUserCount();
		
		out += "\n----- [Cache] -----\n"
			+ "Cached Guilds: " + guildCount + "\n"
			+ " Cached Users: " + userCount;
		out += "\n```";
		
		res.Call(out);
	}
}
