package net.serenitybdd.junit5.extension.testsupport;

import net.serenitybdd.core.environment.WebDriverConfiguredEnvironment;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.MockEnvironmentVariables;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Static helper to be used in integrationtests for the extensions.
 */
public class TestClassExecutionHelper {

    private TestClassExecutionHelper() {
    }

    public static List<TestOutcome> runTestClass(final Class<?> clazz, final EnvironmentConfigurationOverride... environmentConfigurationOverrides) {
        // we need to do that in a separate thread in order to not wreak havoc with the threadlocals that
        // serenity is using
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final EnvironmentVariables mockEnvironmentVariable = MockEnvironmentVariables.fromSystemEnvironment();
        Stream.of(environmentConfigurationOverrides).forEach(it -> mockEnvironmentVariable.setProperty(it.getKey(), it.getValue()));
        Future<List<TestOutcome>> future = executorService.submit(new TestClassExecutionCallable(clazz, mockEnvironmentVariable));
        executorService.shutdown();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Exception during junit-jupiter engine execution!", e);
        }
    }

    private static class TestClassExecutionCallable implements Callable<List<TestOutcome>> {

        private final Class<?> clazz;
        private final EnvironmentVariables mockEnvironmentVariables;

        TestClassExecutionCallable(final Class<?> clazz, final EnvironmentVariables mockEnvironmentVariables) {
            this.clazz = clazz;
            this.mockEnvironmentVariables = mockEnvironmentVariables;
        }

        @Override
        public List<TestOutcome> call() {
            return executeTestClass();
        }

        private List<TestOutcome> executeTestClass() {
            WebDriverConfiguredEnvironment.setTestEnvironmentVariables(mockEnvironmentVariables);
            EngineTestKit
                    .engine("junit-jupiter")
                    .selectors(DiscoverySelectors.selectClass(clazz))
                    .execute();
            return TestOutcomeAccessor.getLastTestOutcomes();
        }

    }

    public static class EnvironmentConfigurationOverride {
        private final String key;
        private final String value;

        public EnvironmentConfigurationOverride(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}