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
package bio.report;

/**
 *
 * @author akumar03
 */
public class ROCStat {

    public int truePositive=0;
    public int trueNegative=0;
    public int falsePositive=0;
    public int  falseNegative=0;
    public int positive=0;
    public int negative=0;
    public double getTPRate() {
        return (double)truePositive/positive;
    }
    public double getFPRate() {
        return (double)falsePositive/negative;
    }
    public int size=0;
}
