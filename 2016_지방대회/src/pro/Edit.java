package pro;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Edit extends Frame {

	JTextField jt[] = new JTextField[6];
	JButton jb[] = new JButton[2];

	public Edit(String code) {
		setFrame("고객 수정");
		cp.setLayout(new GridLayout(6, 2));
		String[] ls = { "고객코드:", "고 객 명:", "생년월일:", "연 락 처:", "주    소:", "회 사 명" };
		for (int i = 0; i < ls.length; i++) {
			jl = new JLabel(" " + ls[i]);
			setSize(jl, 250, 40);
			cp.add(jl);
			cp.add(jt[i] = new JTextField());
		}
		sp.setLayout(new FlowLayout(1));
		String[] bs = { "수정", "닫기" };
		for (int i = 0; i < bs.length; i++) {
			sp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		jt[0].setEditable(false);
		jt[1].setEditable(false);
		getUserInfo(code);
		showFrame();
	}

	private void getUserInfo(String code) {
		try {
			ResultSet rs = DB.getResultSet("SELECT * FROM customer WHERE code = '" + code + "';");
			while (rs.next()) {
				for (int i = 0; i < jt.length; i++) {
					jt[i].setText(rs.getString(i + 1));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			try {
				DB.updateQuery("UPDATE customer SET birth = '" + jt[2].getText() + "', tel = '" + jt[3].getText()
						+ "', address = '" + jt[4].getText() + "', company = '" + jt[5].getText() + "' WHERE code = '"
						+ jt[0].getText() + "';");
				JOptionPane.showMessageDialog(null, "고객수정이 완료되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
				Search.jt.setText(null);
				Search.setDataModel("");
				dispose();
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
				JOptionPane.showMessageDialog(null, "입력을 확인해주세요", "고객수정 에러", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource().equals(jb[1])) {
			dispose();
		}
	}
}
