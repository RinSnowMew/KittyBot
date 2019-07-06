package core.rpg;

public abstract class RPGUnit 
{
	protected int healthMax;
	protected int healthCurrent;
	
	public int getHealthMax() { return healthMax; }
	public int getHealthCurrent() { return healthCurrent; }
	public boolean isAlive() { return healthCurrent > 0; }

	// Generic implementation. Consider implementing armor in 
	// derived classes, for example.
	public void applyDamage(int value) 
	{ 
		if(value < 0)
			value = 0;
		
		healthCurrent -= value; 
	}
	
	// Generic implementation. Consider applying boosts in
	// derived classes, for example.
	public void applyHealing(int value) 
	{
		if(value < 0)
			value = 0;
		
		healthCurrent += value;
		
		if(healthCurrent > healthMax)
			healthCurrent = healthMax;
	}
}
