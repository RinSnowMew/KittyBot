package network;

import java.util.List;

import com.google.gson.Gson;

import offline.Ref;
import utils.HTTPUtils;

public class NetworkSauceNAO 
{
	private static final Gson jsonParser_ = new Gson();
	private static final String API_ROOT = "https://saucenao.com/search.php?db=999&output_type=2&numres=10&api_key=" + Ref.SauceNAOKey +"&url=";
	NetworkE621 e621Sauce = new NetworkE621();
	private class SauceNAOResponseObject
	{
		List<SauceNAOResult> results;
	}
	
	private class SauceNAOResult
	{
		SauceNAOData data;
	}
	
	private class SauceNAOData
	{
		String [] ext_urls;
	}
	
	public String getSauce(String input)
	{
		input = input.trim();
		input = input.replace("+", "%2B");
		input = input.replace(" ", "%20");
		
		String res = HTTPUtils.sendGETRequest(API_ROOT + input);
		
		SauceNAOResponseObject sauce = jsonParser_.fromJson(res, SauceNAOResponseObject.class);
		String sauceUrl = sauce.results.get(0).data.ext_urls[0];
		if(sauceUrl.startsWith("https://e621.net/post/show/"))
		{
			System.out.println("id:" + sauceUrl.substring(26));
			return e621Sauce.getE621("id:" + sauceUrl.substring(27)).toString();
		}
		return sauce.results.get(0).data.ext_urls[0];
	}
	
}
