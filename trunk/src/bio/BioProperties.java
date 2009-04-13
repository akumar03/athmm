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
package bio;

import java.util.*;

/**
 * This file contains all the properties used in the program
 * @author akumar03
 */
public class BioProperties {

    protected static final ResourceBundle sResourceBundle = ResourceBundle.getBundle("bio.BioProperties");
    protected static Map Cache = new HashMap() {
        public Object put(Object key, Object value) {
            if (value == null) {
                return null;
            }
            return super.put(key, value);
        }
    };
    public final static String getString(String key) {
        String result = null;
        try {
            result = sResourceBundle.getString(key);
        } catch (MissingResourceException mre) {
            mre.printStackTrace();
        }

        return result;
    }
}
