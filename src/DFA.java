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
    private void makeTransMap(ArrayList<DFATrans> transFunctions){
        transMap = new HashMap<>();
        for(DFATrans trans: transFunctions){
            transMap.put(trans.getState1() + "" + trans.getSymbol(),trans.getState2());
        }
    }

    public void stringOutput(PrintWriter outStream) {
        if (transMap == null){
            makeTransMap(transitionFunctions);
        }
        for(String stringSymbol : symbols){
            int curState = startState;
            char curSymbol;
            boolean accept = false;
            for(int i=0; i < stringSymbol.length(); i++ ){
                curSymbol = stringSymbol.charAt(i);
                String key = curState+""+curSymbol;
                curState = transMap.get(key);
            }
            for(Integer acceptState : acceptStates){
                if(curState == acceptState){
                    accept = true;
                }
            }
            if(accept){
                outStream.println("true");
            }
            else{
                outStream.println("false");
            }
        }
        outStream.close();
    }

    //gets and sets
    public void setNumStates(int numStates){
        this.numStates = numStates;
    }
    public void setAlph(char[] alph){
        this.alph = alph;
    }
    public void setTransitionFunctions(ArrayList<DFATrans>transFunctions){
        this.transitionFunctions = transFunctions;
    }
    public void setStartState(int startState){
        this.startState = startState;
    }
    public void setAcceptStates(ArrayList<Integer> acceptStates){
        this.acceptStates = acceptStates;
    }
    public void setSymbols(ArrayList<String> symbols){
        this.symbols = symbols;
    }
}
