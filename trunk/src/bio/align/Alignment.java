/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * {name}.java
 *
 * Created on Apr 15, 2009
 *
 * Copyright 2003-2009 Tufts University  Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package bio.align;

import java.util.*;
import java.io.*;
import bio.util.Sequence;

/**
 * This is a datastructure for alignment.  The sequences contain '-' for a gap,
 * we may change the letter later. 
 *
 * @author akumar03
 */
public class Alignment extends ArrayList<Sequence> {
    public int NEIGHBORS = 4;
    public static final char GAP = '-'; // gap char
    public static final double ALIGN_PT = 0.9;
    public static final int MAX_SEQUENCE_LENGTH = 10000;
    private int alignmentLength = 0;
    private ArrayList alignedColumns = new ArrayList();


    public Alignment(ArrayList<String> sequences) throws Exception {
        super();
        for (String sequence : sequences) {
            Sequence seq = new Sequence(sequence);
            add(seq);
            alignmentLength  = seq.sequence.length();
        }

        computeAlignedColumns();
       
    }

    public String getColumn(int i) throws Exception {
        String column = "";
         for(Sequence seq:this) {
                column += (char) seq.sequence.charAt(i);
              }
         return column;
    }

    public void print() throws Exception {
        for (Sequence seq : this) {
            System.out.println(seq.sequence);
        }
    }

    public void printMatchStat() throws Exception {
        for (int i = 0; i < alignmentLength; i++) {
            if(isColumnMatch(i)){
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        }

    }
   public void printStats(Writer writer) throws Exception {
        double[] probs = computeProbilitiesOfMutation() ;
         for (int i = 0; i < alignmentLength; i++) {
             String s = "";  // temp string
              for(Sequence seq:this) {
                s += seq.sequence.charAt(i);
              }
              s += ",";
              s += isColumnMatch(i);

              if(isColumnMatch(i)) {
                  s +=  ","+bio.tools.Entropy.getEntropy(getColumn(i));
                   s +=   ","+computeNeighborhoodEntropy(i);
                   s +=   ","+probs[i];
              }
//              System.out.println(s);
               writer.write(s);
               writer.write("\n");
          }
       
      
       
         writer.flush();
   }
    public  ArrayList getAlignedColumns() throws Exception {
        return this.alignedColumns;
    }
    public boolean isColumnMatch(int columnId) {
        int count = 0;
        for (Sequence seq : this) {
            if (seq.sequence.charAt(columnId) == GAP) {
                count++;
            }
        }
        double allowedGaps = (1 - ALIGN_PT) * size();
        if (count < allowedGaps) {
            return true;
        }
        return false;
    }

    public double computeNeighborhoodEntropy(int columnId) throws Exception {
        int index =  alignedColumns.indexOf(columnId);
        int min = index-NEIGHBORS;
        if(min < 0) { min = 0;}
        int max = index+NEIGHBORS;
        if(max>=alignedColumns.size()-1) { max = alignedColumns.size()-1;}
        int total  = max-min+1;
        double  totalEntropy = 0;
 //       System.out.println("Computing N entroy cId:"+columnId+" min:"+min+" max: "+max);
        for(int i = min; i<=max;i++) {
            totalEntropy += bio.tools.Entropy.getEntropy(getColumn((Integer)alignedColumns.get(i)));
  //          System.out.println("Total Entroy "+ totalEntropy);
        }

        return totalEntropy/total;
    }
    /**
     *  The probability of mutation is inversely related to the entropy.  Probabilities are
     * computed such that expected number of mutations attains a desired rate of mutation
     * @return
     * @throws java.lang.Exception
     */
    public double[] computeProbilitiesOfMutation() throws Exception {
        double probs[] = new double[alignmentLength];
        double[] neighborhoodEntropyInverse = new double[alignmentLength];
        double sumEInverse = 0.0;
        for(int i = 0;i<alignmentLength;i++) {
            if(isColumnMatch(i)) {
            neighborhoodEntropyInverse[i] = 1/computeNeighborhoodEntropy(i);
            sumEInverse += neighborhoodEntropyInverse[i];
            }
        }
         for(int i = 0;i<alignmentLength;i++) {
             if(isColumnMatch(i)) {
             probs[i] =1/(neighborhoodEntropyInverse[i]*sumEInverse);
             } else {
                 probs[i] = 0.0;
             }
         }
        return probs;

    }
      private void computeAlignedColumns() throws Exception {
        int count =0;
         for (int i = 0; i < alignmentLength; i++) {
              if(isColumnMatch(i)) {
                 alignedColumns.add(i);
                  count++;
             }
         }
    }
}
