
/*
 * AlignmentParser.java
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
import java.text.DecimalFormat;

import bio.report.ROC;
import bio.report.MER;
/**
 *
 * @author akumar03
 */
public class AlignmentParser {
    public static DecimalFormat df = new DecimalFormat(".000");

    /**
     * @param args the command line arguments
     */
    public static Alignment parse(String fileName) throws Exception {
        ArrayList<String> sequences = bio.util.FASTASequenceParser.readFASTAFile(fileName);
        return new Alignment(sequences);
    }
    public static void main(String[] args) throws Exception {


//        Alignment a = AlignmentParser.parse(bio.BioProperties.getString("exp.folder")+"b.6.1.3_0.aln");
//        a.printStats(new PrintWriter(System.out));
//        System.out.println("0:"+a.getColumn(0));
//        ArrayList aColumns = a.getAlignedColumns();
//        System.out.println("match length: "+aColumns.size());
//        bio.util.VectorUtils.printIntArray(aColumns, new PrintWriter(System.out));
         bio.scop.AstralFileParser a = new bio.scop.AstralFileParser() {
            public void runMethod(String classId) throws Exception {
                System.out.print(classId+"\t");
                 for(int mPercent =10;mPercent<=60;mPercent+= 10) {
                      ROC roc = new ROC();
                    Alignment a = AlignmentParser.parse(bio.BioProperties.getString("exp.folder")+classId+"_"+mPercent+".aln");
                     Alignment na = AlignmentParser.parse(bio.BioProperties.getString("exp.folder")+classId+"_"+mPercent+"NE.aln");
                     System.out.print(df.format(roc.getAUC(classId, "_"+mPercent))+"\t"+df.format(roc.getAUC(classId, "_"+mPercent+"NE"))+"\t");
//                    System.out.print(df.format(a.getMeanEntropy())+"\t");
//                    System.out.print(df.format(na.getMeanEntropy())+"\t");
                 }
                 System.out.println();
//                  Alignment a = AlignmentParser.parse(bio.BioProperties.getString("exp.folder")+classId+"_0.aln");
//                    Alignment a1 = AlignmentParser.parse(bio.BioProperties.getString("exp.folder")+classId+"_20.aln");
//                      Alignment a2 = AlignmentParser.parse(bio.BioProperties.getString("exp.folder")+classId+"_20NE.aln");
 //                 ROC roc = new ROC();
 //                 MER mer = new MER();
     //             System.out.println(classId+"\t"+a.size()+"\t"+a.getMeanNeighborEntropy()+"\t"+a.getSDNeighborEntropy()+"\t"+roc.getAUC(classId, "_0")+"\t"+roc.getAUC(classId, "_0_all")+"\t"+roc.getAUC(classId, "_20"));
     //              System.out.println(classId+"\t"+a.size()+"\t"+a.getMeanNeighborEntropy()+"\t"+a.getSDNeighborEntropy()+"\t"+roc.getAUC(classId, "_0")+"\t"+roc.getAUC(classId, "_20")+"\t"+roc.getAUC(classId, "_20NE"));
   //                System.out.println(classId+"\t"+a.size()+"\t"+a.getMeanNeighborEntropy()+"\t"+a.getSDNeighborEntropy()+"\t"+a1.getMeanNeighborEntropy()+"\t"+a1.getSDNeighborEntropy()+"\t"+a2.getMeanNeighborEntropy()+"\t"+a2.getSDNeighborEntropy());
            }
        };
        a.runForAllClass();

    }

}
