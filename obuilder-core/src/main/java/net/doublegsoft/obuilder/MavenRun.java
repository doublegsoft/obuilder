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
package net.doublegsoft.obuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Using maven framework to build bundle.
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 *
 * @since 1.0
 */
public final class MavenRun {

    public static final String MVN;

    static {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            MVN = "mvn.bat";
        } else {
            MVN = "mvn";
        }
    }

    public int run(String mavenHome, File pom) throws IOException, InterruptedException {
        Runtime r = Runtime.getRuntime();
        String cmd = mavenHome + "/bin/" + MVN + " clean package -f " + pom.getAbsolutePath();
        Process p = r.exec(cmd);
        return p.waitFor();
    }

    public int run(String mavenHome, File pom, OutputCallback callback) throws IOException, InterruptedException {
        Runtime r = Runtime.getRuntime();
        String cmd = mavenHome + "/bin/" + MVN + " clean package -f " + pom.getAbsolutePath();
        callback.info(cmd);
        Process p = r.exec(cmd);
        
        // io
        BufferedReader input = new BufferedReader(new InputStreamReader(
            p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            callback.info(line);
        }
        
        // err
        input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((line = input.readLine()) != null) {
            callback.info(line);
        }
        return p.waitFor();
    }
    
    public static interface OutputCallback {
        void info(String str);
    }

}
