/*
 *   Copyright 2017 Johnathan Crawford
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package it.johno.jira.mercury;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Johno Crawford (johno@sulake.com)
 */
@Scanned
public class ScriptPostFunction implements FunctionProvider {

    private static final Logger logger = LogManager.getLogger(ScriptPostFunction.class);
    
    private final BundleContext bundleContext;
    private final PluginLicenseManager licenseManager;
    
    public ScriptPostFunction(BundleContext bundleContext, @ComponentImport PluginLicenseManager licenseManager) {
        this.bundleContext = bundleContext;
        this.licenseManager = licenseManager;
    }

    @SuppressWarnings("unchecked")
    public void execute(Map transientVars, Map args, PropertySet propertySet) throws WorkflowException {
        if (!new LicenseManagerHelper(licenseManager).hasValidLicense()) {
            return;
        }

        ScriptRunner scriptRunner = new ScriptRunner();

        Map<String, Object> bindVars = new HashMap<String, Object>();
        bindVars.put("issue", transientVars.get("issue"));
        bindVars.put("project", transientVars.get("project"));
        bindVars.put("transientVars", transientVars);
        bindVars.putAll(args);

        String scriptFileName = (String) args.get("scriptFileName");

        try {
            scriptRunner.run(bundleContext, new File(scriptFileName), bindVars);
        } catch (Exception e) {
            logger.error("Failed executing script " + scriptFileName, e);
        }
    }
    
}
