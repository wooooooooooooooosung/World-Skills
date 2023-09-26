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
		"CREATE TABLE menu("
			+ "m_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "m_group VARCHAR(10), "
			+ "m_name VARCHAR(30), "
			+ "m_price INT);", 
		"CREATE TABLE orderlist("
			+ "o_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "o_date DATE, "
			+ "u_no INT, "
			+ "m_no INT, "
			+ "o_group VARCHAR(10),"
			+ "o_size VARCHAR(1), "
			+ "o_price INT, "
			+ "o_count INT, "
			+ "o_amount INT);", 
		"CREATE TABLE user("
			+ "u_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "u_id VARCHAR(20), "
			+ "u_pw VARCHAR(4), "
			+ "u_name VARCHAR(5), "
			+ "u_bd VARCHAR(14), "
			+ "u_point INT, "
			+ "u_grade VARCHAR(10));",
		"CREATE TABLE shopping("
			+ "s_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "m_no INT, "
			+ "s_price INT, "
			+ "s_count INT, "
			+ "s_size VARCHAR(1), "
			+ "s_amount INT);"
	};

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
				sta.executeUpdate("DROP DATABASE coffee");
			} catch (Exception e) {
				System.out.println("DB 삭제 실패");
			}
			
			sta.executeUpdate("CREATE DATABASE coffee");
			sta.executeUpdate("USE coffee");
			
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
			sta.executeUpdate("GRANT INSERT, DELETE, UPDATE, SELECT ON coffee. * TO 'user'@'localhost'");
			sta.executeUpdate("FLUSH PRIVILEGES");
			
			sta.executeUpdate("SET GLOBAL LOCAL_INFILE = TRUE");
			String arr[] = { "menu", "orderlist", "user" };
			for (int i = 0; i < arr.length; i++)
				sta.executeUpdate("LOAD DATA LOCAL INFILE './DataFiles/" + arr[i] + ".txt' INTO TABLE `" + arr[i] + "` IGNORE 1 LINES;");
			sta.executeUpdate("SET GLOBAL LOCAL_INFILE = FALSE");
			
			JOptionPane.showMessageDialog(null, "DB 생성 성공", "성공", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "실패", JOptionPane.ERROR_MESSAGE);
		}
	}

}
