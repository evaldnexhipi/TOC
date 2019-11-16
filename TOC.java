import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
//Ɛ
public class TOC {
	
	public static HashSet <DFAState> convertToDFA (ArrayList<NFAState> NFA, ArrayList <Character> Alphabet){
		HashSet <DFAState> DFASet = new HashSet<DFAState>();
		//shtojme q0 e NFA tek Q0 i DFA
		DFAState DFA = new DFAState ();
		DFA.addTitle(NFA.get(0));
		DFA.setType(Type.START);
		Queue <DFAState> queue = new LinkedList<DFAState>();
		queue.add(DFA);
		DFASet.add(DFA);
		//bredhin queue-in
		for (int iterator = 0 ; iterator < queue.size(); iterator++) {
			//per secilen nga germat e alphabetit shohim 
			DFAState d = queue.peek();
			for (int i=0 ; i<Alphabet.size();i++) {
				//se ku mund te shkojme nga secila prej gjendjeve qe perben title-in 
				DFAState newDFAState = new DFAState();
				for (NFAState nf : d.getTitle()) {
					for (NFAState wg : nf.goesTo(Alphabet.get(i))) {
						newDFAState.addTitle(wg);
					}
				}
				//System.out.println(newDFAState);
				d.addTransition(Alphabet.get(i), newDFAState);
			
			//krahaso gjendjen e re newState nqs ndodhet ne DFA apo jo
			boolean ndodhet = false;
			for (DFAState dfs : DFASet) {
				if (dfs.equals(newDFAState))
					ndodhet=true;
			}
			if(!ndodhet) {
				queue.add(newDFAState);
				DFASet.add(newDFAState);			
			}
			
			}
			//per gjendjen d- koke e rradhes 
			  queue.remove();
			  iterator--;
		}
		return DFASet;
	}
	
	public static void main (String [] args) {
		//new ConversionFrame();
		ArrayList <Character> alpha = new ArrayList<Character>();
		alpha.add('0');
		alpha.add('1');
		alpha.add('2');
		
		ArrayList <NFAState> NFA = new ArrayList<NFAState>();
		NFAState q0 = new NFAState("q0", Type.START);
		NFAState q1 = new NFAState ("q1",Type.NONE);
		NFAState q2 = new NFAState ("q2",Type.FINAL);
		
		q0.addTransition('0', q0);
		q0.addTransition('1', q0);
		q0.addTransition('2',q0);
		q0.addTransition('0', q1);
		q1.addTransition('1', q2);
		
		NFA.add(q0); NFA.add(q1); NFA.add(q2);
		HashSet <DFAState> DFA = convertToDFA(NFA,alpha);
		System.out.println("Converted");
		for (DFAState d : DFA) {
			d.bridh();
		}
	}
}
