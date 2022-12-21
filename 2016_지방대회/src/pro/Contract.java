package pro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Contract extends Frame {

	JTextField jt[] = new JTextField[5];
	JComboBox<String> com[] = new JComboBox[3];
	JButton jb[] = new JButton[4];

	String la[] = { "고객코드 : ", "고  객  명 : ", "생년월일 : ", "연  락  처 : ", "보험상품 : ", "가입금액 : ", "월보험료 : " };
	String bs[] = { "가입", "삭제", "파일로저장", "닫기" };

	ArrayList<ArrayList<String>> cList = new ArrayList<ArrayList<String>>();
	DefaultTableCellRenderer cell = new DefaultTableCellRenderer();

	int tCnt = 0;
	int lCnt = 0;

	public Contract() {
		setFrame("보험 계약");
		np.add(jp1 = new JPanel(new GridLayout(4, 2)), BorderLayout.WEST);
		for (int i = 0; i < 4; i++) {
			jp1.add(jl = new JLabel(la[lCnt++], JLabel.RIGHT));
			if (i == 1) {
				jp1.add(com[0] = new JComboBox<String>());
			} else {
				jp1.add(jt[tCnt++] = new JTextField());
			}
			setSize(jl, 130, 0);
		}
		np.add(jp1 = new JPanel(new GridLayout(3, 2)), BorderLayout.EAST);
		for (int i = 0; i < 3; i++) {
			jp1.add(jl = new JLabel(la[lCnt++], JLabel.RIGHT));
			if (i == 0) {
				jp1.add(com[1] = new JComboBox<String>());
			} else {
				jp1.add(jt[tCnt++] = new JTextField());
			}
			setSize(jl, 130, 0);
		}
		np.add(jp1 = new JPanel(new FlowLayout(1)), BorderLayout.SOUTH);
		jp1.setBorder(new EmptyBorder(30, 0, 0, 0));
		jp1.add(jl = new JLabel("담당자 : ", JLabel.CENTER));
		jp1.add(com[2] = new JComboBox<String>());
		for (int i = 0; i < jb.length; i++) {
			jp1.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}

		cp.add(jl = new JLabel("< 고객 보험 계약현황 >", JLabel.CENTER), BorderLayout.CENTER);
		sp.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel())), BorderLayout.CENTER);
		cell.setHorizontalAlignment(0);

		np.setBorder(new EmptyBorder(10, 100, 10, 100));
		cp.setBorder(new EmptyBorder(20, 0, 20, 10));
		sp.setBorder(new EmptyBorder(0, 0, 100, 0));

		getCustomer();
		getContract();
		getAdmin();
		com[0].addActionListener(this);
		com[0].setSelectedIndex(0);

		this.addWindowListener(this);
		showFrame();
	}

	private void getCustomer() {
		try {
			for (int i = 0; i < 4; i++) {
				cList.add(new ArrayList<String>());
			}
			ResultSet rs = DB.getResultSet("SELECT * FROM customer ORDER BY name ASC;");
			while (rs.next()) {
				cList.get(0).add(rs.getString(1));
				com[0].addItem(rs.getString(2));
				cList.get(1).add(rs.getString(3));
				cList.get(2).add(rs.getString(4));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void getContract() {
		try {
			ResultSet rs = DB.getResultSet("SELECT DISTINCT(contractName) FROM contract ORDER BY contractName ASC;");
			while (rs.next()) {
				com[1].addItem(rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void getAdmin() {
		try {
			ResultSet rs = DB.getResultSet("SELECT * FROM admin ORDER BY name ASC;");
			while (rs.next()) {
				com[2].addItem(rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void setDataModel(String code) {
		dtm.setDataVector(null,
				new String[] { "customerCode", "contractName", "regPrice", "regDate", "monthPrice", "adminName" });
		try {
			ResultSet rs = DB.getResultSet(
					"SELECT * FROM contract LEFT JOIN customer ON contract.customerCode = customer.code WHERE code LIKE '%"
							+ code + "%' ORDER BY regDate DESC;");
			while (rs.next()) {
				dtm.addRow(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6) });
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		new Home();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			if (isNumeric(jt[3].getText()) && isNumeric(jt[4].getText())) {
				try {
					DB.updateQuery("INSERT INTO contract VALUES('" + jt[0].getText() + "', '"
							+ com[1].getSelectedItem().toString() + "', " + jt[3].getText() + ", NOW(), "
							+ jt[4].getText() + ", '" + com[2].getSelectedItem().toString() + "')");
					com[0].setSelectedIndex(com[0].getSelectedIndex());
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			}
		} else if (e.getSource().equals(jb[1])) {
			if (table.getSelectedRow() != -1) {
				if (JOptionPane.showConfirmDialog(
						null, dtm.getValueAt(table.getSelectedRow(), 0) + "("
								+ dtm.getValueAt(table.getSelectedRow(), 1) + ")" + "을 삭제하시겠습니까?",
						"계약정보 삭제", JOptionPane.YES_NO_OPTION) == 0) {
					try {
						DB.updateQuery("DELETE FROM contract WHERE customerCode LIKE '%"
								+ dtm.getValueAt(table.getSelectedRow(), 0) + "%' AND contractName LIKE '%"
								+ dtm.getValueAt(table.getSelectedRow(), 1) + "%' AND regPrice = "
								+ dtm.getValueAt(table.getSelectedRow(), 2) + " AND regDate = '"
								+ dtm.getValueAt(table.getSelectedRow(), 3) + "' AND monthPrice = "
								+ dtm.getValueAt(table.getSelectedRow(), 4) + " AND adminName LIKE '%"
								+ dtm.getValueAt(table.getSelectedRow(), 5) + "%';");
						com[0].setSelectedIndex(com[0].getSelectedIndex());
					} catch (Exception e1) {
						System.out.println(e1.getMessage());
					}
				}
			}
		} else if (e.getSource().equals(jb[2])) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				String out = "고객명	:	" + com[0].getSelectedItem().toString() + "(" + jt[0].getText() + ")"
						+ System.lineSeparator() + System.lineSeparator() + "담당자명	:	"
						+ com[2].getSelectedItem().toString() + System.lineSeparator() + System.lineSeparator()
						+ "보험상품	가입금액	가입일	월보험료" + System.lineSeparator();
				for (int i = 0; i < table.getRowCount(); i++) {
					for (int j = 1; j <= 4; j++) {
						out += dtm.getValueAt(i, j) + "	";
					}
					out += System.lineSeparator();
				}
				try {
					BufferedWriter w = new BufferedWriter(
							new FileWriter(new File(jfc.getSelectedFile().getPath()), true));
					w.write(out);
					w.flush();
					w.close();
				} catch (IOException e1) {
					System.out.println(e1.getMessage());
				}
			}
		} else if (e.getSource().equals(jb[3])) {
			dispose();
			new Home();
		} else if (e.getSource().equals(com[0])) {
			jt[0].setText(cList.get(0).get(com[0].getSelectedIndex()));
			jt[1].setText(cList.get(1).get(com[0].getSelectedIndex()));
			jt[2].setText(cList.get(2).get(com[0].getSelectedIndex()));
			setDataModel(jt[0].getText());
		}
	}

}
