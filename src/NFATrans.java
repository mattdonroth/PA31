import java.util.HashSet;

public class NFATrans {
    private int state1;
    private char symbol;
    private HashSet<Integer> state2;

    public NFATrans(int state1, char symbol, HashSet<Integer> state2){
        this.state1 = state1;
        this.symbol = symbol;
        this.state2 = state2;
    }
    public int getState1(){
        return state1;
    }
    public char getSymbol(){
        return symbol;
    }
    public HashSet<Integer> getState2(){
        return state2;
    }
}
