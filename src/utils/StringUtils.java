package utils;

public class StringUtils
{
	public static String UnEscape(String str)
	{
		str = str.replaceAll("\\\\b", "\b");
		str = str.replaceAll("\\\\n", "\n");
		str = str.replaceAll("\\\\t", "\t");
		str = str.replaceAll("\\\\r", "\r");
		str = str.replaceAll("\\\\f", "\f");
		str = str.replaceAll("\\\\\"", "\"");
		str = str.replaceAll("\\\\\\\\\\\\", "\\\\");
		
		return str;
	}
	
	public static String ReEscape(String str)
	{
		str = str.replaceAll("\\\b", "\\\\\\b");
		str = str.replaceAll("\\\n", "\\\\\\n");
		str = str.replaceAll("\\\t", "\\\\\\t");
		str = str.replaceAll("\\\r", "\\\\\\r");
		str = str.replaceAll("\\\f", "\\\\\\f");
		
		return str;
	}
	
}
