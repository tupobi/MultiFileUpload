package com.Jay.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 * 数据库封装类，封装了获取数据库连接的方法。
 */
public class JdbcConnectionUtil {
	private Connection connection = null;

	public Connection getConnection_jdbc(String url, String name, String pwd) {
		try {
			// com.microsoft.jdbc.sqlserver.SQLServerDriver 之前版本使用
			// com.microsoft.sqlserver.jdbc.SQLServerDriver SQLServer2008版本使用
			// Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			
			//连接SqlServer:
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			connection = DriverManager.getConnection(
//					"jdbc:sqlserver://localhost:1433;DatabaseName=TestDB",
//					"Jay", "1399369123");
			
			//连接MySQL:
			Class.forName("com.mysql.jdbc.Driver");
//			jdbc:mysql://localhost:3306/testDB
			connection = DriverManager.getConnection(
					url,
					name, pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
//	public Connection getConnection_jndi(){
//		try{
//			Context cxt = new InitialContext();
//			DataSource source = (DataSource) cxt.lookup("java:comp/env/jdbc/news");
//			connection = source.getConnection();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return connection;
//	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
