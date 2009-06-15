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
            alignmentLength = seq.sequence.length();
        }

        computeAlignedColumns();

    }

    public String getColumn(int i) throws Exception {
        String column = "";
        for (Sequence seq : this) {
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
            if (isColumnMatch(i)) {
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        }

    }

    public void printStats(Writer writer) throws Exception {
        double[] probs = computeProbilitiesOfMutation();
        for (int i = 0; i < alignmentLength; i++) {
            String s = "";  // temp string
            for (Sequence seq : this) {
                s += seq.sequence.charAt(i);
            }
            s += ",";
            s += isColumnMatch(i);

            if (isColumnMatch(i)) {
                s += "," + bio.tools.Entropy.getEntropy(getColumn(i));
                s += "," + computeNeighborhoodEntropy(i);
                s += "," + probs[i];
            }
//              System.out.println(s);
            writer.write(s);
            writer.write("\n");
        }



        writer.flush();
    }

    public ArrayList getAlignedColumns() throws Exception {
        return this.alignedColumns;
    }
 
    
    public double getMeanEntropy() throws Exception {
        double mean = 0.0;
        for(int i=0;i<this.alignedColumns.size();i++) {
            int columnId = (Integer)alignedColumns.get(i);
            mean += bio.tools.Entropy.getEntropy(getColumn(columnId));
        }
         mean = mean/this.alignedColumns.size();
         return mean;
    }

    public double getMeanNeighborEntropy() throws Exception {
        double mean = 0.0;

        for(int i=0;i<this.alignedColumns.size();i++) {
            int columnId = (Integer)alignedColumns.get(i);
            mean += computeNeighborhoodEntropy(columnId);
        }
        mean = mean/this.alignedColumns.size();
        return mean;
    }

    public double getSDNeighborEntropy()  throws Exception {
        double sqMean = 0.0;
        double meanEntropy = getMeanNeighborEntropy();
        for(int i=0;i<this.alignedColumns.size();i++) {
            int columnId = (Integer)alignedColumns.get(i);
            double nEntropy =  computeNeighborhoodEntropy(columnId);
            sqMean = Math.pow(nEntropy-meanEntropy,2.0);
        }
        sqMean = sqMean/this.alignedColumns.size();
        return Math.sqrt(sqMean);
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
        int index = alignedColumns.indexOf(columnId);
        int min = index - NEIGHBORS;
        if (min < 0) {
            min = 0;
        }
        int max = index + NEIGHBORS;
        if (max >= alignedColumns.size() - 1) {
            max = alignedColumns.size() - 1;
        }
        int total = max - min + 1;
        double totalEntropy = 0;
        //       System.out.println("Computing N entroy cId:"+columnId+" min:"+min+" max: "+max);
        for (int i = min; i <= max; i++) {
            totalEntropy += bio.tools.Entropy.getEntropy(getColumn((Integer) alignedColumns.get(i)));
        //          System.out.println("Total Entroy "+ totalEntropy);
        }
        return totalEntropy / total;
    }

    /**
     *  The probability of mutation is inversely related to the entropy.  Probabilities are
     * computed such that expected number of mutations attains a desired rate of mutation
     * @return
     * @throws java.lang.Exception
     */
    public double[] computeProbilitiesOfMutation() throws Exception {
        double probs[] = new double[alignmentLength];
        for (int i = 0; i < alignmentLength; i++) {
//            System.out.println("CM:"+i+":"+isColumnMatch(i));
            if (isColumnMatch(i)) {
                probs[i] = 1.0 / alignedColumns.size();
            } else {
                probs[i] = 0.0;
            }
        }
        return probs;
    }

    public double[] computeProbilitiesOfMutationNE() throws Exception {
        double probs[] = new double[alignmentLength];
        double[] neighborhoodEntropyInverse = new double[alignmentLength];
        double sumEInverse = 0.0;
        for (int i = 0; i < alignmentLength; i++) {
            if (isColumnMatch(i)) {
                neighborhoodEntropyInverse[i] = 1 / computeNeighborhoodEntropy(i);
                sumEInverse += neighborhoodEntropyInverse[i];
            }
        }
        for (int i = 0; i < alignmentLength; i++) {
            if (isColumnMatch(i)) {
                probs[i] =  neighborhoodEntropyInverse[i] / sumEInverse;
            } else {
                probs[i] = 0.0;
            }
        }
        return probs;
    }

    public double[] computeProbabilitiesOfMutationNEX() throws Exception {
        double probs[] = new double[alignmentLength];
        double[] neighborhoodEntropyInverse = new double[alignmentLength];
        double sumEInverse = 0.0;
        for (int i = 0; i < alignmentLength; i++) {
            if (isColumnMatch(i)) {
                neighborhoodEntropyInverse[i] = computeNeighborhoodEntropy(i);
                sumEInverse += neighborhoodEntropyInverse[i];
            }
        }
        for (int i = 0; i < alignmentLength; i++) {
            if (isColumnMatch(i)) {
                probs[i] = neighborhoodEntropyInverse[i] / sumEInverse;
            } else {
                probs[i] = 0.0;
            }
        }
        return probs;
    }

    /**
     *  The method appends an alignment with mutated seqeunces. Mutations are
     *  added a specified mutation rate. N is the number of sequences added.
     * @param mutationRate
     * @param N
     * @throws java.lang.Exception
     */
    public void appendMutatedSequences(double mutationRate, int N, String classId) throws Exception {
        ArrayList<Sequence> newSequences = new ArrayList<Sequence>();
        for (Sequence s : this) {
            String[] labelParts = s.label.split("\\|");
            s.label = labelParts[0] + "|mut" + 0 + "|" + classId + "|";
            for (int i = 1; i <= N; i++) {
                Sequence sMutated = new Sequence();
                sMutated.label = labelParts[0] + "|mut" + i + "|" + classId + "|";
                sMutated.sequence = mutateRandom(mutationRate, s.sequence);
                newSequences.add(sMutated);
            }
        }

        addAll(newSequences);
    }

    public void appendMutatedSequencesNE(double mutationRate, int N, String classId) throws Exception {
        ArrayList<Sequence> newSequences = new ArrayList<Sequence>();
        for (Sequence s : this) {
            String[] labelParts = s.label.split("\\|");
            s.label = labelParts[0] + "|mut" + 0 + "|" + classId + "|";
            for (int i = 1; i <= N; i++) {
                Sequence sMutated = new Sequence();
                sMutated.label = labelParts[0] + "|mut" + i + "|" + classId + "|";
                sMutated.sequence = mutateNE(mutationRate, s.sequence);
                newSequences.add(sMutated);
            }
        }

        addAll(newSequences);
    }

    public void appendMutatedSequencesNEX(double mutationRate, int N, String classId) throws Exception {
        ArrayList<Sequence> newSequences = new ArrayList<Sequence>();
        for (Sequence s : this) {
            String[] labelParts = s.label.split("\\|");
            s.label = labelParts[0] + "|mut" + 0 + "|" + classId + "|";
            for (int i = 1; i <= N; i++) {
                Sequence sMutated = new Sequence();
                sMutated.label = labelParts[0] + "|mut" + i + "|" + classId + "|";
                sMutated.sequence = mutateNEX(mutationRate, s.sequence);
                newSequences.add(sMutated);
            }
        }

        addAll(newSequences);
    }

    private String mutateRandom(double mutationRate, String s) throws Exception {
        String r = s; //return string
        double[] probs = computeProbilitiesOfMutation();
        int mutations = (int) (mutationRate * alignedColumns.size());
        int mutationPositions[] = bio.util.Probability.getRandomNumbers(mutations, probs);
        for (int i = 0; i < mutationPositions.length; i++) {
            int positionId = mutationPositions[i];
            char subChar = bio.util.BLOSUM.getRandom(r.charAt(positionId));
            r = r.substring(0, positionId) + subChar + s.substring(positionId + 1);
        }
        return r;
    }

    private String mutateNE(double mutationRate, String s) throws Exception {
        String r = s; //return string
        double[] probs = computeProbilitiesOfMutationNE();
        int mutations = (int) (mutationRate * alignedColumns.size());
        int mutationPositions[] = bio.util.Probability.getRandomNumbers(mutations, probs);
        for (int i = 0; i < mutationPositions.length; i++) {
            int positionId = mutationPositions[i];
            char subChar = bio.util.BLOSUM.getRandom(r.charAt(positionId));
            r = r.substring(0, positionId) + subChar + s.substring(positionId + 1);
        }
        return r;
    }

    private String mutateNEX(double mutationRate, String s) throws Exception {
        String r = s; //return string
        double[] probs = computeProbabilitiesOfMutationNEX();
        int mutations = (int) (mutationRate * alignedColumns.size());
        int mutationPositions[] = bio.util.Probability.getRandomNumbers(mutations, probs);
        for (int i = 0; i < mutationPositions.length; i++) {
            int positionId = mutationPositions[i];
            char subChar = bio.util.BLOSUM.getRandom(r.charAt(positionId));
            r = r.substring(0, positionId) + subChar + s.substring(positionId + 1);
        }
        return r;
    }

    public void write(Writer writer) throws Exception {
        for (Sequence sequence : this) {
            writer.write(">" + sequence.label);
            writer.write("\n");
            for (int i = 0; i <= sequence.sequence.length() / 50; i++) {
                int start = i * 50;
                int stop = start + 50 > sequence.sequence.length() ? sequence.sequence.length() : start + 50;
                writer.write(sequence.sequence.substring(start, stop));
                writer.write("\n");
            }
        }
        writer.flush();
    }

    private void computeAlignedColumns() throws Exception {
        int count = 0;
        for (int i = 0; i < alignmentLength; i++) {
            if (isColumnMatch(i)) {
                alignedColumns.add(i);
                count++;
            }
        }
    }


}
