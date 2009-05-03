/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * HMMOutputParser.java
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
package bio.util;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
/**
 *
 * @author akumar03
 */
public class HMMOutputParser {

    public static final String EXT_HIT = ".hit";
    DecimalFormat df = new DecimalFormat(".000");
    public static final String SEPARATOR = "//";
    public static final String QUERY = "Query sequence:";
    public static final String DESCRIPTION = "Description:";
    public static final String HITS = "Hits";
    public static final String PARSED  = "Parsed for domains";

    
    public ArrayList<Hit> parseHMMHitFile(String fileName,String classId) throws Exception {
        ArrayList<Hit> list = new ArrayList<Hit>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = new String();
        boolean flag = false;
        Hit h = new Hit();
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(QUERY)) {
               h = new Hit();
                h.label = line.substring(QUERY.length());
            }
            if (line.startsWith(DESCRIPTION)) {
                h.label += line.substring(DESCRIPTION.length());
            }
            if (line.startsWith(SEPARATOR)) {
                flag = false;
                list.add(h);
            }
            if (line.contains("no hits above thresholds")) {
                flag = false;
            }
            if (flag && line.length() > 10 && line.startsWith(classId)) {
//                           System.out.println(line);
                String[] words = line.split("\\s+");
                Domain d = new Domain();
                d.pfamA_acc = words[0];
                d.seq_start = Integer.parseInt(words[2]);
                d.seq_end = Integer.parseInt(words[3]);
                d.score = Double.parseDouble(words[8]);
                String eValue = words[9];
                if (eValue.startsWith("e")) {
                    eValue = "1" + eValue;
                }
                d.eValue = Double.parseDouble(eValue);
                if(h.label.contains("positive")) { h.type = true;}
                else { h.type = false;}

                if (d.eValue <= 10) {
                    if (!h.contains(d)) {
                        h.add(d);
                    }
                }
                flag = false;
            }
            if (line.startsWith(PARSED)) {
                flag = true;
            }
        }
        return list;
    }
    public static void printBestHits(ArrayList<Hit> list) throws Exception {
        for(Hit hit: list) {
            System.out.println(hit.label+":"+hit.getBestHit().score+":"+hit.type);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        String classId = "a.1.1.2";
        String fileName = bio.BioProperties.getString("exp.folder") + classId + "_0" + EXT_HIT;
        HMMOutputParser h = new HMMOutputParser();
        ArrayList<Hit> hits = h.parseHMMHitFile(fileName, classId);
        Collections.sort(hits, new HitComparator());
        printBestHits(hits);

    }
}
