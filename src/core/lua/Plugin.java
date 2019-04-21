package core.lua;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

// To promote flexibility, plugins are lua file with predefined callbacks.
// They are executed between the initial built-in preprocessing and the command parsing.
// This class wraps the lua layer - provides all the processing and function calls to the lua binding.
public class Plugin
{
	public Path filepath;
	public String contents;
	private Globals globals;
	private LuaValue func_plugin;
	
	public Plugin(Path filepath)
	{
		this.filepath = filepath;
		
		
		StringBuilder contentBuilder = new StringBuilder();

		try 
		{
			Stream<String> stream = Files.lines(filepath, StandardCharsets.UTF_8);
			stream.forEach(str -> contentBuilder.append(str).append("\n"));
			stream.close();
			contents = contentBuilder.toString();
			
			globals = JsePlatform.standardGlobals();
			
			globals.load(contents).call();
			func_plugin = globals.get("plugin");
		}
		catch (IOException e)
		{
			PluginLog.Error(e.getMessage());
		}
	}
	
	public String Run(String args, PluginUser user)
	{
		try
		{
			LuaValue res = func_plugin.call(LuaValue.valueOf(args), user.AsLua());
			
			if(!res.isnil())
				return res.toString();
		}
		catch(Exception e)
		{
			PluginLog.Error("Failed to run plugin from file " + filepath);
		}
		
		return null;
	}
}
