package pro;

public class Main {

	public static void main(String[] args) {
		try {
			DB.connectDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// new Manage();
		//new Payment(2);
		new Home();
	}

}
