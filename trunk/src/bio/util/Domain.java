/*
 * Domain.java
 *
 * Created on April 15, 2007, 1:57 PM
 *
 * <p><b>License and Copyright: </b>The contents of this file are subject to the
 * Mozilla Public License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at <a href="http://www.mozilla.org/MPL">http://www.mozilla.org/MPL/.</a></p>
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.</p>
 *
 * <p>The entire file consists of original code.  Copyright &copy; 2003-2007
 * Tufts University. All rights reserved.</p>
 *
 * -----------------------------------------------------------------------------
 */

package bio.util;

import java.util.*;

/**
 *
 * @author akumar03
 */
public class Domain {
    public String pfamA_acc;
    public String pfamA_id;
    public int seq_start;
    public int seq_end;
    public double eValue;
    public double score;
    public static double cutoff =  1e-5;
    
    public static Comparator domainComparator = new Comparator(){
        public int compare(Object o1, Object o2) {
            if(o1 instanceof Domain && o2 instanceof Domain) {
                Domain d1 = (Domain) o1;
                Domain d2 = (Domain) o2;
                if(d1.eValue < d2.eValue) {
                    return -1;
                } else if (d1.eValue > d2.eValue) {
                    return 1;
                } else  return 0;
            } else {
                return 0;
            }
        }
        
    };
    /** Creates a new instance of Domain */
    public Domain() {
    }
    
    public boolean doesOverlap(Domain d2) {
        if((seq_start >= d2.seq_start) && (seq_start <= d2.seq_end) && (eValue < cutoff) && (d2.eValue <cutoff)) {
            return true;
        } else if(d2.seq_start >= seq_start && d2.seq_start <= seq_end && (eValue < cutoff) && (d2.eValue <cutoff)) {
            return true;
        }
        return false;
    }
    public boolean equals(Object o) {
        if(o instanceof Domain) {
            if(((Domain) o).pfamA_acc.equalsIgnoreCase(pfamA_acc)) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(o);
        }
    }
    public Domain clone() {
        Domain c = new Domain();
        c.pfamA_acc = this.pfamA_acc;
        
        if (this.pfamA_id != null) c.pfamA_id = this.pfamA_id;
        c.seq_start  = this.seq_start;
        c.seq_end = this.seq_end;
        c.eValue = this.eValue;
        return c;
    }
    
    public String  toString() {
        String s = new  String();
//        s = "pfamA_acc: "+pfamA_acc+", pfamA_id: "+pfamA_id+", seq_start: "+seq_start+", seq_end: "+seq_end+", eValue:"+ eValue;
        s = "HMM Acc: "+pfamA_acc+", seq_start: "+seq_start+", seq_end: "+seq_end+", eValue:"+ eValue;
        return s;
    }
}
