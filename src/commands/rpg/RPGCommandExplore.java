package commands.rpg;

import java.util.Random;

import core.rpg.RPGBattleContext;
import core.rpg.RPGCommand;
import core.rpg.RPGInput;
import core.rpg.RPGState;

public class RPGCommandExplore extends RPGCommand
{
	private class Chance
	{
		double rand;
		float lastSection;
		
		public Chance(float percentChanceTotal)
		{
			rand = new Random().nextDouble() * percentChanceTotal;
			lastSection = 0;
		}
		
		public boolean Next(float amount)
		{
			lastSection += amount;
			return rand < lastSection;
		}
	}
	
	@Override
	public String onRun(RPGState state, RPGInput input)
	{
		Chance chance = new Chance(100);
		
		String out = null;
		String reward = null;
		
		if(state.battleContext != null)
			return "```You can't explore right now, you're in a fight! Try either 'fight' or 'run'!```";

		if(chance.Next(20))
		{
			out = "A creature jumps out of a bush at you!";
			reward = "Prepare to fight!";
			state.battleContext = new RPGBattleContext(state);
		}
		else if(chance.Next(20))
		{
			final int gp = 1;
			
			out = "As you take a stroll, you spy a small shiny glint from a bush and decide to investigate! Looks like it's your lucky day!";
			reward = "+" + gp + "gp";
			state.player.giveGold(gp);
		}
		else if(chance.Next(20))
		{
			final int healing = 1;
			final int xp = 25;
			
			out = "You head out on a lovely stroll down a familiar path - the sun is out and the birds are chirping! Nothing much comes  of it, but you feel refreshed.";
			reward = "+" + xp + "xp, +" + healing + "hp";
			state.player.applyHealing(healing);
			state.player.applyEXP(xp);
		}
		else if(chance.Next(20))
		{
			final int xp = 75;
			final int damage = 1;
			
			out = "Today's the day you head out on a new path. You find a lot of little nicknacks and trinkets on the trail, but leave them be. The trail gets really steep, the rocks jagged, but you keep going. Eventually, you make it out to the other side into a small but pleasant town, and collapse on a bench to catch your breath.";
			reward = "+" + xp + "xp, -" + damage + "hp";
			state.player.applyEXP(xp);
			state.player.applyDamage(damage);
		}
		else
		{
			final int hp = 2;
			
			out = "You step outside but it starts to rain. You decide not to do much today, and hang about the inn and the tavern.";
			reward = "+" + hp + "hp";
			state.player.applyHealing(hp);
		}
		
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
