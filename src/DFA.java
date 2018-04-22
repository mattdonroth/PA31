import java.util.HashMap;
import java.util.ArrayList;
import java.io.PrintWriter;


public class DFA {
    private int numStates;
    private char[] alph;
    private ArrayList<DFATrans> transitionFunctions;
    private HashMap<String, Integer> transMap;
    private int startState;
    private ArrayList<Integer> acceptStates;
    private ArrayList<String> symbols;

    public DFA(){}
    public DFA(int numStates, char[] alph, ArrayList<DFATrans> transitionFunctions, int startState, ArrayList<Integer> acceptStates, ArrayList<String> symbols){
        this.numStates = numStates;
        this.alph = alph;
        this.transitionFunctions = transitionFunctions;
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.symbols = symbols;
        makeTransMap(transitionFunctions);
    }

    private void makeTransMap(ArrayList<DFATrans> transFunctions){
        transMap = new HashMap<>();
        for(DFATrans trans: transFunctions){
            transMap.put(trans.getState1() + "" + trans.getSymbol(),trans.getState2());
        }
    }


    public void printDFA(PrintWriter out){
        out.println(numStates);
        for(char a: alph){
            out.print(a);
        }
        out.println();
        for(DFATrans trans: transitionFunctions){
            out.print(trans.getState1() + " ");
            out.print(trans.getSymbol() + " ");
            out.print(trans.getState2());
        }
        out.println(startState);
        for(int ac:acceptStates){
            out.print(ac+" ");
        }
        out.println();
        out.close();
    }
    //gets and sets
    public int getNumStates(){
        return numStates;
    }
    public void setNumStates(int numStates){
        this.numStates = numStates;
    }
    public char[] getAlph(){
        return alph;
    }
    public void setAlph(char[] alph){
        this.alph = alph;
    }
    public ArrayList<DFATrans> getTransitionFunctions(){
        return transitionFunctions;
    }
    public void setTransitionFunctions(ArrayList<DFATrans>transFunctions){
        this.transitionFunctions = transFunctions;
    }
    public HashMap<String, Integer> getTransMap(){
        return transMap;
    }
    public void setTransMap(HashMap<String, Integer> transMap){
        this.transMap = transMap;
    }
    public int getStartState(){
        return startState;
    }
    public void setStartState(int startState){
        this.startState = startState;
    }
    public ArrayList<Integer> getAcceptStates(){
        return acceptStates;
    }
    public void setAcceptStates(ArrayList<Integer> acceptStates){
        this.acceptStates = acceptStates;
    }
    public ArrayList<String> getSymbols(){
        return symbols;
    }
    public void setSymbols(ArrayList<String> symbols){
        this.symbols = symbols;
    }



}
