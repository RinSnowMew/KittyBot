package utils;

public enum LogFilter
{
	// Assign numbers as flags, so we can | ('or') them together as necessary
	Debug(0), Command(1), Core(2), Util(4), Database(8), Response(16), Network(32), Strings(64);  
	
	private final int value;
	private LogFilter(int value) 
	{
		this.value = value;
	}
	
	public int getValue() 
	{
		return value;
	}
}
