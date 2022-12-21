package pro;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class Home extends Frame {

	JButton jb[] = new JButton[4];

	public Home() {
		setFrame("보험계약 관리화면");
		String bs[] = { "고객 등록", "고객 조회", "계약 관리", "종　 　료" };
		np.setLayout(new FlowLayout());
		for (int i = 0; i < jb.length; i++) {
			np.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		cp.add(new JLabel(new ImageIcon("./제공파일/img.jpg")));
		cp.setBorder(new EmptyBorder(5, 40, 80, 40));
		this.addWindowListener(this);
		showFrame();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		new Login();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			dispose();
			new Join();
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new Search();
		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new Contract();
		} else if (e.getSource().equals(jb[3])) {
			dispose();
			new Login();
		}
	}
}
