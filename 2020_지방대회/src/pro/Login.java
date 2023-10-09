package pro;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends Frame {

	String[] ls = { "ID : ", "PW : " };
	JTextField[] jt = { new JTextField(10), new JPasswordField(10) };

	public Login() {
		setFrame("로그인");

		np.add(jl = new JLabel("병원예약시스템", JLabel.CENTER));
		setFont(jl, 20);

		cp.setLayout(new FlowLayout(1));
		cp.add(jp = new JPanel(new GridLayout(3, 1)));
		for (int i = 0; i < jt.length; i++) {
			jp.add(new JLabel(ls[i], JLabel.RIGHT));
			if (i == 0) jp.add(new JPanel());
		}
		
		cp.add(jp = new JPanel(new GridLayout(3, 1)));
		for (int i = 0; i < jt.length; i++) {
			jp.add(jt[i]);
			if (i == 0) jp.add(new JPanel());
		}

		showFrame();
	}
}
