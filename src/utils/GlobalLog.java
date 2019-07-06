package utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.io.FileUtils;

public class GlobalLog 
{
	private static final String directory = "logs/";
	private static final String log = "Log";
	private static final String warn = "Warning";
	private static final String error = "ERROR";
	private static final String fatal = "FATAL";
	private static PrintWriter outputLog;
	
	public static void initialize() throws FileNotFoundException, UnsupportedEncodingException
	{
		DateFormat dF = new SimpleDateFormat("yyyy_MM_dd_HH-mm");
		Date today = new Date(); 
		
		FileUtils.createDirectoryIfDoesntExist(directory);
		outputLog = new PrintWriter(directory + (dF.format(today) + ".log"), "UTF-8");
		GlobalLog.log(LogFilter.Util, "Finished initializing logging system");
	}
	
	private static void write(String status, LogFilter filter, String body)
	{
		String logLine = "[" + status + "] " + "[" + filter.name() + "] " + body; 
		outputLog.write(logLine + "\n");
		outputLog.flush();
		System.out.println(logLine);
	}
	
	public static void log(LogFilter filter, String msg) { write(log, filter, msg); } 
	public static void warn(LogFilter filter, String msg) { write(warn, filter, msg); }
	public static void error(LogFilter filter, String msg) { write(error, filter, msg); System.err.println(msg); System.err.flush(); }
	public static void fatal(LogFilter filter, String msg) throws Exception { write(fatal, filter, msg); throw new Exception(msg); }
	
	public static void log(String msg) { log(LogFilter.Debug, msg); } 
	public static void warn(String msg) { warn(LogFilter.Debug, msg); }
	public static void error(String msg) { error(LogFilter.Debug, msg); }
	public static void fatal(String msg) throws Exception { fatal(LogFilter.Debug, msg); }
	
}
