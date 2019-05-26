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
		Long last = FileUtils.LastModified(Paths.get(path));
		
		if(last == null)
		{
			file = null;
			IOLog.Error("Couldn't find a readable file at " + path + "!");
		}
		else
		{
			file = new MonitoredFile(Paths.get(path), last);
		}
	}
	
	// Looks at the file and sees if it has undergone any changes. 
	// If so, the fileChanged function provided is called!
	public void Update(Consumer<? super MonitoredFile> fileChanged)
	{
		if(file == null)
		{
			IOLog.Error("Attempted to update a file that doesn't exist");
			return;
		}
		
		Long last = FileUtils.LastModified(file.path);
		if(last == null)
		{
			IOLog.Error("The file previously at " + file.path + " couldn't be read!");
			return;
		}
		
		if(last.longValue() != file.lastModified.longValue())
		{
			IOLog.Log("Most recently modified at " + last + ", stored version at " + file.lastModified);
			file = new MonitoredFile(file.path, last);
			fileChanged.accept(file);
		}
	}
}
