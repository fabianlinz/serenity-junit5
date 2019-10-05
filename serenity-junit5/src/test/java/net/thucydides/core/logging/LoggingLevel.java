package net.thucydides.core.logging;

/**
 * Duplicated from serenity-model to override/shadow the original implementation with the purpose of having an
 * additional "OFF" enum instance that really silences the log output.
 * <p>
 * TODO: find another solution, maybe change original class in serenity-model.
 */
public enum LoggingLevel {
    /**
     * Intended to suppress all logging completely.
     */
    OFF,
    /**
     * No Thucydides logging at all... Except that ConsoleLoggingListener is logging test failures in this level as well
     * ;-(
     */
    QUIET,
    /**
     * Log the start and end of tests.
     */
    NORMAL,
    /**
     * Log the start and end of tests and test steps.
     */
    VERBOSE
}
