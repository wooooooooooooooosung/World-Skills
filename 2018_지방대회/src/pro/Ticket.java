package pro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Ticket extends Frame {
	
	public Ticket(String serialNo, Object[][] param) {
		setFrame("식권");
		this.add(jsp = new JScrollPane(cp = new JPanel()));
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		
		for (int i = 0, flag = 0; i < param.length; i++) {
			for (int j = 0; j < param[i][0].hashCode(); j++) {
				if (!(i == 0 && j == 0)) cp.add(new JPanel());
				cp.add(jp = new JPanel(new BorderLayout()));
				jp.add(jl = new JLabel(serialNo), BorderLayout.NORTH);
				jp.setBackground(flag == 0 ? Color.CYAN : Color.PINK);
				setSize(jl, 0, 30);
				
				jp.add(jp1 = new JPanel(new GridLayout(2, 1)), BorderLayout.CENTER);
				jp1.add(jl = new JLabel("식권", JLabel.CENTER));
				setFont(jl, 30);
				jp1.add(jl = new JLabel(param[i][2].toString(), JLabel.CENTER));
				setFont(jl, 30);
				setSize(jl, 0, 50);
				jp1.setBackground(flag == 0 ? Color.CYAN : Color.PINK);
				
				jp.add(jp1 = new JPanel(new GridLayout(1, 2)), BorderLayout.SOUTH);
				jp1.add(new JLabel("메뉴 : " + param[i][1].toString()));
				jp1.add(new JLabel((j + 1) + "/" + param[i][0].hashCode() + "   ", JLabel.RIGHT));
				jp1.setBackground(flag == 0 ? Color.CYAN : Color.PINK);
				setSize(jp1, 0, 30);
				
				jp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
			flag = flag == 0 ? 1 : 0;
		}
		setSize(jsp, 250, (param.length == 1 && param[0][0].hashCode() == 1) ? 170 : 335);
		
		addWindowListener(this);
		showFrame();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Category();
	}

}
