/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * {name}.java
 *
 * Created on Apr 13, 2009
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
package bio.scop;

/**
 *
 * @author akumar03
 */
public class Util {
    /**
     *
     * @param f1 family 1
     * @param f2 family 2
     * @return returns 0 for no match 1 if classes match, 2 if folds match, 3 if superfamilies match, 4 if families match
     */
    public static int compareScopFamily(String f1,String f2) {
        int score =0;
        String f1Parts[] = f1.split("\\.");
        String f2Parts[] = f2.split("\\.");
        if(f1Parts[0].equals(f2Parts[0])) score =1;
        for(int i =1;i<f1Parts.length;i++) {
            if(f1Parts[i].equals(f2Parts[i]) && score >(i-1)) score++;
        }
        return score;
    }
}
