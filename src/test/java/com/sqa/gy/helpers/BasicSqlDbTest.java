package com.sqa.gy.helpers;

import java.sql.*;

import org.testng.annotations.*;

public class BasicSqlDbTest {
	@Test
	public void excecuteBasicSqlDbTest() throws ClassNotFoundException, SQLException {
		// must be in pom to not get class error
		Class.forName("com.mysql.jdbc.Driver");
		// create connection to db.
		Connection dbconn = DriverManager.getConnection("jdbc:mysql://localhost:8889/sqldb", "root", "root");
		// you need a statement variable into which to store the statement
		Statement stmt = dbconn.createStatement();
		// retrieve a result set by executing query
		ResultSet rs = stmt.executeQuery("select id, user, pass from sqldb");
		// rs.getMetaData().getColumnCount(); if you dont know how many
		while (rs.next()) {
			// You can call by the name of the column or
			// String userID = rs.getString("id");
			String userName = rs.getString(2); // index of columns starting at 1
												// not 0
			String userPass = rs.getString(3);
			System.out.println("\tuser: " + userName + "\tpassword: " + userPass);
		}
		rs.close();
		stmt.close();
		dbconn.close();
	}
}
