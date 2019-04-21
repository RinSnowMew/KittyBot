package core.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

// This class wraps the lua layer - provides all the processing and function
// calls to the lua binding.
public class PluginLoader
{
	private Globals globals;
	
	public PluginLoader()
	{
		globals = JsePlatform.standardGlobals();
		PluginLog.Log("Created new PluginLoader lua environment");
	}
	
	public String Process(Plugin toProcess, String args)
	{
		globals.load(toProcess.contents).call();
		LuaValue pluginFunc = globals.get("plugin");
		
		String output = null;
		
		try
		{
			LuaValue res = pluginFunc.call(LuaValue.valueOf(args));
			
			if(res.isnil())
				output = null;
			else
				output = res.toString();
		}
		catch(Exception e)
		{
			output = null;
		}
		
		return output;
	}
}
