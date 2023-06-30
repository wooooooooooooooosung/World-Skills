package pro;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Category extends Frame {

	JButton[] jb = new JButton[4];
	SimpleDateFormat sdf = new SimpleDateFormat("현재시간 : yyyy년 MM월 dd일 kk시 mm분 ss초");
	Thread th = new Thread(new Runnable() {
		
		@Override
		public void run() {
			while(true) {
				try {
					jl.setText(sdf.format(new Date()));
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	});

	public Category() {
		setFrame("식권 발매 프로그램");
		np.add(jl = new JLabel("식권 발매 프로그램", JLabel.CENTER));
		setFont(jl, 25);
		
		JTabbedPane tab = new JTabbedPane();
		cp.add(tab);
		tab.add("메뉴", jp = new JPanel(new GridLayout(2, 2)));
		for (int i = 0; i < jb.length; i++) {
			jp.add(jb[i] = new JButton(new ImageIcon("./Datafiles/menu_" + (i + 1) + ".png")));
			jb[i].setToolTipText(c[i + 1]);
			jb[i].addActionListener(this);
		}
		
		sp.add(jl = new JLabel("", JLabel.CENTER));
		sp.setBackground(Color.BLACK);
		jl.setForeground(Color.WHITE);
		setFont(jl, 20);
		setSize(jl, 0, 30);
		th.start();
		
		addWindowListener(this);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Home();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < jb.length; i++) {
			if (e.getSource().equals(jb[i])) {
				dispose();
				new Payment(i + 1);
			}
		}
	}

}
