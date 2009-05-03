/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * MER.java
 *
 * Created on Apr 23, 2009
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
package bio.report;

import java.util.*;
import java.text.DecimalFormat;

import bio.util.HMMOutputParser;
import bio.util.Hit;
import bio.util.HitComparator;

/**
 *  This class computes the Minimum error rate which is the minumum of sum of false positive and false
 * negatives for any cutoff value
 * @author akumar03
 */
public class MER {
    public static final String EXT_HIT = ".hit";
    public static DecimalFormat df = new DecimalFormat(".0000");
    public double computeMER(String fileName, String classId) throws Exception {
        HMMOutputParser h = new HMMOutputParser();
        ArrayList<Hit> hits = h.parseHMMHitFile(fileName, classId);
        Collections.sort(hits, new HitComparator());
        double cutoff = Double.POSITIVE_INFINITY;
        double mer = Double.POSITIVE_INFINITY;
        for(Hit hit: hits) {
            cutoff = hit.getBestHit().score;
            ROCStat stat = ROC.computROCStat(hits, cutoff);
            double newMER = (double)(stat.falsePositive+stat.falseNegative)/stat.size;
//            System.out.println(hit.getBestHit().score+"\t"+stat.truePositive+"\t"+stat.falsePositive+"\t"+stat.trueNegative+"\t"+stat.falseNegative+"\t"+newMER);
            if(newMER < mer) { mer = newMER;}
        }
        return mer;
    }

    public double computeCoverage(String fileName, String classId ) throws Exception {
        HMMOutputParser h = new HMMOutputParser();
        ArrayList<Hit> hits = h.parseHMMHitFile(fileName, classId);
        Collections.sort(hits, new HitComparator());
         double cutoff = Double.POSITIVE_INFINITY;
        double coverage = 0.0;
        for(Hit hit: hits) {
            cutoff = hit.getBestHit().score;
             ROCStat stat = ROC.computROCStat(hits, cutoff);
             coverage = (double)stat.truePositive/stat.size;
             if(stat.falsePositive>0) { return coverage; }

        }

        return 1.0;
    }

    public static void printAll(String classId) throws Exception {
          MER m = new MER();
         String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_HIT;
         System.out.print(classId+"\t"+df.format(m.computeMER(fileName, classId))+"\t"+df.format(m.computeCoverage(fileName, classId)));
         fileName =  bio.BioProperties.getString("exp.folder") + classId + "_20" + EXT_HIT;
         System.out.print("\t"+df.format(m.computeMER(fileName, classId))+"\t"+df.format(m.computeCoverage(fileName, classId)));
          fileName =  bio.BioProperties.getString("exp.folder") + classId + "_20NE" + EXT_HIT;
         System.out.print("\t"+df.format(m.computeMER(fileName, classId))+"\t"+df.format(m.computeCoverage(fileName, classId)));
          fileName =  bio.BioProperties.getString("exp.folder") + classId + "_20NEX" + EXT_HIT;
         System.out.print("\t"+df.format(m.computeMER(fileName, classId))+"\t"+df.format(m.computeCoverage(fileName, classId)));
         System.out.println();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        bio.scop.AstralFileParser a = new bio.scop.AstralFileParser() {
            public void runMethod(String classId) throws Exception {
                 printAll(classId);
            }
        };
        a.runForAllClass();
        // TODO code application logic here
        String classId = "c.1.8.1";
          
    }

}
