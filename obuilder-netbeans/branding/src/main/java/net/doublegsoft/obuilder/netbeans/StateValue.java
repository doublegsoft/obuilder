/*
 * Copyright 2015 ganguo.
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
 *
 * @author ganguo
 */
public class StateValue {
    
    public static final int PROCESSING = 0;
    
    public static final int SUCCESS = 1;
    
    public static final int FAILURE = 2;
    
    public static final int SKIP = 3;
    
    private String bundle;
    
    private int state = -1;

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    
}
