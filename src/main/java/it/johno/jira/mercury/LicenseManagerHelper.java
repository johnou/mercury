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

import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.PluginLicense;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author Johno Crawford (johno@sulake.com)
 */
class LicenseManagerHelper {
    
    private static final Logger logger = LogManager.getLogger(ScriptPostFunction.class);
    
    private final PluginLicenseManager licenseManager;
    
    LicenseManagerHelper(PluginLicenseManager licenseManager) {
        this.licenseManager = licenseManager;
    }
    
    boolean hasValidLicense() {
        if (licenseManager.getLicense().isDefined()) {
            PluginLicense license = licenseManager.getLicense().get();
            if (!license.getError().isDefined()) {
                return true;
            }
        }
        logger.warn(getLicenseError());
        return false;
    }
    
    private static final String ADD_ON_NAME = "Mercury for JIRA";
    
    String getLicenseError() {
        if (licenseManager.getLicense().isDefined()) {
            PluginLicense license = licenseManager.getLicense().get();
            if (license.getError().isDefined()) {
                switch (license.getError().get()) {
                    case EXPIRED:
                    case TYPE_MISMATCH:
                        return String.format("Your evaluation license of %s expired. Please use the 'Buy' button to purchase a new license.", ADD_ON_NAME);
                    case USER_MISMATCH:
                        return String.format("Your %s is only licensed for %s users. Please verify you have the correct license installed and try again.", ADD_ON_NAME, license.getEdition().get());
                    case EDITION_MISMATCH:
                        //return "Your ADD_ON_NAME is only licensed for LIMIT_VALUE remote agents. Your ATLASSIAN_APPLICATION installation requires a license for LIMIT remote agents. Please get a ADD_ON_NAME license for LIMIT_VALUE users and try again.";
                    case VERSION_MISMATCH:
                        //return "Your license for maintenance of ADD_ON_NAME is not valid for version X.Y.Z. Please use the 'Renew' button to renew your ADD_ON_NAME license.";
                    case ROLE_EXCEEDED:
                    case ROLE_UNDEFINED:
                        default:
                            return String.format("Your %s license has expired. To continue using this plugin, please buy / renew your license.", ADD_ON_NAME);
    
                }
            }
            return null;
        }
        return String.format("Your %s is unlicensed. To continue using this plugin, please buy / renew your license.", ADD_ON_NAME);
    }
}
