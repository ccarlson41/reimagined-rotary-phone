package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public static Connection conn() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
