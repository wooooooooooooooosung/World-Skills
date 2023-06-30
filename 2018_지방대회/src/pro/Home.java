package pro;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Home extends Frame{
	
	JButton[] jb = new JButton[4];
	
	public Home() {
		setFrame("메인");
		np.add(new JPanel());
		sp.add(new JPanel());
		cp.add(jp = new JPanel(new GridLayout(4, 1)));
		String[] bs = {"사원등록", "사용자", "관리자", "종료"};
		for (int i = 0; i < bs.length; i++) {
			jp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
			setSize(jb[i], 280, 50);
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		showFrame();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			dispose();
			new Join();
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new Category();
		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new Manage();
		} else if (e.getSource().equals(jb[3])) {
			System.exit(0);
		}
	}

}
