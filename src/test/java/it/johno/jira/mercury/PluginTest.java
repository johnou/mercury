package it.johno.jira.mercury;

import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.atlassian.sal.api.ApplicationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class PluginTest {

    private final ApplicationProperties applicationProperties;

    public PluginTest(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Test
    public void testHuuhaa() {
        assertEquals("huuhaa", "huuhaa");
    }
}