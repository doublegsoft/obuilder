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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openide.util.NbPreferences;

/**
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 */
public class OBuilderOptionsPane extends JPanel {
    
    private final OBuilderOptionsPaneController controller;
    
    private JTextField maven;
    
    public OBuilderOptionsPane(OBuilderOptionsPaneController controller) {
        this.controller = controller;
        initialize();
    }
    
    
    
    void load() {
        getMavenTextField().setText(NbPreferences.forModule(OBuilderOptionsPane.class).get("maven.home", ""));
    }
    
    void store() {
        NbPreferences.forModule(OBuilderOptionsPane.class).put("maven.home", getMavenTextField().getText());
    }
    
    boolean valid() {
        return true;
    }
    
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
    
    private JTextField getMavenTextField() {
        if (maven == null) {
            maven = new JTextField();
            maven.setPreferredSize(new Dimension(400, 28));
        }
        return maven;
    }
}
