package starter.math;

import net.serenitybdd.junit5.SerenityTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SerenityTest
class PureJunit5Test {

    @Test
    void throwingExceptionFailure() {
        // When
        throw new RuntimeException("should fail the test");
    }

    @Test
    void abortedTest() {
        assumeTrue("abc".contains("Z"), "abc does not contain Z");
        // aborted ...
    }

    @Test
    void successfulTest() {
        assumeTrue(true);
    }

}
