package network;

import java.sql.ResultSet;

public class JDBCDriverMySQL extends JDBCDriver
{
	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean executeStatement(JDBCStatementType type, String statement, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet executeReturningStatement(JDBCStatementType type, String statement, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
}
