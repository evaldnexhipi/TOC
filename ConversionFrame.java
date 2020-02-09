import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ConversionFrame extends JFrame {
	
	private JPanel northPanel;
	private JPanel northPanel1;
	private JLabel alphabetLabel;
	private JTextField alphabetField;
	private JButton addSymbolButton;
	private JLabel numberOfStatesLabel;
	private JTextField numberOfStatesField;
	private JButton addNumberOfStates;
	private JLabel addStateLabel;
	private JTextField inputStateField;
	private JTextField symbolField;
	private JTextField outputStateField;
	private JButton addTransitionButton;
	
	private JPanel northPanel2;
	private JLabel convertFromLabel;
	private JComboBox<String>convertFromBox;
	private JLabel toLabel;
	private JComboBox<String>convertToBox;
	private JButton addConversionButton;
	private JButton convertButton;
	
	private JPanel centerPanel;
	private JTable inputAutomataTable;
	private JTable outputAutomataTable;
	
	
	private ArrayList<Character> alphabet;
	private int numberOfStates;
	private ArrayList <eNFAState> eNFA;
	private ArrayList <NFAState> NFA;
	private ArrayList<DFAState> DFA;
	private ArrayList <DFAMinimalState> DFAMin;
	
	private String convertFrom;
	private String convertTo;
	
	public ConversionFrame () {
		setSize(1200,700);
		setTitle("TOC Conversion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//getContentPane().setBackground(Color.BLACK);
		
		eNFA = new ArrayList<eNFAState>();
		NFA = new ArrayList <NFAState>();
		DFA = new ArrayList <DFAState>();
		DFAMin = new ArrayList<DFAMinimalState>();
		
		createNorthPanel();
		createCenterPanel();
		
		setVisible(true);
	}
	
	public void createNorthPanel() {
		northPanel=new JPanel();
		northPanel.setLayout(new GridLayout(2,1));
		alphabet=new ArrayList<Character>();
		createNorthPanel1();
		createNorthPanel2();
		
		add(northPanel,BorderLayout.NORTH);
	}
	
	public void createNorthPanel1() {
		northPanel1=new JPanel();
		alphabetLabel=new JLabel("Σ={}");
		alphabetField=new JTextField();
		addSymbolButton=new JButton("Add Symbol");
		
		addSymbolButton.addActionListener(e->{
			char c = alphabetField.getText().charAt(0);
			alphabet.add(c);
			String s = "";
			for (char ch : alphabet)
				s+=ch+",";
			s=s.substring(0,s.length()-1);
			alphabetLabel.setText("Σ={"+s+"}");
		});
		
		numberOfStatesLabel = new JLabel ("|Q|=");
		numberOfStatesField = new JTextField();
		addNumberOfStates = new JButton ("ADD");
		addNumberOfStates.addActionListener(e->{
			int size = Integer.parseInt(numberOfStatesField.getText());
			numberOfStates=size;
			numberOfStatesLabel.setText(numberOfStatesLabel.getText()+size);
		});
		
		addStateLabel = new JLabel("Add State: ");
		inputStateField = new JTextField();
		symbolField = new JTextField();
		outputStateField=new JTextField();
		addTransitionButton = new JButton("Add Transition");
		
		addTransitionButton.addActionListener(e->{
			DefaultTableModel model = (DefaultTableModel)inputAutomataTable.getModel();
			
			if (convertFrom=="eNFA") {
				Vector <String> newRow = new Vector<String>();
				String q1 = inputStateField.getText();
				newRow.add(q1);
//				System.out.println("Q1: "+q1);
				char symb = symbolField.getText().charAt(0);
				newRow.add(""+symb);
				String q2 = outputStateField.getText();
				newRow.add(q2);
//				System.out.println("Q2: "+q2);
				String q11 = q1.substring(1,q1.length()-1);
				String q22 = q2.substring(1, q2.length()-1);
				
				boolean exists1=false; boolean exists2=false;
				for (eNFAState ens : eNFA) {
					if (q1.contains("[")) {
						if (ens.getTitle().compareTo(q11)==0)
							exists1=true;
					}
					else {
						if (ens.getTitle().compareTo(q1)==0)
							exists1=true;
					}
				}
				
				if(!exists1) {
					eNFAState neweNFAState;
					if(q1.contains("[")) {
						neweNFAState = new eNFAState(q11);
						neweNFAState.setType(Tip.FINAL);
						
					}else
					neweNFAState=new eNFAState(q1);
//					neweNFAState.checkType();
//					System.out.println("Added State: "+neweNFAState.getTitle());
					eNFA.add(neweNFAState); 
				}
				
				for (eNFAState ens: eNFA) {
					if(q2.contains("[")) {
						if(ens.getTitle().compareTo(q22)==0)
							exists2=true;  
					}
					else {
						if(ens.getTitle().compareTo(q2)==0)
							exists2=true;
					}
				}
				
				if(!exists2) {
					eNFAState neweNFAState2;
					if(q2.contains("[")) {
						neweNFAState2=new eNFAState(q22);
						neweNFAState2.setType(Tip.FINAL);	
					}else
					neweNFAState2=new eNFAState(q2);
//					neweNFAState2.checkType();
//					System.out.println("Added State: "+neweNFAState2.getTitle());
					eNFA.add(neweNFAState2); 
				}
				
				eNFAState state1 = null,state2 = null;
				for (eNFAState ens : eNFA) {
					if (q1.contains("[")) {
						if(ens.getTitle().compareTo(q11)==0) {
							state1=ens;
							break;
						}
					}
					else {
						if(ens.getTitle().compareTo(q1)==0) {
							state1=ens;
							break;
						}
					}
				}
				
				for (eNFAState ens : eNFA) {
					if (q2.contains("[")) {
						if(ens.getTitle().compareTo(q22)==0) {
							state2=ens;
							break;
						}
					}
					else {
						if(ens.getTitle().compareTo(q2)==0) {
							state2=ens;
							break;
						}
					}
				}
				state1.addTransition(symb, state2);
				model.addRow(newRow);
			}
			
			else if (convertFrom=="NFA") {
				Vector <String> newRow = new Vector <String>();
				String q1 = inputStateField.getText();
				newRow.add(q1);
//				System.out.println("Q1: "+q1);
				char symb = symbolField.getText().charAt(0);
				newRow.add(""+symb);
				String q2 = outputStateField.getText();
//				System.out.println("Q2: "+q2);
				newRow.add(q2);
				String q11 = q1.substring(1,q1.length()-1);
				String q22 = q2.substring(1, q2.length()-1);
				boolean exists1=false; boolean exists2=false;
				for (NFAState ns : NFA) {
					if(q1.contains("[")) {
						if (ns.getTitle().compareTo(q11)==0)
							exists1=true;
					}
					else {
						if (ns.getTitle().compareTo(q1)==0)
							exists1=true;
					}
				}
				
				if(!exists1) {
					NFAState newNFAState;
					if(q1.contains("[")) {
						newNFAState = new NFAState(q11);
						newNFAState.setType(Tip.FINAL);
					}else
					newNFAState=new NFAState(q1);
//					newNFAState.checkType();
//					System.out.println("Added State: "+newNFAState.getTitle());
					NFA.add(newNFAState); 
				}
				
				for (NFAState ns: NFA) {
					if(q2.contains("[")) {
						if(ns.getTitle().compareTo(q22)==0)
							exists2=true;  
					}
					else {
						if(ns.getTitle().compareTo(q2)==0)
							exists2=true; 
					}
				}
				
				if(!exists2) {
					NFAState newNFAState2;
					if(q2.contains("[")) {
						newNFAState2=new NFAState(q22);
						newNFAState2.setType(Tip.FINAL);
					}else
					newNFAState2=new NFAState(q2);
//					newNFAState2.checkType();
//					System.out.println("Added State: "+newNFAState2.getTitle());
					NFA.add(newNFAState2); 
				}
				
				NFAState state1 = null,state2 = null;
				for (NFAState ns : NFA) {
					if(q1.contains("[")) {
						if(ns.getTitle().compareTo(q11)==0) {
							state1=ns;
							break;
						}
					}
					else {
						if(ns.getTitle().compareTo(q1)==0) {
							state1=ns;
							break;
						}
					}
				}
				for (NFAState ns : NFA) {
					if(q2.contains("[")) {
						if(ns.getTitle().compareTo(q22)==0) {
							state2=ns;
							break;
						}
					}
					else {
						if(ns.getTitle().compareTo(q2)==0) {
							state2=ns;
							break;
						}
					}
				}
				state1.addTransition(symb, state2);
				model.addRow(newRow);
			}
			
			else if (convertFrom=="DFA") {
				Vector <String> newRow = new Vector<String>();
				String q1 = inputStateField.getText();
//				System.out.println("Q1: "+q1);
				newRow.add(q1);
				char symb = symbolField.getText().charAt(0);
				newRow.add(""+symb);
				String q2 = outputStateField.getText();
				newRow.add(q2);
//				System.out.println("Q2: "+q2);
				String q11 = q1.substring(1,q1.length()-1);
				String q22 = q2.substring(1, q2.length()-1);
				boolean exists1=false; boolean exists2=false;
				for (DFAState ds : DFA) {
					if (q1.contains("[")) { 
						if (ds.getTitle2().compareTo(q11)==0)
							exists1=true;
					}
					else {
						if (ds.getTitle2().compareTo(q1)==0)
							exists1=true;
					}
				}
				
				if(!exists1) {
					DFAState newDFAState;
					if(q1.contains("[")) {
						newDFAState=new DFAState(q11);
						newDFAState.setType(Tip.FINAL);
					}else
					newDFAState=new DFAState(q1);
//					newDFAState.checkType();
//					System.out.println("Added State: "+newDFAState.getTitle2());
					DFA.add(newDFAState); 
				}
				
				for (DFAState ds: DFA) {
					if(q2.contains("[")) {
						if(ds.getTitle2().compareTo(q22)==0)
							exists2=true;  
					}
					else {
						if(ds.getTitle2().compareTo(q2)==0)
							exists2=true; 
					}
				}
				
				if(!exists2) {
					DFAState newDFAState2;
					if(q2.contains("[")) {
						newDFAState2=new DFAState(q22);
						newDFAState2.setType(Tip.FINAL);
					}else
					newDFAState2=new DFAState(q2);
//					newDFAState2.checkType();
//					System.out.println("Added State: "+newDFAState2.getTitle2());
					DFA.add(newDFAState2); 
				}
				
				DFAState state1 = null,state2 = null;
				for (DFAState ds : DFA) {
					if(q1.contains("[")) {
						if(ds.getTitle2().compareTo(q11)==0) {
							state1=ds;
							break;
						}
					}
					else {
						if(ds.getTitle2().compareTo(q1)==0) {
							state1=ds;
							break;
						}
					}
				}
				for (DFAState ds : DFA) {
					if(q2.contains("[")) {
						if(ds.getTitle2().compareTo(q22)==0) {
							state2=ds;
							break;
						}
					}
					else {
						if(ds.getTitle2().compareTo(q2)==0) {
							state2=ds;
							break;
						}
					}
				}
				state1.addTransition(symb, state2);
				model.addRow(newRow);
			}
			
			else {
				JOptionPane.showMessageDialog(this, "NO Input Form WAS SELECTED!");
			}
			
		});
		
		northPanel1.setLayout(new GridLayout(1,11));
		northPanel1.add(alphabetLabel);
		northPanel1.add(alphabetField);
		northPanel1.add(addSymbolButton);
		northPanel1.add(numberOfStatesLabel);
		northPanel1.add(numberOfStatesField);
		northPanel1.add(addNumberOfStates);
		northPanel1.add(addStateLabel);
		northPanel1.add(inputStateField);
		northPanel1.add(symbolField);
		northPanel1.add(outputStateField);
		northPanel1.add(addTransitionButton);
		
		northPanel.add(northPanel1);
	}
	
	public void createNorthPanel2() {
		northPanel2 = new JPanel();
		convertFromLabel=new JLabel("Convert From: ");
		String [] convertFromStrings = {"eNFA","NFA","DFA"};
		String [] convertToStrings = {"NFA","DFA","DFA-Minimal"};
 		convertFromBox=new JComboBox<String>(convertFromStrings);
		toLabel = new JLabel("To: ");
		convertToBox = new JComboBox<String>(convertToStrings);
		
		addConversionButton = new JButton("Save");
		addConversionButton.addActionListener(e->{
			convertFrom=(String) convertFromBox.getSelectedItem();
			convertTo=(String)convertToBox.getSelectedItem();
		});
		
		convertButton = new JButton("Convert");
	
		convertButton.addActionListener(e->{
			//CONVERSIONS
			DefaultTableModel model = (DefaultTableModel) outputAutomataTable.getModel();
		
			if (convertFrom=="eNFA") {
				if(convertTo=="NFA") {
					ArrayList<NFAState> nfaConv = TOC.convertToNFA(eNFA, alphabet);
					for (NFAState nfas : nfaConv) {
						for (int i=1; i<nfas.getSymbols().size();i++) {
							Vector <String> newRow = new Vector<String>();
							newRow.add(nfas.toString());
							newRow.add(""+nfas.getSymbol(i));
							newRow.add(nfas.getAdjacents(i).toString());
							model.addRow(newRow);
						}
					}
				}
				else if (convertTo=="DFA") {
					ArrayList<DFAState> dfaConv = TOC.convertToDFA(TOC.convertToNFA(eNFA,alphabet), alphabet);
					for (DFAState dfas : dfaConv) {
						for (int i=1; i<dfas.getSymbols().size();i++) {
							Vector <String> newRow = new Vector <String> ();
							newRow.add(""+dfas);
							newRow.add(""+dfas.getSymbol(i));
							newRow.add(""+dfas.getAdjacent(i));
							model.addRow(newRow);
						}
					}
				}
				else if (convertTo=="DFA-Minimal") {
					ArrayList <DFAState> dfaConv = TOC.convertToDFA(TOC.convertToNFA(eNFA,alphabet), alphabet);
					ArrayList <DFAState> inDFA = new ArrayList<DFAState>();
					for (DFAState d : dfaConv)
						inDFA.add(d);
					
					ArrayList <DFAMinimalState> dfaMinConv = TOC.minimizeDFA(inDFA, alphabet);
					for (DFAMinimalState dms : dfaMinConv) {
						for (int i=0; i<dms.getSymbols().size();i++) {
							Vector <String> newRow = new Vector <String>();
							newRow.add(""+dms);
							newRow.add(""+dms.getSymbol(i));
							newRow.add(""+dms.getAdjacent(i));
							model.addRow(newRow);
						}
					}
				}
			}
			else if (convertFrom=="NFA") {
					if (convertTo=="DFA") {
						ArrayList<DFAState> dfaConv = TOC.convertToDFA(NFA, alphabet);
						for (DFAState dfas : dfaConv) {
							for (int i=1; i<dfas.getSymbols().size();i++) {
								Vector <String> newRow = new Vector <String> ();
								newRow.add(dfas.toString());
								newRow.add(""+dfas.getSymbol(i));
								newRow.add(""+dfas.getAdjacent(i));
								model.addRow(newRow);
							}
						}
					}
					else if (convertTo=="DFA-Minimal") {
						ArrayList <DFAState> dfaConv = TOC.convertToDFA(NFA, alphabet);
						ArrayList <DFAState> inDFA = new ArrayList<DFAState>();
						for (DFAState d : dfaConv)
							inDFA.add(d);
						
						ArrayList <DFAMinimalState> dfaMinConv = TOC.minimizeDFA(inDFA, alphabet);
						for (DFAMinimalState dms : dfaMinConv) {
							for (int i=0; i<dms.getSymbols().size();i++) {
								Vector <String> newRow = new Vector <String>();
								newRow.add(""+dms);
								newRow.add(""+dms.getSymbol(i));
								newRow.add(""+dms.getAdjacent(i));
								model.addRow(newRow);
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(this, "Error on Selection");
					}
			}
			else if (convertFrom=="DFA") {
					if(convertTo=="DFA-Minimal") {
						ArrayList <DFAState> dfaConv = DFA;
						ArrayList <DFAState> inDFA = new ArrayList<DFAState>();
						for (DFAState d : dfaConv)
							inDFA.add(d);
						
						ArrayList <DFAMinimalState> dfaMinConv = TOC.minimizeDFA(inDFA, alphabet);
						for (DFAMinimalState dms : dfaMinConv) {
							for (int i=0; i<dms.getSymbols().size();i++) {
								Vector <String> newRow = new Vector <String>();
								newRow.add(""+dms);
								newRow.add(""+dms.getSymbol(i));
								newRow.add(""+dms.getAdjacent(i));
								model.addRow(newRow);
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(this, "Error on Selection");
					}
			}
			else {
				JOptionPane.showMessageDialog(this, "Error on Selection");
			}
		});
		
		northPanel2.setLayout(new GridLayout(1,6));
		northPanel2.add(convertFromLabel);
		northPanel2.add(convertFromBox);
		northPanel2.add(toLabel);
		northPanel2.add(convertToBox);
		northPanel2.add(addConversionButton);
		northPanel2.add(convertButton);
		
		northPanel.add(northPanel2);
	}
	
	public void createCenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1,2));
		
		Vector columnNames1 = new Vector<String>();
		columnNames1.add("Input State");
		columnNames1.add("Symbol");
		columnNames1.add("Output State");
		
		Vector data = new Vector<Object>();
		Vector data2 = new Vector<Object>();
		inputAutomataTable=new JTable(data,columnNames1);
		outputAutomataTable=new JTable(data2,columnNames1);
		
		JScrollPane pane1 = new JScrollPane(inputAutomataTable);
		inputAutomataTable.setFillsViewportHeight(true);
		JScrollPane pane2 = new JScrollPane(outputAutomataTable);
		outputAutomataTable.setFillsViewportHeight(true);
		
		centerPanel.add(pane1);
		centerPanel.add(pane2);
		add(centerPanel,BorderLayout.CENTER);
	}
}
