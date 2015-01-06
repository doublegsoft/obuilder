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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openide.util.NbPreferences;

/**
 * It's the options setting for the application.
 * 
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.0
 */
public class OBuilderOptionsPane extends JPanel {
    
    /**
     * maven home text field.
     */
    private JTextField maven;
    
    /**
     * Default constructor.
     */
    public OBuilderOptionsPane() {
        initialize();
    }
    
    /**
     * Invoked when loading this panel.
     */
    void load() {
        getMavenTextField().setText(NbPreferences.forModule(OBuilderOptionsPane.class).get("maven.home", ""));
    }
    
    /**
     * Invoked to store option settings.
     */
    void store() {
        NbPreferences.forModule(OBuilderOptionsPane.class).put("maven.home", getMavenTextField().getText());
    }
    
    /**
     * Checks the option settings is valid.
     * 
     * @return always {@code true}
     */
    boolean valid() {
        return true;
    }
    
    /**
     * Initialize this panel UI.
     */
    private void initialize() {
        setLayout(new GridBagLayout());  
        GridBagConstraints gbc = new GridBagConstraints();  
        gbc.insets = new Insets(2,2,2,2);  
        //gbc.weighty = 1.0;                 // allows vertical dispersion   
        gbc.anchor = GridBagConstraints.EAST;  
        gbc.gridwidth = GridBagConstraints.RELATIVE;  
        add(new JLabel("Maven Home: "), gbc);  
        gbc.anchor = GridBagConstraints.WEST;  
        gbc.gridwidth = GridBagConstraints.REMAINDER;  
        add(getMavenTextField(), gbc);  
    }
    
    /**
     * Gets maven text field lazily.
     * 
     * @return maven home text field
     */
    private JTextField getMavenTextField() {
        if (maven == null) {
            maven = new JTextField();
            maven.setPreferredSize(new Dimension(400, 28));
        }
        return maven;
    }
}
