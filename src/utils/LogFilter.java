package utils;

public enum LogFilter
{
	Debug(0), Command(1), Core(2), Database(4), Response(8), Network(16), Strings(32); //8, 16, 32... (flags, so we can | together later) 
	
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
