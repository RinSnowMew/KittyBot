package network;

import java.util.List;

import com.google.gson.Gson;

import offline.Ref;
import utils.HTTPUtils;

public class NetworkYoutube 
{
	 ///////////////////////////////////////
	 // Internal JSON class and variables //
	//////////////////////////////////.////
	
	//https://www.youtube.com/watch?v=
	
	private static final Gson jsonParser_ = new Gson();
	private static final String API_ROOT = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&q=";
	
	
	// Use this as the base
	public class YTOuter
	{
		List<YTInner> items;
	}
	
	public class YTInner
	{
		TYID id;
		YTSnippet snippet;
	}
	
	public class TYID
	{
		String videoId;
	}
	
	public class YTSnippet
	{
		String title;
		String description;
	}
	
	public String getYT(String input)
	{
		input = input.trim();
		input = input.replace("+", "%2B");
		input = input.replace(" ", "%20");
		
		String res = HTTPUtils.SendGETRequest(API_ROOT + input + "&order=viewCount&type=video&topicId=%2Fm%2F04rlf&videoDefinition=high&key=" + Ref.youtube);
		YTOuter videoObj = jsonParser_.fromJson(res, YTOuter.class);
		return "https://www.youtube.com/watch?v=" + videoObj.items.get(0).id.videoId;
	}
}
