import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ConversionFrame extends JFrame {
	JPanel mainPanel;
	JButton NFAtoDFAButton;
	JButton eNFAtoNFAButton;
	public ConversionFrame () {
		setSize(300,300);
		setTitle("TOC Conversion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.BLACK);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2,1));
		mainPanel.setSize(new Dimension(350,350));
		NFAtoDFAButton = new JButton ("NFA to DFA Conversion");
		NFAtoDFAButton.addActionListener(e->{
			new NFAtoDFAFrame();
		});
		
		eNFAtoNFAButton = new JButton ("eNFA to NFA Conversion");
		eNFAtoNFAButton.addActionListener(e->{
			new eNFAtoNFAFrame();
		});
		
		mainPanel.add(NFAtoDFAButton);		
		mainPanel.add(eNFAtoNFAButton);
		add(mainPanel);
		
		setVisible(true);
	}
}
