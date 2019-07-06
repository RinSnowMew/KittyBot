package commands.rpg;

import core.rpg.RPGArmor;
import core.rpg.RPGCommand;
import core.rpg.RPGInput;
import core.rpg.RPGState;
import core.rpg.RPGWeapon;

public class RPGCommandInfo extends RPGCommand
{
	@Override
	public String onRun(RPGState state, RPGInput input)
	{
		String out = null;
		switch(input.value.trim().toLowerCase())
		{
			case "weapon":
			case "weapo":
			case "weap":
			case "wea":
			case "we":
			case "w":
			case "wepon":
			case "wepo":
			case "wep":
			case "hand":
			case "att":
			case "attack":
				out = weaponStats(state.player.getWeapon());
				break;

			case "armour":
			case "armou":
			case "armor":
			case "armr":
			case "armo":
			case "arm":
			case "ar":
			case "a":
			case "def":
			case "defense":
			case "body":
			case "dress":
			case "wear":
			case "outfit":
				out = armorStats(state.player.getArmor());
				break;
		}
		
		if(out != null)
			out = "```\n" + out + "```";
		
		return out;
	}
	
	private String weaponStats(RPGWeapon weapon)
	{
		if(weapon == null)
			return null;
		
		String out = "";
		out += "[" + weapon.getName() + "]";
		out += "\n";
		out += "  att: " + weapon.getAttack();
		out += "\n";
		out += "value: " + weapon.getValue() + "gp";
		out += "\n";
		out += "\n";
		out += weapon.getDescription();
		
		return out;
	}
	
	private String armorStats(RPGArmor armor)
	{
		if(armor == null)
			return null;
		
		String out = "";
		out += "[" + armor.getName() + "]";
		out += "\n";
		out += "  def: " + armor.getDefense();
		out += "\n";
		out += "value: " + armor.getValue() + "gp";
		out += "\n";
		out += "\n";
		out += armor.getDescription();
		
		return out;
	}
}
