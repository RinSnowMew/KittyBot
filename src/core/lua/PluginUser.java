package core.lua;

import dataStructures.KittyUser;

public class PluginUser extends PluginStructure
{
	public String name;
	
	public PluginUser(KittyUser user)
	{
		this.name = user.name;
	}
}
