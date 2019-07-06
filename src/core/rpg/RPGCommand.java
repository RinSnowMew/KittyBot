package core.rpg;

public abstract class RPGCommand
{
	public RPGCommand() { }
	
	// OVERRIDE ME
	public abstract String onRun(RPGState state, RPGInput input);
}
