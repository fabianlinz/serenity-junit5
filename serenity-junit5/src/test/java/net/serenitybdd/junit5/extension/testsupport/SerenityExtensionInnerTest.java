package net.serenitybdd.junit5.extension.testsupport;

import net.serenitybdd.junit5.SerenityTestWithoutReporting;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A meta-annotation to be used in tests for the serenity JUnit5 extensions that results in all the extensions _except_
 * creation of reports but with an additional means to access the TestOutcomes.
 *
 * Also carries a tag that allows for not picking these tests up as first level tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SerenityTestWithoutReporting
@ExtendWith(TestOutcomeAccessorExtension.class)
@Tag("inner-test")
public @interface SerenityExtensionInnerTest {

}
