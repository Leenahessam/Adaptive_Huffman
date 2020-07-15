package AdaptiveHuffman;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static char toChar(String code){
        int Code = Integer.parseInt(code, 2);
        return (char) Code;
    }

    public static String toASCII(char symbol){
        int ASCII = symbol;
        String binary = Integer.toBinaryString(ASCII);
        while(binary.length() !=8){
            binary = "0" + binary;
        }
        return binary;
    }

    public static String Compression(String str){
        Tree tree = new Tree();
        String encodedStr = new String();
        for (int i = 0; i < str.length(); i++) {
            String code = new String();
            String symbol = new String();
            String NYT = tree.getCode("");
            symbol += str.charAt(i);
            code = tree.encodeChar(symbol);
            if(i == 0)
                encodedStr += toASCII(symbol.charAt(0));
            else{
                encodedStr += code;
                if(code.equals(NYT))
                    encodedStr += toASCII(symbol.charAt(0));
            }
        }
        return encodedStr;
    }

    public static String Decompression(String str){
        Tree tree = new Tree();
        String output = new String();
        Node current = tree.getRoot();
        for (int i = 0; i < str.length()+1 ;) {
            if(current.getRight() == null){
                if(current == tree.getNYT()){
                    char seq = toChar(str.substring(i, i+8));
                    i += 8;
                    tree.encodeChar(String.valueOf(seq));
                    output += seq;
                }
                else{
                    String sequence = current.getSymbol();
                    tree.encodeChar(sequence);
                    output += sequence;
                }
                current = tree.getRoot();
            }
            else{
                if(i < str.length()){
                    char seq = str.charAt(i);
                    if(seq == '0')
                        current = current.getLeft();
                    else
                        current = current.getRight();
                }
                i++;
            }
        }
        return output;
    }

    public static String readFromFile(String fileName){
        String text = null;
        File file = new File(fileName);
        if(file.exists()) {
            try {
                text = new String(Files.readAllBytes(Paths.get(fileName)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return text;
        }
        else {
            System.out.println("Input file doesn't exist");
            System.exit(0);
        }
        return null;
    }


    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        byte choice;
        String inFileName, outFileName;

        System.out.println("Enter input file name: ");
        inFileName = s.next();
        System.out.println("Enter output file name: ");
        outFileName = s.next();
        String text = readFromFile(inFileName);
        File file = new File(outFileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintStream ps = new PrintStream(fos);

        System.out.println("\n1) Compression\n2) Decompression\nEnter your choice:");
        choice = s.nextByte();

        switch (choice){
            case 1:
                if(text == null)
                    break;
                System.setOut(ps);
                System.out.print(Compression(text));
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ps.close();
                break;

            case 2:
                if(text == null)
                    break;
                System.setOut(ps);
                System.out.print(Decompression(text));
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ps.close();
                break;

            default:
                return;
        }
    }
}
