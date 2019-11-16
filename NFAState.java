import java.util.ArrayList;
import java.util.HashSet;
public class NFAState {
	private String title;
	private Type type;
	private ArrayList <Character> symbols = new ArrayList<Character>();
	private ArrayList <NFAState> adjacents = new ArrayList <NFAState>();
	
	public NFAState (String title, Type type) {
		this.title=title;
		this.type=type;
		symbols.add('Ɛ');
		adjacents.add(this);
	}
	
	public void addTransition (char c, NFAState nextState) {
		symbols.add(c);
		adjacents.add(nextState);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Type getType () {
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
		String s = "";
		for (char c : symbols) {
			s+=c+" ";
		}
		String ad = "";
		for (NFAState st : this.adjacents) {
			ad+=st.getTitle()+" ";
		}
		return title+" simbolet "+s+" gjendjet fqinje "+ad;
	}
	
	public boolean equals(Object anObject) {
		NFAState aState = (NFAState)anObject;
		return false;
	}
}
