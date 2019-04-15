package utils;

public class GlobalLog 
{
	private static final String log = "Log";
	private static final String warn = "Warning";
	private static final String error = "ERROR";
	private static final String fatal = "FATAL";
	
	private static void Write(String status, LogFilter filter, String body)
	{
		System.out.println("[" + status + "] " + "[" + filter.name() + "] " + body);
	}
	
	public static void Log(String msg) { Write(log, LogFilter.Debug, msg); } 
	public static void Warn(String msg) { Write(warn, LogFilter.Debug, msg); }
	public static void Error(String msg) { Write(error, LogFilter.Debug, msg); }
	public static void Fatal(String msg) throws Exception { Write(fatal, LogFilter.Debug, msg); throw new Exception(msg); }
	
	public static void Log(LogFilter filter, String msg) { Write(log, filter, msg); } 
	public static void Warn(LogFilter filter, String msg) { Write(warn, filter, msg); }
	public static void Error(LogFilter filter, String msg) { Write(error, filter, msg); }
	public static void Fatal(LogFilter filter, String msg) throws Exception { Write(fatal, filter, msg); throw new Exception(msg); }
}
