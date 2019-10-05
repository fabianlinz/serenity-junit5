package net.serenitybdd.junit5.extension.testsupport;

import net.thucydides.core.model.TestOutcome;

import java.util.List;

/**
 * Utility class for providing access to the TestOutcomes. Thread-safe by storing the required information in a
 * ThreadLocal.
 */
class TestOutcomeAccessor {

    private static final ThreadLocal<List<TestOutcome>> testOutcomesThreadLocal = new ThreadLocal<>();

    static void resetTestOutcomes() {
        testOutcomesThreadLocal.remove();
    }

    static void registerTestOutcomes(final List<TestOutcome> testOutcomes) {
        testOutcomesThreadLocal.set(testOutcomes);
    }

    static List<TestOutcome> getLastTestOutcomes() {
        return testOutcomesThreadLocal.get();
    }

}
