package com.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;




public  class SQLConnection {

	private static org.apache.log4j.Logger log = Logger.getLogger(SQLConnection.class);
	public static void main(String[] args) {
		System.out.println(new SQLConnection().getConnection());
	}

	public  Connection getConnection() {
		
		ResourceBundle rb1=ResourceBundle.getBundle("Environment");
        String environment = rb1.getString("ActiveEnvironment");
        ResourceBundle rb2=ResourceBundle.getBundle(environment+"_Config");
        
        
		Connection conn=null;
//		String connectionString = rb2.getString("connectionString");
		
		String url = rb2.getString("URL");
		String dbName = rb2.getString("dbInstanceName");
		String driver = rb2.getString("driver");
		String userName = rb2.getString("userName");
		String password = rb2.getString("password");

		/*
		 * String url = "jdbc:mysql://localhost:3306/"; String dbName =
		 * "SecOnt"; String driver = "com.mysql.jdbc.Driver"; String userName =
		 * "root"; String password = "soon1234";
		 */
		try {
			Class.forName(driver).newInstance();
		
			conn = DriverManager.getConnection(url + dbName, userName, password);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DB CONNECTION FAILURE");
			System.out.println("Connection Failure to the database.");
		}
		
		

//		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//
//			conn = DriverManager.getConnection(connectionString);
//
//		} catch (Exception ae) {
//			ae.printStackTrace();
//		}
		return conn;
	}
}

