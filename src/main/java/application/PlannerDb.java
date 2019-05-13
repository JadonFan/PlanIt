package application;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;


public class PlannerDb {
	private static Connection connection;
	private static EntityManagerFactory ENTITY_MANAGER_FACTORY;
	

	public static Connection getConnection() {
		return PlannerDb.connection;
	}

	public static void setConnection(Connection connection) {
		PlannerDb.connection = connection;
	}

	
	public static EntityManagerFactory getEntityManagerFactory() {
		return ENTITY_MANAGER_FACTORY;
	}

	public static void setEntityManagerFactory(EntityManagerFactory ENTITY_MANAGER_FACTORY) {
		PlannerDb.ENTITY_MANAGER_FACTORY = ENTITY_MANAGER_FACTORY;
	}
}
