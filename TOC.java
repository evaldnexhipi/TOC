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
		DFAState DFA = new DFAState ();
		DFA.addTitle(NFA.get(0));
		//DFA.setType(Tip.START);
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
				
//				d.addTransition(Alphabet.get(i), newDFAState);
			
			//krahaso gjendjen e re newState nqs ndodhet ne DFA apo jo
			boolean ndodhet = false;
			DFAState existingState=null; //added
			for (DFAState dfs : DFASet) {
				if (dfs.equals(newDFAState)) {
					ndodhet=true;
					existingState=dfs;
				}
			}
			if(!ndodhet) {
				d.addTransition(Alphabet.get(i), newDFAState); //added
				queue.add(newDFAState);
				DFASet.add(newDFAState);			
			}
			else {
				d.addTransition(Alphabet.get(i),existingState); //added
			}
			
			}
			//per gjendjen d- koke e rradhes 
			  queue.remove();
			  iterator--;
		}
		return DFASet;
	}
	
	public static ArrayList <NFAState> convertToNFA (ArrayList <eNFAState> eNFA, ArrayList <Character> Alphabet){
		ArrayList <NFAState> NFA = new ArrayList <NFAState>();
		//percaktohen gjendjet finale
		//ne momentin kur krijohet nje gjendje e re NFAState, nqs eClosure i saj shkon tek gjendja finale, athr Type.FINAL
		HashSet <eNFAState> eNFAFinalStates = new HashSet<eNFAState>();
		for (eNFAState enfa : eNFA) {
			if (enfa.getType()==Tip.FINAL) {
				eNFAFinalStates.add(enfa);
			}
		}
		
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
		//U ndertuan gjendjet (edhe ato finale) te NFA-se tani te filloje algoritmi per konvertimin
		for (int i=0; i<NFA.size();i++) {
			//eNFA.size == NFA.size
			
			for (int j=0; j<Alphabet.size();j++) {
				HashSet <eNFAState>  eClosure1 = eNFA.get(i).eClosure();
				HashSet <eNFAState> goesToSet = new HashSet <eNFAState>();
				for (eNFAState enfac : eClosure1) {
					for (eNFAState enfa : enfac.goesTo(Alphabet.get(j))) {
						goesToSet.add(enfa);
					}
				}
				HashSet <eNFAState> eClosure2 = new HashSet <eNFAState>();
				for (eNFAState enfac : goesToSet) {
					for (eNFAState enfa : enfac.eClosure()) {
						eClosure2.add(enfa);
					}
				}
				//u be bredhja
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
	
	public static ArrayList <DFAMinimalState> minimizeDFA (ArrayList <DFAState> DFA,ArrayList <Character> Alphabet){
		ArrayList <DFAMinimalState> minimalDFA = new ArrayList<DFAMinimalState>();
		
		int numberOfStates = DFA.size();
		/*
		for (DFAState dfs : DFA) {
			for (int i=1; i<dfs.getSymbols().size(); i++) {
				System.out.println(dfs+" -> "+dfs.getSymbol(i)+" -> "+dfs.whereGoesOn(Alphabet.get(i-1)));
			}
			System.out.print("\n");
		}
		*/
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
			String title="";
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
//							System.out.println("(d1,d2): "+"("+d1+","+d2+")");
//							System.out.println("(q1,q2): "+"("+q1+","+q2+")");
							
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
//				System.out.println(DFA.get(i));
				return i;
			}
		}
		return -1;
	}
	
	
	
	public static void main (String [] args) {
		
		new ConversionFrame();
		/*ArrayList <Character> alpha = new ArrayList<Character>();
		
		
		alpha.add('0');
		alpha.add('1');
		
		ArrayList <NFAState> NFA = new ArrayList<NFAState>();
		NFAState q0 = new NFAState("q0", Type.START);
		NFAState q1 = new NFAState ("q1",Type.NONE);
		NFAState q2 = new NFAState ("q2",Type.FINAL);
				
		q0.addTransition('0', q0);
		q0.addTransition('1', q0);
		q0.addTransition('0',q1);
		q1.addTransition('1', q2);
		
		NFA.add(q0); NFA.add(q1); NFA.add(q2);
		HashSet <DFAState> DFA = convertToDFA(NFA,alpha);
		System.out.println("Converted");
		for (DFAState d : DFA) {
			d.bridh();
		}
		*/
		
		/*
		ArrayList <Character> alpha = new ArrayList<Character>();
		
		alpha.add('0');
		alpha.add('1');
		alpha.add('2');
		//alpha.add('2');

		eNFAState q1 = new eNFAState("q0", Tip.START);
		eNFAState q2 = new eNFAState("q1",Tip.NONE);
		eNFAState q3 = new eNFAState("q2",Tip.FINAL);
		
		q1.addTransition('0',q1);
		q1.addTransition('Ɛ',q2);
		q2.addTransition('Ɛ',q3);
		q2.addTransition('1',q2);
		q3.addTransition('2',q3);
		ArrayList <eNFAState> eNFA = new ArrayList<eNFAState>();
		eNFA.add(q1); eNFA.add(q2); eNFA.add(q3); 
		
		
		System.out.println("Successfully converted from eNFA to NFA");
		for (NFAState nfa : convertToNFA(eNFA, alpha)) {
			nfa.bridh();
		}
		System.out.println("\n Successfully converted from NFA to DFA");
		{
			for (DFAState dfa : convertToDFA(convertToNFA(eNFA,alpha),alpha)) {
				dfa.bridh();
			}
		}
			
		*/
		
		/*
		ArrayList <Character> alpha = new ArrayList<Character>();
		alpha.add('0');
		alpha.add('1');
		alpha.add('2');
		
		DFAState A = new DFAState("A");
		A.setType(Tip.FINAL);
		DFAState B = new DFAState("B");
		B.setType(Tip.FINAL);
		DFAState C = new DFAState("C");
		C.setType(Tip.FINAL);
		DFAState D = new DFAState("D");
		D.setType(Tip.FINAL);
		DFAState E = new DFAState("E");
		
		A.addTransition('0',B);
		A.addTransition('1',D);
		A.addTransition('2',C);
		
		B.addTransition('0',B);
		B.addTransition('1',D);
		B.addTransition('2',C);
		
		C.addTransition('0',E);
		C.addTransition('1',E);
		C.addTransition('2',C);
		
		D.addTransition('0', E);
		D.addTransition('1', D);
		D.addTransition('2', C);
		
		E.addTransition('0', E);
		E.addTransition('1', E);
		E.addTransition('2', E);
	
		
		ArrayList <DFAState> dfaEx = new ArrayList<DFAState>();
		dfaEx.add(A); dfaEx.add(B); dfaEx.add(C); dfaEx.add(D); dfaEx.add(E); 
		
		
		for (DFAState dfa :dfaEx) {
			dfa.bridh();
		}
		System.out.println("");
		//PRINT STATES
		
		for (DFAMinimalState ds : minimizeDFA(dfaEx,alpha)) {
			ds.bridh();
		}
		*/
	}
}
