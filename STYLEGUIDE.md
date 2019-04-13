# KittyBot Style Guide

This document represents the reasonable defaults and expectations for this project, and provides some rationale for the style restrictions! There can always be exceptions to the rules, but they may need justifying.

Code Formatting
---

#### Indenting
Tabs are used so that individual developers can configure their own indent width settings that won't be enforced on others working on the project. 

```java
// I'm not indented!
{
	// I'm indented with one tab!
}
```

#### Braces
When braces are present, Allman style is used meaning that braces start on the next line. Braces are used to prevent scope confusion. Omitting braces on single-line statements is fine if the situation is clear.

```java
if(foo)
{
	System.out.println("This");
	System.out.println("has");
	System.out.println("braces!");
}

if(bar)
	System.out.println("No braces!");
```

#### Naming
Class names and function names are `PascalCase`. Variable names are `camelCase`. 

```java
public class MyClass
{
	private Foo myVariable;

	public function MyFuction()
	{
		// ...
	}
}
```

Favor a broad to narrow naming scheme to promote autocomplete grouping. For example, use a name like `userDescriptionHeader` instead of `headerUserDescription` because there may be other variables related to a user or description, but there's likely only one header section of a description.


#### Ternary Operator (`?:`)
Don't.


Architecture 
---

#### Commands
All commands get spawned on their own threads, and should be implemented in a thread-safe way. Commands all derive from the Command.java class, and use the custom types defined in KittyBot. In order to make any later changes easier and decouple the command logic from core and routing, no JDA structures should be directly exposed in commands, and instead everything needed should be added to the kitty structures that add space to the system. Commands should override the `HelpText` and `Run` functions. Commands are registered in the ObjectBuilderFactory, and the command registration structure can be used for sub-commands as needed. See the RPG for an example.

#### Tracked Values
Does something need to be tracked in a database? Make sure the class with the data you want inherits from DatabaseTrackedObject, and override the appropriate functions. The object, when marked as dirty, will automatically be written to the database when the next database upkeep tick occurs. Refer to CommandBoop to see an example of a tracked global value, and KittyUser for tracked per-user values.

#### Tokens, Secrets, etc
Things like tokens, private keys, an secrets are kept in the offline package. This file is intentionally excluded from the repo. If it shouldn't be in the repo, put it in the offline package. 
