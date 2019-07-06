package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtils
{
	private static Long uniqueID = 0l;
	
	public static String writeTempImageData(BufferedImage data, String extension)
	{
		String name = null;
		synchronized(uniqueID)
		{
			String end = extension;
			if(!extension.contains("."))
			{
				end = "." + extension;
			}
			
			name = "imagetempwriterutil_" + (uniqueID++) + end;
		}
		
		try
		{
			File outputfile = new File(name);
			extension = extension.replace(".", "");
			
			boolean didWrite = ImageIO.write(data, extension, outputfile);
			if(!didWrite)
			{
				GlobalLog.error(LogFilter.Util, "Could not identify the writer for extension type '" + extension + "' - Did you accidentally include a period or other punctuation?");
				return null;
			}
			
			return name;
		}
		catch (IOException e)
		{
			GlobalLog.error(LogFilter.Util, "Exception in writeTempImageData: " + e.toString());
			return null;
		}

	}
	
	// Downloads the file at the url and assigns it a unique local string.
	// The string (filename) is returned, and can be used. It will have to be
	// deleted later. By assigning a unique name to the files, this is 
	public static String downloadFromURL(String URL, String extension) 
	{
		String name = null;
		synchronized(uniqueID)
		{
			name = "imagedownloaderutil_" + (uniqueID++) + extension;
		}
		
		try
		{
			URL yeetee = new URL(URL);
			
			HttpURLConnection con = (HttpURLConnection) yeetee.openConnection();
			
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", HTTPUtils.USER_AGENT);
			
			// Check String code and act on it accordingly.
			con.getResponseCode();
	
			InputStream in = con.getInputStream();
			OutputStream out = new BufferedOutputStream(new FileOutputStream(name));
			for ( int i; (i = in.read()) != -1; ) 
			{
				out.write(i);
			}
			
			in.close();
			out.close();
		
			return name;
		}
		catch(Exception e)
		{
			GlobalLog.error(LogFilter.Util, "Exception in downloadFromURL: " + e.toString());
			return null;
		}
	}
	
	// Deletes the file provided, if it exists. Will continue, in a blocking way,
	// to attempt to delete the file 10 times a second until thread termination.
	public static void blockingFileDelete(File file)
	{
		if(file == null)
			return;
		
		if(!file.exists())
			return;
		
		do
		{
			try { Thread.sleep(100); }
			catch (InterruptedException e) { }
		}
		while(!file.delete());
	}
	
	public static BufferedImage copyImage(BufferedImage image)
	{
		int width = image.getWidth();
        int height = image.getHeight();
        
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    
        for(int x = 0; x < width; ++x)
        {
            for(int y = 0; y < height; ++y)
            {
                Color c = new Color(image.getRGB(x, y), true);
                out.setRGB(x, y, c.getRGB());
            }    
        }
        
        return out;
	}
}
