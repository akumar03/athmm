/*
 * FASTASequenceParser.java
 *
 * Created on October 17, 2007, 9:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bio.util;

/**
 *
 * @author akumar03
 */

import java.io.*;
import java.util.*;

public class FASTASequenceParser {
    
    /** Creates a new instance of FASTASequenceParser */
    public FASTASequenceParser() {
    }
    
    public static Sequence readSequence(String inputFile) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line = new String();
        Sequence s = new Sequence();
        while((line = reader.readLine()) != null) {
            if(line.startsWith(">")) {
                s.label = line;
            } else {
                s.sequence += line.replaceAll("\\W+","");
            }
        }
        return s;
    }
    
    public static  void writeFASTAFile(String fileName,ArrayList<String> seqList) throws Exception{
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for(String s:seqList) {
            String words[] = s.split("\t");
            writer.write(">"+words[1]);
            writer.newLine();
            for(int i = 0;i <=words[0].length()/50;i++) {
                int start = i*50;
                int stop = start+50>words[0].length()?words[0].length():start+50;
                writer.write(words[0].substring(start,stop));
                writer.newLine();
                
            }
        }
        writer.close();
        
    }
    
   public void parse(String inputFile,String outputFile) throws Exception {
        List<Sequence> sList= new ArrayList<Sequence>();
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line = new String();
        Sequence s = new Sequence();
        while((line = reader.readLine()) != null) {
            if(line.startsWith(">")) {
                if(s.sequence.length() >0) {
                    sList.add(s);
                }
                s = new Sequence();
                s.label = line;
                s.sequence = "";
            } else {
                s.sequence += line.replaceAll("\\W+","");
            }
        }
        reader.close();
        printNSequences(1000,sList,outputFile);
    }
    
    public void printNSequences(int n,List<Sequence> sList,String outputFile) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for(int i=1;i<=n;i++ ) {
            int rnd = (int)(Math.random()*(double)sList.size());
            Sequence s = sList.get(rnd);
            sList.remove(rnd);
            System.out.println("i="+i+"size= "+sList.size()+" Label: "+s.label+" Sequence:"+s.sequence);
            writer.write(s.label+"_EGF");
            writer.newLine();
            writer.write(s.sequence);
            writer.newLine();
        }
        writer.close();
        
    }
    
     public static  ArrayList<String> readFASTAFile(String fileName,String defaultLabel) throws Exception {
        BufferedReader seqReader = new BufferedReader(new FileReader(fileName));
        String seqLine  = new String();
        ArrayList<String> seqList = new ArrayList<String>();
        String seq = "";
        int count = 0;
        String label =  defaultLabel;
        while((seqLine = seqReader.readLine()) != null) {
            if(seqLine.startsWith(">") ) {
                if(count != 0){
                    String line = seq+"\t"+label;
                    seqList.add(line);
                }  
                label = seqLine.substring(1);
                seq ="";
                count++;
            } else {
                seq += seqLine;
            }
        }
        // reading the last line
        String line = seq+"\t"+label;
        seqList.add(line);
        return seqList;
    }
     
      
        
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        FASTASequenceParser f = new FASTASequenceParser();
        f.parse("C:\\anoop\\courses\\bio\\research\\function\\egf\\EGF-like.txt","C:\\anoop\\courses\\bio\\research\\function\\egf\\EGF-like-RAND1001.txt");
        
    }
    
    
}
