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
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AdminAdd extends Frame {

	JComboBox<String> jc = new JComboBox<String>("음료 푸드 상품".split(" "));
	JTextField[] jt = new JTextField[2];

	JComponent[] jcc = { jc, new JPanel(), jt[0] = new JTextField(10), new JPanel(), jt[1] = new JTextField(10) };

	JLabel img;
	JButton[] jb = new JButton[3];
	
	String filePath = null;

	public AdminAdd() {
		setFrame("메뉴추가");
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
		np.add(new JPanel());

		ep.add(img = new JLabel(), BorderLayout.CENTER);
		ep.add(jb[0] = new JButton("사진등록"), BorderLayout.SOUTH);

		jb[0].addActionListener(this);
		setSize(img, 130, 130);
		img.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		String[] bs = { "등록", "취소" };
		sp.setLayout(new FlowLayout(1));
		for (int i = 0; i < bs.length; i++) {
			sp.add(jb[i + 1] = new JButton(bs[i]));
			jb[i + 1].addActionListener(this);
		}

		addWindowListener(this);
		showFrame();
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
	public void windowClosing(WindowEvent e) {
		jb[2].doClick();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
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
			if (jt[0].getText().length() == 0 || jt[1].getText().length() == 0 || filePath == null) {
				showMessage("빈칸이 존재합니다.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (isNumeric(jt[1].getText()) == false) {
				showMessage("가격은 숫자로 입력해주세요.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (isExist(jt[0].getText())) {
				showMessage("이미 존재하는 메뉴명입니다.", "메시지", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				Files.copy(new File(filePath).toPath(), new File("./DataFiles/이미지/" + jt[0].getText() + ".jpg").toPath(), StandardCopyOption.REPLACE_EXISTING);
				DB.updateQuery("INSERT INTO menu VALUES(NULL, '" + jc.getSelectedItem() + "', '" + jt[0].getText() + "', " + jt[1].getText() + ");");
			} catch (Exception ex) {
				showMessage(ex.toString(), "오류", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			showMessage("메뉴가 등록되었습니다", "메시지", JOptionPane.INFORMATION_MESSAGE);
			jb[2].doClick();
		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new Admin();
		}
	}
}
