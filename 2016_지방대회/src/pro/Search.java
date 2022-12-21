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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Search extends Frame {

	static JTextField jt;
	static DefaultTableModel dtm;
	JButton jb[] = new JButton[5];

	public Search() {
		setFrame("고객 조회");
		np.setLayout(new FlowLayout(1));
		np.setBorder(new EmptyBorder(0, 100, 0, 100));
		np.add(jl = new JLabel("성명"));
		np.add(jt = new JTextField(10));
		String[] bs = { "조회", "전체보기", "수정", "삭제", "닫기" };
		for (int i = 0; i < bs.length; i++) {
			np.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		cp.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel())));
		setSize(jsp, 0, 600);
		setDataModel("");
		this.addWindowListener(this);
		showFrame();
	}

	public static void setDataModel(String name) {
		dtm.setDataVector(null, new String[] { "code", "name", "birth", "tel", "address", "company" });
		try {
			ResultSet rs = DB.getResultSet("SELECT * FROM customer WHERE name LIKE '%" + name + "%' ORDER BY name;");
			while (rs.next()) {
				dtm.addRow(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6) });
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void removeData(int row) {
		try {
			DB.updateQuery("DELETE FROM customer WHERE code = '" + dtm.getValueAt(row, 0) + "';");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		new Home();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			setDataModel(jt.getText());
		} else if (e.getSource().equals(jb[1])) {
			jt.setText("");
			setDataModel(jt.getText());
		} else if (e.getSource().equals(jb[2])) {
			if (table.getSelectedRow() != -1) {
				new Edit(dtm.getValueAt(table.getSelectedRow(), 0).toString());
			}
		} else if (e.getSource().equals(jb[3])) {
			if (table.getSelectedRow() != -1) {
				if (JOptionPane.showConfirmDialog(null, dtm.getValueAt(table.getSelectedRow(), 1) + "님을 정말 삭제하시겠습니까?",
						"고객정보 삭제", JOptionPane.YES_NO_OPTION) == 0) {
					removeData(table.getSelectedRow());
					setDataModel(jt.getText());
				}
			}
		} else if (e.getSource().equals(jb[4])) {
			dispose();
			new Home();
		}
	}

}
