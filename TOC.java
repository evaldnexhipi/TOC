import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
//Ɛ
public class TOC {

	public static ArrayList <DFAState> convertToDFA (ArrayList<NFAState> NFA, ArrayList <Character> Alphabet){
		ArrayList <DFAState> DFASet = new ArrayList<DFAState>();
		//shtojme q0 e NFA tek Q0 i DFA
		DFAState DFAStartState = new DFAState ();
		DFAStartState.addTitle(NFA.get(0));
		//DFA.setType(Tip.START);
		Queue <DFAState> queue = new LinkedList<DFAState>();
		queue.add(DFAStartState);
		DFASet.add(DFAStartState);
		//bredhin queue-in
		for (int iterator = 0 ; iterator < queue.size(); iterator++) {
			//per secilen nga germat e alphabetit shohim 
			DFAState d = queue.peek();
			for (int i=0 ; i<Alphabet.size();i++) {
				//se ku mund te shkojme nga secila prej gjendjeve qe perben title-in 
				DFAState newDFAState = new DFAState();
				//HashSet <NFAState> title garanton mosperseritjen
				for (NFAState nf : d.getTitle()) {
					for (NFAState wg : nf.goesTo(Alphabet.get(i))) {
						newDFAState.addTitle(wg);
					}
				}
			
			//krahaso gjendjen e re newState nqs ndodhet ne DFA apo jo
			boolean ndodhet = false;
			DFAState existingState=null; //added
			for (DFAState dfs : DFASet) {
				if (dfs.equals(newDFAState)) {
					ndodhet=true;
					existingState=dfs;
				}
			}
			
			//Percaktimi i tranzicioneve
			if(!ndodhet) {
				d.addTransition(Alphabet.get(i), newDFAState); //added
				queue.add(newDFAState);
				DFASet.add(newDFAState);			
			}
			else {
				d.addTransition(Alphabet.get(i),existingState); //added
			}
			
			}
			//heqim nga rradha elementin i cili perfundoi se trajtuari
			  queue.remove();
			  iterator--;
		}
		return DFASet;
	}
	
	public static ArrayList <NFAState> convertToNFA (ArrayList <eNFAState> eNFA, ArrayList <Character> Alphabet){
		ArrayList <NFAState> NFA = new ArrayList <NFAState>();
		//percaktohen gjendjet finale
		//ne momentin kur krijohet nje gjendje e re NFAState, nqs eClosure i saj shkon tek gjendja finale, athr Type.FINAL
		
		//percaktojme gjendjet finale te eNFA
		HashSet <eNFAState> eNFAFinalStates = new HashSet<eNFAState>();
		for (eNFAState enfa : eNFA) {
			if (enfa.getType()==Tip.FINAL) {
				eNFAFinalStates.add(enfa);
			}
		}
		
		//percaktojme eClosure per secilen gjendjen te re te NFA-se nese mund te shkojme te ndonje gjendje finale
		for (eNFAState enfa : eNFA) {
			Tip t = enfa.getType();
			for (eNFAState finals : eNFAFinalStates) {
				if (enfa.eClosure().contains(finals)) {
					t=Tip.FINAL;
				}
			}
			NFAState nfaState = new NFAState(enfa.getTitle(),t);
			NFA.add(nfaState); //u shtuan te gjitha
		}
		//U ndertuan gjendjet (edhe ato finale) te NFA-se tani te filloje algoritmi per percaktimin e kalimeve
		for (int i=0; i<NFA.size();i++) {
			//eNFA.size == NFA.size
			
			//Kalimi i zgjeruar me secilin prej simboleve te alfabetit (=>)
			for (int j=0; j<Alphabet.size();j++) {
				//Kalimi i zgjeruar me Ɛ
				HashSet <eNFAState>  eClosure1 = eNFA.get(i).eClosure();
				
				//Tranzicioni me simbolin e i-te te alfabetit
				HashSet <eNFAState> goesToSet = new HashSet <eNFAState>();
				for (eNFAState enfac : eClosure1) {
					for (eNFAState enfa : enfac.goesTo(Alphabet.get(j))) {
						goesToSet.add(enfa);
					}
				}
				//Kalim i zgjeruar me Ɛ
				HashSet <eNFAState> eClosure2 = new HashSet <eNFAState>();
				for (eNFAState enfac : goesToSet) {
					for (eNFAState enfa : enfac.eClosure()) {
						eClosure2.add(enfa);
					}
				}
				
				//Shtojme tranzicionet(kalimet) ne gjendjen korrente NFA
				NFAState currentNFAState = NFA.get(i);
				for (int indx=0; indx<NFA.size();indx++) {
					for (eNFAState enfa : eClosure2) {
						NFAState nextNFAState = NFA.get(indx);
						if (nextNFAState.getTitle()==enfa.getTitle()) {
							currentNFAState.addTransition(Alphabet.get(j), nextNFAState);
						}
 					}
				}
			}
		}
		
		return NFA;
	}
	
