import java.util.Stack;
import java.util.ArrayList;
import java.lang.reflect.Array;
import java.util.EmptyStackException;

public class Node {
    private char symbol;
    private Node left;
    private Node right;

    public Node(){}
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
    public void setSymbol(char symbol){
        this.symbol = symbol;
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
	public void printPostORder(Parse TreeNode node){
		//print tree in post order for testing
		if (node == null)
			return;
		//recursive left subtree
		printPostOrder(node.getLeftChild());

		//recursive right subtree
		printPostOrder(node.getRightChild());

		System.out.print(node.getSymbol() + " ");
	}

	public void createPostNFA(ParseTreeNode node, Stack<NFA> nfaStack, char[] alphabet) throws Exception{
		//Create a new NFA from going through tree in post order
		if(node == null)
			return;

		//recursivve left subtree
		createPostNFA(node.getLeft(), nfaStack, alphabet);

		//recursive right subtree
		createPostNFA(node.getRight(), nfaStack, alphabet);

		//act on node
		char symbol=node.getSymbol();
		boolean inAlphabet=false;
		for (char letter:alphabet){
			if (symbol == letter){
				NFA nfa=createNFASingleSymbol(alphabet, symbol);
				nfaStack.push(nfa);
				inAlphabet=true;
				break;
			}
			else if (symbol=='e'){
				NFA nfa=createNFAEpsiolon(alphabet, symbol);
				nfaStack.push(nfa);
				inAlphabet=true;
				break;
			}
			else if (symbol == 'N'){
				NFA nfa=createNFAEmptySet(alphabet, symbol);
				nfaStack.push(nfa);
				inAlphabet=true;
				break;
			}
		}
		if (!inAlphabet){
			if (symbol='*'){
				//Star operation on popped NFA, push it back on
				NFA nfaStarToBe=nfaStack.pop();
				nfaStarToBe.star();
				nfaStack.push(nfaStarToBe);
			}
			else if (symbol=='$'){
				//Concat oepration on two popped NFA, push new one
				NFA right=nfaStack.pop();
				NFA left=nfaStack.pop();
				left.concat(right);
				nfaStack.push(left);
			}
			else if (symbol=='|'){
				//Union operation on two popped NFA, push new one
				NFA right=nfaStack.pop();
				NFA left=nfaStack.pop();
				left.union(right);
				nfaStack.push(left);
			}
			else if (symbol == '('){
				//invalid regular expression
				throw new Expection("Invalid expression");
			}
		}
	}

	private NFA createEmptyNFA(char[] alphabet, char symbol){
		NFA nfa=new NFA();
		nfa.setNumStates(1);
		nfa.setAlph(alphabet);
		
		ArrayList<TransitionFunction> transitionFuctions=new ArrayList<>();
		nfa.setTransFunctions(transitionFunctions);
		nfa.setStartState(1);
		
		ArrayList<Integer> acceptStates=new ArrayList<>();

		nfa.setAcceptStates(accepStates);
		return nfa;
	}

	private NFA createEpsilonNFA(char[] alphabet, char symbol){
		NFA nfa=new NFA();
		nfa.setNumbStates(1);
		nfa.setAlph(alphabet);

		ArrayList<TransitionFunction> transFunctions=new ArrayList<>();

		nfa.setTransFucntions(transFunctions);
		nfa.setStartState(1);

		ArrayList<Integer> acceptStates=new ArrayList<>();
		acceptStates.add(1);

		nfa.setAcceptStates(acceptStates);
		return nfa;
	}

	private NFA createSingleSymbolNFA(char[] alphabet, char symbol){
		NFA nfa=new NFA();
		nfa.setNumStates(2);
		nfa.setAlph(alphabet);

		TransitionFunction tf=new TransitionFunction(1,symbol,2);
		ArrayList<TransitionFunction> transitionFunctions=new ArrayList<>();
		transitionFunctions.add(tf);

		nfa.setTransFucntions(transitionFunctions);
		nfa.setStartState(1);

		ArrayList<Integer> acceptStates=new ArrayList<>():
		acceptStates.add(2);

		nfa.setAcceptStates(acceptStates);
		return nfa;
	}
				
}
