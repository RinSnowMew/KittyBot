package utils.io;

import java.nio.file.Path;

public class MonitoredFile implements Comparable<Object>
{
	public final Path path;
	public final Long lastModified;
	
	public MonitoredFile(Path path, Long lastModified)
	{
		this.path = path;
		this.lastModified = lastModified;
	}

	// Override equals operator - used for .contains(...) on list items
	@Override
	public boolean equals(Object other)
	{
		if(this.compareTo(other) == 0)
			return true;
		
		return false;
	}

	@Override
	// Comparison
	public int compareTo(Object other)
	{
		int strCmp = path.getFileName().toString().compareTo(((MonitoredFile)other).path.getFileName().toString());
		
		if(strCmp == 0)
			return lastModified.compareTo(((MonitoredFile)other).lastModified);
		
		return strCmp;
	}
}
