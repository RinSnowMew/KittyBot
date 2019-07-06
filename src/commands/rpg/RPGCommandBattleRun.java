package commands.rpg;

import core.rpg.RPGCommand;
import core.rpg.RPGInput;
import core.rpg.RPGState;

public class RPGCommandBattleRun extends RPGCommand 
{
	@Override
	public String onRun(RPGState state, RPGInput input)
	{
		if(state.battleContext == null)
			return "```There's nothing to run from!```";
		
		long lostGold = (long)(state.player.getGold() * .1);
		String out = null;
		
		state.player.spendGold(lostGold);
		state.battleContext = null;
		
		out = "You duck behind a tree and manage to escape from encounter just barely!";
		if(lostGold > 0)
		{
			out += " As you make your final move to escape, you accidentally drop some of your gold behind. You don't dare try to go back and pick it up.";
			out += "\n";
			out += "\n";
			out += "[-" + lostGold + "gp]";
		}
		
		if(out != null)
			out = "```\n" + out + "```";
		
		return out;
	}

}
