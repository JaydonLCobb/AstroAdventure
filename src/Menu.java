import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel {
	
	public Menu() {
		
		setLayout(new FlowLayout(FlowLayout.CENTER,100000,60));
		setBorder(BorderFactory.createEmptyBorder(260,0, 0, 0));
		
		
		JButton button1 = new JButton("Start Game");
		JButton button2 = new JButton("High Scores");
		JButton button3 = new JButton("Exit");
		
		button1.setPreferredSize(new Dimension(200,40));
		button2.setPreferredSize(new Dimension(200,40));
		button3.setPreferredSize(new Dimension(200,40));
		
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button1) {
				
					System.out.println("button1 pressed");
				}
				else if (e.getSource() == button2) {
				
					System.out.println("button2 pressed");
				}
				else if (e.getSource() == button3) {
			
					System.exit(0);
				}
			}
		};
		
		button1.addActionListener(listener);
		button2.addActionListener(listener);
		button3.addActionListener(listener);
		
		button1.setForeground(Color.blue);
		button2.setForeground(Color.blue);
		button3.setForeground(Color.blue);
		
		add(button1);
		add(button2);
		add(button3);
	}
	
}
