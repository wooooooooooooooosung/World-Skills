package pro;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Join extends Frame {

	JTextField[] jt = new JTextField[3];
	JComboBox[] jc = new JComboBox[] { new JComboBox<String>(getYear()), new JComboBox<String>(getMonth()), new JComboBox<String>() };
	JButton[] jb = new JButton[2];

	public Join() {
		setFrame("회원가입");

		cp.setLayout(new GridLayout(5, 1));

		String[] ls = { "이름", "아이디", "비밀번호", "생년월일" };
		for (int i = 0; i < 3; i++) {
			cp.add(jp = new JPanel(new FlowLayout(1)));
			jp.add(jl = new JLabel(ls[i], JLabel.RIGHT));
			setSize(jl, 70, 25);
			jp.add(jt[i] = new JTextField(18));
		}
		
		cp.add(jp = new JPanel(new FlowLayout(1)));
		jp.add(new JLabel("생년월일"));
		ls = new String[] { "년", "월", "일" };
		for (int i = 0; i < ls.length; i++) {
			jp.add(jc[i]);
			jp.add(new JLabel(ls[i]));
		}
		
		cp.add(jp = new JPanel(new FlowLayout(1)));
		ls = new String[] { "가입 완료", "취소" };
		for (int i = 0; i < ls.length; i++) {
			jp.add(jb[i] = new JButton(ls[i]));
			jb[i].addActionListener(this);
		}
		
		jc[0].addActionListener(this);
		jc[1].addActionListener(this);

		addWindowListener(this);
		showFrame();
	}
	
	private String[] getYear() {
		ArrayList<String> t = new ArrayList<String>();
		t.add("");
		for (int i = 1900, m = new Date().getYear() + 1900; i <= m; i++) {
			t.add(Integer.toString(i));
		}
		return t.toArray(new String[0]);
	}
	
	private String[] getMonth() {
		ArrayList<String> t = new ArrayList<String>();
		t.add("");
		for (int i = 1; i <= 12; i++) {
			t.add(Integer.toString(i));
		}
		return t.toArray(new String[0]);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		jb[1].doClick();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			if (jt[0].getText().length() == 0 || jt[1].getText().length() == 0 || jt[2].getText().length() == 0 || jc[0].getSelectedIndex() == 0 || jc[1].getSelectedIndex() == 0) {
				showMessage("누락된 항목이 있습니다.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new Login();
		} else {
			if (jc[0].getSelectedIndex() == 0 || jc[1].getSelectedIndex() == 0) {
				return;
			}
			jc[2].removeAllItems();
			
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(jc[0].getSelectedItem().toString()), jc[1].getSelectedIndex() - 1, 1);
			for (int i = 1, m = cal.getActualMaximum(Calendar.DAY_OF_MONTH); i <= m; i++) {
				jc[2].addItem(i);
			}
		}
	}

}
