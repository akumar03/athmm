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

    public void createMutatedHMMTrainingSet(String classId, int mPercent) throws Exception {
        double mRate = (double)mPercent/100;
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_"+mPercent+ EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        alignment.appendMutatedSequences( mRate, N, classId);
        alignment.write(writer);

    }

    public void createNEHMMTrainingSet(String classId, int mPercent) throws Exception {
         double mRate = (double)mPercent/100;
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_"+mPercent+"NE" + EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        alignment.appendMutatedSequencesNE(mRate, N, classId);
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

    public void createMutatedTrainingSets(String classId) throws Exception {
        for(int mPercent =10;mPercent<=60;mPercent+= 10) {
            createMutatedHMMTrainingSet(classId,mPercent);
            createNEHMMTrainingSet(classId,mPercent);;

        }
    }

    public void createExpTraningSetsForClass(String classId) throws Exception{
        //control condition without mutations
        createTrainingSet(classId,Conditions.NONE,Conditions.NONE,Conditions.NONE,Conditions.NONE);
        createTrainingSet(classId,Conditions.NONE,Conditions.NONE,"80","10");
        createTrainingSet(classId,"2","X","80","10");
        createTrainingSet(classId,"2","EX","80","10");

    }
    public void createTrainingSet(String classId,String neighbors,String function,String rate,String n) throws Exception {
        if(n.equals(Conditions.NONE)) {
            createControlDataset(classId);
        } else if(function.equals(Conditions.NONE)) {
            createTSNoFunction(classId,rate,n);
        } else if(function.equals("X")) {
            createTSX(classId,neighbors,rate,n);
        } else if(function.equals("EX")) {
            createTSEX(classId,neighbors,rate,n);
        }

    }

    public void createControlDataset(String classId) throws Exception{
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_"+Conditions.NONE + "_"+Conditions.NONE + "_"+Conditions.NONE + "_"+Conditions.NONE + EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        alignment.write(writer);
    }

    public void createTSNoFunction(String classId, String rate,String n) throws Exception {
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_"+Conditions.NONE + "_"+Conditions.NONE + "_"+rate + "_"+n + EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        double mRate = Double.parseDouble(rate)/100;
        alignment.appendMutatedSequences( mRate, Integer.parseInt(n), classId);
        alignment.write(writer);
    }

  public void createTSX(String classId, String neighbors,String rate,String n) throws Exception {
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_"+neighbors+ "_"+"X"+ "_"+rate + "_"+n + EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        double mRate = Double.parseDouble(rate)/100;
        alignment.appendMutatedSequencesNE(Conditions.FUNCTION_X,Integer.parseInt(neighbors),mRate, Integer.parseInt(n), classId);
        alignment.write(writer);
    }

   public void createTSEX(String classId, String neighbors,String rate,String n) throws Exception {
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_ALN;
        String outFileName = bio.BioProperties.getString("exp.folder") + classId + "_"+neighbors+ "_"+"EX"+ "_"+rate + "_"+n + EXT_ALN;
        FileWriter writer = new FileWriter(outFileName);
        Alignment alignment = AlignmentParser.parse(fileName);
        double mRate = Double.parseDouble(rate)/100;
        alignment.appendMutatedSequencesNE(Conditions.FUNCTION_EX,Integer.parseInt(neighbors),mRate, Integer.parseInt(n), classId);
        alignment.write(writer);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        System.out.println("Generating all training sets");
        HMMTrainingSetGenerator h = new HMMTrainingSetGenerator();
        AstralFileParser a = new AstralFileParser();
        ArrayList<String> classList = a.readStatFile();
        for (String classId : classList) {
            System.out.println("Generating Datasets for:"+classId);
            h.createExpTraningSetsForClass(classId);
        }
    }
}
