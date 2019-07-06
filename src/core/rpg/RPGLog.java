package core.rpg;

import utils.GlobalLog;
import utils.LogFilter;

public class RPGLog 
{
	public static void log(String toWrite)
	{
		GlobalLog.log(LogFilter.Command, "[RPG] " + toWrite);
	}
}
