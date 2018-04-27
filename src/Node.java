import java.util.Stack;
import java.util.ArrayList;
import java.lang.reflect.Array;
import java.util.EmptyStackException;

public class Node{
    private char symbol;
    private Node left;
    private Node right;

    public Node(char symbol){
        this.symbol = symbol;
    }
    public Node(char symbol, Node left, Node right){
        this.symbol = symbol;
        this.left = left;
        this.right = right;
    }

    public char getSymbol(){
        return symbol;
    }
    public Node getLeft(){
        return left;
    }
    public void setLeft(Node left){
        this.left = left;
    }
    public Node getRight(){
        return right;
    }
    public void setRight(Node right){
        this.right = right;
    }

	public void createPostNFA(Node node, Stack<NFA> nfaStack, char[] alph) throws Exception{
		if(node == null){
			return;
		}
		createPostNFA(node.getLeft(), nfaStack, alph);
		createPostNFA(node.getRight(), nfaStack, alph);
		char symbol=node.getSymbol();
		boolean inAlphabet=false;
		for(char letter:alph){
			if(symbol == letter){
				NFA nfa=createOneSymbolNFA(alph, symbol);
				nfaStack.push(nfa);
				inAlphabet=true;
				break;
			}
			else if(symbol=='e'){
				NFA nfa=createEpsilonNFA(alph);
				nfaStack.push(nfa);
				inAlphabet=true;
				break;
			}
			else if(symbol == 'N'){
				NFA nfa=createEmptyNFA(alph);
				nfaStack.push(nfa);
				inAlphabet=true;
				break;
			}
		}
		if(!inAlphabet){
			if(symbol=='*'){
				NFA nfaStarToBe=nfaStack.pop();
				nfaStarToBe.star();
				nfaStack.push(nfaStarToBe);
			}
			else if(symbol=='$'){
				NFA right=nfaStack.pop();
				NFA left=nfaStack.pop();
				left.concat(right);
				nfaStack.push(left);
			}
			else if(symbol=='|'){
				NFA right=nfaStack.pop();
				NFA left=nfaStack.pop();
				left.union(right);
				nfaStack.push(left);
			}
			else if(symbol == '('){
				throw new Exception("Invalid expression");
			}
		}
	}

	private NFA createEmptyNFA(char[] alphabet){
		NFA nfa=new NFA();
		nfa.setNumStates(1);
		nfa.setAlph(alphabet);
		ArrayList<DFATrans> transFuncs=new ArrayList<>();
		nfa.setTransFunctions(transFuncs);
		nfa.setStartState(1);
		ArrayList<Integer> acceptStates=new ArrayList<>();
		nfa.setAcceptStates(acceptStates);
		return nfa;
	}

	private NFA createEpsilonNFA(char[] alphabet){
		NFA nfa=new NFA();
		nfa.setNumStates(1);
		nfa.setAlph(alphabet);
		ArrayList<DFATrans> transFunctions=new ArrayList<>();
		nfa.setTransFunctions(transFunctions);
		nfa.setStartState(1);
		ArrayList<Integer> acceptStates=new ArrayList<>();
		acceptStates.add(1);
		nfa.setAcceptStates(acceptStates);
		return nfa;
	}

	private NFA createOneSymbolNFA(char[] alphabet, char symbol){
		NFA nfa=new NFA();
		nfa.setNumStates(2);
		nfa.setAlph(alphabet);
		DFATrans tf = new DFATrans(1,symbol,2);
		ArrayList<DFATrans> transitionFunctions=new ArrayList<>();
		transitionFunctions.add(tf);
		nfa.setTransFunctions(transitionFunctions);
		nfa.setStartState(1);
		ArrayList<Integer> acceptStates=new ArrayList<>();
		acceptStates.add(2);
		nfa.setAcceptStates(acceptStates);
		return nfa;
	}
}
