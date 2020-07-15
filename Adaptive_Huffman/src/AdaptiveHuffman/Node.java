package AdaptiveHuffman;

public class Node {

    private String symbol;
    private int count, number;
    private Node parent, left, right;

    /*--------------------------------------------------------------------------*/

    public Node(String symbol, int number, int count){
        this.left = null;
        this.right = null;
        this.symbol = symbol;
        this.number = number;
        this.count = count;
    }

    public Node(int number, Node parent, Node left, Node right){
        this.number = number;
        this.symbol = "";
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.count = left.count + right.count;
    }

    public void incrementCount(){
        count++;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getCount() {
        return count;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public Node getParent() {
        return parent;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }
    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }
    public void setRight(Node right) {
        this.right = right;
    }
}
