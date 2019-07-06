package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

import offline.Ref;

public class NetworkJDoodle 
{
	private static final Gson jsonParser_ = new Gson();
	private class JDoodleObject
	{
		public String output;
		public String cpuTime;
	}
	
	public String compileJava(String query)
	{
		String result = "";
		String lang = "java"; 
		String version = "0";
		query = query.replace("\\n", "\\\\n");
		query = query.replace("\\t", "\\\\t");
		query = query.replace("\n", "\\n");
		query = query.replace("\"", "\\\"");
		query = query.replace("\t", "\\t");

		String input = "{\"clientId\": \"" + Ref.jdoodleID + "\",\"clientSecret\":\"" + Ref.jdoodleSecret + "\",\"script\":\"" + query +
	            "\",\"language\":\"" + lang + "\",\"versionIndex\":\"" + version + "\"} ";
		
		 try
		 {
	            URL url = new URL("https://api.jdoodle.com/v1/execute");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "application/json");
	            
	            //result += input + "\n";

	            OutputStream outputStream = connection.getOutputStream();
	            outputStream.write(input.getBytes());
	            outputStream.flush();

	            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	            	result = "Please check your inputs : HTTP error code : "+ connection.getResponseCode();
	                return result;
	            }

	            BufferedReader bufferedReader;
	            bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
	            
	            String output;
	            String fullOut = ""; 
	            result += "This is what happened! \n";
	            while ((output = bufferedReader.readLine()) != null) 
	            {
	            	fullOut += output + "\n";
	            }
	            JDoodleObject doodle = jsonParser_.fromJson(fullOut, JDoodleObject.class);
	            
	            result += doodle.output + "\n";
	            result += "The CPU time was " + doodle.cpuTime + "\n";
	            connection.disconnect();
	            return result;
		 }
		 catch (MalformedURLException e) 
		 {
			 return "You probably shouldn't be seeing this";
	     }
		 catch (IOException e) 
		 {
	         return "You probably shouldn't be seeing this";
	     }
	}
}
