/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * HMMTrainingSetGenerator.java
 *
 * Created on Apr 20, 2009
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

import java.io.*;
import java.util.*;
import bio.scop.AstralFileParser;

/**
 *
 * @author akumar03
 */
public class HMMTrainingSetGenerator {

    public static final String EXT_ALN = ".aln";
    public static final int N = 10; // number of mutated sequences to be added

    public void createMutatedHMMTrainingSet(String classId) throws Exception {
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_20" + EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        alignment.appendMutatedSequences(Double.parseDouble(bio.BioProperties.getString("mutation.rate")), N, classId);
        alignment.write(writer);

    }

    public void createNEHMMTrainingSet(String classId) throws Exception {
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_20NE" + EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        alignment.appendMutatedSequencesNE(Double.parseDouble(bio.BioProperties.getString("mutation.rate")), N, classId);
        alignment.write(writer);
    }

    public void createNEXHMMTrainingSet(String classId) throws Exception {
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_20NEX" + EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        alignment.appendMutatedSequencesNEX(Double.parseDouble(bio.BioProperties.getString("mutation.rate")), N, classId);
        alignment.write(writer);
    }

    public void createAllTrainingSet() throws Exception {
//        String classId = "b.6.1.3";
        AstralFileParser a = new AstralFileParser();
        ArrayList<String> classList = a.readStatFile();
        for (String classId : classList) {
            createMutatedHMMTrainingSet(classId);
            createNEHMMTrainingSet(classId);
            createNEXHMMTrainingSet(classId);
            System.out.println("Generated Training files for: "+classId);
            Thread.sleep(10);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        HMMTrainingSetGenerator h = new HMMTrainingSetGenerator();
        h.createAllTrainingSet();
//        h.createNEHMMTrainingSet("d.2.1.3");
    }
}
