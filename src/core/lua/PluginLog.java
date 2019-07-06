package core.lua;

import utils.GlobalLog;
import utils.LogFilter;

public class PluginLog
{
	public static void log(String s) { GlobalLog.log(LogFilter.Plugin, s); }
	public static void warn(String s) { GlobalLog.warn(LogFilter.Plugin, s); }
	public static void error(String s) { GlobalLog.error(LogFilter.Plugin, s); }
}
