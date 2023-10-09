package pro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Chart extends Frame {

	ChartPanel ccp;
	String[] name = new String[3];
	int[] cnt = new int[3];

	public Chart() {
		setFrame("통계");

		wp.add(jsp = new JScrollPane(
				table = new JTable(dtm = new DefaultTableModel(null, new String[] { "진료과", "진료건수" }))));
		try {
			ResultSet rs = DB.executeQuery("SELECT r_section, COUNT(*) FROM reservation GROUP BY r_section;");
			while (rs.next()) {
				dtm.addRow(new String[] { rs.getString(1), rs.getString(2) });
			}
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
		setSize(jsp, 200, 500);
		table.getColumnModel().getColumn(0).setCellRenderer(cell);
		table.getColumnModel().getColumn(1).setCellRenderer(cell);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				doRepaintPanel();
			}
		});

		table.changeSelection(0, 0, false, false);
		cp.add(ccp = new ChartPanel());
		setSize(ccp, 700, 500);
		doRepaintPanel();

		addWindowListener(this);
		showFrame();
	}

	private void doRepaintPanel() {
		try {
			ResultSet rs = DB.executeQuery(
					"SELECT d_name, COUNT(*) FROM reservation r LEFT JOIN doctor d ON r.d_no = d.d_no WHERE d_section = '"
							+ dtm.getValueAt(table.getSelectedRow(), 0)
							+ "' GROUP BY d.d_name ORDER BY COUNT(*) DESC LIMIT 3;");
			int i = 0;
			while (rs.next()) {
				name[i] = rs.getString(1);
				cnt[i] = rs.getInt(2);
				i++;
			}
			ccp.setValue(dtm.getValueAt(table.getSelectedRow(), 0).toString(), name, cnt);
			ccp.repaint();
		} catch (Exception e) {
			showMessage(e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		dispose();
		new Home();
	}

	private class ChartPanel extends JPanel {

		String title;
		String[] name;
		int[] cnt;
		float val;

		Color[] color = { Color.RED, Color.ORANGE, Color.YELLOW };

		private void setValue(String title, String[] name, int[] cnt) {
			this.title = title;
			this.name = name;
			this.cnt = cnt;
			val = (float) 370 / cnt[0];
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g.setColor(new Color(233, 233, 233));
			g.fillRect(0, 0, 700, 500);

			g.setColor(Color.BLACK);
			g.setFont(new Font("", Font.BOLD, 25));
			g.drawString(title, 320, 30);

			for (int i = 0; i < 3; i++) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("", Font.BOLD, 17));
				g.drawString(name[i], 200 * i + 120, 450);
				g.drawString(cnt[i] + "", 200 * i + 135, 50 + 370 - (int) val * cnt[i]);

				g.setColor(color[i]);
				g.fillRect(200 * i + 120, 60 + 370 - (int) val * cnt[i], 50, (int) val * cnt[i]);

				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(3));
				g2.drawRect(200 * i + 120, 60 + 370 - (int) val * cnt[i], 50, (int) val * cnt[i]);
			}

		}
	}
}
