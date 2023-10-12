package pro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class AdminEdit extends Frame {
	
	JComboBox<String> jc = new JComboBox<String>("음료 푸드 상품".split(" "));
	JTextField[] jt = new JTextField[2];

	JComponent[] jcc = { jc, new JPanel(), jt[0] = new JTextField(10), new JPanel(), jt[1] = new JTextField(10) };
	JComboBox<String> jcom = new JComboBox<String>(new String[] { "전체", "음료", "푸드", "상품" });
	JTextField st = new JTextField(20);
	JButton sb = new JButton("찾기");
 
	JLabel img;
	JButton[] jb = new JButton[4];
	
	String filePath = null;

	public AdminEdit() {
		setFrame("메뉴 수정");
		
		// North Panel
		np.setLayout(new FlowLayout(0));
		np.add(new JLabel("검색"));
		np.add(jcom);
		np.add(st);
		np.add(sb);
		sb.addActionListener(this);
		
		// West Panel
		wp.add(jsp = new JScrollPane(table = new JTable(dtm = new DefaultTableModel(null, new String[] { "", "분류", "메뉴명", "가격" }))));
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setPreferredWidth(0);
		for (int i = 1; i < 4; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(cell);
			table.getColumnModel().getColumn(i).setMaxWidth(i != 2 ? 100 : 200);
		}
		setSize(jsp, 400, 205);
		
		// Center Panel
		cp.add(jp = new JPanel(new FlowLayout(1)), BorderLayout.CENTER);
		jp.add(jp1 = new JPanel(new GridLayout(5, 1)));
		
		String[] ls = { "분류", "메뉴명", "가격" };
		for (int i = 0; i < ls.length; i++) {
			jp1.add(jl = new JLabel(ls[i]));
			setSize(jl, 50, 25);
			if (i != 2) {
				jp1.add(new JPanel());
			}
		}

		jp.add(jp1 = new JPanel(new GridLayout(5, 1)));
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				jp1.add(jp2 = new JPanel(new GridLayout(1, 3)));
				jp2.add(jcc[i]);
				jp2.add(new JPanel());
				jp2.add(new JPanel());
				continue;
			}
			jp1.add(jcc[i]);
		}
		
		cp.add(ep = new JPanel(new BorderLayout()), BorderLayout.EAST);
		ep.add(img = new JLabel(), BorderLayout.CENTER);
		ep.add(jb[0] = new JButton("사진등록"), BorderLayout.SOUTH);

		jb[0].addActionListener(this);
		setSize(img, 130, 130);
		img.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		String[] bs = { "삭제", "수정", "취소" };
		cp.add(jp = new JPanel(new FlowLayout(1)), BorderLayout.SOUTH);
		for (int i = 0; i < bs.length; i++) {
			jp.add(jb[i + 1] = new JButton(bs[i]));
			jb[i + 1].addActionListener(this);
		}
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (table.getRowCount() == 0) return;
				jt[0].setText(dtm.getValueAt(table.getSelectedRow(), 2).toString());
				jt[1].setText(dtm.getValueAt(table.getSelectedRow(), 3).toString());
				img.setIcon(new ImageIcon(new ImageIcon("./DataFiles/이미지/" + dtm.getValueAt(table.getSelectedRow(), 2).toString() + ".jpg").getImage().getScaledInstance(130, 140, Image.SCALE_FAST)));
			}
		});
		
		sp.add(new JPanel());
		
		setMenuList();
		addWindowListener(this);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		jb[3].doClick();
	}
	
	private void setMenuList() {
		try {
			dtm.setRowCount(0);
			filePath = null;
			jt[0].setText("");
			jt[1].setText("");
			img.setIcon(null);
			ResultSet rs = DB.getResultSet("SELECT * FROM menu WHERE" + (jcom.getSelectedIndex() == 0 ? "" : " m_group = '" + jcom.getSelectedItem() + "' AND") + " m_name LIKE '%" + st.getText() + "%';");
			while(rs.next()) dtm.addRow(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) });
		} catch (Exception ex) {
			showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private boolean isExist(String title) {
		try {
			ResultSet rs = DB.getResultSet("SELECT * FROM menu WHERE m_name = '" + title + "';");
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(sb)) {
			setMenuList();
		} else if (e.getSource().equals(jb[0])) {
			JFileChooser jfc = new JFileChooser();
			jfc.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image", "jpg", "jpeg"));
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					filePath = jfc.getSelectedFile().getPath();
					img.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(jfc.getSelectedFile())).getImage().getScaledInstance(130, 130, Image.SCALE_FAST)));
				} catch (Exception ex) {
					showMessage(ex.toString(), "오류", JOptionPane.ERROR_MESSAGE);
					img.setIcon(null);
					filePath = null;
					return;
				}
			}
		} else if (e.getSource().equals(jb[1])) {
			if (table.getSelectedRow() == -1) {
				showMessage("삭제할 메뉴를 선택해주세요.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				File f = new File("./DataFiles/이미지/" + jt[0].getText() + ".jpg");
				if (f.exists()) f.delete();
				DB.updateQuery("DELETE FROM menu WHERE m_no = " + dtm.getValueAt(table.getSelectedRow(), 0) + ";");
				showMessage("삭제되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
				setMenuList();
			} catch (Exception ex) {
				showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if (e.getSource().equals(jb[2])) {
			if (table.getSelectedRow() == -1) {
				showMessage("수정할 메뉴를 선택해주세요.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (jt[0].getText().length() == 0 || jt[1].getText().length() == 0 || (filePath == null && img.getIcon() == null)) {
				showMessage("빈칸이 존재합니다.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (isNumeric(jt[1].getText()) == false) {
				showMessage("가격을 다시 입력해주세요.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (isExist(jt[0].getText())) {
				showMessage("이미 존재하는 메뉴명입니다.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (filePath != null || img.getIcon() != null) {
				try {
					if (filePath != null) {
						File f = new File("./DataFiles/이미지/" + dtm.getValueAt(table.getSelectedRow(), 2) + ".jpg");
						if (f.exists()) f.delete();
						
						Files.copy(new File(filePath).toPath(), new File("./DataFiles/이미지/" + jt[0].getText() + ".jpg").toPath(), StandardCopyOption.REPLACE_EXISTING);
					}
					DB.updateQuery("UPDATE menu SET m_name = '" + jt[0].getText() + "', m_price = " + jt[1].getText() + " WHERE m_no = " + dtm.getValueAt(table.getSelectedRow(), 0) + ";");
					showMessage("수정되었습니다", "메시지", JOptionPane.INFORMATION_MESSAGE);

					setMenuList();
				} catch (Exception ex) {
					showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		} else if (e.getSource().equals(jb[3])) {
			dispose();
			new Admin();
		}
	}
}
