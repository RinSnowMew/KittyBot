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
import utils.FileUtils;
import utils.GlobalLog;
import utils.LogFilter;

// A quick-and-dirty localization tool that scrapes the project for calls to itself, then
// generates/updates a file externally with all the stub values as keys that are localized.
public abstract class LocBase
{
	// Pre-defined values 
	public static final String KittySourceDirectory = "./src";
	
	// Filename
	public final String filename; // Example: "localization.config"; 
	public final String functionName; // Example: "Localizer.Stub";
	
	// Local translation storage
	protected TaggedPairStore stringStore; 
	
	// Logging
	private void Log(String str) { GlobalLog.Log(LogFilter.Strings, str); }
	private void Warn(String str) { GlobalLog.Warn(LogFilter.Strings, str); }
	private void Error(String str) { GlobalLog.Error(LogFilter.Strings, str); }
	
	// Ok... so this is an array because if it's not an array, the parser will parse the string 
	public LocBase(String filename, String functionName)
	{
		this.filename = filename;
		this.functionName = functionName;
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
	public void StripForContents(Path path, ArrayList<LocInfo> strings)
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
							
							Log("Found lookup call in " + path + ": " + toLocalize);
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
	public String GetKey(String input)
	{
		if(stringStore == null)
			return input;
		
		String value = stringStore.GetKey(input);
		if(value == null || value.trim().length() < 1)
			return input;
		
		return value;
	}
	
	// Reads a file to string, adapted from https://stackoverflow.com/a/326440/5383198
	private String ReadFileAsString(String path, Charset encoding)
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
	public void UpdateLocFromDisk()
	{
		Log("Attempting to read localization file: " + filename);
		
		try
		{
			String fileContents = ReadFileAsString(filename, Charset.defaultCharset());
			
			if(fileContents == null)
			{
				File file = new File(filename);
				file.createNewFile();
			}
			
			stringStore = new TaggedPairStore(fileContents);
		}
		catch(IOException e)
		{
			Error("IO exception during localization file read");
		}
	}
	
	// Rewrites out at the specified filename with existing stubs.
	// This preserves existing localized phrases.
	public void SaveLocToDisk()
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

	private void TryStripSpecified(Path path, ArrayList<LocInfo> toFill)
	{
		try
		{
			StripForContents(path, toFill);
		}
		catch(Exception e)
		{
			Error("issue with file: " + path.toString());
		}
	}
	
	// Scrape the project and generate all the possible localizeable phrases.
	// This stubs out phrases to be localized.
	public void ScrapeAll()
	{
		ArrayList<LocInfo> localizeList = new ArrayList<LocInfo>();
		FileUtils.AcquireAllFiles(KittySourceDirectory).forEach((path) -> TryStripSpecified(path, localizeList));
				
		for(LocInfo toStub : localizeList)
			stringStore.AddKeyValue(toStub.file, toStub.phrase, toStub.phrase);
	}
}
