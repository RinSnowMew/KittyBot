package core.lua;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import dataStructures.KittyUser;

// Reads in, handles, and manipulates plugins. Plugins are loaded in the order they appear in the folder.
public class PluginManager
{
	public final String pluginFolder;
	public ArrayList<Plugin> plugins;

	public void AddPlugin(Path path)
	{
		plugins.add(new Plugin(path));
	}
	
	public PluginManager(String folder)
	{
		this.pluginFolder = folder;
		plugins = new ArrayList<Plugin>();

		try
		{
			try (Stream<Path> paths = Files.walk(Paths.get(this.pluginFolder)))
			{
				paths.filter(Files::isRegularFile).forEach((path)->{ AddPlugin(path); });
			}
		}
		catch(Exception e)
		{
			PluginLog.Error(e.getMessage());
		}
	}
	
	// Runs all plugins, returning when it gets a non-nill result.
	// Otherwise, returns null.
	public String RunAll(String input, KittyUser user)
	{
		for(int i = 0; i < plugins.size(); ++i)
		{
			Plugin plugin = plugins.get(i);
			String out = plugin.Run(input, new PluginUser(user));
			
			if(out != null)
			{
				PluginLog.Log("Executed plugin at " + plugin.filepath);
				return out;
			}
		}
		
		return null;
	}
	
	public void PrintAll()
	{
		for(int i = 0; i < plugins.size(); ++i)
			PluginLog.Log(plugins.get(i).contents.toString());
	}
}
