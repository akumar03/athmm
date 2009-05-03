/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * ROC.java
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
import bio.util.Hit;
import bio.util.HMMOutputParser;
import bio.util.HitComparator;

/**
 *
 * @author akumar03
 */
public class ROC {
     public static final String EXT_HIT = ".hit";
       public static DecimalFormat df = new DecimalFormat(".0000");
  
    public static ROCStat computROCStat(ArrayList<Hit> hits,double cutoff) {
        ROCStat stat = new ROCStat();
        stat.size= hits.size();
        for(Hit hit: hits) {
            double score = hit.getBestHit().score;
            if(hit.type) {
                stat.positive++;
                if(score>=cutoff) {
                    stat.truePositive++;
                } else {
                    stat.falseNegative++;
                }
            }else {
                stat.negative++;
                if(score>=cutoff) {
                    stat.falsePositive++;
                } else {
                    stat.trueNegative++;
                }
            }
        }
        return stat;
    }

    public double auc(ArrayList<Hit> hits)  throws Exception {
        Collections.sort(hits, new HitComparator());
        double cutoff = Double.POSITIVE_INFINITY;
        double coverage = 0.0;
        double auc = 0.0;
        double oldTpRate = 0.0;
        double oldFpRate =0;
        for(Hit hit: hits) {
            cutoff = hit.getBestHit().score;
            ROCStat stat = ROC.computROCStat(hits, cutoff);
            double tpRate = stat.getTPRate();
            double fpRate = stat.getFPRate();
            auc += (tpRate+oldTpRate)*(fpRate-oldFpRate)/2;
            oldTpRate = tpRate;
            oldFpRate= fpRate;
//            System.out.println(cutoff+"\t"+fpRate+"\t"+tpRate+"\t"+auc);

        }
        return auc;
    }

      public static void printAll(String classId) throws Exception {
          ROC r = new ROC();
            HMMOutputParser h = new HMMOutputParser();

         String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_HIT;
          ArrayList<Hit> hits = h.parseHMMHitFile(fileName, classId);

         System.out.print(classId+"\t"+df.format(r.auc(hits)));
         fileName =  bio.BioProperties.getString("exp.folder") + classId + "_20" + EXT_HIT;
         hits = h.parseHMMHitFile(fileName, classId);
         System.out.print("\t"+df.format(r.auc(hits)));
          fileName =  bio.BioProperties.getString("exp.folder") + classId + "_20NE" + EXT_HIT;
         hits = h.parseHMMHitFile(fileName, classId);
         System.out.print("\t"+df.format(r.auc(hits)));
         System.out.println();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
      bio.scop.AstralFileParser a = new bio.scop.AstralFileParser() {
            public void runMethod(String classId) throws Exception {
                 printAll(classId);
            }
        };
        a.runForAllClass();;
    }

}
