package commands;

import java.util.Random;

import core.Command;
import core.Localizer;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.OptionParser;

// This command is a port of worldGen command line program by Matthew Cech (https://www.matthewcech.com/)
// Permissions were granted for use and modification in this bot.
public class CommandMap extends Command 
{
	final int MaxWidth = 50;
	final int MaxHeight = 35;
	
	public CommandMap(KittyRole roleLevel, KittyRating contentRating) { super(roleLevel, contentRating); }
	
	@Override
	public String HelpText() { return String.format(Localizer.Stub("Generates a map! You can pass additional information if you want with the flags `-s<seed> -w<width> -h<height>`. If one of the fields isn't provided, its default will be used. Note that adjusting the width and height impacts the map outcomes.\n\nDefault seed: Random,\nDefault Width: 35(max %s),\nDefault Height: 25(max %s)"), "" + MaxWidth, "" + MaxHeight); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res) 
	{
		// Variables
		int width = 35;
		int height = 25;
		long seed = 0;
		String header = "";
		String body = "";
		Random randGenerator = null;
		
		// Parse out a seed if one was provided
		boolean hasSeed = false;
		if(input.args != null && input.args.length() > 0)
		try
		{
			OptionParser parser = new OptionParser(input.args);
			
			String seedStr = parser.GetOption("-s", true);
			if(seedStr != null)
			{
				seed = Long.parseLong(seedStr);
				hasSeed = true;
			}
		
			String widthStr = parser.GetOption("-w", true);
			if(widthStr != null)
				width = ValidateSize(Integer.parseInt(widthStr), MaxWidth);
			
			String heightStr = parser.GetOption("-h", true);
			if(heightStr != null)
				height = ValidateSize(Integer.parseInt(heightStr), MaxHeight);
		}
		catch(NumberFormatException e) 
		{
			res.Call(Localizer.Stub("Invalid arguments provided!"));
			return;
		} 
		
		// Set up the random generator, creating a seed if needed.
		if(hasSeed)
		{
			randGenerator = new Random(seed);
		}
		else
		{
			seed = new Random().nextLong();
			randGenerator = new Random(seed);
		}
		
		// Response header creation
		header += Localizer.Stub("Using mapgen v0.1") + "\n";
		header += Localizer.Stub("Seed") + ": `" + seed + "`, ";
		header += Localizer.Stub("Width") + "`"+ width +"`, ";
		header += Localizer.Stub("Height") + ": `"+ height + "`\n";
		
		// Response body creation
		body += "```\n";
		body += GenerateMap(width, height, randGenerator);
		body += "\n```";
		
		// Send back the map
		res.Call(header + body);
	}
	
