package pro;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class OrderList extends Frame {

	JTextField jt = new JTextField(20);
	JButton jb = new JButton("닫기");

	public OrderList() {
		setFrame("구매내역");
		np.add(jl = new JLabel(CV.u_name + "회원님 구매내역", JLabel.CENTER));
		setFont(jl, 20);
		
		int c = 0;
		
		try {
			ResultSet rs = DB.getResultSet("SELECT * FROM orderlist o LEFT JOIN menu m ON o.m_no = m.m_no WHERE o.u_no = " + CV.u_no);
			cp.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel(null, new String[] { "구매일자", "메뉴명", "가격", "사이즈", "수량", "총금액" }))));
			while (rs.next()) {
				dtm.addRow(new String[] { rs.getString(2), rs.getString(12), rs.getString(7), rs.getString(6), rs.getString(8), rs.getString(9) });
				c += rs.getInt(9);
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
		setSize(jsp, 700, 250);
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < 6; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(i == 1 ? 200 : 100);
			table.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}

		jt.setEditable(false);
		jt.setHorizontalAlignment(JTextField.RIGHT);
		jt.setText(new DecimalFormat("###,###").format(c));

		sp.setLayout(new FlowLayout(1));
		sp.add(new JLabel("총 결제 금액"));
		sp.add(jt);
		sp.add(jb);
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Home();
			}
		});

		addWindowListener(this);
		showFrame();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		jb.doClick();
	}

}
