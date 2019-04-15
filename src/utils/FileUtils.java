package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FileUtils
{
	// Reads all lines from a file as a string
	public static String ReadContent(File file) { return ReadContent(file.toPath()); }
	public static String ReadContent(Path filePath)
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
			GlobalLog.Error(LogFilter.Util, e.getMessage());
		}
		
		return contentBuilder.toString();
	}

	// Recursively acquires all files at and below the specified directory, returning them as an arraylist of paths.
	public static ArrayList<Path> AcquireAllFiles(String startingDir) 
	{
		ArrayList<Path> items = new ArrayList<Path>();
		
		File tmpDir = new File(startingDir);
		if(!tmpDir.exists())
			return new ArrayList<Path>();
		
		
		try
		{
			Files.find(Paths.get(startingDir), 999, (path, attributes) -> attributes.isRegularFile()).forEach(items::add);
		}
		catch (IOException e)
		{
			GlobalLog.Error(LogFilter.Util, e.getMessage());
		}
		
		return items;
	}
}
