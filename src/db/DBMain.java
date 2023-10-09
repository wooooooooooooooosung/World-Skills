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
		"CREATE TABLE patient("
			+ "p_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "p_name VARCHAR(10), "
			+ "p_id VARCHAR(15), "
			+ "p_pw VARCHAR(15), "
			+ "p_bd DATE);", 
		"CREATE TABLE doctor("
			+ "d_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "d_section VARCHAR(10), "
			+ "d_name VARCHAR(15), "
			+ "d_day VARCHAR(1), "
			+ "d_time VARCHAR(2));", 
		"CREATE TABLE examination("
			+ "e_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "e_name VARCHAR(10));",
		"CREATE TABLE reservation("
			+ "r_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "p_no INT, "
			+ "d_no INT, "
			+ "r_section VARCHAR(10), "
			+ "r_date VARCHAR(14), "
			+ "r_time VARCHAR(10), "
			+ "e_no INT);", 
		"CREATE TABLE sickroom("
			+ "s_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "s_people INT, "
			+ "s_room INT, "
			+ "s_roomno VARCHAR(20));", 
		"CREATE TABLE hospitalization("
			+ "h_no INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ "p_no INT, "
			+ "s_no INT, "
			+ "h_bendo INT, "
			+ "h_sday VARCHAR(14), "
			+ "h_fday VARCHAR(14), "
			+ "h_meal INT, "
			+ "h_amount INT);"	
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
				sta.executeUpdate("DROP DATABASE hospital");
			} catch (Exception e) {
				System.out.println("DB 삭제 실패");
			}

			sta.executeUpdate("CREATE DATABASE hospital");
			sta.executeUpdate("USE hospital");
			
			for (int i = 0; i < tblCreateQuery.length; i++)
				sta.executeUpdate(tblCreateQuery[i]);
			
			sta.executeUpdate("ALTER TABLE reservation ADD FOREIGN KEY(p_no) REFERENCES patient(p_no);");
			sta.executeUpdate("ALTER TABLE reservation ADD FOREIGN KEY(d_no) REFERENCES doctor(d_no);");
			sta.executeUpdate("ALTER TABLE hospitalization ADD FOREIGN KEY(p_no) REFERENCES patient(p_no);");
			sta.executeUpdate("ALTER TABLE hospitalization ADD FOREIGN KEY(s_no) REFERENCES sickroom(s_no);");
			
			try {
				sta.executeUpdate("DROP USER 'user'@'localhost'");
				sta.executeUpdate("FLUSH PRIVILEGES");
			} catch (Exception e) {
				System.out.println("유저 삭제 실패");
			}

			sta.executeUpdate("CREATE USER 'user'@'localhost' IDENTIFIED BY '1234'");
			sta.executeUpdate("FLUSH PRIVILEGES");
			sta.executeUpdate("GRANT INSERT, DELETE, UPDATE, SELECT ON hospital. * TO 'user'@'localhost'");
			sta.executeUpdate("FLUSH PRIVILEGES");
			
			sta.executeUpdate("SET GLOBAL LOCAL_INFILE = TRUE");
			String arr[] = { "patient", "doctor", "examination", "reservation", "sickroom", "hospitalization" };
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