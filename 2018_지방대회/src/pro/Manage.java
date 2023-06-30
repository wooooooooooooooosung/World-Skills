package pro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Manage extends Frame {

	JButton[] jb = new JButton[5];

	public Manage() {
		setFrame("관리");
		cp.add(jp = new JPanel(new FlowLayout(1)), BorderLayout.NORTH);
		String[] bs = { "메뉴등록", "메뉴관리", "결제조회", "메뉴별주문현황", "종료" };
		for (int i = 0; i < bs.length; i++) {
			jp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		cp.add(new JLabel(new ImageIcon("./Datafiles/main.jpg")), BorderLayout.CENTER);
		cp.add(new JPanel(), BorderLayout.SOUTH);
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
			dispose();
			new MenuEdit(0);
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new MenuManage();
		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new PaymentList();
		} else if (e.getSource().equals(jb[3])) {
			dispose();
			new MenuSort();
		} else if (e.getSource().equals(jb[4])) {
			dispose();
			new Home();
		}
	}
}
