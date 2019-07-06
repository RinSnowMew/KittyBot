package commands.rpg;

import core.rpg.RPGArmor;
import core.rpg.RPGCommand;
import core.rpg.RPGExpTable;
import core.rpg.RPGInput;
import core.rpg.RPGPlayer;
import core.rpg.RPGState;
import core.rpg.RPGWeapon;

public class RPGCommandStats extends RPGCommand
{
	@Override
	public String onRun(RPGState state, RPGInput input) 
	{
		RPGPlayer player = state.player;
		long exp = player.getEXP();
		long level = RPGExpTable.levelFromEXP(player.getEXP());
		long ceil = RPGExpTable.expCeil(level);
		
		String indent = "";
		String linebreak = "\n";
		
		String out = "";
		out += indent + "[" + player.getName() + ", lv. " + level + "]";
		out += indent + linebreak;
		out += indent + "_______________";
		out += indent + linebreak;
		
		out += indent + "   EXP: " + exp;
		out += indent + " (until next: " + (ceil - exp) + ")";
		out += indent + linebreak;
				
		out += indent + "  Gold: " + player.getGold();
		out += indent + linebreak;
		
		out += indent + "Health: " + player.getHealthCurrent() + "/" + player.getHealthMax();
		out += indent + linebreak;
		
		RPGWeapon weapon = player.getWeapon();
		out += indent + "_____";
		out += indent + linebreak; 
		out += indent + "Weapon: " + weapon.getName();
		out += indent + linebreak;
		out += indent + "   att: " + weapon.getAttack();
		out += indent + linebreak;
		
		RPGArmor armor = player.getArmor();
		out += indent + "_____";
		out += indent + linebreak; 
		out += indent + "Armor: " + armor.getName();
		out += indent + linebreak;
		out += indent + "  def: " + armor.getDefense() ;
		out += indent + linebreak;
		
		out += indent + linebreak;
		out += "Use 'info armor' / 'info weapon' for details!";
		
		return "```\n" + out + "```";
	}
}
