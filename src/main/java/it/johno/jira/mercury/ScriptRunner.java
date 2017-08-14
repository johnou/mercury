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

import it.johno.jira.mercury.osgi.OSGiScriptEngineManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Johno Crawford (johno@sulake.com)
 */
class ScriptRunner {

    private final Logger logger = LogManager.getLogger(ScriptRunner.class);
    
    void run(BundleContext context, File scriptFile, Map<String, Object> bindVars) throws Exception {
        if (scriptFile == null) {
            throw new NullPointerException("scriptFile");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Run invoked with args: " + scriptFile + ", " + bindVars);
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        ScriptEngine scriptEngine = getScriptEngine(context, scriptFile);
        
        scriptEngine.put("log", this.logger);
        scriptEngine.put("logger", this.logger);
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(scriptFile), "UTF-8"));
            for (Map.Entry<String, Object> entry : bindVars.entrySet()) {
                String fieldId = entry.getKey();
                scriptEngine.put(fieldId, entry.getValue());
            }
            Object value = scriptEngine.eval(reader);
            
            Bindings engineScope = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
            for (String fieldId : result.keySet()) {
                result.put(fieldId, engineScope.get(fieldId));
            }
        } catch (Exception e) {
            this.logger.warn("The script failed : " + e.toString());
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    private ScriptEngine getScriptEngine(BundleContext context, File scriptFile) throws Exception {
        ScriptEngineManager engineManager = new OSGiScriptEngineManager(context);
        String fileName = scriptFile.getName();
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
        ScriptEngine scriptEngine = engineManager.getEngineByExtension(ext);
    
        if (scriptEngine == null) {
            throw new Exception("Failed to locate script engine for extension: \"" + ext + "\", (make sure it's on the classpath).");
        }
        return scriptEngine;
    }
}
