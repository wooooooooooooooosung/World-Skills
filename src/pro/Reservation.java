package pro;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Reservation extends Frame {

	JButton[] jb = new JButton[2];

	public Reservation() {
		setFrame("진료예약현황");
		np.add(jl = new JLabel(CV.p_name + "님 진료예약현황", JLabel.CENTER));
		setFont(jl, 15);
		cp.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel(null, new String[] { "", "진료과", "의사", "진료날짜", "시간" }))));
		try {
			ResultSet rs = DB.executeQuery("SELECT r_no, r_section, d_name, r_date, r_time FROM reservation r LEFT JOIN doctor d ON r.d_no = d.d_no WHERE p_no = " + CV.p_no + ";");
			while(rs.next()) {
				dtm.addRow(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5) });
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
		setSize(jsp, 450, 300);

		sp.setLayout(new FlowLayout(2));
		String[] bs = { "예약취소", "닫기" };
		for (int i = 0; i < bs.length; i++) {
			sp.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}

		cp.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
		for (int i = 0; i < 5; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(i == 3 ? 120 : 30);
			table.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setPreferredWidth(0);

		addWindowListener(this);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		jb[1].doClick();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			if (table.getSelectedRow() == -1) {
				showMessage("삭제할 예약을 선택해주세요.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new Home();
		}
	}
}
