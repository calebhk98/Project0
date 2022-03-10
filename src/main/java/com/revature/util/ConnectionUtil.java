package com.revature.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;


public class ConnectionUtil {

	private static Logger logBot=Logger.getLogger(ConnectionUtil.class);	
	private static Connection conn=null;
	private ConnectionUtil() {}
	/*public static Connection getConnection() {
		try {
			if(conn!=null && !conn.isClosed()) {				
				logBot.info("Returned the connection");
				return conn;				
			}			
		}
		catch(SQLException e) {
			logBot.error("No connection");
			e.printStackTrace();
			return null;
		}		
		String url="";
		String username="";
		String password="";
		Properties prop =new Properties();
		try {
			prop.load(new FileReader("D:\\Desktop\\Training\\DatabaseDemo\\src\\main\\resources\\application.properties"));
			url=prop.getProperty("url");
			username=prop.getProperty("username");
			password=prop.getProperty("password");					
			conn=DriverManager.getConnection(url, username, password);			
		} catch (FileNotFoundException e) {
			logBot.error("Couldn't find the file");			
			e.printStackTrace();
		} catch (IOException e) {
			logBot.error("Couldn't read from the properties path");		
			e.printStackTrace();
		} catch (SQLException e) {
			logBot.error("Couldn't connect to the database");	
			e.printStackTrace();
		}
		return conn;	
	}	
*/
	public static String getSchema() {	
		try {
			Properties prop=new Properties();		
			prop.load(new FileReader( "D:\\Desktop\\Projects\\Project 0\\project-0-calebhk98\\src\\main\\resources\\application.properties"));			
			String schema=prop.getProperty("schema");
			return schema;
		} catch (FileNotFoundException e) {
			logBot.error("No file exists for the properties");
			e.printStackTrace();
		} catch (IOException e) {
			logBot.error("Can't read from the properties file");
			e.printStackTrace();
		}
		return "project_zero";
		
	}
	
	
	public static Connection myConnection() {		
		try {
			if(conn!=null && !conn.isClosed()) {
				
				return conn;
			}
		} catch (SQLException e) {
			logBot.error("The connection has an error");
			e.printStackTrace();
		}		
		try {
			Properties prop=new Properties();		
			prop.load(new FileReader( "D:\\Desktop\\Projects\\Project 0\\project-0-calebhk98\\src\\main\\resources\\application.properties"));			
			String url=prop.getProperty("url");
			String userName=prop.getProperty("username");
			String password=prop.getProperty("password");			
			conn = DriverManager.getConnection(url, userName, password);
		} catch (FileNotFoundException e) {
			logBot.error("No file exists for the properties");
			e.printStackTrace();
		} catch (IOException e) {
			logBot.error("Can't read from the properties file");
			e.printStackTrace();
		} catch (SQLException e) {
			logBot.error("Unable to connect to the database");
			e.printStackTrace();
		}		
		return conn;
	}
}
