package pro;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends Frame {

	JTextField jt[] = { new JTextField(14), new JPasswordField(14) };
	JButton jb[] = new JButton[2];

	public Login() {
		setFrame("로그인");
		np.add(jl = new JLabel("관리자 로그인", JLabel.CENTER));
		setFont(jl, 25);
		String ls[] = { "이름", "비밀번호" };
		cp.setLayout(new GridLayout(2, 1));
		cp.setBorder(new EmptyBorder(10, 40, 10, 40));
		for (int i = 0; i < ls.length; i++) {
			cp.add(jp = new JPanel());
			jp.add(jl = new JLabel(ls[i], JLabel.LEFT));
			setSize(jl, 50, 25);
			jp.add(jt[i]);
		}
		String bs[] = { "확인", "종료" };
		sp.setLayout(new FlowLayout());
		for (int i = 0; i < jb.length; i++) {
			sp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		this.addWindowListener(this);
		showFrame();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			try {
				ResultSet rs = DB.getResultSet("SELECT * FROM admin WHERE name = '" + jt[0].getText()
						+ "' AND passwd = '" + jt[1].getText() + "';");
				if (rs.next()) {
					dispose();
					new Home();
				}
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		} else if (e.getSource().equals(jb[1])) {
			System.exit(0);
		}
	}
}
