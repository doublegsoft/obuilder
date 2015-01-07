/*
 * Copyright 2015 doublegsoft.net.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.doublegsoft.obuilder.netbeans;

/**
 * The value object is used in the first column of the artifacts table.
 * 
 * @author <a href="guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.0
 */
public class StateValue {
    
    /**
     * Processing value
     */
    public static final int PROCESSING = 0;
    
    /**
     * Success value
     */
    public static final int SUCCESS = 1;
    
    /**
     * Failure value
     */
    public static final int FAILURE = 2;
    
    /**
     * Skip value
     */
    public static final int SKIP = 3;
    
    /**
     * Not found value
     */
    public static final int NOT_FOUND = 4;
    
    /**
     * The generated bundle name
     */
    private String bundle;
    
    /**
     * The state
     */
    private int state = -1;
    
    /**
     * Gets the generated bundle file name.
     * 
     * @return the generated bundle file name.
     */
    public String getBundle() {
        return bundle;
    }
    
    /**
     * Sets the generated bundle file name.
     * 
     * @param bundle
     *          the generated bundle file name
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }
    
    /**
     * Gets the state.
     * 
     * @return the state
     */
    public int getState() {
        return state;
    }
    
    /**
     * Sets the state.
     * 
     * @param state 
     *          the state
     */
    public void setState(int state) {
        this.state = state;
    }
    
}
