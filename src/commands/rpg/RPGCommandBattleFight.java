package commands.rpg;

import core.rpg.RPGCommand;
import core.rpg.RPGInput;
import core.rpg.RPGState;

public class RPGCommandBattleFight extends RPGCommand
{
	@Override
	public String onRun(RPGState state, RPGInput input)
	{
		if(state.battleContext == null)
			return "```There's nothing to fight!```";
		
		String out = null;
		String reward = null;
		
		out = "No one ever trained you to fight, so you do a silly dance and it weirds your opponent out and they leave. (i need to implement this still)";
		reward = "+150xp";
		
		state.battleContext = null;
		state.player.applyEXP(150);
		
		if(out != null && reward != null)
		{
			out += "\n";
			out += "\n";
			out += "[" + reward + "]";
		}
		
		if(out != null)
			out = "```\n" + out + "```";
		
		return out;
	}

}
