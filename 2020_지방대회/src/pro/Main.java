package pro;

import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {
		try {
			DB.connectDB();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "DB 접속 실패", "오류", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
//		new Login();
//		new Chart();
		new Reservation();
//		new Home();
	}

}
