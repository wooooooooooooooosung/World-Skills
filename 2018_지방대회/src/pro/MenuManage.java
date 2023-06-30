package pro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MenuManage extends Frame {
	
	JButton[] jb = new JButton[6];
	JComboBox<String> jc;

	public MenuManage() {
		setFrame("메뉴 관리");
		np.add(jp = new JPanel(new FlowLayout(1)));
		String bs[] = { "모두 선택", "검색", "수정", "삭제", "오늘의 메뉴 선정", "닫기" };
		for (int i = 0; i < bs.length; i++) {
			jp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
			if (i == 0) {
				jp.add(new JLabel("종류:"));
				jp.add(jc = new JComboBox<String>(new String[] { "한식", "중식", "일식", "양식" }));
			}
		}

		cp.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel()) {
			@Override
			public Class getColumnClass(int col) {
				return col == 0 ? Boolean.class : String.class;
			}
		}), BorderLayout.CENTER);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (table.columnAtPoint(e.getPoint()) == 0) {
					setSelectAll();
				}
			}
		});
		setSize(jsp, 0, 500);

		addWindowListener(this);
		setDataModel(1);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Manage();
	}

	private void setDataModel(int cuisineNo) {
		try {
			ResultSet rs = DB.executeQuery("SELECT * FROM meal WHERE cuisineNo = " + cuisineNo + ";");
			dtm.setDataVector(null, new String[] { "", "mealName", "price", "maxCount", "todayMeal", "asd" });
			while (rs.next())
				dtm.addRow(new Object[] { false, rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getInt(6) == 1 ? 'Y' : 'N', rs.getInt(1) });

			for (int i = 1; i <= 4; i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(cell);
				table.getColumnModel().getColumn(i).setPreferredWidth(250);
			}
			table.getColumnModel().getColumn(5).setWidth(0);
			table.getColumnModel().getColumn(5).setMinWidth(0);
			table.getColumnModel().getColumn(5).setMaxWidth(0);
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void setSelectAll() {
		jb[0].setEnabled(true);
		for (int i = 0, t = table.getRowCount(); i < t; i++)
			if ((boolean) dtm.getValueAt(i, 0) == false) return;
		jb[0].setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			for (int i = 0, t = table.getRowCount(); i < t; i++) dtm.setValueAt(true, i, 0);
			jb[0].setEnabled(false);
		} else if (e.getSource().equals(jb[1])) {
			setDataModel(jc.getSelectedIndex() + 1);
			jb[0].setEnabled(true);
		} else if (e.getSource().equals(jb[5])) {
			dispose();
			new Manage();
		} else {
			Stack<Integer> list = new Stack<Integer>();
			for (int i = 0, t = table.getRowCount(); i < t; i++)
				if (dtm.getValueAt(i, 0) == Boolean.TRUE) list.add(i);

			// 수정
			if (e.getSource().equals(jb[2])) {
				if (list.size() == 0 || list.size() > 1) {
					showMessage(list.size() == 0 ? "수정할 메뉴를 선택해주세요." : "하나씩 수정가능합니다.", "Message", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int n = list.pop();
				
				dispose();
				new MenuEdit((int) dtm.getValueAt(n, 5));
			}
			// 삭제
			else if (e.getSource().equals(jb[3])) {
				if (list.size() == 0) {
					showMessage("삭제할 메뉴를 선택해주세요.", "Message", JOptionPane.ERROR_MESSAGE);
					return;
				}

				while (list.size() != 0) {
					int d = list.pop();
					int s = (int) dtm.getValueAt(d, 5);
					try {
						DB.updateQuery("DELETE FROM meal WHERE mealNo = " + s);
						dtm.removeRow(d);
					} catch (Exception ex) {
						showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
			// 오늘의 메뉴
			else {
				if (list.size() > 25) {
					showMessage("25개를 초과할 수 없습니다.", "Message", JOptionPane.ERROR_MESSAGE);
					return;
				}

				while (list.size() != 0) {
					int d = list.pop();
					int s = (int) dtm.getValueAt(d, 5);
					try {
						DB.updateQuery("UPDATE meal SET todayMeal = 1 WHERE mealNo = " + s);
					} catch (Exception ex) {
						showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
					}
					dtm.setValueAt("Y", d, 4);
				}
			}
		}
	}

}
