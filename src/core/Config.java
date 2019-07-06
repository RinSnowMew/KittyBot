package core;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import utils.GlobalLog;
import utils.LogFilter;
import utils.io.FileMonitor;

public class Config
{	
	private static final String filepath = Constants.AssetDirectory + Constants.ConfigFilename; 
	
	private ConfigCSV configCSV;
	private List<IConfigSection> sections;
	private FileMonitor monitoredConfigFile;
	
	public static Config instance;
	
	public Config()
	{
		if(instance == null)
		{
			try
			{
				sections = new Vector<IConfigSection>();
				
				// Add all sections
				sections.add(new LocCommands());
				sections.add(new LocStrings());
				sections.add(new CommandEnabler());
				
				// Build the config file, then start monitoring it.
				buildConfigFile(filepath);
				monitoredConfigFile = new FileMonitor(filepath);
	
				instance = this;
			}
			catch(Exception e)
			{
				String logText = "Failure during config startup. The config must start correctly, or the bot cannot run - aborting. Error: " + e.getMessage();
				GlobalLog.error(LogFilter.Core, logText);
				System.exit(-1);
			}
		}
		else
		{
			GlobalLog.error(LogFilter.Core, "Only one config class is allowed, period. Aborting.");
			System.exit(-1);
		}
	}

	// This has more overhead but safely builds the file. 
	// It returns if it succeeded or not.
	private boolean safelyBuildConfigFile(String path)
	{
		try
		{
			buildConfigFile(path);
		}
		catch (Exception e)
		{
			GlobalLog.error("Failure during config file updating, aborting update immediately and attempting to salvage operation! The error was: " + e);
			return false;
		}
		
		return true;
	}
	
	// Builds the config file and will throw an exception if it fails
	public void buildConfigFile(String path) throws IOException
	{
		configCSV = new ConfigCSV(sections, path);
		
		Map<String, List<ConfigItem>> groupedSections = ConfigCSV.groupByColumn(configCSV.getItems(), 0);
		
		if(groupedSections != null)
		{
			for(IConfigSection section : sections)
			{
				List<ConfigItem> items = groupedSections.getOrDefault(section.getSectionTitle(), new Vector<ConfigItem>());
				section.consume(items);
			}
		}
		
		configCSV.writeFile();
	}

	// Call very frequently, but lazily. This will update the file content.
	public void upkeep()
	{
		monitoredConfigFile.update((monitoredFile) ->
		{
			GlobalLog.log("File updated at " + monitoredFile.path);
			
			// Attempt to rebuild. If we do it safely, then force-sync the DB and re-register.
			if(safelyBuildConfigFile(monitoredFile.path.toString()))
			{
				// Re-register all commands now that we've updated the config.
				// Before we do that tho, sync off anything that needs syncing.
				// TODO: This is not thread safe.
				DatabaseManager.instance.upkeep();
				CommandManager.instance.registerAllCommands();
			}
		});
	}
}
