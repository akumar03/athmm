/*
 * BLOSUM.java
 *
 * Created on September 23, 2007, 8:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bio.util;

/**
 *
 * @author akumar03
 */

import java.util.*;
import java.io.*;


public class BLOSUM {
    public static final String PATH = "C:\\anoop\\courses\\bio\\research\\function\\";
    public static final String BLOSUM_FILE = PATH+"SortedBlosum62.csv";
    public static final String AA_LETTERS = "ACDEFGHILKMNPQRSTVWY";
    public static final double[] AAF = {0.074,0.025,0.054,0.054,0.047,0.074,0.026,0.068,0.099,0.058,0.025,0.045,0.039,0.034,0.052, 0.057,0.051,0.073,0.013,0.034};
    public static int[][]  BLOSUM = new int[20][20];
    public static double[][] probs = new double[20][20];
    public static final double L = 0.347;
    public static boolean loaded = false;
    /** Creates a new instance of BLOSUM */
    public BLOSUM() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        if(!loaded) {
            loadBlosum();
        }
        printSelfProbs();
        String s  = "VVIAIEEITGRGTQEPGGDPPVSYHVGQVFAIICGDPKQVVNRNSWYVTA";
        String s1 = getRandom(s);
        String s100 = getRandom(s,100);
        String s1000 = getRandom(s,1000);
        String s10000 = getRandom(s,10000);
        System.out.println("A Random: "+getRandom('A'));
        System.out.println("A Random: "+getRandom('A'));
        System.out.println("A Random: "+getRandom('A'));
        System.out.println("D Random: "+getRandom('D'));
        System.out.println("D Random: "+getRandom('D'));
        System.out.println("String0    : "+s);
        System.out.println("String1    : "+s1 +" N="+compare(s,s1));
        System.out.println("String100  : "+s100+" N="+compare(s,s100));
        System.out.println("String1000 : " +s1000+" N="+compare(s,s1000));
        System.out.println("String10000: " +s10000+" N="+compare(s,s10000));
        
        
        
        
    }
    public static char getRandom(char c) throws Exception {
        if(!loaded) {
            loadBlosum();
        }
        double rand = Math.random();
        int count = AA_LETTERS.indexOf(c);
        // in case special characters appear
        if(count <0) {
            return c;
        }
        for(int i = 0;i<20;i++) {
//                        System.out.println("char: "+ c+ " probability: "+rand+ " AA: "+AA_LETTERS.charAt(i)+ "count: "+count+" i:"+i);
            if(rand<probs[count][i]) {
                return AA_LETTERS.charAt(i);
            } else {
                rand -= probs[count][i];
            }
        }
        return c;
    }
    
    public static String getRandom(String s) throws Exception {
        int sub = (int)(Math.random()*(double)(s.length()-1));
        char subChar =  getRandom(s.charAt(sub));
        s = s.substring(0,sub)+subChar+s.substring(sub+1);
        return s;
    }
    public static String getRandom(String s, int n) throws Exception{
        for(int i=0;i<n;i++) {
            s = getRandom(s);
        }
        return s;
    }
    
    public static int compare(String s1,String s2) throws Exception {
        int count = 0;
        for(int i = 0;i<s1.length();i++) {
            if(!s1.substring(i,i+1).equals(s2.substring(i,i+1))) count++;
        }
        return count;
    }
    public static void printSelfProbs()  {
        for(int i = 0;i<20;i++) {
            System.out.println("Self prob "+AA_LETTERS.charAt(i)+" prob:"+probs[i][i]);
        }
    }
    public  static void loadBlosum() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(BLOSUM_FILE));
        String line;
        int count = 0;
        while((line=reader.readLine()) != null) {
            //           System.out.println(line);
            String words[] = line.split(",");
            double total = 0;
            for(int i =0; i<words.length;i++) {
                BLOSUM[count][i] = Integer.parseInt(words[i]);
                probs[count][i] = Math.pow(10,(double)BLOSUM[count][i]*L)*AAF[count]*AAF[i];
                total += probs[count][i];
//                System.out.println(AA_LETTERS.charAt(i)+"\t"+probs[count][i]+"\t"+total+"\t"+BLOSUM[count][i]);
                
                
            }
            for(int i =0; i<words.length;i++) {
                probs[count][i] = probs[count][i]/total;
//             System.out.println(AA_LETTERS.charAt(i)+"\t"+probs[count][i]+"\t"+total+"\t"+BLOSUM[count][i]);
                
            }
            
            count++;
        }
        
        reader.close();
        loaded = true;
    }
    
    
}
