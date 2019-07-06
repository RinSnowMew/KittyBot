package commands.general;

import core.Command;
import core.LocStrings;
import core.CommandManager.ThreadData;
import core.Constants;
import dataStructures.KittyChannel;
import dataStructures.KittyEmbed;
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
	public String getHelpText() { return LocStrings.stub("StatsInfo"); }
	
	// Called when the command is run!
	@Override 
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		// Variables
		Stats stats = Stats.instance;
		String out = "";
		long seen = stats.getMessagesSeen();
		long processed = stats.getCommandsProcessed();
		
		// General
		out += "General";
		out += "```\n";
		out += " Messages observed: " + seen + "\n";
		out += "Commands processed: " + processed + "\n";
		out += "  Command invoke %: " + ((int)((processed / (float)seen) * 1000)) / 10.0f + "%\n";
		out += "        Bot uptime: " + stats.getFormattedUptime() + "\n";
		out += "```\n";
		
		// Health
		out += "Health";
		out += "```\n";
		out += " SMT cores: " + stats.getCPUAvailable() + "\n";
		
		// If CPU load works on this OS, list it. -1.0 is the error state 
		double CPULoad = stats.getSystemCPULoad();
		out += CPULoad > -0.9
			 ? "  CPU Load: " + (CPULoad * 100) + "%\n" : "";
		
		ThreadData data = stats.getThreadData();
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
		
		out += "```\n";
		
		
		// Cache
		Integer guildCount = stats.getGuildCount();
		Integer userCount = stats.getUserCount();
		
		out += "Cache";
		out += "```\n"
			+ "Cached Guilds: " + guildCount + "\n"
			+ " Cached Users: " + userCount;
		
		out += "\n```";
		
		KittyEmbed embed = new KittyEmbed();
		embed.title = "Bot Stats";
		embed.descriptionText = out;
		embed.color = Constants.ColorDefault;
		
		res.send(embed);
	}
}
