package pro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Join extends Frame {

	JTextField[] jt = new JTextField[2];
	JPasswordField[] jtp = new JPasswordField[2];
	JButton[] jb = new JButton[2];

	public Join() {
		setFrame("사원등록");
		cp.add(new JPanel(), BorderLayout.NORTH);
		cp.add(new JPanel(), BorderLayout.SOUTH);
		cp.add(jp = new JPanel(new GridLayout(5, 2)), BorderLayout.CENTER);
		String[] ls = { "사원번호:", "사 원 명:", "패스워드:", "패스워드 재입력:" };
		for (int i = 0; i < ls.length; i++) {
			jp.add(new JLabel(ls[i]));
			if (i > 1) jp.add(jtp[i - 2] = new JPasswordField());
			else jp.add(jt[i] = new JTextField());
		}
		jt[0].setEditable(false);
		String[] bs = { "등록", "닫기" };
		for (int i = 0; i < bs.length; i++) {
			jp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
			setSize(jb[i], 200, 50);
		}

		try {
			ResultSet rs = DB.executeQuery("SELECT * FROM member ORDER BY memberNo DESC LIMIT 1;");
			if (rs.next()) {
				jt[0].setText(Integer.toString(rs.getInt(1) + 1));
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}

		addWindowListener(this);
		showFrame();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		new Home();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			for (int i = 0; i < jt.length; i++) {
				if (jt[i].getText().length() == 0 || jtp[i].getText().length() == 0) {
					showMessage("항목 누락", "Message", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if (!jtp[0].getText().equals(jtp[1].getText())) {
				showMessage("패스워드 확인 요망", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				System.out.println(String.format("INSERT INTO member VALUES(null, %s, %s);", jt[1].getText(), jtp[0].getText()));
				DB.updateQuery(String.format("INSERT INTO member VALUES(null, '%s', '%s');", jt[1].getText(), jtp[0].getText()));
				showMessage("사원이 등록되었습니다.", "오류", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
			jb[1].doClick();
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new Home();
		}
	}

}
