package application;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;


public class PlannerDb {
	private static Connection connection;
	private static EntityManagerFactory emf;
	

	public static Connection getConnection() {
		return PlannerDb.connection;
	}

	public static void setConnection(Connection connection) {
		PlannerDb.connection = connection;
	}

	
	public static EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	public static void setEntityManagerFactory(EntityManagerFactory emf) {
		PlannerDb.emf = emf;
	}
}
