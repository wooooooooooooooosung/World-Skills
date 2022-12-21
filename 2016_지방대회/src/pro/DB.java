package pro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DB {

	public static Connection con = null;
	public static Statement sta = null;

	public static void connectDB() throws Exception {
		con = DriverManager.getConnection(
				"jdbc:mysql://localhost/company_01?serverTimezone=UTC&characterEncoding=UTF8&useSSL=false&allowPublicKeyRetrieval=true&allowLoadLocalInfile=true",
				"root", "1234");
		sta = con.createStatement();
	}

	public static ResultSet getResultSet(String query) throws Exception {
		return sta.executeQuery(query);
	}

	public static void updateQuery(String query) throws Exception {
		sta.executeUpdate(query);
	}
}
