/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * {name}.java
 *
 * Created on Apr 24, 2009
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
public class HitComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Hit h1 = (Hit) o1;
        Hit h2 = (Hit) o2;
        if(h2.getBestHit().score > h1.getBestHit().score ) {
            return 1;
        } else if(h2.getBestHit().score < h1.getBestHit().score ) {
            return -1;
        } else {
            return 0;
        }
    }

}
