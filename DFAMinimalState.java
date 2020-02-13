import java.util.ArrayList;

public class DFAMinimalState {
	private ArrayList<DFAState> title;
	private Tip type;
	private ArrayList <Character> symbols;
	private ArrayList <DFAMinimalState> adjacents;
	
	public DFAMinimalState() {
		title=new ArrayList<DFAState>();
		symbols=new ArrayList<Character>();
		adjacents=new ArrayList<DFAMinimalState>();
		type=Tip.NONE;
	}
	
	public char getSymbol(int i) {
		return symbols.get(i);
	}
	
	public DFAMinimalState getAdjacent(int i) {
		return adjacents.get(i);
	}
	
	public void setType (Tip t) {
		type=t;
	}
	
	public boolean containsDFAState(DFAState dfas) {
		for (DFAState s : title) {
			if(s==dfas)
				return true;
		}
		return false;
	}
	
	public ArrayList <DFAState> getTitle(){
		return this.title;
	}
	
	public void addTitle(DFAState ds) {
		title.add(ds);
	}
	
	public ArrayList <Character> getSymbols(){
		return this.symbols;
	}
	
	public void addTransition (char c, DFAMinimalState nextState) {
		symbols.add(c);
		adjacents.add(nextState);
	}
	
	public String toString() {
		String s = "";
		for (DFAState dfas : title) {
			if(dfas.toString().contains("["))
				s+=dfas.toString().substring(1,dfas.toString().length()-1)+",";
			else
				s+=dfas+",";
		}
		s=s.substring(0,s.length()-1);
		
		if(this.type==Tip.FINAL)
			s="["+s+"]";
		
		return s;
	}
}
