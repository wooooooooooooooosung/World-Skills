package pro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Chart extends Frame{
	
	JComboBox<String> jc = new JComboBox<String>("음료 푸드 상품".split(" "));
	ChartPanel ccp = new ChartPanel();
	
	public Chart() {
		setFrame("인기상품 Top5");
		np.setLayout(new FlowLayout(1));
		np.add(jc);
		np.add(jl = new JLabel("인기상품 Top5"));
		setFont(jc, 15);
		setFont(jl, 20);
		
		cp.add(ccp);
		setSize(ccp, 410, 400);
		
		addWindowListener(this);
		jc.addActionListener(this);
		
		jc.setSelectedIndex(0);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Home();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			ResultSet rs = DB.getResultSet("SELECT m_name, SUM(o_count) FROM menu m LEFT JOIN orderlist o ON m.m_no = o.m_no WHERE m_group = '" + jc.getSelectedItem() + "' GROUP BY m.m_no ORDER BY SUM(o_count) DESC LIMIT 5;");
			int i = 0;
			String[] name = new String[5];
			int[] cnt = new int[5];
			while(rs.next()) {
				name[i] = rs.getString(1);
				cnt[i] = rs.getInt(2);
				i++;
			}
			ccp.setValue(name, cnt);
			ccp.repaint();
			
		} catch (Exception ex) {
			showMessage(ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class ChartPanel extends JPanel {
		
		Color[] color = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN };
		String[] name;
		int[] cnt;
		float v;
		
		private void setValue(String[] name, int[] cnt) {
			this.name = name;
			this.cnt = cnt;
			this.v = (float) 300 / cnt[0];
		}
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			g.setColor(new Color(233, 233, 233));
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(2));
			g2.drawLine(50, 20, 50, 340);
			
			for (int i = 0; i < 5; i++) {
				g.setColor(color[i]);
				g.fillRect(50, 50 + i * 60, (int) v * cnt[i], 25);
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(2));
				g2.drawRect(50, 50 + i * 60, (int) v * cnt[i], 25);
				g.setFont(new Font("", Font.BOLD, 12));
				g.drawString(name[i] + "-" + cnt[i] + "개", 55, 90 + i * 60);
			}
		}
	}
}
