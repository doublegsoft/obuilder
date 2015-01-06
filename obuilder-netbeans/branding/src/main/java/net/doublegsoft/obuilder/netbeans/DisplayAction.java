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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

/**
 * It's a default action when clicking OBuilder icon in the UI.
 * 
 * @author <a href="guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.0
 */
@ActionID(
    category = "File",
    id = "net.doublegsoft.obuilder.netbeans.DisplayAction")
@ActionRegistration(
    iconBase = "images/obuilder-24.png",
    displayName = "OBuilder")
@ActionReferences({
    @ActionReference(path = "Toolbars/OBuilderToolbar", position = 2)
})
public class DisplayAction extends AbstractAction {
    
    /**
     * default constructor.
     */
    public DisplayAction() {
        putValue("iconBase", "images/obuilder-32.png");
        putValue(NAME, "OBuilder");
    }
    
    /**
     * @see AbstractAction#actionPerformed(java.awt.event.ActionEvent)
     * 
     * @param e 
     *          action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        OBuilderTopComponent comp = new OBuilderTopComponent();
        comp.open();
        comp.requestActive();
    }

}
