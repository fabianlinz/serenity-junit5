package net.serenitybdd.junit5.extension.page;

import net.thucydides.core.annotations.TestCaseAnnotations;
import net.thucydides.core.webdriver.Configuration;

public class TestConfiguration {

    private final Class<?> testClass;
    private final Configuration configuration;

    private TestConfiguration(Class<?> testClass, Configuration configuration) {
        this.testClass = testClass;
        this.configuration = configuration;
    }

    public static TestConfigurationBuilder forClass(Class<?> testClass) {
        return new TestConfigurationBuilder(testClass);
    }

    public boolean shouldClearTheBrowserSession() {
        return (isAWebTest() && TestCaseAnnotations.shouldClearCookiesBeforeEachTestIn(testClass));
    }


    public boolean isAWebTest() {
        return TestCaseAnnotations.isWebTest(testClass);
    }

    public static class TestConfigurationBuilder {

        private final Class<?> testClass;

        public TestConfigurationBuilder(Class<?> testClass) {
            this.testClass = testClass;
        }

        public TestConfiguration withSystemConfiguration(Configuration configuration) {
            return new TestConfiguration(testClass, configuration);
        }
    }


}
