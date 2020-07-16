package com.intellective.sample.selector;

import com.vegaecm.vspace.TestUtils;
import com.vegaecm.vspace.config.ConfigHolder;
import com.vegaecm.vspace.config.manager.ConfigManager;
import com.vegaecm.vspace.config.manager.ConfigManagerFactory;
import com.vegaecm.vspace.i18n.I18nDataHolder;
import com.vegaecm.vspace.i18n.Locale;
import com.vegaecm.vspace.settings.UserSettings;
import com.vegaecm.vspace.utils.ApplicationContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;

/**
 * Base class for selector tests
 */
public class BaseSelectorTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected ConfigHolder configHolder;

    @Autowired
    protected WebApplicationContext applicationContext;
    
    @Rule 
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
		TestUtils.initAuthContext();
		ClassPathResource unityConfigurationResource = new ClassPathResource("unity_config.xml");
		ConfigManager configManager = ConfigManagerFactory.getConfigManager(unityConfigurationResource.getURI().toString());
		configHolder = new ConfigHolder(configManager, true);
		
        ApplicationContextHolder.setApplicationContext(applicationContext.getServletContext());
		
        I18nDataHolder.init("vspace");
        UserSettings userSettings = UserSettings.get();
        userSettings.setLocale(new Locale("en_US"));
        UserSettings.set(userSettings);
    }

    @After
    public void tearDown() {
        ApplicationContextHolder.setApplicationContext(null);
    }
}
