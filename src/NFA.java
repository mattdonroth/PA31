import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class NFA{
    private int numStates;
    private char[] alph;
    private ArrayList<DFATrans> transFunctions;
    private HashMap<String, HashSet<Integer>> transMap;
    private int startState;
    private ArrayList<Integer> acceptStates;

    public NFA(){}
    public NFA(int numStates, char[] alph, ArrayList<DFATrans> transFunctions, int startState, ArrayList<Integer> acceptStates) {
        this.numStates = numStates;
        this.alph = alph;
        this.transFunctions = transFunctions;
        this.startState = startState;
        this.acceptStates = acceptStates;
    }

    public DFA convtoDFA(){
        DFA fin = new DFA();
        fin.setAlph(alph);
        ArrayList<NFATrans> transFunc = new ArrayList<>();
        while(!transFunctions.isEmpty()){
            ArrayList<DFATrans> gone = new ArrayList<>();
            HashSet<Integer> nextStateSet = new HashSet<>();
            nextStateSet.add(transFunctions.get(0).getState2());
            gone.add(transFunctions.get(0));
            int tempState1 = transFunctions.get(0).getState1();
            char tempSymbol = transFunctions.get(0).getSymbol();

            for(int i = 0; i < transFunctions.size(); i++){
                if(tempState1 == transFunctions.get(i).getState1() && tempSymbol == transFunctions.get(i).getSymbol()){
                    nextStateSet.add(transFunctions.get(i).getState2());
                    gone.add(transFunctions.get(i));
                }
            }
            NFATrans setTrans = new NFATrans(tempState1, tempSymbol, nextStateSet);
            transFunc.add(setTrans);
            transFunc.removeAll(gone);
        }

        transMap = new HashMap<>();
        for(NFATrans tran : transFunc){
            transMap.put(tran.getState1() + "" + tran.getSymbol(), tran.getState2());
        }
        LinkedBlockingQueue<HashSet<Integer>> a = new LinkedBlockingQueue<>();

        ArrayList<TransFunction> transFunction = new ArrayList<>();
        for(char s : alph){
            HashSet<Integer> cur = new HashSet<>();
            cur.add(-1);
            transFunction.add(new TransFunction(cur, s, cur));
        }
        HashSet<Integer> set1 = new HashSet<>();
        set1.add(startState);
        set1 = getEps(set1);
        getTrans(a, transFunction, set1);
        while(!a.isEmpty()){
            HashSet<Integer> newset1 = a.poll();
            boolean hasSet = false;
            for(TransFunction trans : transFunction){
                if(trans.getState1().equals(newset1)){
                    hasSet = true;
                }
            }
            if(!hasSet){
                getTrans(a, transFunction, newset1);
            }
        }
        ArrayList<DFATrans> dfaTrans = getDFATrans(transFunction, fin, set1);
        fin.setTransitionFunctions(dfaTrans);
        return fin;
    }

    private HashSet<Integer> getEps(HashSet<Integer> set1){
        HashSet<Integer> set2 = new HashSet<>();
        for(int state : set1){
            HashSet<Integer> temp = transMap.get(state + "e");
            if(temp != null){
                set2.addAll(temp);
            }
        }
        if(set1.addAll(set2)){
            set1 = getEps(set1);
        }
        return set1;
    }

    private void getTrans(LinkedBlockingQueue<HashSet<Integer>> a, ArrayList<TransFunction> transFunc, HashSet<Integer> set1){
        for(char l : alph){
            HashSet<Integer> set2 = new HashSet<>();
            for(int cur : set1){
                HashSet<Integer> temp = transMap.get(cur + "" + l);
                if(temp != null){
                    set2.addAll(temp);
                }
            }
            if(!set2.isEmpty()){
                set2 = getEps(set2);
            }
            else{
                set2.add(-1);
            }
            TransFunction tran = new TransFunction(set1, l, set2);
            boolean hasSet = false;
            for(TransFunction at : transFunc){
                if(tran.getState2().equals(at.getState1())){
                    hasSet = true;
                    break;
                }
            }
            if(!hasSet){
                try{
                    a.put(set2);
                }
                catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }
            transFunc.add(tran);
        }
    }

    private ArrayList<DFATrans> getDFATrans(ArrayList<TransFunction> transFunc, DFA dfa, HashSet<Integer> set1){
        ArrayList<TransFunction> newTrans = new ArrayList<>();
        int numStatesDFA = 0;
        int stateNumDFA = 1;
        HashSet<Integer> acceptedStates = new HashSet<>();
        int startStateDFA = -1;
        while(!transFunc.isEmpty()){
            ArrayList<TransFunction> gone = new ArrayList<>();
            HashSet<Integer> tempStartSet = transFunc.get(0).getState1();
            transFunc.get(0).setStartState(stateNumDFA);
            if(transFunc.get(0).getState1().equals(set1)){
                startStateDFA=stateNumDFA;
            }
            for(int state:transFunc.get(0).getState1()){
                for(int accept: acceptStates){
                    if(state==accept){
                        acceptedStates.add(stateNumDFA);
                    }
                }
            }
            for(int i = 1; i < transFunc.size(); i++){
                if(tempStartSet.equals(transFunc.get(i).getState1())){
                    transFunc.get(i).setStartState(stateNumDFA);
                    gone.add(transFunc.get(i));
                }
            }
            newTrans.addAll(gone);
            transFunc.removeAll(gone);
            numStatesDFA++;
            stateNumDFA++;
        }
        for(TransFunction tran: newTrans){
            HashSet<Integer> tempSet = tran.getState1();
            int tempDFAState = tran.getstartState();
            for(int i = 0; i < newTrans.size(); i++){
                if(newTrans.get(i).getState2().equals(tempSet)){
                    newTrans.get(i).setEndState(tempDFAState);
                }
            }
        }
        ArrayList<DFATrans> transitions = new ArrayList<>();
        for(TransFunction tomp: newTrans){
            DFATrans temp = new DFATrans(tomp.getstartState(), tomp.getSymbol(), tomp.getEndState());
            transitions.add(temp);
        }
        return transitions;
    }

    public void star(){
        numStates++;
        for(DFATrans tran: transFunctions){
            tran.setCurState(tran.getState1()+1);
            tran.setNextState(tran.getState2()+1);
        }
        for(int i = 0; i < acceptStates.size(); i++){
            acceptStates.set(i, acceptStates.get(i)+1);
        }
        acceptStates.add(startState);
        DFATrans newStartTran = new DFATrans(1, 'e', startState+1);
        transFunctions.add(newStartTran);
        for(int stat:acceptStates){
            DFATrans newEndTran = new DFATrans(stat, 'e', startState+1);
            transFunctions.add(newEndTran);
        }
    }
    public void concat(NFA right){
        int oldNumStates = numStates;
        for(DFATrans tran:right.getTransFunctions()){
            tran.setCurState(tran.getState1()+oldNumStates);
            tran.setNextState(tran.getState2()+oldNumStates);
        }
        transFunctions.addAll(right.getTransFunctions());
        for(int i = 0; i < right.getAcceptStates().size(); i++){
            right.getAcceptStates().set(i, right.getAcceptStates().get(i)+oldNumStates);
        }
        for(int stat:acceptStates){
            DFATrans newEps = new DFATrans(stat, 'e', right.getStartState()+oldNumStates);
            transFunctions.add(newEps);
        }
        acceptStates=right.getAcceptStates();
        numStates=oldNumStates+right.getNumStates();
    }
    public void union(NFA right){
        numStates++;
        int oldNumStates = numStates;
        for(DFATrans tran: transFunctions){
            tran.setCurState(tran.getState1()+1);
            tran.setNextState(tran.getState2()+1);
        }
        for(int i = 0; i <acceptStates.size(); i++){
            acceptStates.set(i, acceptStates.get(i)+1);
        }
        for(DFATrans tran: right.getTransFunctions()){
            tran.setCurState(tran.getState1()+oldNumStates);
            tran.setNextState(tran.getState2()+oldNumStates);
        }
        transFunctions.addAll(right.getTransFunctions());
        for(int i = 0; i < right.getAcceptStates().size(); i++){
            right.getAcceptStates().set(i, right.getAcceptStates().get(i)+oldNumStates);
        }
        acceptStates.addAll(right.getAcceptStates());
        DFATrans newStart = new DFATrans(1, 'e', startState+1);
        transFunctions.add(newStart);
        DFATrans newTrans = new DFATrans(1, 'e', right.getStartState()+oldNumStates);
        numStates=numStates+right.getNumStates();
    }

    public int getNumStates(){
        return numStates;
    }
    public void setNumStates(int num){
        this.numStates = num;
    }
    public char[] getAlph(){
        return alph;
    }
    public void setAlph(char[] alph){
        this.alph = alph;
    }
    public ArrayList<DFATrans> getTransFunctions(){
        return transFunctions;
    }
    public void setTransFunctions(ArrayList<DFATrans> func){
        this.transFunctions = func;
    }
    public int getStartState(){
        return startState;
    }
    public void setStartState(int state){
        this.startState = state;
    }
    public ArrayList<Integer> getAcceptStates(){
        return acceptStates;
    }
    public void setAcceptStates(ArrayList<Integer> states){
        this.acceptStates = states;
    }
}

