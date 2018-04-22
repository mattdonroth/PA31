public class DFATrans {
    private int curState;
    private char symbol;
    private int nextState;
    private boolean accept = false;

    public DFATrans(int curState, char symbol, int nextState){
        this.curState = curState;
        this.symbol = symbol;
        this.nextState = nextState;
    }
    public int getState1(){
        return curState;
    }
    public char getSymbol(){
        return symbol;
    }
    public int getState2(){
        return nextState;
    }
    public void setCurState(int curState){
        this.curState = curState;
    }
    public void setSymbol(char symbol){
        this.symbol = symbol;
    }
    public void setNextState(int nextState){
        this.nextState = nextState;
    }
    public boolean isAccept(){
        return accept;
    }
    public void setAccept(boolean accept){
        this.accept = accept;
    }

}
