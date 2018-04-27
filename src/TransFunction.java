import java.util.HashSet;
public class TransFunction {
    private HashSet<Integer> state1;
    private char symbol;
    private HashSet<Integer> state2;
    private int startState;
    private int endState;

    public TransFunction(HashSet<Integer> state1, char symbol, HashSet<Integer> state2){
        this.state1 = state1;
        this.symbol = symbol;
        this.state2 = state2;
    }

    public HashSet<Integer> getState1(){
        return state1;
    }
    public char getSymbol(){
        return symbol;
    }
    public HashSet<Integer> getState2(){
        return state2;
    }
    public int getstartState(){
        return startState;
    }
    public void setStartState(int start){
        this.startState = start;
    }
    public int getEndState(){
        return endState;
    }
    public void setEndState(int end){
        this.endState = end;
    }
}
