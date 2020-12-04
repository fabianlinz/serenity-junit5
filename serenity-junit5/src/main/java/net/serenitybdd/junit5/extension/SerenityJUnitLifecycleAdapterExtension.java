package net.serenitybdd.junit5.extension;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.thucydides.core.annotations.Manual;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestTag;
import net.thucydides.core.steps.StepEventBus;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;
import org.opentest4j.TestAbortedException;

import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static net.thucydides.core.steps.StepEventBus.getEventBus;
import static net.thucydides.core.steps.TestSourceType.TEST_SOURCE_JUNIT;

// Junit4: net.thucydides.junit.listeners.JUnitStepListener
public class SerenityJUnitLifecycleAdapterExtension implements BeforeEachCallback, AfterAllCallback, TestWatcher, LifecycleMethodExecutionExceptionHandler {

    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        notifyTestStarted(extensionContext, extensionContext.getDisplayName().replace("()", ""));

        final Method testMethod = extensionContext.getRequiredTestMethod();
        if (testMethod.isAnnotationPresent(Pending.class) && !testMethod.isAnnotationPresent(Manual.class)) {
            throw new PendingException();
        }
    }

    public void afterAll(final ExtensionContext extensionContext) {
        StepEventBus.getEventBus().testSuiteFinished();
    }

    @Override
    // net.thucydides.junit.listeners.JUnitStepListener.notifyTestFinished => Junit4: Called when an atomic test has finished, whether the test succeeds or fails.
    // not using AfterTestExecutionCallback.afterTestExecution as it is called before org.junit.jupiter.api.extension.TestWatcher.testFailed or org.junit.jupiter.api.extension.TestWatcher.testAborted
    public void testSuccessful(final ExtensionContext extensionContext) {
        notifyTestFinished();
    }

    @Override
    // Junit4: net.serenitybdd.junit.runners.SerenityRunner.processTestMethodAnnotationsFor
    public void testDisabled(ExtensionContext extensionContext, Optional<String> reason) {
        beforeEach(extensionContext);
        getEventBus().testIgnored();
        notifyTestFinished();
    }

    @Override
    public void testAborted(final ExtensionContext extensionContext, final Throwable cause) {
        if (cause instanceof ManualTestAbortedException) {
            // do nothing... eventBus registration was already done by net.serenitybdd.junit5.extension.SerenityManualExtension
        } else if (cause instanceof PendingException) {
            getEventBus().testPending();
        } else {
            getEventBus().assumptionViolated(cause.getMessage());
        }
        notifyTestFinished();
    }

    @Override
    public void testFailed(final ExtensionContext extensionContext, final Throwable cause) {
        getEventBus().testFailed(cause);
        notifyTestFinished();
    }

    @Override
    public void handleBeforeAllMethodExecutionException(final ExtensionContext extensionContext, final Throwable throwable) throws Throwable {
        handleTestClassLevelLifecycleFailure(extensionContext, throwable, "Initialization");
    }

    @Override
    public void handleAfterAllMethodExecutionException(final ExtensionContext extensionContext, final Throwable throwable) throws Throwable {
        handleTestClassLevelLifecycleFailure(extensionContext, throwable, "Tear down");
    }

    private void notifyTestStarted(final ExtensionContext extensionContext, final String name) {
        startTestSuiteForFirstTest(extensionContext);
        getEventBus().clear();
        getEventBus().setTestSource(TEST_SOURCE_JUNIT.getValue());
        getEventBus().testStarted(name, extensionContext.getRequiredTestClass());
        getEventBus().addTagsToCurrentTest(extensionContext.getTags().stream().map(TestTag::withValue).collect(toList()));
    }

    private void startTestSuiteForFirstTest(ExtensionContext extensionContext) {
        if (!getEventBus().testSuiteHasStarted()) {
            getEventBus().testSuiteStarted(extensionContext.getRequiredTestClass());
        }
    }

    private void handleTestClassLevelLifecycleFailure(final ExtensionContext extensionContext, final Throwable throwable, final String scenario) throws Throwable {
        notifyTestStarted(extensionContext, scenario);
        testFailed(extensionContext, throwable);
        throw throwable;
    }


    private void notifyTestFinished() {
        getEventBus().testFinished();
    }

    private static class PendingException extends TestAbortedException {

    }

    @RequiredArgsConstructor
    @ToString
    static class ManualTestAbortedException extends TestAbortedException {

        private final TestResult annotatedResult;
    }
}
