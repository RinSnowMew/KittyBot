package dataStructures;

// A generic class designed to hold two types, as a first and second variable.
public class Pair<T1, T2>
{
	public T1 First;
	public T2 Second;
	
	public Pair(T1 first, T2 second)
	{
		this.First = first;
		this.Second = second;
	}
}
