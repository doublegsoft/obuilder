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

import java.util.Locale;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author ganguo
 */
public class OBuilderInstall extends ModuleInstall {

    @Override
    public void restored() {
        Locale.setDefault(Locale.US);
        System.out.println("locale reset");
    }
    
}
