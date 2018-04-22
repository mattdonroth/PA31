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
}
