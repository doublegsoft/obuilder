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
import org.openide.LifecycleManager;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

/**
 * The action to quit the application.
 * 
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.0
 */
@ActionID(
    category = "File",
    id = "net.doublegsoft.obuilder.netbeans.QuitAction")
@ActionRegistration(
    iconBase = "images/quit-24.png",
    displayName = "Quit")
@ActionReferences({
    @ActionReference(path = "Toolbars/OBuilderToolbar", position = 1)
})
public class QuitAction extends AbstractAction {
    
    /**
     * @see  AbstractAction#actionPerformed(java.awt.event.ActionEvent) 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        LifecycleManager.getDefault().exit();
    }
    
}
