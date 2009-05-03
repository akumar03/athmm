/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * {name}.java
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
/**
 *
 * @author akumar03
 */
public class Hit extends ArrayList<Domain>{
    public String label;
    public String sequenceId;
    public boolean type; // type of hit positive or negative

      public Domain getBestHit() {
        Domain best = new Domain();
        best.score = Double.NEGATIVE_INFINITY;
        for(Domain d: this ) {
            if(d.score > best.score ) {
                best = d;
            }
        }
        return best;
    }

}
