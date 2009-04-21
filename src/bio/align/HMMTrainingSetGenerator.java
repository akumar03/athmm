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
/**
 *
 * @author akumar03
 */
public class HMMTrainingSetGenerator {
    public static final String EXT_ALN = ".aln";
    public void createMutatedHMMTrainingSet(String classId) throws Exception {
        String fileName = bio.BioProperties.getString("exp.folder")+classId+"_0"+EXT_ALN;
        Alignment alignment =AlignmentParser.parse(fileName);

        
    }
    public void createNEHMMTrainingSet(String classId) throws Exception {

    }
    public void createNEXHMMTrainingSet(String classId) throws Exception {

    }

    public void createAllTrainingSet() throws Exception {
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        HMMTrainingSetGenerator h = new HMMTrainingSetGenerator();
        h.createMutatedHMMTrainingSet("b.6.1.3");
    }

}
