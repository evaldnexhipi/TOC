import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class NFAtoDFAFrame extends JFrame{
	
	public ArrayList<String> alphabetList;
	public int statesSize;
	DefaultTableModel modelNFA = new DefaultTableModel();
	DefaultTableModel modelDFA = new DefaultTableModel();
	
	public JPanel northPanel1;
	public JLabel alphabetLabel;
	public JTextField alphabetField;
	public JButton alphabetButton;
	public JLabel statesLabel;
	public JTextField statesField;
	public JButton statesButton;

	public JPanel northPanel2;
	public JPanel northPanel;
	public JPanel mainPanel;
	
	public NFAtoDFAFrame() {
		setSize(1200,700);
		setTitle("NFA to DFA");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.BLACK);
		
		buildNorthPanel();
		buildMainPanel();
		setVisible(true);
	}
	
	public void buildNorthPanel() {
		northPanel = new JPanel();
		alphabetList = new ArrayList<String>();
		northPanel.setLayout(new GridLayout(2,1));
		northPanel.add(createNorthPanel1());
		northPanel.add(createNorthPanel2());
		this.add(northPanel,BorderLayout.NORTH);
	}
	
	
	public JPanel createNorthPanel1() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 133, 51));
		panel.setLayout(new GridLayout(1,7));
		
		alphabetLabel = new JLabel("Σ={}");
		panel.add(alphabetLabel);
		alphabetField = new JTextField("");
		panel.add(alphabetField);
		
		alphabetButton = new JButton("Add to Σ");
		alphabetButton.setBackground(new Color(88, 24, 69));
		alphabetButton.setForeground(Color.WHITE);
		alphabetButton.addActionListener(e->{
			if(alphabetField.getText()!=null && alphabetField.getText()!="") {
				if(alphabetField.getText().length()!=1) {
					JOptionPane.showMessageDialog(this, "Only Add Symbols");
				}
				else {
					String text = alphabetField.getText();
					alphabetList.add(text);
					String st = "Σ={";
					for (int i=0; i<alphabetList.size();i++) {
						if(i!=alphabetList.size()-1) {
							st+=alphabetList.get(i)+",";
						}
						else {
							st+=alphabetList.get(i)+"}";
						}
					}
					alphabetLabel.setText(st);
				}
			}
		});
		panel.add(alphabetButton);
		
		statesLabel = new JLabel("|Q|="+statesSize);
		panel.add(statesLabel);
		statesField = new JTextField("");
		panel.add(statesField);
		statesButton = new JButton("Add to Q");
		statesButton.setBackground(new Color(88, 24, 69));
		statesButton.setForeground(Color.WHITE);
		statesButton.addActionListener(e->{
			String txt = statesField.getText();
			if (txt!=null && txt!="") {
				try {
					int number = Integer.parseInt(txt);
					statesSize=number;
					statesLabel.setText("|Q|="+statesSize);
				}
				catch(NumberFormatException pe) {
					JOptionPane.showMessageDialog(this,"Add numbers");
				}
			}
		});
		
		
		panel.add(statesButton);
		panel.add(createBuildNFAButton());
		
		return panel;
	}
	
	public JPanel createNorthPanel2() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(createSetFinalStatesButton());
		panel.add(createSetConnectionsButton());
		return panel;
	}
	
	public JButton createBuildNFAButton() {
		JButton button = new JButton("Build NFA");
		button.addActionListener(e->{
			
			
		});
		return button;
	}
	
	public JButton createSetFinalStatesButton() {
		JButton button = new JButton ("Set Final States");
		button.setBackground(new Color(88, 24, 69));
		button.setForeground(Color.WHITE);
		button.addActionListener(e->{
			//mainPanel.repaint();
		});
		return button;
	}
	
	public JButton createSetConnectionsButton() {
		JButton button = new JButton ("Set Connections");
		button.setBackground(new Color(88, 24, 69));
		button.setForeground(Color.WHITE);
		button.addActionListener(e->{
			
		});
		return button;
	}
	
	class MainPanel extends JPanel{
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			//Painting instructions
		}
	}
	
	public void buildMainPanel() {
		mainPanel = new MainPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new GridLayout(1,2));
		
		modelNFA.addColumn("Input State");
		modelNFA.addColumn("Symbol");
		modelNFA.addColumn("Output State");
		modelNFA.addRow(new String[] {"Input State","Symbol","Output State"});
		
		
		JTable nfaTable = new JTable(modelNFA);
		JTable dfaTable = new JTable(modelDFA);
		
		mainPanel.add(nfaTable);		
		mainPanel.add(dfaTable);	
		this.add(mainPanel,BorderLayout.CENTER);
	}
	
}

