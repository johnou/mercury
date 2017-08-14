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

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Johno Crawford (johno@sulake.com)
 */
public class ScriptPostFunctionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {
    
    private static final String ERROR_MESSAGE_FIELD_NAME = "errorMessage";
    private static final String SCRIPT_FIELD_NAME = "scriptFileName";
    
    private final LicenseManagerHelper licenseManagerHelper;
    
    public ScriptPostFunctionFactory(@ComponentImport PluginLicenseManager licenseManager) {
        this.licenseManagerHelper = new LicenseManagerHelper(licenseManager);
    }
    
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        velocityParams.put(SCRIPT_FIELD_NAME, "");
    }

    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForView(velocityParams, descriptor);
    }

    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor abstractDescriptor) {
        if ((abstractDescriptor instanceof FunctionDescriptor)) {
            FunctionDescriptor descriptor = (FunctionDescriptor) abstractDescriptor;
            String fileName = (String) descriptor.getArgs().get(SCRIPT_FIELD_NAME);
            velocityParams.put(SCRIPT_FIELD_NAME, fileName);
            
            String licenseError = licenseManagerHelper.getLicenseError();
            if (licenseError != null) {
                velocityParams.put(ERROR_MESSAGE_FIELD_NAME, licenseError);
                return;
            }
    
            File file = new File(fileName);
            if ((!file.canRead()) || (!file.isFile())) {
                velocityParams.put(ERROR_MESSAGE_FIELD_NAME, "Check script path and permissions.");
            }
        } else {
            throw new IllegalArgumentException("Descriptor not supported.");
        }
    }

    public Map<String, ?> getDescriptorParams(Map<String, Object> conditionParams) {
        Map<String, Object> params = new HashMap<String, Object>();
        String fileName = extractSingleParam(conditionParams, SCRIPT_FIELD_NAME);
        params.put(SCRIPT_FIELD_NAME, fileName);
        return params;
    }
}
