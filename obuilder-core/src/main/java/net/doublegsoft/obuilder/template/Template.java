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
package net.doublegsoft.obuilder.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.stringtemplate.v4.ST;

/**
 * Outputs pom.xml.
 * 
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.0
 */
public final class Template {

    private final String tplstr;
    
    /**
     * Default constructor.
     * 
     * @throws IOException 
     *          in case of any IO errors
     */
    public Template() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream in = Template.class.getClassLoader().getResourceAsStream("tpl/pom.xml.st")) {
            byte[] buf = new byte[4096];
            int len = 0;

            while ((len = in.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }
        }
        tplstr = sb.toString();
    }

    /**
     * Outputs the pom.xml to the destination directory. And if the destination directory is not existing, will create
     * it.
     *
     * @param dir the destination directory
     * 
     * @param data the date applied in template
     * 
     * @return the generating pom.xml
     * 
     * @throws IOException
     *          in case of any IO errors
     */
    public File output(File dir, Map<String, Object> data) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File pom = new File(dir, "pom.xml");
        if (!pom.exists()) {
            pom.createNewFile();
        }

        ST st = new ST(tplstr, '$', '$');
        data.forEach((k, v) -> {
            st.add(k, v);
        });
        try (FileOutputStream out = new FileOutputStream(pom)) {
            out.write(st.render().getBytes());
            out.flush();
        }
        return pom;
    }
}
