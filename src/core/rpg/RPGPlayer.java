package core.rpg;

public class RPGPlayer extends RPGUnit 
{
	private String name;
	private long gold;
	private long exp;
	
	RPGWeapon weapon;
	RPGArmor armor;
	
	public RPGPlayer()
	{
		super();
		
		name = "Wanderer";
		healthCurrent = 5;
		healthMax = 5;
		gold = 0;
		weapon = new RPGWeapon();
		armor = new RPGArmor();
	}
	
	// Getters
	public long getEXP() { return exp; }
	public long getGold() { return gold; }
	public String getName() { return name; }
	public RPGArmor getArmor() { return armor; };
	public RPGWeapon getWeapon() { return weapon; };
	
	// Setters
	public void setName(String name) { this.name = name; }
	
	// Interactions
	public void applyEXP(int expToGive)
	{
		if(expToGive < 0)
			expToGive = 0;
		
		exp += expToGive;
	}
	
	public void giveGold(long amount) 
	{
		if(amount < 0)
			amount = 0;
		
		gold += amount;
	}
	
	public void spendGold(long amount)
	{
		if(amount < 0)
			amount = 0;
		
		gold -= amount;
	}
}
