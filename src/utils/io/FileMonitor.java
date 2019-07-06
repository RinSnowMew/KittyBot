package utils.io;

import java.nio.file.Paths;
import java.util.function.Consumer;

// Monitors a single file for changes - the file must exist and is expected to continue to exist.
public class FileMonitor
{
	MonitoredFile file;
	
	// Constructs file monitor for a given file at a specific path.
	public FileMonitor(String path)
	{
		Long last = FileUtils.lastModified(Paths.get(path));
		
		if(last == null)
		{
			file = null;
			IOLog.error("Couldn't find a readable file at " + path + "!");
		}
		else
		{
			file = new MonitoredFile(Paths.get(path), last);
		}
	}
	
	// Looks at the file and sees if it has undergone any changes. 
	// If so, the fileChanged function provided is called!
	public void update(Consumer<? super MonitoredFile> fileChanged)
	{
		if(file == null)
		{
			IOLog.error("Attempted to update a file that doesn't exist");
			return;
		}
		
		Long last = FileUtils.lastModified(file.path);
		if(last == null)
		{
			IOLog.error("The file previously at " + file.path + " couldn't be read!");
			return;
		}
		
		if(last.longValue() != file.lastModified.longValue())
		{
			IOLog.log("Most recently modified at " + last + ", stored version at " + file.lastModified);
			
			file = new MonitoredFile(file.path, last);
			fileChanged.accept(file);
			
			file = new MonitoredFile(file.path, FileUtils.lastModified(file.path));
		}
	}
}
