package core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import dataStructures.TaggedPairStore;
import utils.GlobalLog;
import utils.LogFilter;
import utils.io.FileMonitor;
import utils.io.FileUtils;
import utils.io.MonitoredFile;

// A quick-and-dirty localization tool that scrapes the project for calls to itself, then
// generates/updates a file externally with all the stub values as keys that are localized.
public abstract class BaseLocFile
{
	// Pre-defined values 
	public static final String KittySourceDirectory = "./src";
	
	// Filename
	public final String filename; // Example: "localization.config"; 
	public final String functionName; // Example: "Localizer.Stub";
	
	// Local translation storage
	protected TaggedPairStore stringStore; 
	
	// Logging
	private void log(String str) { GlobalLog.log(LogFilter.Strings, str); }
	private void warn(String str) { GlobalLog.warn(LogFilter.Strings, str); }
	private void error(String str) { GlobalLog.error(LogFilter.Strings, str); }
	
	// File monitoring
	protected FileMonitor fileMonitor;
	
	// Constructor 
	public BaseLocFile(String filename, String functionName)
	{
		this.filename = filename;
		this.functionName = functionName;
	}
	
	// Checks the file specified for updates
	public void update()
	{
		synchronized(stringStore)
		{
			fileMonitor.update(this::onFileChange);
		}
	}
	
	// When the file is changed
	protected void onFileChange(MonitoredFile file)
	{
		log("Loc file was modified at path " + file.path);
		updateLocFromDisk();
	}
	
	// Structure used for holding a pair of strings and any other info we need 
	// about localized information that is being looked up.
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
	
	// Do processing on each path in the scraped directory here, assuming it's .java
	public void stripForContents(Path path, ArrayList<LocInfo> strings)
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
						// At this point, we find the first ), then verify there's a ") behind it, and that
						// the " is not an escaped character.
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
	
	// Nothing for now, but in the future will return a parsed and localized version of
	// the string in question if one can be found. If the localized string is empty, 
	// returns a the key instead which is the default phrase.
	public String getKey(String input)
	{
		if(stringStore == null)
			return input;
		
		String value = stringStore.getKey(input);
		if(value == null || value.trim().length() < 1)
			return input;
		
		return value;
	}
	
	// Reads a file to string, adapted from https://stackoverflow.com/a/326440/5383198
	private String readFileAsString(String path, Charset encoding)
	{
		try
		{
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		}
		catch (IOException e)
		{
			warn("No file found to read from!");
		}
		
		return null;
	}

	// Update localization from the disk on file. Creates the file if it doesn't exist.
	// This file is internally formatted as an ini file.
	public void updateLocFromDisk()
	{
		log("Attempting to read localization file: " + filename);
		
		try
		{
			String fileContents = readFileAsString(filename, Charset.defaultCharset());
			
			if(fileContents == null)
			{
				File file = new File(filename);
				file.createNewFile();
			}
			
			stringStore = new TaggedPairStore(fileContents);
		}
		catch(IOException e)
		{
			error("IO exception during localization file read");
		}
	}
	
	// Rewrites out at the specified filename with existing stubs.
	// This preserves existing localized phrases.
	public void saveLocToDisk()
	{
		log("Attempting to write updated localization file");
		
		try
		{	
			PrintWriter pw = new PrintWriter(filename);
			pw.println(stringStore.toString());
			pw.close();
		}
		catch(IOException e)
		{
			error("IO exception during localization file write");
		}
	}

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
	
	// Scrape the project and generate all the possible localizeable phrases.
	// This stubs out phrases to be localized.
	public void scrapeAll()
	{
		ArrayList<LocInfo> localizeList = new ArrayList<LocInfo>();
		FileUtils.acquireAllFiles(KittySourceDirectory).forEach((path) -> tryStripSpecified(path, localizeList));
				
		for(LocInfo toStub : localizeList)
			stringStore.addKeyValue(toStub.file, toStub.phrase, toStub.phrase);
	}
}
