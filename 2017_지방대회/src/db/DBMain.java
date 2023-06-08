package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DBMain {

	private Connection con;
	private Statement sta;

	private String[] tblCreateQuery = { 
			"CREATE TABLE TBL_Customer("
			+ "cID VARCHAR(6) PRIMARY KEY NOT NULL, "
			+ "cPW VARCHAR(4), "
			+ "cName VARCHAR(10), "
			+ "cHP VARCHAR(13))", 
			"CREATE TABLE TBL_BUS("
			+ "bNumber VARCHAR(4) PRIMARY KEY NOT NULL, "
			+ "bDeparture VARCHAR(5), "
			+ "bArrival VARCHAR(4), "
			+ "bTime TIME, "
			+ "bElapse VARCHAR(10), "
			+ "bCount VARCHAR(1), "
			+ "bPrice INT(6))", 
			"CREATE TABLE TBL_Ticket("
			+ "bDate DATE, "
			+ "bNumber VARCHAR(4), "
			+ "bNumber2 VARCHAR(5), "
			+ "bSeat INT(2), "
			+ "cID VARCHAR(6), "
			+ "bPrice INT(6), "
			+ "bState VARCHAR(1))" };

	public static void main(String[] args) {
		new DBMain();
	}

	public DBMain() {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/mysql?"
					+ "serverTimezone=UTC&"
					+ "characterEncoding=UTF8&"
					+ "allowLoadLocalInfile=true&"
					+ "allowPublicKeyRetrieval=true&"
					+ "useSSL=false",
					"root", 
					"1234");
			sta = con.createStatement();

			try {
				sta.executeUpdate("DROP DATABASE sw3_001");
			} catch (Exception e) {
				System.out.println("DB 삭제 실패");
			}

			sta.executeUpdate("CREATE DATABASE sw3_001");
			sta.executeUpdate("USE sw3_001");
			
			for (int i = 0; i < tblCreateQuery.length; i++) {
				sta.executeUpdate(tblCreateQuery[i]);
			}
			
			try {
				sta.executeUpdate("DROP USER 'user'@'localhost'");
				sta.executeUpdate("FLUSH PRIVILEGES");
			} catch (Exception e) {
				System.out.println("유저 삭제 실패");
			}

			sta.executeUpdate("CREATE USER 'user'@'localhost' IDENTIFIED BY '1234'");
			sta.executeUpdate("FLUSH PRIVILEGES");
			sta.executeUpdate("GRANT INSERT, DELETE, UPDATE, SELECT ON sw3_001. * TO 'user'@'localhost'");
			sta.executeUpdate("FLUSH PRIVILEGES");
			
			sta.executeUpdate("SET GLOBAL LOCAL_INFILE = TRUE");
			String arr[] = { "TBL_Customer", "TBL_Bus", "TBL_Ticket" };
			for (int i = 0; i < arr.length; i++)
				sta.executeUpdate("LOAD DATA LOCAL INFILE './Resource/" + arr[i] + ".txt' INTO TABLE `" + arr[i] + "` IGNORE 1 LINES;");
			sta.executeUpdate("SET GLOBAL LOCAL_INFILE = FALSE");

			JOptionPane.showMessageDialog(null, "DB 생성 성공", "성공", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "DB 생성 실패", "실패", JOptionPane.ERROR_MESSAGE);
		}
	}

}
