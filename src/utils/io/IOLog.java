package utils.io;

import utils.GlobalLog;
import utils.LogFilter;

// Logging shim
public class IOLog
{
	public static void log(String s) { GlobalLog.log(LogFilter.Util, s); }
	public static void warn(String s) { GlobalLog.warn(LogFilter.Util, s); }
	public static void error(String s) { GlobalLog.error(LogFilter.Util, s); }
}
