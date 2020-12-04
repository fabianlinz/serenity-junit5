![master build status](https://github.com/fabianlinz/serenity-junit5/workflows/Build/badge.svg?branch=master)
[![maven-central](https://img.shields.io/maven-central/v/io.github.fabianlinz/serenity-junit5.svg)](https://search.maven.org/search?q=g:io.github.fabianlinz%20AND%20a:serenity-junit5)

**This is a early version of JUnit5 support for Serenity BDD. Feedback and help are highly appreciated.**

# Writing and running Serenity BDD tests with JUnit5 (Jupiter test engine)
The [Serenity BDD](https://github.com/serenity-bdd/serenity-core) features described for JUnit4 (http://thucydides.info/docs/serenity-staging/#_serenity_with_junit)
should also work for JUnit5 (unless mentioned in the known limitations section below).

Aspects specific to JUnit4 (e.g. packages for the `@Test` annotation or the name of lifecycle annotations) are
different of cause (see also https://junit.org/junit5/docs/current/user-guide/#migrating-from-junit4-tips).

To run a JUnit5 test with Serenity BDD, simply add the annotation `@net.serenitybdd.junit5.SerenityTest` (instead of `@org.junit.runner.RunWith(net.serenitybdd.junit.runners.SerenityRunner.class`) for JUnit4)

Basic example (analog to http://thucydides.info/docs/serenity-staging/#_basic_junit_integration):
```java
@SerenityTest                                                       
public class WhenCalculatingFrequentFlyerPoints {

    @Steps                                                                              
    TravellerSteps travellerSteps;

    @Test
    void shouldCalculatePointsBasedOnDistance() {
        // GIVEN
        travellerSteps.a_traveller_has_a_frequent_flyer_account_with_balance(10000);    

        // WHEN
        travellerSteps.the_traveller_flies(1000);                                       

        // THEN
        travellerSteps.traveller_should_have_a_balance_of(10100);                       

    }
}
```


To get started just include a dependency to [`io.github.fabianlinz:serenity-junit5`](https://search.maven.org/search?q=g:io.github.fabianlinz%20AND%20a:serenity-junit5).

Adding a direct dependency to `serenity-core` allows to easily use newer versions of Serenity. The transitive dependency to JUnit4 should be excluded though:
```
    testImplementation ('io.github.fabianlinz:serenity-junit5:VERSION')
    testImplementation ('net.serenity-bdd:serenity-core:VERSION') { // optional: in case a newer version than the transitively included should be used
        exclude group: 'junit', module: 'junit'
    }
```

# Why JUnit5 support for Serenity BDD?
* Serenity BDD supports different options to define and run tests e.g. Cucumber, JBehave, JUnit.
* While Serenity BDD with JUnit4 style tests can be executed on the JUnit5 platform using the JUnit 5 vintage test engine
(see also https://junit.org/junit5/docs/current/user-guide/#migrating-from-junit4-running) the syntax is different in several aspects.
For teams using JUnit to declare the Serenity scenarios, the difference is sometimes tedious. For example
    * JUnit5 does not require public methods and classes
    * different annotations are used for the lifecycle hooks
    * different extension mechanism: JUnit4 Rule vs. JUnit5 Extension

## Notes on supported features
### Junit5 `@Disabled` vs Serenity `@Pending`
* Junit5 `@Disabled` annotation can be used on *test* and *step* methods
* The following table outlines the difference of using JUnit5 `@Disabled` and Serenity `@Pending` on a step method

    | Consequence of annotated step method for...     | JUnit5 `@Disabled` (same as `@Ignore` in JUnit4)         | Serenity `@Pending`  | 
    | ------------------------------------------------|--------------------------------------------------------------| --------| 
    | test outcome                                    | `ignored` | `pending` |
    | annotated step method                           | skipped (= not executed) | skipped (= not executed) |
    | annotated step method in report                 | `ignored`  | `pending` |
    | step methods after the annotated step           | executed   | skipped (= not executed)|
    | step methods after the annotated step in report | depending on execution e.g. `success`   | `ignored` |

### Tagging
* Serenity and Junit5 provide annotations to tag tests and test classes. 
   * The tags are shown in the Serenity report and can be used to filter the tests to be executed.
      * In contrast to Serenity `@WithTag` the JUnit5 `@Test` annotation is repeatable. So multiple tags can be defined on a method or class without having to use a container annotation (Serenity: `@WithTags`, Junit5 (optional): `@Tags`)
      * Junit5 `@Test` can also be used on meta-annotations. 
* In Serenity tags have a _type_ and a _name_. For the Serenity report a Junit5 `@Tag` value is parsed like a Serenity `@WithTags` value. So it is also possible to define a type using Junit5 `@Tag`.
   * Examples
   
       |Serenity | Junit5 | Result in Serenity report: type | Result in Serenity report: name |
       | ------------------------------------------------|--------------------------------------------------------------| --------------------------------------------------------------| --------------------------------------------------------------|
       | `@WithTag("tagName")`<br><br>`@WithTag(type="tag" name="tagName")`<br><br>`@WithTagValuesOf({"tagName"})` | `@Tag("tagName")`  | `tag` | `tagName` |
       | `@WithTag(name="tagName")`<br><br>`@WithTag("feature:tagName")`<br><br>`@WithTagValuesOf({"feature:tagName"})` | `@Tag("feature:tagName")` | `feature` | `tagName` |
       | `@WithTag("tagType=tagName")`<br><br>`@WithTag("tagType:tagName")`<br><br>`@WithTag(type="tagType" name="tagName")`<br><br>`@WithTagValuesOf({"tagType=tagName"})`<br><br>`@WithTagValuesOf({"tagType:tagName"})` | `@Tag("tagType=tagName")`<br><br>`@Tag("tagType:tagName")` | `tagType` | `tagName` |
 * see also 
   * JUnit5: https://junit.org/junit5/docs/current/user-guide/#writing-tests-tagging-and-filtering
   * Serenity: http://thucydides.info/docs/serenity-staging/#_filtering_test_executing_with_tags
    
# Known limitations/currently not supported features:

## Serenity BDD features
* `@WithTag` and `@WithTagValuesOf` (http://thucydides.info/docs/serenity-staging/#_filtering_test_executing_with_tags)
    * Tags are shown in the report only for methods and declared as `public`. ([#21](https://github.com/fabianlinz/serenity-junit5/issues/21))
    * Filtering of tests not yet possible => works with JUnit5 `@Tag` though ([#22](https://github.com/fabianlinz/serenity-junit5/issues/22))
* `@Title` (http://thucydides.info/docs/serenity-staging/#_human_readable_method_titles)
    * The title is not considered for the Serenity report unless the method is delcared as `public` ([#21](https://github.com/fabianlinz/serenity-junit5/issues/21)) nor as test name from JUnit perspective
    * Consistently with Junit4 the `@Title` annotation does not influence the name in the Junit report.
* Retrying failed tests
    * http://thucydides.info/docs/serenity-staging/#_retrying_failed_tests
* SerenityParameterizedRunner (including Serenity `@Concurrent`)
    * http://thucydides.info/docs/serenity-staging/#_data_driven_tests
    * Overlap to JUnit5 parameterized test support
* Difference for WebDriver configuration on method level (compared to JUnit4)
    * If the test method is annotated with @WithDriver the specified driver is also used for an @Managed field of type WebDriver without an explicit driver type.
 In JUnit4 the @Managed field would be the general default web driver. As this seems arbitrary anyway this difference seems to be acceptable in order to not further complicate the implementation.
* Serenity annotation `@UsePersistantStepLibraries`
    * JUnit4: `SerenityRunner#runChild >> `SerenityRunner#resetStepLibrariesIfRequired() + `net.serenitybdd.junit5.extension.page.TestConfiguration#shouldResetStepLibraries` (be careful with dependency to page stuff)
* Extension point analog to JUnit4 SerenityRunner#additionalBrowserCleanup

## JUnit5 features
* `@Nested`
    * Injection not working (Steps, Page and WebDriver)
    * Story name should probably be a combination of the parent name and the nested test class name
    * see also https://junit.org/junit5/docs/current/user-guide/#writing-tests-nested
* `@DisplayName`
    * on test method level: works = does control the scenario name
    * on test class level: is NOT considered for the story name
        * issue: there is no API to manipulate net.thucydides.core.model.Story.from
        (which is called via `BaseStepListener.testSuiteStarted(java.lang.Class<?>)`) (implicit assumption: JUnit4 is used).
        Would be great if there were an SPI to hook in other strategies for determining the name. Setting the Story directly has the downside
        that the BaseStepListener is unaware of the test class.

## Not yet verified/analysed features  

### Serenity BDD
* `@Issue` and `@Issues`
    * http://thucydides.info/docs/serenity-staging/#_linking_with_issues_for_junit
* Splitting serenity tests to batches
    * http://thucydides.info/docs/serenity-staging/#_splitting_serenity_tests_to_batches

### JUnit5
* @ParameterizedTest
* @Timeout
    * https://junit.org/junit5/docs/current/user-guide/#writing-tests-declarative-timeouts
* Parallel execution
    * https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution
* @RepeatedTest
* @TestFactory
* @TestTemplate

## General notes:
* Should there be an alternative for identifying WebTests?
    * currently a test is a web test if the test has at least one field of type `WebDriver` that is annotated with `@Managed`
    * downside of this approach: test has to declare a field that it does not need
    * options
        * A) Always activate: downside is that it adds some initialization/performance overhead => if is the only downside this might not be a real issue
        * B) Allow alternative strategies to activate web test support (e.g. annotation or naming pattern)
* Is the WebDriver configuration on test class and method level still relevant?
    * configuration options
        * serenity.properties
        * `@Manager` can be used to set dirver name and options on test class level
        * `@WithDriver` + `@DriverOptions` can be used on method level
    * The option to configure on method level
        * is not mentioned in the Serenity doc anymore, only in older documentation
         http://thucydides.info/docs/thucydides-one-page/thucydides.html)
        * in JUnit4: only applies to WebDriver in steps NOT the one injected into the @Manager WebDriver field; WTF
    * Proposal: deprecate `@WithDriver` + `@DriverOptions`
* Is it a common use case to have multiple web driver fields in a test class?
    * What would I expect to be the effective web driver in Steps?
    * At the moment this seems to be arbitrary (depending on which field Java reflection provides as the "first" field)
    * See also comments in `net.serenitybdd.junit5.extension.page.SerenityPageExtension`
* Using `SerenityRunner#methodInvoker` would allow to reduce the code but it would not allow to control the webdriver on test method level (via @WithDriver).
    * Additional complexity accepted in favour of higher consistency with JUnit4 support.
* SerenityStepExtension#interceptTestMethod => Different to JUnit4 in case step assertion failure or exception is not thrown in test method itself but e.g. a @BeforeEach/@AfterEach
* Various experimental features of JUnit5 were used (https://junit.org/junit5/docs/current/user-guide/#api-evolution-experimental-apis)
    * org.junit.jupiter.api.extension.InvocationInterceptor
    * org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler
    * org.junit.jupiter.api.extension.TestWatcher
* Not integrated because currently only used for tests and not necessary for JUnit5
  * net.serenitybdd.junit.runners.SerenityRunner#clearMetadataIfRequired (net.serenitybdd.junit5.extension.page.TestConfiguration#shouldClearMetadata)
net.serenitybdd.core.Serenity#getCurrentSession
* serenity-core and serenity-model versions before `2.0.84` have a runtime dependencies to JUnit4. This was removed by [#1884](https://github.com/serenity-bdd/serenity-core/issues/1858)