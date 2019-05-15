package api;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;

public class ApiDatabase {
	private static Connection connection;
	private static EntityManagerFactory emf;
	

	public static Connection getConnection() {
		return ApiDatabase.connection;
	}

	public static void setConnection(Connection connection) {
		ApiDatabase.connection = connection;
	}

	
	public static EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	public static void setEntityManagerFactory(EntityManagerFactory ENTITY_MANAGER_FACTORY) {
		ApiDatabase.emf = ENTITY_MANAGER_FACTORY;
	}
}
