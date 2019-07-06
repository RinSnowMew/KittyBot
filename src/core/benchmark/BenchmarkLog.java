package core.benchmark;

import utils.GlobalLog;

//Logging shim
public class BenchmarkLog
{	
	public static void log(String str) { GlobalLog.log("[Benchmark] " + str); }
	public static void warn(String str) { GlobalLog.warn("[Benchmark] " + str); }
	public static void error(String str) { GlobalLog.error(" [Benchmark] " + str); }
}
