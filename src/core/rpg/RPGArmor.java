package core.rpg;

public class RPGArmor extends RPGItem
{
	int defense;
	
	public RPGArmor() 
	{
		super("Tattered Clothes", "The remains of your first sewing project. You did a pretty good job!", 5);
		defense = 1;
	}
	
	public long getDefense() { return defense; }
}
