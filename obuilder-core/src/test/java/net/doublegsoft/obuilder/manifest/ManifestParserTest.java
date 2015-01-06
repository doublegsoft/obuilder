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
package net.doublegsoft.obuilder.manifest;

import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.junit.Test;

/**
 * It's a test for {@link ManifestParser}.
 * 
 * @author <a href="guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.0
 */
public class ManifestParserTest {
    
    @Test
    public void testActiveMQBundle() throws Exception {
        Manifest m = new JarFile("src/test/resources/bundles/activemq-osgi-5.10.0.jar").getManifest();
        BundleInfo info = ManifestParser.parseManifest(m);
        Set<BundleRequirement> brs = info.getImports();
        brs.forEach((br) -> {
            System.out.println(br.getName() + "(" + br.getVersion() + ")");
        });
    }
    
}
