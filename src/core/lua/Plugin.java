package core.lua;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
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
	
	public List<String> Run(String args, PluginUser user)
	{
		try
		{
			List<String> outputs = new Vector<String>();
			
			LuaValue res = func_plugin.call(LuaValue.valueOf(args), user.AsLua());
			
			if(!res.isnil())
			{
				if(res.istable())
				{
					LuaValue key = LuaValue.NIL;
					while (true)
					{
						Varargs n = res.next(key);
						key = n.arg1();
						if (key.isnil())
							break;
						
						LuaValue value = n.arg(2);
						outputs.add(value.toString());
					}
				}
				else
				{
					System.out.println("Loc2.5 " + res.toString());
					outputs.add(res.toString());
				}
			}
			
			if(outputs.size() <= 0)
				return null;
			
			return outputs;
			
		}
		catch(Exception e)
		{
			PluginLog.Error("Failed to run plugin from file " + filepath);
		}
		
		return null;
	}
}
