package pro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DB {

	private static Connection con = null;
	private static Statement sta = null;

	public static void connectDB() throws Exception {
		con = DriverManager.getConnection(
				"jdbc:mysql://localhost/hospital?"
				+ "serverTimezone=UTC&"
				+ "characterEncoding=UTF8&"
				+ "allowLoadLocalInfile=true&"
				+ "allowPublicKeyRetrieval=true&"
				+ "useSSL=false",
				"user", 
				"1234");
		sta = con.createStatement();
	}

	public static ResultSet executeQuery(String query) throws Exception {
		return sta.executeQuery(query);
	}

	public static void updateQuery(String query) throws Exception {
		sta.executeUpdate(query);
	}
}