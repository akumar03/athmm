/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * Entropy.java
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
package bio.tools;

/**
 * Methods to compute relative entropy. The entropy used is all relative
 * @author akumar03
 */
public class Entropy {

    /**
     * Computes the entropy of sequences, assumes amino acid sequence and computes
     * the entropy by summing - p_i* log(p_i) where p_i the probability of amino acid i
     * @param sequence letters in certain position
     * @return 
     * @throws java.lang.Exception
     */
    public static double getEntropy(String sequence) throws Exception {
        sequence = sequence.toUpperCase();
        // gettinng the count of letters
        int[] counts = new int[bio.util.BLOSUM.AA_SIZE];
        double[] probability = new double[bio.util.BLOSUM.AA_SIZE];
        double entropy = 0;
        int totalCount = 0;
        for (int i = 0; i < bio.util.BLOSUM.AA_SIZE; i++) {
            counts[i] = 0;
        }
        for (int i = 0; i < sequence.length(); i++) {
            int letter = sequence.charAt(i);
            int aaIndex = bio.util.BLOSUM.AA_LETTERS.indexOf(letter);
            if (aaIndex >= 0) {
                counts[aaIndex]++;
                totalCount++;
            }
        }
        for (int i = 0; i < bio.util.BLOSUM.AA_SIZE; i++) {
            probability[i] = (double) counts[i] / totalCount;
//            System.out.println(bio.util.BLOSUM.AA_LETTERS.charAt(i) + ":" + counts[i] + ":" + probability[i]);
            if (probability[i] > 0) {
                entropy -= probability[i] * Math.log(probability[i]) / Math.log(2);// converting to bits
            }
        }
        return entropy;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Entropy e = new Entropy();
        System.out.println(e.getEntropy("aAyts-ad"));

    }
}
