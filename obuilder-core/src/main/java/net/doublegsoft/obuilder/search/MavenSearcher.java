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
package net.doublegsoft.obuilder.search;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import net.doublegsoft.obuilder.manifest.VersionRange;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * It gets data from <a href="http://search.maven.org">Maven Central Repository</a> via HTTP REST.
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 *
 * @since 1.0
 */
public class MavenSearcher {

    /**
     * Finds the matching bundle or jar in line with package name and version range.
     *
     * @param importPackage import package name
     *
     * @param versionRange version range
     *
     * @return the bundle info or null
     *
     * @throws Exception in case of any errors
     */
    public MavenDependency search(String importPackage, VersionRange versionRange) throws Exception {
        HttpResponse<JsonNode> resp = Unirest.get("http://search.maven.org/solrsearch/select")
            .header("accept", "application/json")
            .queryString("q", "fc:" + importPackage)
            .queryString("rows", 100)
            .asJson();
        JSONObject response = resp.getBody().getObject().getJSONObject("response");
        if (response == null) {
            return null;
        }
        MavenDependency retVal = new MavenDependency();
        JSONArray docs = response.getJSONArray("docs");
        for (int i = 0; i < docs.length(); i++) {
            JSONObject doc = docs.getJSONObject(i);
            String group = doc.getString("g");
            String artifact = doc.getString("a");
            String version = doc.getString("v");
            String packaging = doc.getString("p");
            try {
                if ("bundle".equals(packaging) || "jar".equals(packaging)) {
                    retVal.setGroupId(group);
                    retVal.setArtifactId(artifact);
                    retVal.setVersion(version);
                    if (versionRange == null) {
                        return retVal;
                    } else {
                        if (versionRange.contains(version)) {
                            return retVal;
                        }
                    }
                }
            } catch (RuntimeException ex) {
                // nothing to do
            }
        }
        return null;
    }

}
