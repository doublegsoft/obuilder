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
package net.doublegsoft.obuilder.search;

import java.util.Objects;

/**
 * It's a type to encapulates the data of maven dependency.
 * 
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.0
 */
public class MavenDependency {
    
    /**
     * The group id
     */
    private String groupId;
    
    /**
     * The artifact id
     */
    private String artifactId;
    
    /**
     * The version
     */
    private String version;
    
    /**
     * The packaging type
     */
    private String packaging;
    
    /**
     * Gets the group id.
     * 
     * @return the group id
     */
    public String getGroupId() {
        return groupId;
    }
    
    /**
     * Sets the group id.
     * 
     * @param groupId 
     *          the group id
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    /**
     * Gets the artifact id.
     * 
     * @return the artifact id
     */
    public String getArtifactId() {
        return artifactId;
    }
    
    /**
     * Sets the artifact id.
     * 
     * @param artifactId
     *          the artifact id
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    
    /**
     * Gets the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Sets the version.
     * 
     * @param version 
     *          the version
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * Gets the packaging type.
     * 
     * @return the packaging type 
     */
    public String getPackaging() {
        return packaging;
    }
    
    /**
     * Sets the packaging type.
     * 
     * @param packaging 
     *          the packaging type
     */
    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
    
    /**
     * @see Object#toString() 
     */
    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder();
        retVal.append("<dependency>");
        retVal.append("<groupId>").append(getGroupId()).append("</groupId>");
        retVal.append("<artifacdtId>").append(getArtifactId()).append("</artifactId>");
        retVal.append("<version>").append(getVersion()).append("</version>");
        retVal.append("</denpendency>");
        return retVal.toString();
    }
    
    /**
     * @see Object#hashCode() 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }
    
    /**
     * @see Object#equals(java.lang.Object) 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MavenDependency other = (MavenDependency) obj;
        if (!Objects.equals(this.groupId, other.groupId)) {
            return false;
        }
        if (!Objects.equals(this.artifactId, other.artifactId)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }
    
}
