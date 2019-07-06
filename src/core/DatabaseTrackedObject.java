package core;

// Objects inheriting from this are capable of being stored in and 
// have their data populated from database entries.
public abstract class DatabaseTrackedObject
{
	private Boolean isDirty;
	public final String identifier;
	
	public DatabaseTrackedObject(String identifier)
	{
		this.isDirty = false;
		this.identifier = identifier;
	}
	
	public final boolean isDirty()
	{
		synchronized(isDirty)
		{
			return isDirty;
		}
	}
	
	public final void markDirty()
	{
		synchronized(isDirty)
		{
			isDirty = true;
		}
	}
	
	public final void resolve()
	{
		synchronized(isDirty)
		{
			isDirty = false;
		}
	}
	
	// Consider an object factory instead of dedicated serialization methods
	// if this starts to become impractical. For now, it works.
	public abstract String serialize();
	public abstract void deSerialzie(String string);
}