	public static ArrayList <DFAMinimalState> minimizeDFA (ArrayList <DFAState> inDFA,ArrayList <Character> Alphabet){
		ArrayList <DFAState> DFA = removeUnreachableStates(inDFA, Alphabet);
		ArrayList <DFAMinimalState> minimalDFA = new ArrayList<DFAMinimalState>();
		int numberOfStates = DFA.size();
		
		int [][] matrix = new int [numberOfStates][numberOfStates];
		matrix = initializeMatrix(matrix,numberOfStates, DFA);
		matrix = updateMatrix(matrix,numberOfStates,DFA, Alphabet);
		
		//ADD RESULTS FROM THE MATRIX
		ArrayList <HashSet> results = new ArrayList <HashSet>();
		for (int i=0; i<numberOfStates; i++) {
			for (int j=0; j<i; j++) {
				if (matrix[i][j]==0) {
					HashSet<DFAState> statesPair = new HashSet<DFAState>();
					statesPair.add(DFA.get(i));
					statesPair.add(DFA.get(j));
					results.add(statesPair);
				}
			}
		}
		
		//UNITE STATES
		for (int i=0; i<results.size();i++) {
			HashSet <DFAState> hash1 = results.get(i);
			for (int j=i+1; j<results.size();j++) {
				HashSet <DFAState> hash2 = results.get(j);
				boolean canBeUnited=false;
				for (DFAState ds : hash2) {
					if (hash1.contains(ds)) {
						canBeUnited=true;
						break;
					}
				}
				if(canBeUnited) {
					for (DFAState ds : hash2) {
						hash1.add(ds);
					}
					results.remove(j);
					j--;
				}
			}
		}
		
		//ADD INTO RESULTS STATES OF THE LEFT-OVER DFA STATES 
		for (DFAState ds : DFA) {
			boolean contains = false;
			for (HashSet<DFAState> hs : results) {
				for (DFAState ds2 : hs) {
					if (ds2==ds)
						contains=true;
				}
			}
			if (!contains) {
				HashSet<DFAState> leftOverStates = new HashSet<DFAState>();
				leftOverStates.add(ds);
				results.add(leftOverStates);
			}
		} 
		
		//ADD STATES FROM RESULTS TO MINIMALDFA
		for (HashSet <DFAState> hs : results) {
			DFAMinimalState newState = new DFAMinimalState ();
			for (DFAState ds : hs) {
				if(ds.getType()==Tip.START) {
					newState.setType(Tip.START);
				}
				if (ds.getType()==Tip.FINAL) {
					newState.setType(Tip.FINAL);
				}
				newState.addTitle(ds);
			}
			minimalDFA.add(newState);
		}
		
		//LINK STATES TO ONE ANOTHER
		for (DFAMinimalState dfms : minimalDFA) {
			DFAState getATitle = dfms.getTitle().get(0);
			for (char c : Alphabet) {
				DFAState destination = getATitle.whereGoesOn(c);
				for (DFAMinimalState dfms2 : minimalDFA) {
					if(dfms2.containsDFAState(destination)) {
						dfms.addTransition(c,dfms2);
						break;
					}
				}
			}
		}
		return minimalDFA;
	}
	
	public static ArrayList <DFAState> removeUnreachableStates(ArrayList <DFAState> inDFA,ArrayList <Character> Alphabet){
		HashSet <DFAState> reachableDFA = new HashSet<DFAState>();
		Queue <DFAState> queue = new LinkedList<DFAState>();
		queue.add(inDFA.get(0));
		for (int iterator=0; iterator<queue.size(); iterator++) {
			for (char c : Alphabet) {
				DFAState destinationState = queue.peek().whereGoesOn(c);
				if (!reachableDFA.contains(destinationState)) {
					queue.add(destinationState);
				}
			}
			reachableDFA.add(queue.peek());
			queue.remove();
			iterator--;
		}
		return new ArrayList<DFAState>(reachableDFA);
	}
	
	public static int[][] initializeMatrix (int [][] matrix,int size,ArrayList<DFAState> DFA) {
		for (int i=0; i<size; i++) {
			for (int j=0; j<=i; j++) {
				if (i==j) {
					matrix[i][j]=-1;
					matrix[j][i]=-1;
				}
				else if (DFA.get(i).getType()==Tip.FINAL && DFA.get(j).getType()!=Tip.FINAL) {
					matrix[i][j]=1;
					matrix[j][i]=1;
				}
				else if (DFA.get(j).getType()==Tip.FINAL && DFA.get(i).getType()!=Tip.FINAL) {
					matrix[i][j]=1;
					matrix[j][i]=1;
				}
				else {
					matrix[i][j]=0;
					matrix[j][i]=0;
				}
			}
		}
		return matrix;
	}
	
	public static int[][] updateMatrix (int [][]matrix,int size, ArrayList<DFAState>DFA, ArrayList <Character> Alphabet){
		int q1,q2;
		boolean done = false;
		boolean changed;
		while (!done) {
			changed=false;
			
			for (int i=0; i<size; i++) {
				for (int j=0; j<i; j++) {
					if(matrix[i][j] == 0 || matrix[j][i] == 0) {
						for (char c : Alphabet) {
							//changed - derisa te mos kemi modifikime ne matrice		
							q1 = getIndexOfState(DFA,DFA.get(i).whereGoesOn(c));
							q2 = getIndexOfState(DFA,DFA.get(j).whereGoesOn(c));

							if (matrix[q1][q2]==1 || matrix[q2][q1]==1) {
								matrix[i][j]=1;
								matrix[j][i]=1;
								changed=true;
								break;
							}
						}
					}
				}
			}
			if(!changed) 
				done=true;
		}
		return matrix;
	}
	
	public static int getIndexOfState (ArrayList<DFAState>DFA, DFAState ds) {
		for (int i=0; i<DFA.size();i++) {
			if(DFA.get(i)==ds){
				return i;
			}
		}
		return -1;
	}
	
	public static void main (String [] args) {
		new ConversionFrame();
	}
}
