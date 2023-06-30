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
		"CREATE TABLE member("
			+ "memberNo INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "memberName VARCHAR(20), "
			+ "passwd VARCHAR(4))",
		"CREATE TABLE cuisine("
			+ "cuisineNo INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "cuisineName VARCHAR(10))", 
		"CREATE TABLE meal("
			+ "mealNo INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "cuisineNo INT, "
			+ "mealName VARCHAR(20), "
			+ "price INT, "
			+ "maxCount INT, "
			+ "todayMeal TINYINT(1))", 
		"CREATE TABLE orderlist("
			+ "orderNo INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "cuisineNo INT, "
			+ "mealNo INT, "
			+ "memberNo INT, "
			+ "orderCount INT, "
			+ "amount INT, "
			+ "orderDate DATETIME)"
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
				sta.executeUpdate("DROP DATABASE meal");
			} catch (Exception e) {
				System.out.println("DB 삭제 실패");
			}

			sta.executeUpdate("CREATE DATABASE meal");
			sta.executeUpdate("USE meal");
			
			for (int i = 0; i < tblCreateQuery.length; i++)
				sta.executeUpdate(tblCreateQuery[i]);
			
			try {
				sta.executeUpdate("DROP USER 'user'@'localhost'");
				sta.executeUpdate("FLUSH PRIVILEGES");
			} catch (Exception e) {
				System.out.println("유저 삭제 실패");
			}

			sta.executeUpdate("CREATE USER 'user'@'localhost' IDENTIFIED BY '1234'");
			sta.executeUpdate("FLUSH PRIVILEGES");
			sta.executeUpdate("GRANT INSERT, DELETE, UPDATE, SELECT ON meal. * TO 'user'@'localhost'");
			sta.executeUpdate("FLUSH PRIVILEGES");
			
			sta.executeUpdate("SET GLOBAL LOCAL_INFILE = TRUE");
			String arr[] = { "member", "cuisine", "meal", "orderlist" };
			for (int i = 0; i < arr.length; i++)
				sta.executeUpdate("LOAD DATA LOCAL INFILE './Datafiles/" + arr[i] + ".txt' INTO TABLE `" + arr[i] + "` IGNORE 1 LINES;");
			sta.executeUpdate("SET GLOBAL LOCAL_INFILE = FALSE");

			JOptionPane.showMessageDialog(null, "DB 생성 성공", "성공", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "DB 생성 실패", "실패", JOptionPane.ERROR_MESSAGE);
		}
	}

}