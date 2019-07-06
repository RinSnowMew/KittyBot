package core.rpg;

public abstract class RPGItem
{
	protected String name;
	protected String description;
	protected int value;
	
	// Defaults
	public RPGItem() { this("unknown"); } 
	public RPGItem(String name) { this(name, "nothig is known about this item"); }
	public RPGItem(String name, String description) { this(name, description, 1); }

	// Ctor
	public RPGItem(String name, String description, int value)
	{
		this.name = name;
		this.description = description;
		this.value = value;
	}
	
	public String getName() { return name; }
	public String getDescription() { return description; }
	public long getValue() { return value; }
}
