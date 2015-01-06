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

import java.util.Locale;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

/**
 * It is to initialize default settings for the application working around netbeans default settings.
 * 
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * 
 * @since 1.o
 */
public class OBuilderInstall extends ModuleInstall {
    
    /**
     * @see ModuleInstall#restored() 
     */
    @Override
    public void restored() {
        // change to defautl locale
        Locale.setDefault(Locale.US);
        // change title to remove timestamp
        WindowManager.getDefault().invokeWhenUIReady(() -> {
            WindowManager.getDefault().getMainWindow().setTitle("OBuider V1.0");
        });
    }
    
}
