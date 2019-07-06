package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Adjustments to the excellent code provided via:
 * https://www.journaldev.com/7148/java-httpurlconnection-example-java-http-request-get-post
 * @author Wisp
 */
public class HTTPUtils 
{

	// Configuration for requests
	public static final String USER_AGENT = "KittyBot/2.0";

	
	// Sends a GET request for the specified URL.
	public static String sendGETRequest(String url)
	{
		try
		{
			// Get and open a connection, stored in connection object
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// Set ourselves as if we're "vaguely netscape"
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			
			// Handle String
			return (handleHttpResponse(con));
		}
		catch(IOException e)
		{
			return new String();
		}
	}
	
	
	// Sends a POST request to the desired URL.
	// There are two forms of this function, one of which takes 
	public static String sendPOSTRequest(String url) { return sendPOSTRequest(url, ""); }
	public static String sendPOSTRequest(String url, String params) 
	{	
		try
		{
			// Get and open a connection, stored in connection object
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// Set ourselves as if we're "vaguely netscape"
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);

			// For POST only - Configure parameters
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(params.getBytes());
			os.flush();
			os.close();

			// Handle String
			return (handleHttpResponse(con));
		}
		catch(IOException e)
		{
			return new String();
		}
	}	
		
		
	// Internal static function for parsing HTTP Responses based on the 
	// connection object provided by java. Assumes you only want to parse 
	// valid connections, others are discarded.
	private static String handleHttpResponse(HttpURLConnection con) throws IOException
	{
		// Check String code and act on it accordingly.
		int responseCode = con.getResponseCode();

		// If we succeeded...
		if (responseCode == HttpURLConnection.HTTP_OK) 
		{ 
			// Variable declaration
			String inputLine;
			StringBuffer String = new StringBuffer();
			BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));

			// Read all lines from the String
			inputLine = in.readLine();
			while (inputLine != null) {
				String.append('\n');
				String.append(inputLine);
				inputLine = in.readLine();
			}

			// Close and return our data
			in.close();
			return new String(String.toString());
		}
		else
		{
			GlobalLog.error(LogFilter.Network, con.getRequestMethod() + " responded with " + responseCode + " instead of 200.");
			return Integer.toString(responseCode);
		}
	}
}