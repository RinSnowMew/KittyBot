package utils.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import utils.FileUtils;

// NOTE: Consider shifting internal behavior to https://docs.oracle.com/javase/tutorial/essential/io/notification.html
// for external stability and support
public class DirectoryMonitor
{
	// Variables
	private String directory; 
	private ArrayList<MonitoredFile> last;
	
	// Constructs a new file monitor and logs initialization
	public DirectoryMonitor(String directory)
	{
		IOLog.Log("Reading directory files for montioring in: " + directory.toString());
		
		this.directory = directory;
		this.last = Scrape();
		
		Print(last);
	}
	
	// Scrape the target directory for all files and store them as custom objects in the list
	private ArrayList<MonitoredFile> Scrape()
	{
		ArrayList<MonitoredFile> files = new ArrayList<MonitoredFile>();
		
		try
		{
			try (Stream<Path> paths = Files.walk(Paths.get(directory)))
			{
				paths.filter(Files::isRegularFile).forEach((path)->
				{ 
					files.add(new MonitoredFile(path, FileUtils.LastModified(path)));
				});
			}
		}
		catch(Exception e)
		{
			IOLog.Error(e.getMessage());
		}
		
		return files;
	}
	
	// Update function - the first callback is called if a new item is added to the directory,
	// second is called if an item is updated, and third is called if an item is removed.
	@SuppressWarnings("unchecked")
	public void Update(Consumer<? super MonitoredFile> onAdd, Consumer<? super MonitoredFile> onUpdate, Consumer<? super MonitoredFile> onDelete)
	{
		// Unparsed
		ArrayList<MonitoredFile> active = Scrape();
		ArrayList<MonitoredFile> curr = (ArrayList<MonitoredFile>)active.clone(); 
		ArrayList<MonitoredFile> prev = (ArrayList<MonitoredFile>)last.clone();
		
		// Parsed
		ArrayList<MonitoredFile> added = new ArrayList<MonitoredFile>();
		ArrayList<MonitoredFile> updated = new ArrayList<MonitoredFile>();
		ArrayList<MonitoredFile> deleted = new ArrayList<MonitoredFile>();
		ArrayList<MonitoredFile> existing = new ArrayList<MonitoredFile>();
		
		// Iterate over everything
		for(int i = 0; i < active.size(); ++i)
		{
			// If we contain the previous, remove it.
			if(prev.contains(active.get(i)))
			{
				MonitoredFile item = active.get(i);
				
				prev.remove(item);
				curr.remove(item);
				existing.add(item);
			}
			
			// Check all other cases
			for(int j = 0; j < prev.size(); ++j)
			{
				MonitoredFile l1 = active.get(i);
				MonitoredFile l2 = prev.get(j);
						
				// Check if item is updated.
				if(l1.path.toString().equals(l2.path.toString()))
				{
					updated.add(l1);
					prev.remove(l2);
					curr.remove(l1);
					onUpdate.accept(l2);
				}
			}
		}
	
		// Anything that wasn't removed from prev has been removed.
		for(int j = 0; j < prev.size(); ++j)
		{
			MonitoredFile item = prev.get(j);
			
			deleted.add(item);
			onDelete.accept(item);
		}
		
		prev.clear();
		
		// Add all the remaining things in the current directory as added items.
		for(int i = 0; i < curr.size(); ++i)
		{
			MonitoredFile item = curr.get(i);
			
			added.add(item);
			onAdd.accept(item);
		}
		
		// Rebuild local state
		last.clear();
		last.addAll(existing);
		last.addAll(added);
		last.addAll(updated);
		Collections.sort(last);
		
		if(last.size() == 0)
			IOLog.Warn("There's nothing at all in a FileMonitor's target folder! Folder: " + directory);
	}
	
	// Returns the current list of files the monitor is tracking in the directory.
	public List<MonitoredFile> GetCurrentFiles()
	{
		return last;
	}
	
	// Prints out an arraylist of items from a directory by path
	private void Print(ArrayList<MonitoredFile> toPrint)
	{
		for(int i = 0; i < toPrint.size(); ++i)
			IOLog.Log(toPrint.get(i).path.toString());
	}
}
