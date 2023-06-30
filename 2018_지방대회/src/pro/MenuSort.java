package pro;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MenuSort extends Frame {

	JButton jb;
	String[] ca = new String[] { "", "한식", "중식", "일식", "양식" };

	public MenuSort() {
		setFrame("메뉴별 주문현황");
		np.setLayout(new FlowLayout(2));
		np.add(jb = new JButton("닫기"));
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Manage();
			}
		});
		cp.add(jsp = new JScrollPane(
				table = new JTable(dtm = new DefaultTableModel(null, new String[] { "종류", "주문수량" }) {
					@Override
					public Class<?> getColumnClass(int columnIndex) {
						return columnIndex == 1 ? Integer.class : super.getColumnClass(columnIndex);
					}
				})));
		int c = 0;
		try {
			ResultSet rs = DB.executeQuery("SELECT DISTINCT(cuisineNo), COUNT(orderCount) FROM orderlist GROUP BY cuisineNo ORDER BY cuisineNo ASC;");
			while (rs.next()) {
				dtm.addRow(new Object[] { ca[rs.getInt(1)], rs.getInt(2) });
				c += rs.getInt(2);
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
		table.getColumnModel().getColumn(0).setCellRenderer(cell);
		table.getColumnModel().getColumn(1).setCellRenderer(cell);
		table.setAutoCreateRowSorter(true);
		setSize(jsp, 300, 100);
		sp.setLayout(new FlowLayout(2));
		sp.add(new JLabel("합계:" + c + "개"));
		addWindowListener(this);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Manage();
	}
}
