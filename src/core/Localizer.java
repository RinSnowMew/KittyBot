package core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

// A quick-and-dirty localization tool that scrapes the project for calls to itself, then
// generates/updates a file externally (phrases.config) with all the stub values as keys that 
// can then be localized.
public class Localizer
{
	// Filename
	public final static String filename = "phrases.config"; 
	public final static String functionName = "Localizer.Stub";
	
	// Local translation
	private static HashMap<String, LocPair> translated = new HashMap<String, LocPair>();
	
	// Logging
	private static void Log(String str) { System.out.println("[Log] " + str); }
	private static void Warn(String str) { System.out.println("[Warn] " + str); }
	private static void Error(String str) { System.err.println("[Error] " + str); }
	
	// Util: Reads all lines from a file as a string
	private static String ReadContent(Path filePath)
	{
		StringBuilder contentBuilder = new StringBuilder();
		
		try 
		{
			Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8);
			
			stream.forEach(str -> contentBuilder.append(str).append("\n"));
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return contentBuilder.toString();
	}

	// Recursively acquires all files at and below the specified directory, returning them as an arraylist of paths.
	public static ArrayList<Path> AcquireAllFiles(String startingDir) 
	{
		ArrayList<Path> items = new ArrayList<Path>();
		
		try
		{
			Files.find(Paths.get(startingDir), 999, (path, attributes) -> attributes.isRegularFile()).forEach(items::add);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return items;
	}
	
	public static class LocPair
	{
		public String file;
		public String phrase;
		
		public LocPair(String f, String p)
		{
			this.file = f;
			this.phrase = p;
		}
	}
	
	// Do processing on each path in the scraped directory here, assuming it's .java
	public static void StripForContents(Path path, ArrayList<LocPair> strings)
	{
		String filename = path.getFileName().toString();
		if(filename.contains(".java"))
		{
			String contents = ReadContent(path);
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
							
							strings.add(new LocPair(filename.substring(0, filename.lastIndexOf('.')), toLocalize));
							
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
	// the string in question if one can be found.
	public static String Stub(String input)
	{
		if(translated.containsKey(input))
		{
			String value = translated.get(input).phrase;
			if(value.trim().length() > 0)
				return value;
		}
		
		return input;
	}
	
	// Update localization from the disk on file. Creates the file if it doesn't exist.
	// This file is internally formatted as an ini file.
	public static void UpdateLocFromDisk()
	{
		Log("Attempting to read localization file");
		try
		{
			File file = new File(filename);
			file.createNewFile();
			
			Wini ini = new Wini(file);
			for(String str : ini.keySet())
			{
				// Store everything in all .ini sections
				Section sec = ini.get(str);
				for(String s : sec.keySet())
				{
					if(!translated.containsKey(s))
						translated.put(s, new LocPair(sec.getName(), sec.get(s, String.class)));
				}
			}
		}
		catch(InvalidFileFormatException e) 
		{
			Error("File issue with format during read");
		}
		catch(IOException e)
		{
			Error("IO exception during read");
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
			pw.close();
			
			File file = new File(filename);
			file.createNewFile();
			
			Wini ini = new Wini(file);

			for(String s : translated.keySet())
			{
				LocPair toWrite = translated.get(s);
				ini.put(toWrite.file, s, toWrite.phrase);
			}
				
			ini.store();
		}
		catch(InvalidFileFormatException e)
		{
			Error("File issue with format during write");
		}
		catch(IOException e)
		{
			Error("IO exception during write");
		}
	}

	// Scrape the project and generate all the possible localizeable phrases.
	// This stubs out phrases to be localized.
	public static void ScrapeAll()
	{
		ArrayList<LocPair> localizeList = new ArrayList<LocPair>();
		AcquireAllFiles(".\\src").forEach((path) -> StripForContents(path, localizeList));
		
		for(LocPair toStub : localizeList)
		{
			if(!translated.containsKey(toStub.phrase))
			{
				translated.put(toStub.phrase, new LocPair(toStub.file, ""));
				Log("Found new stubbed phrase '" + toStub.phrase + "' in " + toStub.file);
			}
		}
	}
}