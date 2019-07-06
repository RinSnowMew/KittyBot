package core.benchmark;

import java.util.HashMap;
import commands.benchmark.*;

// Based on general core structure, this registers sub-commands and keeps tabs on them.
public class BenchmarkFramework
{
	// Variables
	public HashMap<String, BenchmarkCommand> benchmarkCommand;
	public BenchmarkManager benchmarkManager;
	
	// Constructor and command registration
	public BenchmarkFramework()
	{
		this.benchmarkManager = new BenchmarkManager();
		this.benchmarkCommand = new HashMap<String, BenchmarkCommand>();
		
		registerCommand("find", new BenchmarkCommandFind());
		registerCommand("compare", new BenchmarkCommandCompare());
		registerCommand("info", new BenchmarkCommandInfo());
	}
	
	// Runs a command if possible.
	public BenchmarkFormattable run(String args)
	{
		BenchmarkInput input = new BenchmarkInput(args);
		return executeCommand(input.key, input);
	}
	
	public void update()
	{
		synchronized(benchmarkManager)
		{
			benchmarkManager.update();
		}
	}
	
	// Registers a command
	private void registerCommand(String commandName, BenchmarkCommand command)
	{
		commandName = commandName.toLowerCase();
		if(benchmarkCommand.put(commandName, command) != null)
			BenchmarkLog.log("Multiple registration of a command with name '" + commandName + "'!");
		
		BenchmarkLog.log("Registered " + commandName);
	}
	
	// Executes a command with the specified name, and provides it with some extra input data.
	private BenchmarkFormattable executeCommand(String name, BenchmarkInput input)
	{
		BenchmarkCommand command = benchmarkCommand.get(name.toLowerCase());
		
		if(command != null && benchmarkManager != null)
			return command.onRun(benchmarkManager,  input);
		
		return null; 
	}
}
