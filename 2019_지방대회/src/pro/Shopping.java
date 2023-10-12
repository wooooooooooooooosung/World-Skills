package pro;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Shopping extends Frame {

	JButton[] jb = new JButton[3];

	public Shopping() {
		setFrame("장바구니");
		np.add(jl = new JLabel(CV.u_name + "회원님 장바구니", JLabel.CENTER));
		setFont(jl, 20);

		try {
			ResultSet rs = DB.getResultSet("SELECT * FROM shopping s LEFT JOIN MENU m ON s.m_no = m.m_no;");
			cp.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel(null, new String[] { "", "메뉴명", "가격", "수량", "사이즈", "금액" }))));
			while (rs.next()) {
				dtm.addRow(new String[] { rs.getString(1), rs.getString(9), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6) });
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
		setSize(jsp, 600, 250);

		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setPreferredWidth(0);
		
		for (int i = 1; i < 6; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(i == 0 ? 200 : 100);
		}
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		sp.setLayout(new FlowLayout(1));
		String[] bs = { "구매", "삭제", "닫기" };
		for (int i = 0; i < jb.length; i++) {
			sp.add(jb[i] = new JButton(bs[i]));
			setSize(jb[i], 120, 30);
			jb[i].addActionListener(this);
		}

		addWindowListener(this);
		setButtonEnable();
		showFrame();
	}
	
	private void setButtonEnable() {
		if (table.getRowCount() == 0) {
			jb[0].setEnabled(false);
			jb[1].setEnabled(false);
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		jb[2].doClick();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			
		} else if (e.getSource().equals(jb[1])) {
			if (table.getSelectedRow() == -1) {
				showMessage("삭제할 메뉴를 선택해주세요.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				DB.updateQuery("DELETE FROM shopping WHERE s_no = " + dtm.getValueAt(table.getSelectedRow(), 0) + ";");
				dtm.removeRow(table.getSelectedRow());
			} catch (Exception ex) {
				showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
			setButtonEnable();
		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new Home();
		}
	}

}
