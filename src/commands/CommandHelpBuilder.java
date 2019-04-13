package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import core.Command;
import core.Localizer;
import core.Stats;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.GlobalLog;
import utils.ImageUtils;

public class CommandHelpBuilder extends Command {

	public CommandHelpBuilder(KittyRole role, KittyRating rating) {
		super(role, rating);
	}

	@Override
	public String HelpText() { return Localizer.Stub("Emit help for all commands as formatted HTML"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		// Holds command groups
		HashMap<KittyRole, ArrayList<Command>> commandsByRole = new HashMap<KittyRole, ArrayList<Command>>();
		
		// Populate w/ empty array lists
		for(int i = 0; i < KittyRole.values().length; ++i)
			commandsByRole.put(KittyRole.values()[i], new ArrayList<Command>());
		
		// Sort commands out
		ArrayList<Command> commands = Stats.instance.GetAllCommands();
		for(int i = 0; i < commands.size(); ++i)
		{
			Command current = commands.get(i);
			commandsByRole.get(current.RequiredRole()).add(current);
		}
		
		// Format outstring and send it back for now
		String out = "";
		out += PopulateSection(KittyRole.Admin, commandsByRole, true) + "\n";
		out += PopulateSection(KittyRole.Mod, commandsByRole, true) + "\n";
		out += PopulateSection(KittyRole.General, commandsByRole, false) + "\n";
		
		// Write out file
		String filename = "buildhelp_out.txt";

		try {
			PrintWriter writer = new PrintWriter(filename);
			writer.println(out);
			writer.flush();
			writer.close();
		} 
		catch (FileNotFoundException e)
		{
			GlobalLog.Error(e.toString());
			return;
		}
		
		File file = new File(filename);
		res.CallFile(file, "txt");
		ImageUtils.BlockingFileDelete(file);
	}
	
	// Generates the section for a specific role, optionally adding spacing after for another section 
	private String PopulateSection(KittyRole role, HashMap<KittyRole, ArrayList<Command>> commandsByRole, boolean delimitSection)
	{
		// Formatting variables
		final String headerStart = "<h2>";
		final String headerEnd = "</h2>\n";
		final String sectionStart = "<p>\n";
		final String sectionEnd = "</p>";
		final String indent = "  ";
		final String leadStart = "<code>";
		final String leadEnd = "</code>";
		final String leadFollow = ": ";	
		final String lineDelimiter = "<br/>\n";
		final String sectionDelimiter = "\n\n";
		
		String accumulated = "";
		ArrayList<Command> commands = commandsByRole.get(role);
		ArrayList<Command> commandsSoFar = new ArrayList<Command>();
		if(commands.size() > 0)
		{
			String roleStr = role.toString();
			roleStr = roleStr.substring(0, 1).toUpperCase() + roleStr.toLowerCase().substring(1);
			
			// Section header
			accumulated += headerStart + roleStr + " " + Localizer.Stub("commands") + headerEnd;
			accumulated += sectionStart;
			
			// Section content
			for(int i = 0; i < commands.size(); ++i)
			{
				accumulated += indent;
				Command command = commands.get(i);
				
				// Skip multiples
				if(commandsSoFar.contains(command))
					continue;
				else
					commandsSoFar.add(command);
				
				// Write out all the keys and the help text
				ArrayList<String> keys = command.RegisteredNames();
				for(int j = 0; j < keys.size(); ++j)
				{
					if(j != 0)
						accumulated += ", ";
					
					accumulated += leadStart + keys.get(j) + leadEnd;
				}
				
				accumulated += leadFollow + commands.get(i).HelpText() + lineDelimiter;;
			}
						
			accumulated += sectionEnd;
			
			// Add spaces after if needed
			if(delimitSection)
				accumulated += sectionDelimiter;
		}
		
		return accumulated;
	}
}