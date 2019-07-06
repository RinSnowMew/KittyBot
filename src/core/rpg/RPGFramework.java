package core.rpg;

import java.util.HashMap;

import commands.rpg.RPGCommandBattleFight;
import commands.rpg.RPGCommandBattleRun;
import commands.rpg.RPGCommandExplore;
import commands.rpg.RPGCommandInfo;
import commands.rpg.RPGCommandStats;
import core.rpg.RPGInput;

// Holds the framework for the text RPG, for any number of users
public class RPGFramework
{
	// User ID to user state. Users are conceptually just an ID.
	public HashMap<String, RPGState> gameStates;
	public HashMap<String, RPGCommand> gameCommands;
	
	// Ctor
	public RPGFramework()
	{
		this.gameStates = new HashMap<String, RPGState>();
		this.gameCommands = new HashMap<String, RPGCommand>();
		
		registerCommand("stats", new RPGCommandStats());
		registerCommand("about", new RPGCommandInfo());
		registerCommand("info", new RPGCommandInfo());
		registerCommand("explore", new RPGCommandExplore());
		registerCommand("run", new RPGCommandBattleRun());
		registerCommand("fight", new RPGCommandBattleFight());
	}
	
	// Primary external
	public String run(String userID, String inputRaw)
	{
		RPGState state = lookupState(userID);
		RPGInput input = new RPGInput(inputRaw);
		return executeCommand(input.key, state, input);
	}

	// Get state for executing a command
	public RPGState lookupState(String userID)
	{
		RPGState state;
		synchronized(gameStates)
		{
			// Note: Hardcoded right now. Later: Extract.
			state = gameStates.get(userID);
			
			if(state == null)
			{
				state = new RPGState(userID);
				gameStates.put(userID, state);
			}
		}
		
		return state;
	}

	// Registers a command
	private void registerCommand(String commandName, RPGCommand command)
	{
		commandName = commandName.toLowerCase();
		if(gameCommands.put(commandName, command) != null)
			RPGLog.log("Managed to register the same RPG command twice! Not ideal!");
		
		RPGLog.log("Registered " + commandName);
	}
	
	private String executeCommand(String name, RPGState state, RPGInput input)
	{
		synchronized(gameCommands)
		{
			RPGCommand command = gameCommands.get(name.toLowerCase());
			
			if(command != null && state != null)
				return command.onRun(state,  input);
		}
		
		return null; 
	}
}
