package core.benchmark;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dataStructures.Pair;
import utils.io.DirectoryMonitor;
import utils.io.FileUtils;
import utils.io.MonitoredFile;

// All things considered, this doesn't need to be particularly efficient since anything
// less than 10ms of search time won't be noticable to an end user really, not for
// a networked application that's not performance bound. 
public class BenchmarkManager
{
	// Variables
	public final String directory = "assets/userbench/";
	public final String extension = ".csv";
	public final String lineDelimiter = "\n";
	
	private List<BenchmarkEntry> raw;
	private DirectoryMonitor directoryMonitor;
	private boolean needsUpdate;
	
	// Read in all extensions
	public BenchmarkManager()
	{
		long start = Instant.now().toEpochMilli();
		
		raw = new ArrayList<BenchmarkEntry>();
		directoryMonitor = new DirectoryMonitor(directory);
		needsUpdate = false;
		
		RebuildLookup();
		
		BenchmarkLog.Log("Took " + (Instant.now().toEpochMilli() - start) + " ms to load " + raw.size() + " entries from " + directoryMonitor.GetCurrentFiles().size() + " file" + (raw.size() > 1 ? "s" : ""));
	}
	
	// Rebuilds the files being monitored
	public void RebuildLookup()
	{
		long start = Instant.now().toEpochMilli();
		
		synchronized(raw)
		{
			raw.clear();
			List<MonitoredFile> files;
			
			synchronized(directoryMonitor)
			{
				files = directoryMonitor.GetCurrentFiles();
			}
			
			if(files != null)
			{
				for(MonitoredFile mf : files)
				{
					String contents = FileUtils.ReadContent(mf.path);
					String[] lines = contents.split(lineDelimiter);
					
					for(int i = 1; i < lines.length; ++i)
						raw.add(new BenchmarkEntry(lines[i]));
				}
			}
			else
			{
				BenchmarkLog.Warn("No " + extension + " files where found in " + directory);
			}
		}
		
		long end = Instant.now().toEpochMilli();
		BenchmarkLog.Log("Rebuilt data in " + (end - start) + "ms");
	}
	
	// Re-evaluates and re-orders the input list based on the Levenshtein distance of the
	// original search and the contents of the provided list of benchmark entries.
	public List<BenchmarkEntry> EvaluateLevenshteinDistance(List<BenchmarkEntry> entries, String search)
	{
		// Populate cost list with heuristic results
		List<Pair<BenchmarkEntry, Integer>> cost = new ArrayList<Pair<BenchmarkEntry, Integer>>();
		int bestSoFar = Integer.MAX_VALUE;
		for(int i = 0; i < entries.size(); ++i)
		{
			BenchmarkEntry entry = entries.get(i);
			String entryString = entry.brand + " " + entry.model;
			
			int heuristic = LevenshteinHeuristic(entryString, search, bestSoFar);
			if(heuristic < bestSoFar)
				bestSoFar = heuristic;
			
			cost.add(new Pair<BenchmarkEntry, Integer>(entry, heuristic));
		}
		
		// Sort list 
		Collections.sort(cost, (i1, i2) -> i1.Second.compareTo(i2.Second));
		
		// Build new output list
		List<BenchmarkEntry> sortedEntries = new ArrayList<BenchmarkEntry>();
		for(int i = 0; i < cost.size(); ++i)
			sortedEntries.add(cost.get(i).First);
		
		return sortedEntries;
	}
	
	// Finds the minimum integer in an array of ints and returns it.
	private int min(int[] arr)
	{
		int min = Integer.MAX_VALUE;
		for(int i = 0; i < arr.length; ++i)
		{
			if(arr[i] < min)
				min = arr[i];
		}
		
		return min;
	}
		
	// Returns cost based on distance of characters from string. This is a kinda sloppy way
	// to do it, but because I keep tabs on the best result so far, it could be much worse. 
	// Still chucks a lot - a non-recursive result w/ memoization would be best but this will do.
	private int LevenshteinHeuristic(String str1, String str2, int bestSoFar)
	{
		int cost;
		
		if(str1.length() <= 0) 
			return str2.length();
		
		if(str2.length() <= 0)
			return str1.length();
		
		if(str1.charAt(0) == str2.charAt(0))
			cost = 0;
		else 
			cost = 1;
		
		int distance = Math.abs(str1.length() - str2.length());
		if(distance > bestSoFar)
			return distance;
		
		int s1 = LevenshteinHeuristic(str1.substring(1), str2, bestSoFar) + 1;
		int s2 = LevenshteinHeuristic(str1, str2.substring(1), bestSoFar) + 1;
		int s3 = LevenshteinHeuristic(str1.substring(1), str2.substring(1), bestSoFar) + cost;
		
		return min(new int[] {s1, s2, s3 });
	}
	
	// Search for a substring in the model name
	public List<BenchmarkEntry> FindModel(String modelSubstr)
	{
		long start = Instant.now().toEpochMilli();
		List<BenchmarkEntry> matching = new ArrayList<BenchmarkEntry>();
		String searchSubstr = modelSubstr.toLowerCase();
		
		for(BenchmarkEntry e : raw)
		{
			String model = e.model.toLowerCase().trim();
			
			if(model.contains(searchSubstr))
				matching.add(e);
		}
		
		BenchmarkLog.Log("Searched for '" + searchSubstr + "' for "+ (Instant.now().toEpochMilli() - start) + "ms and found " + matching.size() + " entries.");
		return matching;
	}
	
	// Keeps tabs on any changes of the files.
	public void Update()
	{
		needsUpdate = false;
		directoryMonitor.Update(this::OnRescan, this::OnRescan, this::OnRescan);
		
		if(needsUpdate)
			RebuildLookup();
	}
	
	// When a file is changed, handle it.
	private void OnRescan(MonitoredFile file)
	{
		// For now, all we need is to note that something was adjusted. 
		if(file.path.toString().contains(extension))
		{
			BenchmarkLog.Log("File status changed: " + file.path);
			needsUpdate = true;
		}
	}
}
