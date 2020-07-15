package AdaptiveHuffman;

import java.util.ArrayList;
import java.util.HashMap;


public class Tree {

    private Node root;
    private Node NYT;
    private HashMap<String, Node> nodes = new HashMap();
    private HashMap<Integer, ArrayList<Node>> blocks = new HashMap();

    public Tree(){
        Node temp = new Node("", 100, 0);
        temp.setParent(null);
        root = NYT = temp;
    }

    public Node getRoot() {
        return root;
    }

    public Node getNYT() {
        return NYT;
    }

    public String getCode(String str){
        String code = "";
        Node current, parent;
        if(str.equals("")){
            current = NYT;
        }
        else
            current = nodes.get(str);
        parent = current.getParent();
        while(parent != null){
            if(current == parent.getLeft())
                code = "0" + code;
            else
                code = "1" + code;
            current = parent;
            parent = current.getParent();
        }
        return code;
    }

    public Node createNode(String value){
        Node newNode = new Node(value, NYT.getNumber()-1, 1);
        Node newParent = new Node(NYT.getNumber(), NYT.getParent(), NYT, newNode);
        if(newParent.getParent() == null)
            root = newParent;
        else{
            NYT.getParent().setLeft(newParent);
        }
        newNode.setParent(newParent);
        NYT.setParent(newParent);
        NYT.setNumber(NYT.getNumber()-2);

        nodes.put(value, newNode);

        if(root != newParent)
            addNodeToBlock(newParent, 1);
        addNodeToBlock(newNode, 1);
        return newNode;
    }

    public void addNodeToBlock(Node node, int k){
        ArrayList<Node> values = blocks.get(k);

        if(values != null){
            values = blocks.get(k);
            if(values.size() == 0)
                values.add(node);
            else{
                boolean isAdded = false;
                for (int i = 0; i < values.size(); i++) {
                    if(values.get(i).getNumber() < node.getNumber()) {
                        values.add(i, node);
                        isAdded = true;
                        break;
                    }
                }
                if(!isAdded)
                    values.add(values.size(), node);
            }
        }
        else{
            values = new ArrayList<>();
            values.add(node);
            blocks.put(k, values);
        }
    }

    public void editBlock(Node node){
        ArrayList<Node> oldList = blocks.get(node.getCount());
        int index = oldList.indexOf(node);
        oldList.remove(index);
        addNodeToBlock(node, node.getCount()+1);
    }

    void updateBlock(Node node) {
        ArrayList<Node> oldList = blocks.get(node.getCount());
        int index = oldList.indexOf(node);
        oldList.remove(index);
        addNodeToBlock(node, node.getCount());
    }

    public void updateNode(Node node){
        ArrayList<Node> block = blocks.get(node.getCount());
        ArrayList<Node> higherNodes = new ArrayList<>();
        int index = block.indexOf(node);
        for (int i = 0; i < index; i++) {
            higherNodes.add(block.get(i));
        }
        ArrayList<Node> parents = new ArrayList<>();
        Node current = node.getParent();
        while (current != root){
            parents.add(current);
            current = current.getParent();
        }
        for (int i = 0; i < higherNodes.size(); i++) {
            if(!parents.contains(higherNodes.get(i))) {
                swap(node, higherNodes.get(i));
                break;
            }
        }
    }

    public void updateTree(Node current){

        while (current != null){
            if(current != root) {
                updateNode(current);
                editBlock(current);
            }
            current.incrementCount();
            current = current.getParent();
        }

    }

    public void swap(Node first, Node second){

        int tempNum = first.getNumber();
        first.setNumber(second.getNumber());
        second.setNumber(tempNum);

        Node firstParent = first.getParent();
        Node secondParent = second.getParent();

        if(firstParent == secondParent) {
            if(first == firstParent.getLeft()) {
                firstParent.setRight(first);
                firstParent.setLeft(second);
            } else {
                firstParent.setRight(second);
                firstParent.setLeft(first);
            }
        } else {
            first.setParent(secondParent);
            second.setParent(firstParent);

            if(firstParent.getLeft() == first) {
                firstParent.setLeft(second);
            }
            else
                firstParent.setRight(second);

            if(secondParent.getLeft() == second)
                secondParent.setLeft(first);
            else
                secondParent.setRight(first);
        }

        updateBlock(second);
    }

    public String encodeChar(String value){
        String code = new String();
        Node node = nodes.get(value);

        if(node == null){
            code = getCode("");
            node = createNode(value);
            node = node.getParent().getParent();
        }
        else
            code = getCode(value);

        updateTree(node);
        return code;
    }

    public void printTree(Node root) {

        this.root = root;

        preorder(root, true);
    }

    public void preorder(Node currentNode, boolean lastChild) {

        if(currentNode != null) {

            System.out.println(printNode(currentNode));
            preorder(currentNode.getRight(), true);
            preorder(currentNode.getLeft(), false);
        }
    }

    private String printNode(Node node) {
        return node.getNumber() + " |" + node.getSymbol() + "| " + node.getCount();
    }
}
