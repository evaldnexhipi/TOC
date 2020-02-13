import java.util.ArrayList;
import java.util.HashSet;
public class NFAState {
	private String title;
	private Tip type;
	private ArrayList <Character> symbols = new ArrayList<Character>();
	private ArrayList <NFAState> adjacents = new ArrayList <NFAState>();
	
	public NFAState (String title, Tip type) {
		this.title=title;
		this.type=type;
		symbols.add('Ɛ');
		adjacents.add(this);
	}
	
	public NFAState(String title) {
		this.title=title;
		this.type=Tip.NONE;
		symbols.add('Ɛ');
		adjacents.add(this);
	}
	
	public void checkType() {
		if(title.contains("[")) {
			this.type=Tip.FINAL;
		}
	}
	
	public ArrayList<Character> getSymbols (){
		return symbols;
	}
	
	public ArrayList<NFAState> getAdjacents(){
		return adjacents;
	}
	
	public char getSymbol(int i){
		return symbols.get(i);
	}
	
	public NFAState getAdjacents(int i){
		return adjacents.get(i);
	}
	
	public void addTransition (char c, NFAState nextState) {
		symbols.add(c);
		adjacents.add(nextState);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Tip getType () {
		return type;
	}
	
	public HashSet <NFAState> goesTo (char symbol){
		HashSet <NFAState> outputStates = new HashSet<NFAState>();
		for (int i=0; i<symbols.size();i++) {
			if(symbols.get(i).equals(symbol)) {
				outputStates.add(adjacents.get(i));
			}
		}
		return outputStates;
	}
	
	public String toString() {
		String s = this.title;
		if (this.type==Tip.FINAL) {
			s = "["+s+"]";
		}
		return s;
	}
	
	public void setType(Tip tip) {
		type=tip;
	}
}
