package pro;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Admin extends Frame {

	JButton[] jb = new JButton[3];

	public Admin() {
		setFrame("관리자 메뉴");
		np.add(new JPanel());
		sp.add(new JPanel());
		cp.setLayout(new GridLayout(3, 1));
		String[] bs = { "메뉴 등록", "메뉴 관리", "로그아웃" };
		for (int i = 0; i < bs.length; i++) {
			cp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		setSize(jb[0], 250, 40);

		addWindowListener(this);
		showFrame();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		jb[2].doClick();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			dispose();
			new AdminAdd();
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new AdminEdit();
		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new Login();
		}
	}
}
