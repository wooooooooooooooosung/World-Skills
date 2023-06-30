package pro;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class PaymentList extends Frame {

	JTextField jt;
	JButton[] jb = new JButton[4];

	public PaymentList() {
		setFrame("결제조회");
		np.setLayout(new FlowLayout(1));
		np.add(new JLabel("메뉴명:"));
		np.add(jt = new JTextField(10));
		String[] bs = { "조회", "모두보기", "인쇄", "닫기" };
		for (int i = 0; i < bs.length; i++) {
			np.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}
		cp.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel()) {
			@Override
			public TableCellRenderer getCellRenderer(int arg0, int arg1) {
				return cell;
			}
		}));
		setSize(jsp, 0, 400);
		setDataModel();
		
		addWindowListener(this);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Manage();
	}

	private void setDataModel() {
		dtm.setDataVector(null, new String[] { "종류", "메뉴명", "사원명", "결제수량", "총결제금액", "결제일" });
		String[] c = { "", "한식", "중식", "일식", "양식" };
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			ResultSet rs = DB.executeQuery("SELECT * FROM orderlist o LEFT JOIN meal m ON o.mealNo = m.mealNo LEFT JOIN member u ON o.memberNo = u.memberNo WHERE m.mealName LIKE '%" + jt.getText() + "%';");
			while (rs.next()) dtm.addRow(new String[] { c[rs.getInt(2)], rs.getString(10), rs.getString(15), rs.getString(5), rs.getString(6), sdf.format(rs.getDate(7)) });
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0]) || e.getSource().equals(jb[1])) {
			if (e.getSource().equals(jb[1]))jt.setText("");
			setDataModel();
		} else if (e.getSource().equals(jb[2])) {
			try {
				table.print();
			} catch (PrinterException pe) {
				showMessage(pe.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource().equals(jb[3])) {
			dispose();
			new Manage();
		}
	}
}
