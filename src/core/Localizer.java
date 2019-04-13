package core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import dataStructures.SectionedKeyValueStore;
import utils.FileUtils;
import utils.GlobalLog;
import utils.LogFilter;

// A quick-and-dirty localization tool that scrapes the project for calls to itself, then
// generates/updates a file externally (phrases.config) with all the stub values as keys that 
// can then be localized.
public class Localizer
{
	// Filename
	public final static String filename = "localization.config"; 
	public final static String functionName = "Localizer.Stub";
	
	// Local translation storage
	private static SectionedKeyValueStore stringStore; 
	
	// Logging
	private static void Log(String str) { GlobalLog.Log(LogFilter.Strings, str); }
	private static void Warn(String str) { GlobalLog.Warn(LogFilter.Strings, str); }
	private static void Error(String str) { GlobalLog.Error(LogFilter.Strings, str); }
	
	
	// Structure used for holding a pair of strings and any other info we need 
	// about localized information that is being looked up.
	private static class LocInfo
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
	public static void StripForContents(Path path, ArrayList<LocInfo> strings)
	{
		String filename = path.getFileName().toString();
		if(filename.contains(".java"))
		{
			String contents = FileUtils.ReadContent(path);
			String[] split = contents.split(functionName);
			
			// Identify all localizer function calls
			for(int i = 1; i < split.length; ++i)
			{
				int loc = split[i].indexOf(")");
				
				if(loc != -1)
				{
					if(split[i].charAt(loc - 1) == '"' && split[i].charAt(loc - 2) != '\\')
					{
						// At this point, we find the first ), then verify there's a ") behind it, and that
						// the " is not an escaped character.
						try
						{
							String toLocalize = split[i].substring(2, loc - 1);
							
							strings.add(new LocInfo(filename.substring(0, filename.lastIndexOf('.')), toLocalize));
							
							Log("Found stubbed phrase in " + path + " : " + toLocalize);
						}
						catch(IndexOutOfBoundsException e)
						{
							Warn("Found phrase but couldn't parse in file " + path);
						}
					}
				}
			}
		}
	}
	
	// Nothing for now, but in the future will return a parsed and localized version of
	// the string in question if one can be found. If the localized string is empty, 
	// returns a the key instead which is the default phrase.
	public static String Stub(String input)
	{
		if(stringStore == null)
			return input;
		
		String value = stringStore.GetKey(input);
		if(value == null || value.length() < 1)
			return input;
		
		return value;
	}
	
	// Reads a file to string, adapted from https://stackoverflow.com/a/326440/5383198
	static String ReadFileAsString(String path, Charset encoding)
	{
		try
		{
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		}
		catch (IOException e)
		{
			Warn("No file found to read from!");
		}
		
		return null;
	}

	// Update localization from the disk on file. Creates the file if it doesn't exist.
	// This file is internally formatted as an ini file.
	public static void UpdateLocFromDisk()
	{
		Log("Attempting to read localization file");
		
		try
		{
			String fileContents = ReadFileAsString(filename, Charset.defaultCharset());
			
			if(fileContents == null)
			{
				File file = new File(filename);
				file.createNewFile();
			}
			else
			{
				stringStore = new SectionedKeyValueStore(fileContents);
			}
		}
		catch(IOException e)
		{
			Error("IO exception during localization file read");
		}
	}
	
	// Rewrites out at the specified filename with existing stubs.
	// This preserves existing localized phrases.
	public static void SaveLocToDisk()
	{
		Log("Attempting to write updated localization file");
		
		try
		{	
			PrintWriter pw = new PrintWriter(filename);
			pw.println(stringStore.toString());
			pw.close();
		}
		catch(IOException e)
		{
			Error("IO exception during localization file write");
		}
	}

	// Scrape the project and generate all the possible localizeable phrases.
	// This stubs out phrases to be localized.
	public static void ScrapeAll()
	{
		ArrayList<LocInfo> localizeList = new ArrayList<LocInfo>();
		FileUtils.AcquireAllFiles(".\\src").forEach((path) -> StripForContents(path, localizeList));
		
		for(LocInfo toStub : localizeList)
			stringStore.AddKeyValue(toStub.file, toStub.phrase, toStub.phrase);
	}
}