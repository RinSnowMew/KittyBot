package network;

import utils.HTTPUtils;

public class NetworkColiru 
{
	public String compileCPlusPlus(String query)
	{
		// Escape characters that need escaping.
		// Primarily types of whitespace.
		query = query.replace("\\n", "\\\\n");
		query = query.replace("\\t", "\\\\t");
		query = query.replace("\n", "\\n");
		query = query.replace("\"", "\\\"");
		query = query.replace("\t", "\\t");
		
		// Send the compilation request!
		String result = HTTPUtils.SendPOSTRequest("http://coliru.stacked-crooked.com/compile"
				, "{ \"cmd\": \"g++ main.cpp && ./a.out\", \"src\": \"" + query + "\" }");
		
		// If we got a valid response...
		if(!result.isEmpty())
		{
			result = "Here's what happened when I went to compiled that! \n```" + result + "```";
		}
		
		return result;
	}
	
}
