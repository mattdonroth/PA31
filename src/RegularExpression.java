import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class RegularExpression{
    private char[] alph;
    private ArrayList<String> input;
    private String reg;

    public RegularExpression(char[] alph, String reg, ArrayList<String> input){
        this.alph=alph;
        this.input=input;
        this.reg=reg;
    }

    public NFA regToNFA() throws EmptyStackException, Exception{
        //took out declared NFA
        char[] regChar = reg.replaceAll(" ","").toCharArray();
        Stack<Character> opStack = new Stack<>();
        Stack<Node> randStack = new Stack<>();
        int catCounter=0;
        for(char symbol:regChar){
            boolean alphSymbol = false;
            for(char letter : alph){
                if(symbol==letter || symbol=='e' || symbol=='N'){
                    alphSymbol=true;
                    catCounter++;
                    catCounter= checkCat(alphSymbol,catCounter,opStack,randStack);
                    Node tempNode = new Node(symbol);
                    randStack.push(tempNode);
                    break;
                }
            }
            if(!alphSymbol){
                if(symbol=='('){
                    if(catCounter==1){
                        catCounter++;
                        catCounter = checkCat(alphSymbol, catCounter, opStack, randStack);
                        opStack.push(symbol);
                    }
                }
                else{
                    catCounter=0;
                    if(symbol=='*'){
                        catCounter++;
                        if(!opStack.isEmpty()){
                            boolean precedence=false;
                            while(!precedence && !opStack.isEmpty()){
                                char peek=opStack.peek();
                                if(peek!='*') {
                                    precedence = true;
                                }
                                else{
                                    star(opStack, randStack);
                                }
                            }
                        }
                        opStack.push(symbol);
                    }
                    else if(symbol=='|'){
                        if(!opStack.isEmpty()){
                            boolean firstFlag=false;
                            while(!firstFlag && !opStack.isEmpty()){
                                char topOp=opStack.peek();
                                if(topOp=='('){
                                    firstFlag = true;
                                }
                                else if(topOp=='*'){
                                    star(opStack,randStack);
                                }
                                else if(topOp=='|' || topOp=='$'){
                                    unionAndCat(opStack, randStack);
                                }
                            }
                        }
                        opStack.push(symbol);
                    }
                    else if(symbol==')'){
                        catCounter++;
                        while(opStack.peek()!='('){
                            if(opStack.peek()=='|' || opStack.peek()=='$'){
                                unionAndCat(opStack, randStack);
                            }
                            else{
                                star(opStack, randStack);
                            }
                        }
                        opStack.pop();
                    }
                }
            }
        }
        while(!opStack.isEmpty()){
            if(opStack.peek()=='$' || opStack.peek()=='|'){
                unionAndCat(opStack, randStack);
            }
            else{
                star(opStack, randStack);
            }
        }
        Node rut = randStack.pop();
        Stack<NFA> nfaStack=new Stack<>();
        rut.createPostNFA(rut, nfaStack, alph);
        return nfaStack.pop();
    }

    private void star(Stack<Character> opStack, Stack<Node> randStack){
        char op = opStack.pop();
        Node opNode=new Node(op);
        opNode.setLeft(randStack.pop());
        randStack.push(opNode);
    }

    private void unionAndCat(Stack<Character> opStack, Stack<Node> randStack) throws EmptyStackException{
        //handles stack changes with union and concatenation
        char op=opStack.pop();
        Node opNode=new Node(op);
        opNode.setRight(randStack.pop());
        opNode.setLeft(randStack.pop());
        randStack.push(opNode);
    }

    private int checkCat(boolean alphSymbol, int catCount, Stack<Character> opStack, Stack<Node> randStack) throws EmptyStackException{
        if(catCount==2){
            if(alphSymbol){
                catCount = 1;
            }
            else{
                catCount = 0;
            }
            if(!opStack.isEmpty()){
                boolean firstFlag=false;
                while(!firstFlag && !opStack.isEmpty()){
                    if(opStack.peek()=='(' || opStack.peek()=='|'){
                        firstFlag = true;
                    }
                    else if(opStack.peek()=='*'){
                        star(opStack, randStack);
                    }
                    else{
                        unionAndCat(opStack, randStack);
                    }
                }
            }
            opStack.push('$');
        }
        return catCount;
    }

    public ArrayList<String> getSymbols(){
        return input;
    }
}
