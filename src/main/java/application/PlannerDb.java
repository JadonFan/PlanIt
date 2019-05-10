package application;

import java.sql.Connection;

public class PlannerDb {
	private static Connection connection;
	

	public static Connection getConnection() {
		return PlannerDb.connection;
	}

	public static void setConnection(Connection connection) {
		PlannerDb.connection = connection;
	}
}
