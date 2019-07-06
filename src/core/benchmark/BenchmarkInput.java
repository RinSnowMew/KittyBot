package core.benchmark;

import utils.StringUtils;

// Lifted from an early iteration of RPGInput
public class BenchmarkInput
{
	public String raw;
	public String key;
	public String value;
	
	public BenchmarkInput(String raw)
	{
		this.raw = raw;
		this.key = "";
		this.value = "";
		
		if(raw == null || raw.length() == 0)
			return;
		
		raw = raw.trim();
		int whitespacePos = StringUtils.findFirstWhitespace(raw);
		
		if(whitespacePos == -1)
		{
			key = raw;
			return;
		}
		
		key = raw.substring(0, whitespacePos).trim();
		value = raw.substring(whitespacePos).trim();
	}
}

