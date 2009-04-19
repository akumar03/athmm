/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * {name}.java
 *
 * Created on Apr 13, 2009
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
package bio.scop;

import java.io.*;
import java.util.*;

/**
 * The class contains code to create training and test sets
 * @author akumar03
 */
public class DatasetGenerator {
     public static final int MAX_SIZE = 100;

      public void createTestSet() throws Exception {
        String fastaFile = bio.BioProperties.getString("exp.folder")+bio.BioProperties.getString("astral.file");
         AstralFileParser a = new AstralFileParser();

        ArrayList<String> seqList = bio.util.FASTASequenceParser.readFASTAFile(fastaFile,"");
        ArrayList<String> classList =  a.readStatFile();
        for(String classId: classList)  {
                     createTestSet(classId,seqList);
        }
    }
     public void createTestSet(String classId, ArrayList<String> seqList ) throws Exception {
        ArrayList<String> posList  =new ArrayList<String>();
        ArrayList<String> negList = new ArrayList<String>();
        ArrayList<String> writeSeqList = new ArrayList<String>();
        for(String seq: seqList) {
            String seqParts[] = seq.split("\t");
            String labelParts[] = seqParts[1].split(" ");
            String scopFamily = labelParts[1];
            int score = bio.scop.Util.compareScopFamily(classId,scopFamily);
            if(score == 3) {
                String[]  parts = seq.split("\t");
                String s = parts[0].replaceAll("\\s+","");
                s +="\t"+ "positive "+parts[1];
                posList.add(s);
            }
            if(score < 3) {
                String[]  parts = seq.split("\t");
                String s = parts[0].replaceAll("\\s+","");
                s +="\t"+ "negative "+parts[1];
                negList.add(s);
            }
        }
        int minSize = posList.size();
        if(negList.size()< minSize) minSize = negList.size();
        if(MAX_SIZE< minSize) minSize = MAX_SIZE;
        Collections.shuffle(posList);
        Collections.shuffle(negList);
        for(int i = 0;i<minSize;i++) {
            writeSeqList.add(posList.get(i));
            writeSeqList.add(negList.get(i));
        }
        Collections.shuffle(writeSeqList);
        String writeFile = bio.BioProperties.getString("exp.folder")+classId+"_test.fasta";
        bio.util.FASTASequenceParser.writeFASTAFile(writeFile,writeSeqList);
    }
    public void createTrainingSet() throws Exception {
        AstralFileParser a = new AstralFileParser();
        HashMap<String, ArrayList<String>> scopFamilyMap = a.parseFile();
        ArrayList<String> classList = a.readStatFile();
        for (String classId : classList) {

            createHMMTrainingSet(classId, scopFamilyMap.get(classId));

        }
    }

    public void createHMMTrainingSet(String classId, ArrayList<String> seqList) throws Exception {
        System.out.println("Creating Training set for " + classId + " with 0 mutations");
        String writeFile = bio.BioProperties.getString("exp.folder") + classId + "_0.fasta";
        ArrayList<String> writeSeqList = new ArrayList<String>();
        for (String seq : seqList) {
            String writeSeq = getAnnotatedSequence(seq, classId, 0);
            writeSeqList.add(writeSeq);

        }

        Collections.shuffle(writeSeqList);
        bio.util.FASTASequenceParser.writeFASTAFile(writeFile, writeSeqList);

    }

    public String getAnnotatedSequence(String sequence, String annotation, int mut) {
        String s = new String();
        String[] seqParts = sequence.split("\t");
        s = seqParts[0].replaceAll("\\s+", "");
        s += "\t" + seqParts[1] + "|mut" + mut + "|" + annotation + "|";
        return s;
    }

    public static void main(String[] args) throws Exception {
        DatasetGenerator d = new DatasetGenerator();
        d.createTrainingSet();
        d.createTestSet();
    }
}
