package pro;

public class Main {

	public static void main(String[] args) {
		try {
			DB.connectDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new Home();
	}

}
