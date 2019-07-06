package core.rpg;

import utils.StringUtils;

public class RPGInput
{
	public String raw;
	public String key;
	public String value;
	
	public RPGInput(String raw)
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
