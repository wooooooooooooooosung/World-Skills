package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DBMain {

	public static Connection con = null;
	public static Statement sta = null;

	public static void main(String[] args) {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/mysql?serverTimezone=UTC&characterEncoding=UTF8&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&useSSL=false",
					"root", "1234");
			sta = con.createStatement();

			try {
				sta.executeUpdate("DROP DATABASE company_01");
			} catch (Exception e) {
			}

			sta.executeUpdate("CREATE DATABASE company_01");
			sta.executeUpdate("USE company_01");

			sta.executeUpdate("CREATE TABLE admin(" + "name VARCHAR(20) NOT NULL, " + "passwd VARCHAR(20) NOT NULL, "
					+ "position VARCHAR(20), " + "jumin CHAR(14), " + "inputDate DATE, "
					+ "PRIMARY KEY(name, passwd));");
			sta.executeUpdate("CREATE TABLE customer(" + "code CHAR(7) NOT NULL, " + "name VARCHAR(20) NOT NULL, "
					+ "birth DATE, " + "tel VARCHAR(20), " + "address VARCHAR(100), " + "company VARCHAR(20), "
					+ "PRIMARY KEY(code, name));");
			sta.executeUpdate("CREATE TABLE contract(" + "customerCode CHAR(7) NOT NULL, "
					+ "contractName VARCHAR(20) NOT NULL, " + "regPrice INT, " + "regDate DATE NOT NULL, "
					+ "monthPrice INT, " + "adminName VARCHAR(20) NOT NULL);");

			try {
				sta.executeUpdate("DROP USER 'user'@'localhost'");
				sta.executeUpdate("FLUSH PRIVILEGES");
			} catch (Exception e) {
			}

			sta.executeUpdate("CREATE USER 'user'@'localhost' IDENTIFIED BY '1234'");
			sta.executeUpdate("FLUSH PRIVILEGES");
			sta.executeUpdate("GRANT INSERT, DELETE, UPDATE, SELECT ON Daejeon. * TO 'user'@'localhost'");
			sta.executeUpdate("FLUSH PRIVILEGES");

			sta.executeUpdate("SET GLOBAL LOCAL_INFILE = TRUE");
			String arr[] = { "admin", "customer", "contract" };
			for (int i = 0; i < arr.length; i++)
				sta.executeUpdate("LOAD DATA LOCAL INFILE './제공파일/" + arr[i] + ".txt' INTO TABLE `" + arr[i]
						+ "` IGNORE 1 LINES;");

			JOptionPane.showMessageDialog(null, "DB 생성 성공", "성공", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "DB 생성 실패", "실패", JOptionPane.ERROR_MESSAGE);
		}
	}

}
