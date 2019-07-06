package core.rpg;

public final class RPGExpTable
{
	public static long expFloor(long level)
	{
		if(level > Levels.length - 1)
			level = Levels.length - 1;
		
		else if (level <= 0)
			level = 1;
		
		return Levels[(int) level];
	}
	
	public static long expCeil(long level)
	{
		if(level > Levels.length - 1)
			level = Levels.length - 1;
		
		else if (level <= 0)
			level = 1;
		
		return Levels[(int) (level + 1)];
	}
	public static long levelFromEXP(long exp)
	{
		if(exp < 0)
			exp = 0;
		
		for(int i = 0; i < Levels.length; ++i)
		{
			if(Levels[i] > exp)
				return i - 1;
		}

		return Levels.length - 1;
	}
	
	// Based on D&D Pathfinder levels
	public static long Levels[] = 
	{
		0,       // level 0
		0,       // level 1
		3000,    // level 2
		7500,    // level 3
		14000,   // level 4
		23000,   // level 5
		35000,   // level 6
		53000,   // level 7
		77000,   // level 8
		115000,  // level 9
		160000,  // level 10
		235000,  // level 11
		330000,  // level 12
		475000,  // level 13
		665000,  // level 14
		955000,  // level 15
		1350000, // level 16
		1900000, // level 17
		2700000, // level 18
		3850000, // level 19
		5350000  // level 20
	};
}
