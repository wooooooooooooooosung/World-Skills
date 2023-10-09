package pro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Home extends Frame {

	JButton[] jb = new JButton[5];

	public Home() {
		setFrame("메인");
		np.add(jl = new JLabel(CV.p_name + "환자", JLabel.CENTER), BorderLayout.CENTER);
		np.add(new JPanel(), BorderLayout.NORTH);
		np.add(new JPanel(), BorderLayout.SOUTH);
		setFont(jl, 22);

		String[] bs = { "진료예약", "입퇴원 신청", "예약현황", "진료과별 분석", "종료" };
		cp.setLayout(new GridLayout(5, 1));
		for (int i = 0; i < bs.length; i++) {
			cp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		setSize(jb[0], 300, 50);
		sp.add(new JPanel());

		addWindowListener(this);
		showFrame();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		jb[4].doClick();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {

		} else if (e.getSource().equals(jb[1])) {

		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new Reservation();
		} else if (e.getSource().equals(jb[3])) {
			dispose();
			new Chart();
		} else if (e.getSource().equals(jb[4])) {
//			dispose();
//			new Login();
			System.exit(0);
		}
	}
}
