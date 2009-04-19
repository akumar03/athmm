/*
 * FASTASequenceParser.java
 *
 * Created on October 17, 2007, 9:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bio.util;


import java.io.*;
import java.util.*;

/** This class  reads a file cotaining sequences in fasta format into a list
 * 
 * @author akumar03
 */
public class FASTASequenceParser {
    
    /** Creates a new instance of FASTASequenceParser */
    public FASTASequenceParser() {
    }

    /**
     *  reads a single sequence from a file (assumes that file contains only one
     * sequence
     * @param inputFile path to input file
     * @return  Sequence the protein sequence
     **/
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

    /**
     * writes a fasta file from a list of sequences
     * @param fileName filename to output sequence
     * @param seqList
     * @throws java.lang.Exception
     */
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
    /**
     *
     * @param fileName  absolulte path to the file containing fasta sequences
     * @param defaultLabel a label when label doesn't exist
     * @return list of protein sequences
     * @throws java.lang.Exception
     */
    

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

      public static  ArrayList<String> readFASTAFile(String fileName) throws Exception {
          return readFASTAFile(fileName,"");
      }
      
        
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        FASTASequenceParser f = new FASTASequenceParser();
        
    }
    
    
}
