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
import bio.util.FASTASequenceParser;

/**
 *
 * @author akumar03
 */
public class AstralFileParser {

    public static final String EXP_FOLDER = bio.BioProperties.getString("exp.folder");
    public static final int CUTOFF_TRAIN = 10; // atleast 10 seqs in training set
    public static final int CUTOFF_POS_MIN = 10; // atleast 10 positive test sequennces
    public static final int CUTOFF_POS_MAX = 100;
    private String astralFileName = EXP_FOLDER + bio.BioProperties.getString("astral.file");

    /**
     *  The method outputs stats of astral file in the format:
     *   family count +ve -ve
     * @param out
     * @throws Exeption
     */
    public void printStats(Writer writer) throws Exception {
        HashMap<String, ArrayList<String>> scopFamilyMap = parseFile();
        ArrayList<String> seqList = bio.util.FASTASequenceParser.readFASTAFile(astralFileName, "");
        Set<String> keys = scopFamilyMap.keySet();
        int count = 1;
        for (String classId : keys) {
            int posCount = 0;
            int negCount = 0;
            for (String seq : seqList) {
                String seqParts[] = seq.split("\t");
                String labelParts[] = seqParts[1].split(" ");
                String scopFamily = labelParts[1];
                int score = Util.compareScopFamily(classId, scopFamily);
                if (score == 3) {
                    posCount++;
                }
                if (score < 3) {
                    negCount++;

                }
            }
            int classSize = scopFamilyMap.get(classId).size();
            if (classSize >= CUTOFF_TRAIN && posCount >= CUTOFF_POS_MIN && posCount <= CUTOFF_POS_MAX) {
                System.out.println(classId + "\t" + classSize + "\t" + posCount + "\t" + negCount);
                writer.write(classId + "\t" + classSize + "\t" + posCount + "\t" + negCount);
                writer.write("\n");
            }
            count++;
        }

    }

    /**
     * @return map of list of protein sequences that belong to each family
     * @throws java.lang.Exception
     */
    public HashMap<String, ArrayList<String>> parseFile() throws Exception {
        ArrayList<String> seqList = FASTASequenceParser.readFASTAFile(astralFileName, "");
        HashMap<String, ArrayList<String>> scopFamilyMap = new HashMap<String, ArrayList<String>>();
        for (String seq : seqList) {
            String seqParts[] = seq.split("\t");
            String labelParts[] = seqParts[1].split(" ");
            String scopFamily = labelParts[1];
            if (scopFamilyMap.containsKey(scopFamily)) {
                ArrayList<String> proteinList = scopFamilyMap.get(scopFamily);
                proteinList.add(seq);
            } else {
                ArrayList<String> proteinList = new ArrayList<String>();
                proteinList.add(seq);
                scopFamilyMap.put(scopFamily, proteinList);
            }
        }

        return scopFamilyMap;
    }

    public ArrayList<String> readStatFile() throws Exception {
        String fileName = EXP_FOLDER+bio.BioProperties.getString("astral.stat.file");
        ArrayList<String> classIds = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = new String();
        while ((line = reader.readLine()) != null) {
            String words[] = line.split("\t");
            classIds.add(words[0]);
            System.out.println(words[0]);
        }
        return classIds;
    }

    public void runForAllClass()throws Exception {
        ArrayList<String> classList = this.readStatFile();
        for (String classId : classList) {
            runMethod(classId);
        }
    }

    public void runMethod(String ClassId) throws Exception {

    }
    public static void main(String[] args) throws Exception {
        AstralFileParser a = new AstralFileParser();
 // generating the stats file
 //       java.io.FileWriter writer = new FileWriter(EXP_FOLDER + "astralStats.txt");
 //       a.printStats(writer);
//        writer.close();

        a.readStatFile();
    }
}
