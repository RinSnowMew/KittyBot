package core.lua;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


import dataStructures.KittyUser;
import utils.GlobalLog;
import utils.LogFilter;
import utils.io.DirectoryMonitor;

// Reads in, handles, and manipulates plugins. Plugins are loaded in the order they appear in the folder.
public class PluginManager
{
	// Variables
	public ArrayList<Plugin> plugins;
	public DirectoryMonitor directoryMonitor;
	
	// Constructor
	public PluginManager(String folder)
	{
		this.directoryMonitor = new DirectoryMonitor(folder);
		plugins = new ArrayList<Plugin>();

		try
		{
			directoryMonitor.getCurrentFiles().forEach((file) -> addPlugin(file.path));
		}
		catch(Exception e)
		{
			PluginLog.error(e.getMessage());
		}
	}
	
	public void update()
	{
		directoryMonitor.update(
				(addedFile) ->
				{
					addPlugin(addedFile.path);
					
					GlobalLog.log(LogFilter.Plugin, "Found new plugin at " + addedFile.path);
				}, 
				(updatedFile) ->
				{
					for(Plugin plugin : plugins)
					{
						if(plugin.filepath.toString().equalsIgnoreCase(updatedFile.path.toString()))
						{
							plugin.reRead();
							break;
						}
					}
					
					GlobalLog.log(LogFilter.Plugin, "A plugin was updated " + updatedFile.path);
				}, 
				(deletedFile) ->
				{
					for(int i = plugins.size() - 1; i >= 0; i--)
					{
						Plugin plugin = plugins.get(i);
						
						if(plugin.filepath.toString().equalsIgnoreCase(deletedFile.path.toString()))
							plugins.remove(i);
					}
					
					GlobalLog.log(LogFilter.Plugin, "Plugin deleted at " + deletedFile.path);
				});
	}
	
	// Registers a plugin based on the path
	public void addPlugin(Path path)
	{
		plugins.add(new Plugin(path));
	}
	
	// Runs all plugins, returning when it gets a non-nill result. If there
	// are mutliple strings returned in the list, it is because the plugin
	// that was run returned multiple strings. Since plugins don't stack, it
	// will never indicate that multiple 
	// Otherwise, returns null.
	public List<String> runAll(String input, KittyUser user)
	{
		for(int i = 0; i < plugins.size(); ++i)
		{
			Plugin plugin = plugins.get(i);
			List<String> out = plugin.run(input, new PluginUser(user));

			if(out != null)
			{
				PluginLog.log("Executed plugin at " + plugin.filepath);
				return out;
			}
		}
		
		return null;
	}
	
	// Dumps the contents of all plugins for debug
	public void printAll()
	{
		for(int i = 0; i < plugins.size(); ++i)
		{
			PluginLog.log(plugins.get(i).contents.toString());
		}
	}
}
