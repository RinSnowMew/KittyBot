package core.rpg;

public class RPGWeapon extends RPGItem
{
	private long attack;
	private double accuracy;
	
	RPGWeapon() 
	{
		super("Singed Stick", "A really cool stick that you found! You poked at your campfire last night with it a bit, so the end is a bit toasty.", 2);
		attack = 1;
		accuracy = 0.8;
	}
	
	public long getAttack() { return attack; }
	public double getAccuracy() { return accuracy; }
}
