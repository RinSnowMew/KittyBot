package core;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.RFC4180ParserBuilder;

import utils.GlobalLog;
import utils.StringUtils;

public class ConfigCSV
{
	// Variables
	private final String path;
	private List<ConfigItem> fileContents;
	private List<IConfigSection> sections;
	
	// Constructor
	public ConfigCSV(List<IConfigSection> sections, String path) throws IOException
	{
		this.path = path;
		this.sections = sections;
		
		fileContents = new Vector<ConfigItem>();
		
		read(path);
	}
	
	// Creates the writer in the format we desire.
	public static CSVWriter makeWriter(Writer writer)
	{
		return new CSVWriter(writer,
				CSVWriter.DEFAULT_SEPARATOR,
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER,
				CSVWriter.DEFAULT_LINE_END);
	}
	
	// Read the file. This is internal only.
	private void read(String filepath) throws IOException
	{
		File configFile = new File(filepath);
		
		// If we had to create a new file, write the default header into it.
		if(configFile.createNewFile())
		{
			Writer writer = Files.newBufferedWriter(Paths.get(path));
			CSVWriter csvWriter = makeWriter(writer);
			csvWriter.writeNext(ConfigItem.header);
			csvWriter.close();
		}
		
		Reader reader = Files.newBufferedReader(Paths.get(filepath));
		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(new RFC4180ParserBuilder().build()).build();
		fileContents.clear();
		
		// Read contents. As for the line number, we not only start counting at 1, we also skip the header.
		int line = 2; 
		String[] record = null;
		while ((record = csvReader.readNext()) != null)
		{
			if(record.length < ConfigItem.items)
			{
				GlobalLog.warn("Incorrect number of items on line " + line + " in " + path);
				continue;
			}
			
			ConfigItem parsedLine = new ConfigItem(StringUtils.unEscape(record[0]), StringUtils.unEscape(record[1]), StringUtils.unEscape(record[2]));
			fileContents.add(parsedLine);
			++line;
		}
		
		// Close down
		csvReader.close();
		reader.close();
	}
	
	// Get a copy of the internal items
	public List<ConfigItem> getItems()
	{
		List<ConfigItem> out = new Vector<ConfigItem>();
		
		for(ConfigItem item : fileContents)
		{
			out.add(new ConfigItem(item.type, item.key, item.value));
		}
		
		return out;
	}
	
	// Set the internal items
	public void setItems(List<ConfigItem> newList)
	{
		fileContents = newList;
	}
	
	// Write out the file, header then body.
	public void writeFile()
	{
		try
		{
			List<String[]> toWrite = new Vector<String[]>();
			toWrite.add(ConfigItem.header);
			
			for(IConfigSection section : sections)
			{
				List<ConfigItem> items = section.produce();
				
				for(ConfigItem item : items)
				{
					toWrite.add(item.getAll());
				}
			}
			
			Writer writer = Files.newBufferedWriter(Paths.get(path));
			
			CSVWriter csvWriter = makeWriter(writer);
			csvWriter.writeAll(toWrite);
			csvWriter.flush();
			csvWriter.close();
			
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// Static utility function
	public static Map<String, List<ConfigItem>> groupByColumn(List<ConfigItem> items, int columnNumber)
	{
		if(items == null || items.size() <= 0 || columnNumber < 0)
		{
			return null;
		}
		
		Map<String, List<ConfigItem>> sortedItems = new HashMap<String, List<ConfigItem>>();
		
		for(ConfigItem item : items)
		{
			String key = item.getColumn(columnNumber);
			List<ConfigItem> list = sortedItems.getOrDefault(key, new Vector<ConfigItem>());
			list.add(item);
			sortedItems.put(key, list);
		}
		
		return sortedItems;
	}
}
