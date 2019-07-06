package utils;

import org.apache.commons.lang3.StringEscapeUtils;

@SuppressWarnings("deprecation")
public class StringUtils
{
	public static String unEscape(String str)
	{
		return StringEscapeUtils.unescapeJava(str);
	}
	
	public static String reEscape(String str)
	{
		return StringEscapeUtils.escapeJava(str);
	}
	
	// Finds first whitespace in the string
	public static int findFirstWhitespace(String str)
	{
		for (int i = 0; i < str.length(); ++i) 
		{
			if (Character.isWhitespace(str.charAt(i)))
				return i;
		}
	
		return -1;
	}
}
