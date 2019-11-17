import java.util.ArrayList;
import java.util.HashSet;
public class DFAState {
	private HashSet<NFAState> title = new HashSet <NFAState>();
	private Type type;
	private ArrayList <Character> symbols = new ArrayList<Character>();
	private ArrayList <DFAState> adjacents = new ArrayList <DFAState>();
	
	public DFAState () {
		this.type=Type.NONE;
		symbols.add('Ɛ');
		adjacents.add(this);
	}
	
	public void addTitle (NFAState s) {
		title.add(s);
		if (s.getType()==Type.FINAL) {
			this.type=Type.FINAL;
		}
	}
	
	public Type getType () {
		return type;
	}
	
	public void setType (Type t) {
		type=t;
	}
	
	public HashSet <NFAState> getTitle (){
		return title;
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
	
	public String toString () {
		String s = "{";
		for (NFAState nfas : this.title) {
			s+=nfas.getTitle()+",";
		}
		s=s.substring(0, s.length()-1);
		s+="}";
		
		if (s.indexOf("}")==0) {
			s="{ERROR}";
		}
		
		if(this.type==Type.FINAL) {
			String str = "{";
			str+=s;
			str+="}";
			return str;
		}		
		
		return s;
	}
}
