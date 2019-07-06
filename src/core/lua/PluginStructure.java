package core.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class PluginStructure
{
	public LuaValue asLua()
	{
		return CoerceJavaToLua.coerce(this);
	}
}
