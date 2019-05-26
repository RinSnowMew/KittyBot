package network;

import java.util.Arrays;
import java.util.Optional;

public enum JDBCStatementType
{
	Insert (0), Update(1), Select(2), Create(4);
	
	private final int value;
	private JDBCStatementType(int value) 
	{
		this.value = value;
	}
	
	public int getValue() 
	{
		return value;
	}
	
	public static Optional<JDBCStatementType> valueOf(int value) 
	{
		return Arrays.stream(values()).filter(role -> role.value == value).findFirst();
	}
}