	// Verifies and appropriately caps input as necessary
	int ValidateSize(int input, int max)
	{
		if(input < 0)
			throw new NumberFormatException();
		
		if(input > max)
			input = max;
		
		return input;
	}
	
	
	// Does the map generation
	String GenerateMap(int width, int height, Random gen)
	{
		String landscape[][] = new String[width][height];
		int r1 = rand(gen);

		// Variables to determine various feature quantities.
		int LandScarcity = (r1 % 200) + 100; // Higher = more water. 
		int landPasses = 7;                  // Higher = larger land chunks.
		int MountainScarcity = 50;           // Higher = fewer mountains;
		int mountainPasses = r1 % 8 + 2;     // Times to plant mountains.
		int marshPasses = r1 % 3 + 10;       // Times to plant marshes.
		int lavaPasses = 1;                  // Times to plant lava.
		int beachPasses = 1;
		int i, j;
		int sizeX = height;
		int sizeY = width;
		
		// Previously defines
		String LAND = "\u02F6";     // Used to be "
		String WATER = " ";         // Used to be ~ 
		String MOUNTAIN = "\u25B2"; // Used to be ^
		String BEACH = "=";         // Used to be ~
		String MARSH = "\u00A5";    // Used to be ,
		String LAVA = "#";          // Used to be *
		//String SPACE = " ";
		
		////////////////////
		//GENERATION LOOPS//
		////////////////////
		
		// Populate grid with water.
		for(i = 0; i < sizeY; i++)
			for(j = 0; j < sizeX; j++)
				landscape[i][j] = WATER;
			
		// land initial distribution. 
		for(i = 0; i < sizeY; i++)
			for(j = 0; j < sizeX; j++)
			{
				r1 = rand(gen) % LandScarcity;
				if(r1 == 0)
					landscape[i][j] = LAND;
			}
	
		// Land expansion, standard land. 
		while(landPasses-- > 0)
			for(i = 0; i < sizeY; i++)
				for(j = 0; j < sizeX; j++)
					if(landscape[i][j] == LAND)
					{
						if((rand(gen) % 3) == 0)
							expandLand(sizeX, sizeY, landscape, i, j, LAND);
					}
	
		//Beach creation based on land
		while(beachPasses-- > 0)
			for(i = 0; i < sizeY; i++)
				for(j = 0; j < sizeX; j++)
					if(landscape[i][j] == LAND)
					{
						Surround beachQuals = new Surround(new String[]{WATER, WATER, WATER, WATER});
						
						if((rand(gen) % 3) == 0)
							replaceIf(sizeX, sizeY, landscape, beachQuals, i, j, BEACH);
					}
	
		//Mountain creation based on land
		for(i = 0; i < sizeY; i++)
			for(j = 0; j < sizeX; j++)
				if(landscape[i][j] == LAND)
				{
					Surround MountainQuals = new Surround(new String[]{LAND, LAND, LAND, LAND});
					
					if((rand(gen) % MountainScarcity) == 0)
					{
						replaceIf(sizeX, sizeY, landscape, MountainQuals, i, j, MOUNTAIN);
						landscape[i][j] = MOUNTAIN;
					}
				}
	
		//Mountain creation based on Mountain
		while(mountainPasses-- > 0) 
			for(i = 0; i < sizeY; i++)
				for(j = 0; j < sizeX; j++)
					if(landscape[i][j] == LAND)
					{
						Surround MountainQuals = new Surround(new String[]{MOUNTAIN, MOUNTAIN, MOUNTAIN, MOUNTAIN});
						
						int scalar = 0;
						scalar = replaceIf(sizeX, sizeY, landscape, MountainQuals, i, j, MOUNTAIN);
						if(scalar > 0)
							if((rand(gen) % (30 / scalar)) == 0)
								landscape[i][j] = MOUNTAIN;
						
					}
	
		//Marsh creation based on land
		while(marshPasses-- > 0) 
			for(i = 0; i < sizeY; i++)
				for(j = 0; j < sizeX; j++)
					if( landscape[i][j] == LAND)
					{
						Surround ForestQuals = new Surround();
						ForestQuals.left = BEACH;
						ForestQuals.right = BEACH;
						ForestQuals.up = BEACH;
						ForestQuals.down = BEACH;
						
						if((rand(gen) % 80) == 0)
						{
							replaceIf(sizeX, sizeY, landscape, ForestQuals, i, j, MARSH);
							landscape[i][j] = MARSH;
						}
					}  
	
		//Lava creation based on land
		while(lavaPasses-- > 0)
			for(i = 0; i < sizeY; i++)
				for(j = 0; j < sizeX; j++)
					if( landscape[i][j] == MOUNTAIN)
					{
						Surround Mountains = new Surround(new String[]{MOUNTAIN, MOUNTAIN, MOUNTAIN, MOUNTAIN});
						
						if((rand(gen) % 5) == 0)
							if(replaceIf(sizeX, sizeY, landscape, Mountains, i, j, MOUNTAIN) == 4)
								landscape[i][j] = LAVA;
					}
	
		////////////
		//CLEAN UP//
		////////////
		
		//Remove lone islands, with the exception of things on the edge.
		for(i = 0; i < sizeY; i++)
			for(j = 0; j < sizeX; j++)
				{
					Surround water = new Surround(new String[]{WATER, WATER, WATER, WATER});
					if(replaceIf(sizeX, sizeY, landscape, water, i, j, WATER) == 4)
						landscape[i][j] = WATER;
				}
		
		
		
	
		// Build the map
		String output = "";
		for(int y = 0; y < height; ++y)
		{
			if(y != 0)
				output += '\n';
			
			for(int x = 0; x < width; ++x)
				output += landscape[x][y];
		}
		
		// Return the map
		return output;
	}
	
	// Standardized application of rand for the purposes of this generaton
	int rand(Random rand)
	{
		return rand.nextInt();
	}
	
	// Ported from C struct
	class Surround 
	{ 
		public String left;
		public String right; 
		public String up;
		public String down;
		
		public Surround()
		{
			left = null;
			right = null;
			up = null;
			down = null;
		}
		
		public Surround(String[] args)
		{
			if(args.length > 0)
				left = args[0];
			
			if(args.length > 1)
				right = args[1];
			
			if(args.length > 2)
				up = args[2];
			
			if(args.length > 3)
				down = args[3];
		}
	}

	// Expands the current land block
	static void expandLand(int x, int y, String landscape[][], int i, int j, String toAdd)
	{
		// Four checks, one for each block in the 4 cardinal directions.
		if(i - 1 > -1)            
			landscape[i - 1][j] = toAdd;
		
		if(i + 1 < y)
			landscape[i + 1][j] = toAdd;
		
		if(j - 1 > -1)
			landscape[i][j - 1] = toAdd; 
		
		if(j + 1 < x)
			landscape[i][j + 1] = toAdd;  
	}

	// Replaces surrounding stuff
	int replaceIf(int x, int y, String[][] landscape, Surround spaces, int i, int j, String toAdd)
	{
		// Running total of tiles changed.
		int tilesChanged = 0;
		
		// Check square left of me.
		if(spaces.left != null)
			if(j - 1 > -1)
				if(landscape[i][j - 1] == spaces.left)
				{
					landscape[i][j - 1] = toAdd;
					tilesChanged++;
				}
		
		// Check square right of me.
		if(spaces.right != null)
			if(j + 1 < x)
				if(landscape[i][j + 1] == spaces.right)
				{
					landscape[i][j + 1] = toAdd;
					tilesChanged++;
				}
		
		// Check square above me.
		if(spaces.up != null)
			if(i - 1 > -1)
				if(landscape[i - 1][j] == spaces.up)
				{
					landscape[i - 1][j] = toAdd;
					tilesChanged++;
				}
		
		// Check square below me.
		if(spaces.down != null)
			if(i + 1 < y)
				if(landscape[i + 1][j] == spaces.down)
				{
					landscape[i + 1][j] = toAdd;
					tilesChanged++;
				}
		
		return tilesChanged;
	}
}
