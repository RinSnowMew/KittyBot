package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

import dataStructures.Pair;
import utils.GlobalLog;
import utils.LogFilter;
import utils.io.FileUtils;

public class BaseKeyValueFile
{
	// Variables
	public static final String headerStart = "[";
	public static final String headerEnd = "]";
	public static final String pairSplit = "=";
	public static final char pairSeparator = '\n';
	
	protected final String filename;
	protected final String header;
	
	
	// Constructor
	public BaseKeyValueFile(String filename)
	{
		this.filename = filename;
		this.header = headerStart + this.getClass().getSimpleName() + headerEnd;
	}
	
	// Reads in and calls the specifid function for each keyvalue pair we find
	protected void Parse(Consumer<? super Pair<String, String>> keyValueCallback)
	{
		File f = new File(filename);
		if(f.isFile() && f.canRead())
		{
			String content = FileUtils.ReadContent(f).trim();
			String[] lines = content.split("" + pairSeparator);
			
			for(int i = 0; i < lines.length; ++i)
			{
				if(lines[i].contains(header))
					continue;
				
				String[] pair = lines[i].split(pairSplit);
				
				if(pair.length < 2)
					continue;
				
				String key = pair[0].trim().toLowerCase();
				String value = pair[1].trim().toLowerCase();
				
				keyValueCallback.accept(new Pair<String, String>(key, value));
			}
		}
	}
	
	// Writes out a set of keyvalue pairs
	protected void Write(List<Pair<String, String>> toWrite)
	{
		try
		{
			ListIterator<Pair<String, String>> iter = toWrite.listIterator();
			
			String outString = "";
			outString += header + pairSeparator;
			
			while(iter.hasNext())
			{
				Pair<String, String> pair = iter.next();
				String key = pair.First.toLowerCase();
				String value = pair.Second.toLowerCase();
				
				outString += key + pairSplit + value + pairSeparator;
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(outString);
			writer.close();
		}
		catch (IOException e)
		{
			GlobalLog.Error(LogFilter.Core,  "Issue writing file " + filename + ": " + e.getMessage());
		}
	}
}
