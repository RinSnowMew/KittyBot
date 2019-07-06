package commands.benchmark;

import java.util.List;

import core.benchmark.BenchmarkCommand;
import core.benchmark.BenchmarkEntry;
import core.benchmark.BenchmarkFormattable;
import core.benchmark.BenchmarkInput;
import core.benchmark.BenchmarkManager;
import dataStructures.KittyEmbed;
import java.awt.Color;

public class BenchmarkCommandInfo extends BenchmarkCommand 
{
	static final String lineDelimiter = "\n";
	
	public static String formatInfo(BenchmarkEntry entry)
	{
		String output = "";
		
		output += "`" + entry.brand + " " + entry.model + "`" + lineDelimiter;
		output += "<" + entry.url + ">";
		
		return output;
	}
	
	public static KittyEmbed formatInfoEmbed(BenchmarkEntry entry)
	{
		// Populate general embed info
		KittyEmbed embed = new KittyEmbed();
		
		embed.title = "" + entry.brand + " " + entry.model + "" + lineDelimiter;
		embed.authorLink = entry.url;
		embed.authorImage = "https://kittybot.net/assets/generic/baseline_assessment_black_48dp.png";
		embed.authorText = "[view online]";
		embed.color = Color.LIGHT_GRAY;
		embed.descriptionText = "Average Bench: " + entry.benchmark + "%";
		
		// Brand coloring
		String brandClean = entry.brand.toLowerCase().trim();
		
		if(brandClean.contains("intel"))
			embed.color = Color.getHSBColor((float) (205.0/360), 1.00f, 0.75f);
		else if(brandClean.contains("amd"))
			embed.color = Color.getHSBColor((float) (357.0/360), 0.88f, 0.92f);
		else if (brandClean.contains("nvidia"))
			embed.color = Color.getHSBColor((float) (081.0/360), 1.00f, 0.72f);
		
		// Add relative performance numbers
		switch(entry.type)
		{
			case CPU:
				embed.footerText = "(100% ~ Intel i7-7700k)";
				break;
			case GPU:
				embed.footerText = "(100% ~ Nvidia GTX 1070)";
				break;
			case SSD:
				embed.footerText = "(100% ~ Samsung 850 Pro)";
				break;
			case HDD:
				embed.footerText = "(100% ~ Seagate Baracuda 3TB (2016))";
				break;
			case RAM:
				embed.footerText = "(100% ~ Corsair Vengence LPX 2666 C15 4x8GB w/ Intel Skylake)";
				break;
			case USB:
				embed.footerText = "(100% ~ Sandisk Extreme 64GB)";
				break;
			default:
		}
	
		return embed;
	}
		
	@Override
	public BenchmarkFormattable onRun(BenchmarkManager manager, BenchmarkInput input) 
	{
		String searchString = input.value.trim();
		long start = System.currentTimeMillis();
		List<BenchmarkEntry> entries = manager.findModel(searchString);
		
		if(entries.size() < 1)
			return new BenchmarkFormattable("Couldn't find any models containing `" + searchString + "`!");
		
		List<BenchmarkEntry> sortedEntries = manager.evaluateLevenshteinDistance(entries, searchString);
		BenchmarkEntry entry = sortedEntries.get(0);
		KittyEmbed embed = formatInfoEmbed(entry);
		
		if(entries.size() > 1)
			embed.footerText += " (Chose the " + entry.model + " from " + entries.size() + " potential components. To see others, try the command 'benchmark find " + searchString +"')";
		
		embed.footerText += " (took " + (System.currentTimeMillis() - start) + "ms)"; 
		
		return new BenchmarkFormattable(embed);
	}
	
}
