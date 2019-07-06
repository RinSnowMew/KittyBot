package core.rpg;

import java.util.Scanner;

// This is for testing only, and generally doesn't run unless it's specified.
public class RPGMain
{
	private static RPGFramework framework;
	private static Scanner scanner;
	
	public static void RPGmain(String[] args)
	{
		init();
		while(run()) { };
		shutdown();
	}
	
	// Initializes stuff (small factory, ish)
	private static void init()
	{
		framework = new RPGFramework();
		scanner = new Scanner(System.in);
	}
	
	// Main loop
	private static boolean run()
	{
		// move outside
		String out = framework.run("Test", scanner.nextLine()); 
		RPGLog.log(out);
		return true;
	}
	
	// Cleanup
	private static void shutdown()
	{
		scanner.close();
	}
}