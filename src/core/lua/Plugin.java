package core.lua;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

//To promote flexibility, plugins are lua file with predefined callbacks.
//They are executed between the initial built-in preprocessing and the command parsing.
public class Plugin
{
	public Path filepath;
	public String contents;
	
	public Plugin(Path filepath)
	{
		this.filepath = filepath;
		
		StringBuilder contentBuilder = new StringBuilder();
		
		try 
		{
			Stream<String> stream = Files.lines(filepath, StandardCharsets.UTF_8);
			stream.forEach(str -> contentBuilder.append(str).append("\n"));
			stream.close();
			contents = contentBuilder.toString();
		}
		catch (IOException e)
		{
			PluginLog.Error(e.getMessage());
		}
	}
}
