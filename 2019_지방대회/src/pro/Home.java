package pro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Home extends Frame {

	JButton jb[] = new JButton[4];
	JButton jbb[] = new JButton[3];
	JButton jbbb[] = new JButton[2];
	String m_no[];
	JLabel img[];
	JLabel name[];
	int price[];
	
	int s;

	String bs[] = { "구매내역", "장바구니", "인기상품 Top5", "Logout" };
	String bss[] = { "음료", "푸드", "상품" };
	String bsss[] = { "장바구니에 담기", "구매하기" };

	JLabel sImg;
	
	JComboBox<Integer> jc1 = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	JComboBox<String> jc2 = new JComboBox<String>(new String[] { "M", "L" });
	
	JTextField[] jt = new JTextField[3];
	
	JComponent[] jc = { jt[0] = new JTextField(), jt[1] = new JTextField(), jc1, jc2, jt[2] = new JTextField() };
	
	public Home() {
		setFrame("STARBOX");
		np.add(jp = new JPanel(new GridLayout(2, 1)));
		jp.add(jl = new JLabel(" 회원명 : " + CV.u_name + " / 회원등급 : " + CV.u_grade + " / 총 누적 포인트 : " + CV.u_point,
				JLabel.LEFT));
		setFont(jl, 18);
		jp.add(jp1 = new JPanel(new FlowLayout(0)));
		for (int i = 0; i < bs.length; i++) {
			jp1.add(jb[i] = new JButton(bs[i]));
			jb[i].addActionListener(this);
		}

		wp.setLayout(new GridLayout(10, 1, 0, 5));
		for (int i = 0; i < bss.length; i++) {
			wp.add(jbb[i] = new JButton(bss[i]));
			jbb[i].addActionListener(this);
		}

		cp.add(jsp = new JScrollPane(jp = new JPanel()));
		setSize(jsp, 660, 450);
		
		
		sp.add(new JPanel());
		jt[0].setEditable(false);
		jt[1].setEditable(false);
		jt[2].setEditable(false);
		
		addWindowListener(this);
		showFrame();
		
		jbb[0].doClick();
	}

	private void doDrawImage(String group) {
		ep.removeAll();
		jp.removeAll();
		
		jc2.setEnabled(!group.equals("상품"));

		try {
			ResultSet rs = DB.getResultSet("SELECT COUNT(*) FROM menu WHERE m_group = '" + group + "';");
			rs.next();
			img = new JLabel[rs.getInt(1)];
			m_no = new String[rs.getInt(1)];
			name = new JLabel[rs.getInt(1)];
			price = new int[rs.getInt(1)];
			jp.setLayout(new GridLayout(rs.getInt(1) / 3 + 1, 3));

			rs = DB.getResultSet("SELECT * FROM menu WHERE m_group = '" + group + "';");

			int i = 0;

			while (rs.next()) {
				jp1 = new JPanel(new BorderLayout());
				jp1.add(img[i] = new JLabel( new ImageIcon(new ImageIcon("./DataFiles/이미지/" + rs.getString("m_name") + ".jpg").getImage() .getScaledInstance(i % 3 == 2 ? 210 : 190, 190, Image.SCALE_FAST))), BorderLayout.CENTER);
				img[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				img[i].addMouseListener(this);
				
				jp1.add(name[i] = new JLabel(rs.getString("m_name"), JLabel.CENTER), BorderLayout.SOUTH);
				price[i] = rs.getInt(4);
				m_no[i] = rs.getString(1);
				
				jp1.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, i % 3 == 2 ? 0 : 20));
				jp.add(jp1);
				i++;
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}

		this.revalidate();
		this.pack();
	}
	
	private void doDrawSelectItem(int n) {
		s = n;
		
		ep.removeAll();
		
		ep.add(jp2 = new JPanel(new FlowLayout(1)), BorderLayout.CENTER);
		jp2.add(jp3 = new JPanel(new BorderLayout()));
		jp3.add(sImg = new JLabel(new ImageIcon(new ImageIcon("./DataFiles/이미지/" + name[n].getText() + ".jpg").getImage().getScaledInstance(130, 130, Image.SCALE_FAST))), BorderLayout.NORTH);
		
		sImg.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setSize(sImg, 130, 130);
		
		jp3.setBorder(BorderFactory.createEmptyBorder(0, 20, 55, 0));
		
		String[] ls = { "주문메뉴:", "가격:", "수량:", "사이즈:", "총금액:" };
		jp2.add(jp3 = new JPanel(new GridLayout(5, 1)));
		for (int i = 0; i < ls.length; i++) {
			jp3.add(jp4 = new JPanel(new FlowLayout(1)));
			jp4.add(jl = new JLabel(ls[i], JLabel.RIGHT));
			jp4.add(jc[i]);
			setSize(jl, 60, 30);
			setSize(jc[i], 150, 30);
		}
		
		ep.add(jp3 = new JPanel(new FlowLayout(1)), BorderLayout.SOUTH);
		for (int i = 0; i < bsss.length; i++) {
			jp3.add(jbbb[i] = new JButton(bsss[i]));
			jbbb[i].addActionListener(this);
		}
		
		jt[0].setText(name[n].getText());
		jt[1].setText(price[n] + "");
		
		jp3.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
		jp2.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 10));
		
		this.revalidate();
		this.pack();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		jb[3].doClick();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb[0])) {
			dispose();
			new OrderList();
		} else if (e.getSource().equals(jb[1])) {
			dispose();
			new Shopping();
		} else if (e.getSource().equals(jb[2])) {
			dispose();
			new Chart();
		} else if (e.getSource().equals(jb[3])) {
			dispose();
			new Login();
		} else if (e.getSource().equals(jbb[0])) {
			doDrawImage(bss[0]);
		} else if (e.getSource().equals(jbb[1])) {
			doDrawImage(bss[1]);
		} else if (e.getSource().equals(jbb[2])) {
			doDrawImage(bss[2]);
		} else if (e.getSource().equals(jbbb[0])) {
			try {
				DB.updateQuery("INSERT INTO shopping VALUES(null, "
						+ m_no[s] + ", " 
						+ price[s] + ", " 
						+ jc1.getSelectedItem() + ", '" 
						+ jc2.getSelectedItem() + "', "
						+ (jc2.getSelectedIndex() == 1 ? price[s] + 1000 : price[s] * (jc1.getSelectedIndex() + 1)) + ")");
				showMessage("장바구니에 담았습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
				ep.removeAll();
				this.revalidate();
				this.pack();
			} catch (Exception ex) {
				showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource().equals(jbbb[1])) {
			
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < img.length; i++) {
			if (e.getSource().equals(img[i])) {
				doDrawSelectItem(i);
			}
		}
		
	}

}
