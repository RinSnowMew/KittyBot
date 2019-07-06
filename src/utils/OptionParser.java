package utils;

import java.util.ArrayList;
import java.util.HashMap;

public class OptionParser
{
	public final char OptionIndicator = '-';
	
	private class Option
	{
		public String optionRaw = "";
		public String argRaw = null;
		
		public Option(String option, String arg)
		{
			optionRaw = option;
			
			if(arg != null && arg.length() == 0)
				arg = null;
				
			argRaw = arg;
		}
	}
	
	// lowercase option to Option object 
	// -a argument -B -c would result in the following:
	// Key: a, Value: a, argument
	// Key: b, Value: B, <null>
	// Key: c, Value: c, <null>
	private HashMap<String, Option> lookup;
	
	// Accumulates the freefloating options
	private ArrayList<String> floating;
	
	// Constructor
	public OptionParser(String toParse)
	{
		lookup = new HashMap<String, Option>();
		floating = new ArrayList<String>();
		
		if(toParse != null && toParse.length() > 0)
			parse(toParse);
	}
	
	public void parse(String vals)
	{
		String[] strings = vals.split("\\s+");
		
		// Clean
		for(int i = 0; i < strings.length; ++i)
			strings[i] = strings[i].trim();
		
		// Add to map
		for(int i = 0; i < strings.length; ++i)
		{
			if(strings[i] != null && strings[i].length() > 0)
			{
				if(strings[i].charAt(0) == OptionIndicator && strings[i].length() > 1)
				{
					String option = "" + strings[i].charAt(1);
					
					if(i + 1 < strings.length && strings[i + 1].charAt(0) != OptionIndicator)
					{
						lookup.put(option.toLowerCase(), new Option(option, strings[i + 1]));
						++i;
					}
					else
					{
						String val = null;

						if(strings[i].length() > 2)
							val = strings[i].substring(2, strings[i].length());
						
						lookup.put(option.toLowerCase(), new Option(option, val));
					}
				}
				else
				{
					floating.add(strings[i]);
				}
			}
				
		}
	}
	
	public ArrayList<String> getFloating()
	{
		return floating;
	}
	
	public String getOption(String arg)
	{
		return getOption(arg, false);
	}
	
	public String getOption(String option, boolean isCaseInsensitive)
	{
		if(option == null)
			return null;
		
		option.trim();
		
		if(option.length() == 0)
			return null;
		
		if(option.charAt(0) == OptionIndicator && option.length() > 1)
			option = option.substring(1, option.length());
		
		Option parsedOption = lookup.get(option.toLowerCase());
		if(parsedOption != null)
		{
			if(isCaseInsensitive)
			{
				return parsedOption.argRaw;
			}
			else
			{
				if(parsedOption.optionRaw == option)
					return parsedOption.argRaw;
			}
		}
		
		return null;	
	}
}
