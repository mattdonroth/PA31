import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main (String args[]){
        String inPath=null;
        String outPath=null;
        if(args.length < 1){
            System.out.println("Please follow the usage");
            System.exit(0);
        }
        else{
            inPath = args[0];
            outPath= args[1];
        }
        Scanner inScan = null;
        try{
            inScan = new Scanner(new File(inPath));
        }
        catch(FileNotFoundException e){
            System.out.println("Please make sure the input is in the directory");
            System.exit(0);
        }
        PrintWriter outWriter = null;
        try{
            outWriter = new PrintWriter(new BufferedWriter(new FileWriter(outPath)));
        }
        catch(IOException e){
            System.out.println("Please make sure the output is in the directory");
            System.exit(0);
        }
        RegularExpression reg = null;
        try{
            reg = parseRegularExpression(inScan);
        }
        catch(InputMismatchException e){
            System.out.println("Cannot parse input.");
            System.exit(0);
        }
        NFA nfa = null;
        try{
            nfa = reg.regToNFA();
        }
        catch(Exception e){
            outWriter.println("Cannot convert regular expression");
            outWriter.close();
        }
        if(nfa!=null){
            DFA dfa = nfa.convtoDFA();
            dfa.setSymbols(reg.getSymbols());
            dfa.printDFA(outWriter);
        }
    }

    private static RegularExpression parseRegularExpression(Scanner inScanner){
        char[] alph = inScanner.next().toCharArray();
        for(char symbol : alph){
            if (symbol=='N' || symbol=='|' || symbol == 'e' || symbol=='*') {
                System.out.println("Cannot recognize alphabet");
                System.exit(0);
            }
        }
        inScanner.nextLine();
        String reg = inScanner.nextLine();
        ArrayList<String> inputSymbols = new ArrayList<>();
        while(inScanner.hasNext()){
            inputSymbols.add(inScanner.nextLine());
        }
        inScanner.close();
        return new RegularExpression(alph, reg, inputSymbols);
    }
}
