package core.lua;

import utils.GlobalLog;
import utils.LogFilter;

public class PluginLog
{
	public static void Log(String s) { GlobalLog.Log(LogFilter.Plugin, s); }
	public static void Warn(String s) { GlobalLog.Warn(LogFilter.Plugin, s); }
	public static void Error(String s) { GlobalLog.Error(LogFilter.Plugin, s); }
}
