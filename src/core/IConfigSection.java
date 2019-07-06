package core;

import java.util.List;

// A config section is a unit of config that holds data. 
public abstract interface IConfigSection
{
	// Get the title of the section
	public abstract String getSectionTitle();
	
	// Provide the parsed key and values for a given section
	public abstract void consume(List<ConfigItem> pairs);
	
	// Ask the section to produce a list of pairs to be written out to the key and value columns in the csv
	public abstract List<ConfigItem> produce();
}
