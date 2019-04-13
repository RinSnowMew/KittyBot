package dataStructures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

// This is a class designed to parse ini inspired key-value pairs that are sectioned off.
// The difference here is that this is more permissive than an ini file, and only accepts
// a single split character, not the traditional set an ini does. All of the following are valid:
//
// [ExampleSection]
// Key=Value
//  Valid Ridiculous Key&\n\t_.:;foo  =  \tValid Ridiculous Value*&%$()^.[]{}@
// EmptyValue=
//
// Note that the sections are NOT designed to allow for duplicate keys across them. 
// This is a restriction of the structure, but can be changed later potentially. 
// The only value not allowed in a key or value is the KeyValueSplit.
public class SectionedKeyValueStore
{
	// Variables
	public final char SectionStart = '[';
	public final char SectionEnd = ']';
	public final char KeyValueLineSeparator = '\n';
	public final String KeyValueSplit = "=";
	
	// [Key: SectionName, [Key: KeyString, Value: ValueString]]
	private HashMap<String, HashMap<String, String>> sectionKeyValue;
	
	// [Key: KeyString, Value: ValueString]]
	private HashMap<String, String> keyValue;
	
	// String constructor that parses the input string into the object
	public SectionedKeyValueStore(String input)
	{
		sectionKeyValue = new HashMap<String, HashMap<String, String>>();
		keyValue = new HashMap<String, String>();
		Parse(input);
	}
	
	// Calls back on each item in the entire structure. Provides section, then a pair of the keyString and valueString.
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void ForEach(BiConsumer<? super String, Pair<? super String, ? super String>> action)
	{
		Iterator it = sectionKeyValue.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry pair = (Map.Entry)it.next();
			Iterator internal = ((HashMap<String, String>)pair.getValue()).entrySet().iterator();
			
			while(internal.hasNext())
			{
				Map.Entry internalPair = (Map.Entry)internal.next();
				action.accept((String)pair.getKey(), new Pair<String, String>((String)internalPair.getKey(), (String)internalPair.getValue()));
			}
		}
	}
	
	// Calls back each item in the structure but does not priv
	@SuppressWarnings({"rawtypes"})
	public void ForEach(Consumer<Pair<? super String, ? super String>> action)
	{
		Iterator it = keyValue.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry pair = (Map.Entry)it.next();
			action.accept(new Pair<String, String>((String)pair.getKey(), (String)pair.getValue()));
		}
	}
	
	// Parses the internal hashmap as a string then reutrns it, featuring sections. 
	// Iterates over all key/value pairs in the section and print them. Does not print 
	// the value of a given key if it is the same as the key.
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String toString()
	{
		String out = "";
		
		// Iterates over the sections and 
		Iterator it = sectionKeyValue.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry pair = (Map.Entry)it.next();
			out += ("" + SectionStart + pair.getKey() + SectionEnd + KeyValueLineSeparator);
			
			Iterator internal = ((HashMap<String, String>)pair.getValue()).entrySet().iterator();
			while(internal.hasNext())
			{
				Map.Entry internalPair = (Map.Entry)internal.next();
				String key = (String)internalPair.getKey();
				String value = (String)internalPair.getValue();
				
				if(key == value)
					out += (key + KeyValueSplit) + KeyValueLineSeparator;
				else
					out += (key + KeyValueSplit + value) + KeyValueLineSeparator;
			}
			
			out += KeyValueLineSeparator;
		}
		
		return out;
	}
	
	// Parses out the string passed in into the sectionkeyValue HashMap. 
	// Any character used in a split call is escaped just in case on 
	// account of some characters having specific regex meanings.
	private void Parse(String input)
	{
		String[] sections = input.split("\\" + SectionStart);

		for(int sec = 0; sec < sections.length; ++sec)
		{
			// Gather information about the contents of the section, and the header.
			String section = sections[sec];
			if(section.length() < 2)
				continue;
			
			int pos = section.indexOf(SectionEnd);
			if(pos == -1)
				continue;
			
			// Parse section name. If it already exists, don't bother making it.
			String sectionName = section.substring(0, pos);
			AddSection(sectionName);
				
			// Parse out the pairs within the section, split them all out. 
			String unparsedPairs = section.substring(pos + 1);
			String[] pairs = unparsedPairs.split("\\" + KeyValueLineSeparator);
			
			// Parse out valid key-value pairs, and store them in the specified section.
			// At this point, we can be guarenteed that sectionName is in the Hashmap.
			for(int pair = 0; pair < pairs.length; ++pair)
			{
				String line = pairs[pair];
				int splitPos = line.indexOf(KeyValueSplit);
				if(splitPos < 0)
					continue;
				
				String key = line.substring(0, splitPos);
				String value = line.substring(splitPos + KeyValueSplit.length());
				sectionKeyValue.get(sectionName).putIfAbsent(key, value);
				keyValue.putIfAbsent(key, value);
			}
		}
	}
	
	// Dumps out a string array
	@SuppressWarnings("unused")
	private void Dump(String[] toPrint)
	{
		System.out.println("Length: " + toPrint.length);
		
		for(int i = 0; i < toPrint.length; ++i)
			System.out.println(toPrint[i]);
	}
	
	// Adds a KeyValue pair to the specified section if it's not already there.
	// Also creates the section if it's not already present.
	public void AddKeyValue(String sectionName, String key, String value)
	{
		AddSection(sectionName);
		sectionKeyValue.get(sectionName).putIfAbsent(key, value);
		keyValue.putIfAbsent(key, value);
	}
	
	// Adds a given section to the hashmap if it's not already present
	public void AddSection(String sectionName)
	{
		sectionKeyValue.putIfAbsent(sectionName, new HashMap<String, String>());
	}
	
	// Returns a HashMap of Keys to Values for a given section
	@SuppressWarnings("unchecked")
	public HashMap<String, String> GetSection(String sectionName)
	{
		return (HashMap<String, String>) sectionKeyValue.get(sectionName).clone();
	}

	// Look up a global key
	public String GetKey(String key)
	{
		if(keyValue.containsKey(key))
			return keyValue.get(key);
		
		return null;
	}
}
