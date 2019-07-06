package core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import utils.GlobalLog;
import utils.LogFilter;
import utils.io.FileUtils;


// A quick-and-dirty localization tool that scrapes the project for calls to itself, then
// generates/updates a file externally with all the stub values as keys that are localized.
public abstract class BaseLocFile implements IConfigSection
{
	// Variables
	protected final String headerName; // Example: Loc Strings
	protected final String functionName; // Example: "Localizer.Stub";
	protected Map<String, String> localized;
	
	// Logging
	private void log(String str) { GlobalLog.log(LogFilter.Strings, str); }
	private void warn(String str) { GlobalLog.warn(LogFilter.Strings, str); }
	private void error(String str) { GlobalLog.error(LogFilter.Strings, str); }
	
	// Constructor 
	public BaseLocFile(String headerName, String functionName)
	{
		this.headerName = headerName;
		this.functionName = functionName;
		localized = new HashMap<String, String>();
	}

	  //////////////////////////////////////////////////
	 // First Step: Populating with existing strings //
	//////////////////////////////////////////////////
	
	private void buildLocalized(List<ConfigItem> pairs)
	{
		for(ConfigItem item : pairs)
		{
			if(item.value.trim().length() <= 0)
			{
				localized.put(item.key, item.key);
			}
			else
			{
				localized.put(item.key, item.value);
			}
		}
	}
	
	  //////////////////////////////////////////
	 // Second Step: Scraping existing files //
	//////////////////////////////////////////
	
	// Structure used for holding a pair of strings and any other info we need 
	// about localized information that is being looked up.
	@SuppressWarnings("unused")
	private class LocInfo
	{
		public String file;
		public String phrase;
		
		public LocInfo(String f, String p)
		{
			this.file = f;
			this.phrase = p;
		}
	}
	
	// Try to perform stripping java files for contents to localize
	private void tryStripSpecified(Path path, ArrayList<LocInfo> toFill)
	{
		try
		{
			stripForContents(path, toFill);
		}
		catch(Exception e)
		{
			error("issue with file: " + path.toString());
		}
	}
	
	// Do processing on each path in the scraped directory here, assuming it's .java
	private void stripForContents(Path path, ArrayList<LocInfo> strings)
	{
		String filename = path.getFileName().toString();
		if(filename.contains(".java"))
		{
			String contents = FileUtils.readContent(path);
			String[] split = contents.split(functionName);
			
			// Identify all localizer function calls
			for(int i = 1; i < split.length; ++i)
			{
				int loc = split[i].indexOf(")");
				String noWhitespace = split[i].replaceAll("\\s+","");
				
				if(loc != -1)
				{
					if(noWhitespace.charAt(noWhitespace.indexOf(")") - 1) == '"' && split[i].charAt(loc - 2) != '\\')
					{
						// At this point we find the first ), then verify there's a ") behind it, 
						// and that the " is not an escaped character.
						try
						{
							String toLocalize = split[i].substring(2, loc - 1);
							
							strings.add(new LocInfo(filename.substring(0, filename.lastIndexOf('.')), toLocalize));
							
							log("Found lookup call in " + path + ": " + toLocalize);
						}
						catch(IndexOutOfBoundsException e)
						{
							warn("Found phrase but couldn't parse in file " + path);
						}
					}
				}
			}
		}
	}
	
	// Returns the localized string if available. 
	// If the localized string is empty, returns a the key instead as the default phrase.
	public String getKey(String input)
	{
		if(localized == null)
		{
			return input;
		}
		
		String value = localized.get(input);
		if(value == null || value.trim().length() < 1)
		{
			return input;
		}
		
		return value;
	}
	
	// Scrape the project and generate all the possible localizeable phrases.
	// This stubs out phrases to be localized, by default placing the key in as the value.
	public void scrapeAll()
	{
		ArrayList<LocInfo> localizeList = new ArrayList<LocInfo>();
		FileUtils.acquireAllFiles(Constants.SourceDirectory).forEach((path) -> tryStripSpecified(path, localizeList));
				
		for(LocInfo toStub : localizeList)
		{
			localized.putIfAbsent(toStub.phrase, toStub.phrase);
		}
	}
	
	  ///////////////////////////////////
	 // IConfigSection Implementation //
	///////////////////////////////////
	
	@Override
	public String getSectionTitle()
	{
		return headerName;
	}
	
	@Override
	public void consume(List<ConfigItem> pairs)
	{
		localized.clear();
		buildLocalized(pairs);
		scrapeAll();
	}
	
	@Override
	public List<ConfigItem> produce()
	{
		List<ConfigItem> items = new Vector<ConfigItem>();

		for(Entry<String, String> keyValuePair : localized.entrySet())
		{
			String key = keyValuePair.getKey();
			String value = keyValuePair.getValue();
			
			if(key.equalsIgnoreCase(value))
			{
				items.add(new ConfigItem(headerName, key, ""));
			}
			else
			{
				items.add(new ConfigItem(headerName, key, value));
			}
		}
		
		items.sort((item1, item2) -> item1.key.compareToIgnoreCase(item2.key));
		
		return items;
	}
}
