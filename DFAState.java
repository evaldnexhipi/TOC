import java.util.ArrayList;
import java.util.HashSet;
public class DFAState {
	private HashSet<NFAState> title = new HashSet <NFAState>();
	private Tip type;
	private ArrayList <Character> symbols = new ArrayList<Character>();
	private ArrayList <DFAState> adjacents = new ArrayList <DFAState>();
	private String title2;
	
	public DFAState () {
		this.type=Tip.NONE;
		symbols.add('Ɛ');
		adjacents.add(this);
	}
	
	public DFAState(String stateTitle) {
		this.type=Tip.NONE;
		symbols.add('Ɛ');
		adjacents.add(this);
		title2=stateTitle;
	}
	
	public ArrayList <Character> getSymbols(){
		return symbols;
	}
	
	public ArrayList <DFAState> getAdjacents(){
		return adjacents;
	}
	
	public char getSymbol(int i) {
		return symbols.get(i);
	}
	
	public DFAState getAdjacent(int i) {
		return adjacents.get(i);
	}
	
	public void addTitle (NFAState s) {
		title.add(s);
		if (s.getType()==Tip.FINAL) {
			this.type=Tip.FINAL;
		}
	}
	
	public void checkType() {
		if(title2.contains("[")) {
			this.type=Tip.FINAL;
//			title2=title2.substring(1,title2.length()-1);
		}
	}
	
	public Tip getType () {
		return type;
	}
	
	public void setType (Tip t) {
		type=t;
	}
	
	public HashSet <NFAState> getTitle (){
		return title;
	}
	
	public String getTitle2() {
		return title2;
	}
	
	public void setTitle2(String title2) {
		this.title2=title2;
	}
	
	public void addTransition (char c, DFAState nextState) {
		symbols.add(c);
		adjacents.add(nextState);
	}
	
	
	public boolean equals (Object anObject) {
		DFAState aState = (DFAState)anObject;
		if (aState.getTitle().size()!=this.title.size())
			return false;
		else {
			int count = 0;
			for (NFAState nfas : this.title) {
				if(aState.getTitle().contains(nfas)) {
					count++;
				}
			}
			return (count==this.title.size());
		}
	}
	
	public void bridh () {
		String title = toString();
		for (int i=1; i<symbols.size();i++) {
			System.out.println(title+"-> "+symbols.get(i)+" -> "+adjacents.get(i));
		}
	}
	
	public DFAState whereGoesOn (char c) {
		for (int i=0; i<this.symbols.size(); i++) {
			if(this.symbols.get(i)==c)
				return adjacents.get(i);
		}
		
		return null;
	}
	
	public String toString () {
		if (this.title.size()!=0) {	
			//String s = "";
			String s = "{";
			for (NFAState nfas : this.title) {
				s+=nfas.getTitle()+",";
			}
			s=s.substring(0, s.length()-1);
			s+="}";
			
			if(this.type==Tip.FINAL) {
				String str = "[";
				str+=s;
				str+="]";
				return str;
			}		
		return s;
		}
		else {
			String s = "";
			if(this.type==Tip.FINAL) {
				s+="["+this.title2+"]";
			}
			else {
				s+=this.title2;
			}
			return s;
		}
	}
	
	
}
