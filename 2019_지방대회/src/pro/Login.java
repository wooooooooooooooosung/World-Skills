package pro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends Frame {

	JTextField jtt[] = { new JTextField(14), new JPasswordField(14) };
	JButton jb[] = new JButton[3];

	public Login() {
		setFrame("로그인");

		np.setLayout(new BorderLayout());

		np.add(jl = new JLabel("STARBOX", JLabel.CENTER));
		jl.setFont(new Font("Arial Black", Font.BOLD, 25));

		cp.add(jp = new JPanel(new GridLayout(2, 1)), BorderLayout.WEST);
		String ls[] = { "   ID : ", "PW : " };
		for (int i = 0; i < 2; i++) {
			jp.add(jp1 = new JPanel(new FlowLayout(1, 0, 5)));
			jp1.add(new JLabel(ls[i], JLabel.RIGHT));
		}

		cp.add(jp = new JPanel(new GridLayout(2, 1)), BorderLayout.CENTER);
		for (int i = 0; i < 2; i++) {
			jp.add(jp1 = new JPanel(new FlowLayout(1, 0, 5)));
			jp1.add(jtt[i]);
		}

		String bs[] = { "로그인", "회원가입", "종료" };
		cp.add(jp = new JPanel(), BorderLayout.EAST);
		jp.add(jb[0] = new JButton("<html><body><br>로그인<br><br></body></html>"));

		cp.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 10));

		sp.add(jp = new JPanel(new FlowLayout(1)));
		for (int i = 0; i < bs.length; i++) {
			if (i != 0) jp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		showFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			if (jtt[0].getText().length() == 0 || jtt[1].getText().length() == 0) {
				showMessage("빈칸이 존재합니다.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (jtt[0].getText().equals("admin") && jtt[1].getText().equals("1234")) {
				dispose();
				new Admin();
			}
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new Join();
		} else if (e.getSource().equals(jb[2])) {
			System.exit(0);
		}
	}
}
