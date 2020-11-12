package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.junit5.extension.testsupport.QuietSerenityLoggingExtension;
import net.serenitybdd.junit5.extension.testsupport.QuietSerenityReportExtension;
import net.thucydides.core.annotations.Steps;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;

import static org.assertj.core.api.Assertions.fail;

/**
 * This test - which is not itself a {@link SerenityTest} - is a safeguard against regressions that break our serenity
 * extensions in a way that would silence all other possible test failures. All other tests then can make use of the
 * {@link SerenityTest} annotation themselves.
 */
class WhenRunningExtensionTests {

    @Test
    @ExtendWith(QuietSerenityLoggingExtension.class)
    void failures_should_be_possible() {
        // when
        final EngineExecutionResults results = EngineTestKit
                .engine("junit-jupiter")
                .selectors(DiscoverySelectors.selectClass(FailingSerenityExtensionTest.class))
                .execute();

        // then
        results.testEvents().assertStatistics(stats -> stats.failed(3).succeeded(0));
    }

    @SerenityTest
    @Tag("inner-test")
    @ExtendWith(QuietSerenityReportExtension.class)
    static class FailingSerenityExtensionTest {

        @Steps
        JUnit5Steps junit5;

        @Test
        void shouldFailByException() {
            throw new RuntimeException("This is a test failure!");
        }

        @Test
        void shouldFailByAssertion() {
            fail("This is a test failure!");
        }

        @Test
        void shouldFailInStep() {
            junit5.failingStep();
        }

    }

}
