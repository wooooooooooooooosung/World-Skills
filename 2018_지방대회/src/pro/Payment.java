package pro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Payment extends Frame {
	
	int cuisineNo;
	
	JButton[] jbf;
	JTextField[] jt = new JTextField[2];
	JButton[] jb = new JButton[3];
	
	ArrayList<Node> list = new ArrayList<Node>();
	ArrayList<String> ml = new ArrayList<String>();
	ArrayList<String> mp = new ArrayList<String>();
	int select = 0;
	
	JComboBox<Object> jc;
	JTextField pass;
	
	public Payment(int cuisineNo) {
		this.cuisineNo = cuisineNo;
		setFrame("결제");
		np.add(new JPanel());
		wp.add(new JPanel());
		ep.add(new JPanel());
		sp.add(new JPanel());
		cp.add(jl = new JLabel(c[cuisineNo], JLabel.CENTER), BorderLayout.NORTH);
		setFont(jl, 25);
		cp.add(jp = new JPanel(new FlowLayout(1)), BorderLayout.CENTER);
		
		try {
			jp.add(jp1 = new JPanel(new BorderLayout()));
			ResultSet rs = DB.executeQuery("SELECT COUNT(*) FROM meal WHERE cuisineNo = " + cuisineNo + " AND maxCount > 1 AND todayMeal = 1");
			int row = rs.next() ? rs.getInt(1) : 0, i = 0;
			rs = DB.executeQuery("SELECT * FROM meal WHERE cuisineNo = " + cuisineNo + " AND maxCount > 1 AND todayMeal = 1");
			jp1.add(jp2 = new JPanel(new GridLayout(row / 5 + (row % 5 > 0 ? 1 : 0), 5)), BorderLayout.CENTER);
			jbf = new JButton[row];
			while(rs.next()) {
				jp2.add(jbf[i] = new JButton(String.format("%s (%d)", rs.getString(3), rs.getInt(4))));
				jbf[i++].addActionListener(this);
				list.add(new Node(rs.getInt(1), rs.getString(3), rs.getInt(4), rs.getInt(5)));
			}
			setSize(jbf[0], 130, 70);
			jp1.add(jp2 = new JPanel(), BorderLayout.SOUTH);
			setSize(jp2, 0, 90);
			
			rs = DB.executeQuery("SELECT * FROM member");
			while (rs.next()) {
				ml.add(rs.getString(1));
				mp.add(rs.getString(3));
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
		
		jp.add(jp1 = new JPanel(new BorderLayout()));
		jp1.add(jp2 = new JPanel(new GridLayout(1, 2)), BorderLayout.NORTH);
		jp2.add(jl = new JLabel("총결제금액:", JLabel.LEFT));
		setFont(jl, 20);
		jp2.add(jl = new JLabel("0원", JLabel.RIGHT));
		setFont(jl, 20);
		jp1.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel(null, new String[] { "상품번호", "품명", "수량", "금액", "" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		})), BorderLayout.CENTER);
		setSize(jsp, 500, 250);
		
		jp1.add(jp2 = new JPanel(new BorderLayout()), BorderLayout.SOUTH);
		jp2.add(jp3 = new JPanel(new FlowLayout(1)), BorderLayout.NORTH);
		JComponent[] jc = { new JLabel("선택품명:"), jt[0] = new JTextField(30), new JLabel("수량:"), jt[1] = new JTextField(5) };
		for (int i = 0; i < jc.length; i++) jp3.add(jc[i]);
		jt[0].setEditable(false);
		
		jp2.add(jp3 = new JPanel(new FlowLayout(1)), BorderLayout.CENTER);
		String[] bs = { "입력", "결제", "닫기" };
		for (int i = 0; i < bs.length; i++) {
			jp3.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
			setSize(jb[i], 130, 40);
		}
		
		table.getColumnModel().getColumn(4).setWidth(0);
		table.getColumnModel().getColumn(4).setMinWidth(0);
		table.getColumnModel().getColumn(4).setMaxWidth(0);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2) return;
				jbf[Integer.parseInt(dtm.getValueAt(table.getSelectedRow(), 4).toString())].setEnabled(true);
				dtm.removeRow(table.getSelectedRow());
				updateAmount();
			}
		});
		
		addWindowListener(this);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Category();
	}
	
	private void updateAmount() {
		int sum = 0;
		for (int i = 0; i < table.getRowCount(); i++) sum += Integer.parseInt(dtm.getValueAt(i, 3).toString());
		jl.setText(new DecimalFormat("###,###,###원").format(sum));
	}
	
	private JPanel getAuthPanel() {
		JPanel tp = new JPanel(new GridLayout(2, 2));
		JComponent jcp[] = { new JLabel("사원번호"), jc = new JComboBox<Object>(ml.toArray()), new JLabel("패스워드"), pass = new JTextField() };
		for (int i = 0; i < jcp.length; i++) tp.add(jcp[i]);
		return tp;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			if (jbf[select].isEnabled() == false) {
				return;
			}
			if (jt[1].getText().length() == 0 || !isNumeric(jt[1].getText()) || Integer.parseInt(jt[1].getText()) == 0) {
				showMessage("수량을 입력해주세요.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (Integer.parseInt(jt[1].getText()) > list.get(select).count) {
				showMessage("조리가능수량이 부족합니다.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			dtm.addRow(new Object[] { list.get(select).no, list.get(select).food, jt[1].getText(), list.get(select).amount * Integer.parseInt(jt[1].getText()), select });
			jbf[select].setEnabled(false);
			
			updateAmount();
		} else if (e.getSource().equals(jb[1])) {
			if (table.getRowCount() == 0) {
				showMessage("메뉴를 선택해주세요.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (JOptionPane.showConfirmDialog(this, getAuthPanel(), "결제자 인증", JOptionPane.YES_NO_OPTION) == 1) {
				return;
			}
			if (!mp.get(jc.getSelectedIndex()).equals(pass.getText())) {
				showMessage("비밀번호가 일치하지 않습니다.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Object[][] param = new Object[table.getRowCount()][3];
			try {
				for (int i = 0; i < param.length; i++) {
					param[i][0] = Integer.parseInt(dtm.getValueAt(i, 2).toString());
					param[i][1] = dtm.getValueAt(i, 1).toString();
					param[i][2] = new DecimalFormat("###,###원").format(list.get(Integer.parseInt(dtm.getValueAt(i, 4).toString())).amount);
					DB.updateQuery(String.format("INSERT INTO orderlist VALUES(0, %d, %d, '%s', %d, %d, NOW())", cuisineNo, 
							Integer.parseInt(dtm.getValueAt(i, 0).toString()), jc.getSelectedItem().toString(), param[i][0], Integer.parseInt(dtm.getValueAt(i, 3).toString())));
					DB.updateQuery("UPDATE meal SET maxCount = maxCount - " + param[i][0] + " WHERE mealNo = " + dtm.getValueAt(i, 0).toString());
				}
			} catch (Exception ex) {
				showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
			
			dispose();
			new Ticket(String.format("%s-%s-%d", new SimpleDateFormat("yyyyMMddKKmmss").format(new Date()), jc.getSelectedItem().toString(), cuisineNo), param);
			
		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new Category();
		} else {
			for (int i = 0; i < jbf.length; i++) {
				if (e.getSource().equals(jbf[i])) {
					jt[0].setText(list.get(i).food);
					select = i;
				}
			}
		}
	}

	class Node {
		int no, amount, count;
		String food;

		public Node(int no, String food, int amount, int count) {
			this.no = no;
			this.food = food;
			this.amount = amount;
			this.count = count;
		}
	}
}
