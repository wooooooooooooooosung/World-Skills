package pro;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Join extends Frame {

	JTextField jt[] = new JTextField[6];
	JButton jb[] = new JButton[2];

	public Join() {
		setFrame("고객 등록");
		cp.setLayout(new GridLayout(6, 2));
		String[] ls = { "고객 코드:", "* 고 객 명:", "*생년월일(YYYY-MM-DD):", "*연 락 처:", "주    소:", "회    사:" };
		for (int i = 0; i < ls.length; i++) {
			jl = new JLabel(" " + ls[i]);
			setSize(jl, 250, 40);
			cp.add(jl);
			cp.add(jt[i] = new JTextField());
		}
		sp.setLayout(new FlowLayout(1));
		String[] bs = { "추가", "닫기" };
		for (int i = 0; i < bs.length; i++) {
			sp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		jt[0].setEditable(false);
		jt[2].addKeyListener(this);
		this.addWindowListener(this);
		showFrame();
	}

	private String getJoinCode() {
		if (isDateFormat(jt[2].getText())) {
			return "S" + Integer.toString(Calendar.getInstance().get(Calendar.YEAR)).substring(2, 4)
					+ (Integer.parseInt(jt[2].getText().split("-")[0]) + Integer.parseInt(jt[2].getText().split("-")[1])
							+ Integer.parseInt(jt[2].getText().split("-")[2]));
		}
		return "";
	}

	public boolean isDateFormat(String n) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			sdf.parse(n);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		new Home();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			jt[0].setText(getJoinCode());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			for (int i = 1; i < 4; i++) {
				if (jt[i].getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "필수항목(*)을 모두 입력하세요", "고객등록 에러", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			for (int i = 0; i < jb.length; i++) {
				if (jt[i].getText().length() == 0) {
					return;
				}
			}
			try {
				DB.updateQuery("INSERT INTO customer VALUES('" + jt[0].getText() + "', '" + jt[1].getText() + "', '"
						+ jt[2].getText() + "', '" + jt[3].getText() + "', '" + jt[4].getText() + "', '"
						+ jt[5].getText() + "');");
				JOptionPane.showMessageDialog(null, "고객추가가 완료되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new Home();
		}
	}

}
