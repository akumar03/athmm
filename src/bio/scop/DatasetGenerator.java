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
    }
}
