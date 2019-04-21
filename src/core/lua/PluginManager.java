package core.lua;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Reads in, handles, and manipulates plugins. Plugins are loaded in the order they appear in the folder.
public class PluginManager
{
	public PluginLoader pluginLoader;
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
		this.pluginLoader = new PluginLoader();
		
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
	
	public String CallAll(String input)
	{
		for(int i = 0; i < plugins.size(); ++i)
		{
			Plugin script = plugins.get(i);
			String out = pluginLoader.Process(script, input);
			
			if(out != null)
			{
				PluginLog.Log("Executed plugin at " + script.filepath);
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
