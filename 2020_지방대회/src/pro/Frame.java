package pro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Frame extends JFrame implements ActionListener, WindowListener, KeyListener {

	JPanel jp, jp1, jp2, jp3, jp4, np, ep, cp, sp, wp;
	JLabel jl;

	JTable table;
	JScrollPane jsp;
	DefaultTableModel dtm;
	DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
	
	public void setFrame(String title) {
		this.setTitle(title);
		this.add(np = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		this.add(cp = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		this.add(ep = new JPanel(new BorderLayout()), BorderLayout.EAST);
		this.add(wp = new JPanel(new BorderLayout()), BorderLayout.WEST);
		this.add(sp = new JPanel(new BorderLayout()), BorderLayout.SOUTH);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cell.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public void showFrame() {
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void setSize(JComponent c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
	}

	public void setFont(JComponent c, int sz) {
		c.setFont(new Font("", Font.BOLD, sz));
	}

	public boolean isNumeric(String input) {
		try {
			Long.parseLong(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void showMessage(String message, String title, int type) {
		JOptionPane.showMessageDialog(null, message, title, type);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}