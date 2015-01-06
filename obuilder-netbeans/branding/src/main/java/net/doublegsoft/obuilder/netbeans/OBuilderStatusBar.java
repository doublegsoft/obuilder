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

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 * It's customized status bar to display processing progress.
 * 
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.0
 */
@ServiceProvider(service=StatusLineElementProvider.class)
public class OBuilderStatusBar implements StatusLineElementProvider {
    
    /**
     * The real component to display on status bar.
     */
    public static final JLabel STATUS = new JLabel();
    
    static {
        STATUS.setHorizontalTextPosition(SwingConstants.LEFT);
    }
    
    /**
     * @see StatusLineElementProvider#getStatusLineElement() 
     */
    @Override
    public Component getStatusLineElement() {
        return STATUS;
    }
    
    /**
     * Clears the content.
     */
    public static void clear() {
        STATUS.setIcon(null);
        STATUS.setText(null);
    }
    
}
