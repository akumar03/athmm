/*
 * Sequence.java
 *
 * Created on February 17, 2008, 3:37 PM
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

/**
 *  A simple data structure to save the label and and sequeunce of protein
 * sequnece
 * @author akumar03
 *
 */
public class Sequence {
    public static final String SPLIT = "\t";
    public String label = new String();
    public String sequence = new String();
    /** Creates a new instance of Sequence */
    public Sequence() {
    }
    /*
     *  We have two formats for a sequence. One with sequence+"\t"+label and 
     * other that uses this datastructure. Eventually everything should use this 
     * datastructure
     * 
     **/
    public Sequence(String sequence) {
       String[] words = sequence.split(SPLIT);
       this.label = words[1];
       this.sequence = words[0];
    }
    
}
