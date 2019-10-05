package net.serenitybdd.junit5.extension;

import net.thucydides.core.model.TestResult;
import net.thucydides.core.steps.BaseStepListener;
import net.thucydides.core.steps.StepEventBus;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.opentest4j.TestAbortedException;

import java.lang.reflect.Method;

import static net.thucydides.core.steps.StepAnnotations.injector;
import static net.thucydides.core.steps.StepEventBus.getEventBus;
import static net.thucydides.core.steps.StepFactory.getFactory;

// Junit4: net.serenitybdd.junit.runners.SerenityStatement
public class SerenityStepExtension implements BeforeEachCallback, InvocationInterceptor {

    @Override
    public void beforeEach(final ExtensionContext context) {
        injector().injectScenarioStepsInto(context.getRequiredTestInstance(), getFactory());
    }

    @Override
    public void interceptTestMethod(final Invocation<Void> invocation,
                                    final ReflectiveInvocationContext<Method> invocationContext,
                                    final ExtensionContext extensionContext) throws Throwable {
        try {
            invocation.proceed();
        } catch (final AssertionError assertionError) {
            // suppress assertion error outside a step method if a previous step method failed.
            // net.thucydides.core.steps.StepInterceptor.executeTestStepMethod handles assertion error inside a step.
            if (noStepInTheCurrentTestHasFailed(extensionContext)) {
                throw assertionError;
            }
        }

        throwStepFailures(extensionContext);
        throwStepAssumptionViolations(extensionContext);
    }

    private boolean noStepInTheCurrentTestHasFailed(final ExtensionContext extensionContext) {
        workaroundUntilStepInterceptorHandlesJunit5TestAbortedExceptionAnalogToJunit4AssumptionViolatedException(extensionContext);
        return !getEventBus().aStepInTheCurrentTestHasFailed();
    }

    private void throwStepFailures(final ExtensionContext extensionContext) throws Throwable {
        workaroundUntilStepInterceptorHandlesJunit5TestAbortedExceptionAnalogToJunit4AssumptionViolatedException(extensionContext);
        final BaseStepListener baseStepListener = baseStepListener();
        if (baseStepListener.aStepHasFailed()) {
            throw baseStepListener.getTestFailureCause().toException();
        }
    }

    private void throwStepAssumptionViolations(final ExtensionContext extensionContext) {
        final StepEventBus eventBus = getEventBus();
        if (eventBus.assumptionViolated()) {
            throw new TestAbortedException(eventBus.getAssumptionViolatedMessage());
        }
    }

    private void workaroundUntilStepInterceptorHandlesJunit5TestAbortedExceptionAnalogToJunit4AssumptionViolatedException(final ExtensionContext context) {
        final BaseStepListener baseStepListener = baseStepListener();
        baseStepListener.latestTestOutcome().ifPresent(testOutcome -> {
                    testOutcome.getTestSteps().stream()
                            .filter(step -> TestResult.ERROR.equals(step.getResult()))
                            .filter(step -> step.getException() != null && step.getException().getOriginalCause() instanceof TestAbortedException)
                            .forEach(step -> step.setResult(TestResult.IGNORED));
                    if (testOutcome.getTestFailureCause() != null && testOutcome.getTestFailureCause().getOriginalCause() instanceof TestAbortedException && TestResult.ERROR.equals(testOutcome.getAnnotatedResult())) {
                        baseStepListener.clearForcedResult();
                        baseStepListener.getEventBus().clearStepFailures();
                    }
                }
        );
    }

    private BaseStepListener baseStepListener() {
        return getEventBus().getBaseStepListener();
    }

}
