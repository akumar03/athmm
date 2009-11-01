/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * Proability.java
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
package bio.util;

import java.util.*;

/**
 *
 * @author akumar03
 */
public class Probability {

    public static Random random = new Random();

    /**
     * The method genereates n random numbers from the prbability distrubtion
     * @param n
     * @param probs
     * @return
     */
    public static int[] getRandomNumbers(int n, double probs[]) {
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            boolean flag = true;
            double newRandom = random.nextDouble();
            int j = 0;
            while (flag) {
                if (newRandom <= 0) {
                    flag = false;
                    x[i] = j-1;
                } else {
 //                 System.out.println(j+":"+newRandom+":"+probs[j]);
                    newRandom -= probs[j];
                    j++;
                }
            }
        }
        return x;
    }

    public static  void printArrayStats(int[] array, int n) {
        System.out.println("Array stats");
        int[] a = new int[n];
        for(int i=0;i<array.length;i++) {
            a[array[i]]++;
        }
        for(int i =0;i<n;i++) {
            System.out.println(i+":"+a[i]);
        }
    }

    public static void main(String[] args) throws Exception {
        double probs[] = {0.2, 0.0, 0.2, 0.5, 0.1};
        int[] x = getRandomNumbers(10000, probs);
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        printArrayStats(x,probs.length);
    }
}
