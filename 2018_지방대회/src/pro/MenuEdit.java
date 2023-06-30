package pro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MenuEdit extends Frame {

	int mealNo = 0;
	JComboBox[] jc = new JComboBox[3];
	JButton[] jb = new JButton[2];
	JTextField jt;

	public MenuEdit(int mealNo) {
		this.mealNo = mealNo;
		setFrame(mealNo == 0 ? "메뉴 등록" : "메뉴 수정");
		cp.add(new JPanel(), BorderLayout.NORTH);
		cp.add(jp = new JPanel(new GridLayout(5, 2)), BorderLayout.CENTER);
		JComponent[] jcp = new JComponent[] { 
				new JLabel("종류"), jc[0] = new JComboBox<String>(new String[] { "한식", "중식", "일식", "양식" }), 
				new JLabel("메뉴명"), jt = new JTextField(), 
				new JLabel("가격"), jc[1] = new JComboBox<Integer>(Arrays.stream(IntStream.range(2, 25).map(i -> i * 500).toArray()).boxed().toArray(Integer[]::new)),
				new JLabel("조리가능수량"), jc[2] = new JComboBox<Integer>(Arrays.stream(IntStream.range(0, 51).map(i -> i).toArray()).boxed().toArray(Integer[]::new)),
				jb[0] = new JButton("등록"), jb[1] = new JButton("닫기") };
		for (int i = 0; i < jcp.length; i++) jp.add(jcp[i]);
		setSize(jt, 200, 50);
		cp.add(new JPanel(), BorderLayout.SOUTH);
		
		if (mealNo != 0) {
			try {
				ResultSet rs = DB.executeQuery("SELECT * FROM meal WHERE mealNo = " + mealNo);
				while (rs.next()) {
					jc[0].setSelectedIndex(rs.getInt(2) - 1);
					jt.setText(rs.getString(3));
					jc[1].setSelectedItem(rs.getInt(4));
					jc[2].setSelectedItem(rs.getInt(5));
				}
			} catch (Exception e) {
				showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
		}
		jb[0].addActionListener(this);
		jb[1].addActionListener(this);
		
		addWindowListener(this);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		if (mealNo == 0) new Manage();
		else new MenuManage();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			if (jt.getText().trim().length() == 0) {
				showMessage("메뉴명을 입력해주세요.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try {
				String query = mealNo == 0 ? String.format("INSERT INTO meal VALUES(0, %d, '%s', %d, %d, 0)", (jc[0].getSelectedIndex() + 1), jt.getText(), jc[1].getSelectedItem(), jc[2].getSelectedItem())
						: String.format("UPDATE meal SET cuisineNo = %d, mealName = '%s', price = %d, maxCount = %d WHERE mealNo = %d", (jc[0].getSelectedIndex() + 1), jt.getText(), jc[1].getSelectedItem(), jc[2].getSelectedItem(), mealNo);
				String msg = "메뉴가 " + (mealNo == 0 ? "등록" : "수정") + "되었습니다.";
				
				DB.updateQuery(query);
				showMessage(msg, "Message", JOptionPane.INFORMATION_MESSAGE);
				jb[1].doClick();
			} catch (Exception ex) {
				showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}

		} else if (e.getSource().equals(jb[1])) {
			dispose();
			if (mealNo == 0) new Manage();
			else new MenuManage();
		}
	}

}
