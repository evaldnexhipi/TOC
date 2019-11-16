import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
public class eNFAState {
	private String title;
	private Type type;
	private ArrayList <Character> symbols = new ArrayList<Character>();
	private ArrayList <eNFAState> adjacents = new ArrayList <eNFAState>();
	
	public eNFAState (String title, Type type) {
		this.title=title;
		this.type=type;
		symbols.add('Ɛ');
		adjacents.add(this);
	}
	
	public void addTransition (char c, eNFAState nextState) {
		symbols.add(c);
		adjacents.add(nextState);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Type getType () {
		return type;
	}
	
	public HashSet <eNFAState> goesTo (char symbol){
		HashSet <eNFAState> outputStates = new HashSet<eNFAState>();
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
		for (eNFAState st : this.adjacents) {
			ad+=st.getTitle()+" ";
		}
		return title+" simbolet "+s+" gjendjet fqinje "+ad;
	}
	
	public boolean equals(Object anObject) {
		eNFAState aState = (eNFAState)anObject;
		return false;
	}
	
	public ArrayList <Character> getSymbols (){
		return this.symbols;
	}
	
	public ArrayList <eNFAState> getAdjacents(){
		return this.adjacents;
	}
	
	public HashSet <eNFAState> eClosure (){
		HashSet <eNFAState> eClosure = new HashSet<eNFAState>();
		Queue <eNFAState> queue = new LinkedList <eNFAState>();
		queue.add(this);
		eClosure.add(this);
		
		for (int itr=0; itr<queue.size();itr++) {
			eNFAState q = queue.peek();
			for (int i=0; i<q.getSymbols().size();i++) {
				if (q.getSymbols().get(i).equals('Ɛ')) {
					if (!eClosure.contains(q.getAdjacents().get(i))) {
						queue.add(q.adjacents.get(i));
						eClosure.add(q.getAdjacents().get(i));
					}
				}
			}
			queue.remove();
			itr--;
		}
		return eClosure;
	}
}
