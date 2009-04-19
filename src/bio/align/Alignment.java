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

    public static final char GAP = '-'; // gap char
    public static final double ALIGN_PT = 0.9;
    private int alignmentLength = 0;

    public Alignment(ArrayList<String> sequences) {
        super();
        for (String sequence : sequences) {
            Sequence seq = new Sequence(sequence);
            add(seq);
            alignmentLength  = seq.sequence.length();
        }

    }

    public String getColumn(int i) throws Exception {
        String column = "";
         for(Sequence seq:this) {
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
            if(isColumnMatch(i)){
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        }

    }

   public void printStats(Writer writer) throws Exception {

          for (int i = 0; i < alignmentLength; i++) {
              for(Sequence seq:this) {
                writer.write(seq.sequence.charAt(i));
              }
              writer.write(",");
              writer.write(""+isColumnMatch(i));
              writer.write("\n");
          }
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
}
