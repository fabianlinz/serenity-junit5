package net.serenitybdd.junit5;

import org.junit.runner.RunWith;

@RunWith(SerenityNoOpRunner.class) // because junit4 dependency to serenity-core: net.thucydides.core.annotations.TestCaseAnnotations.isASerenityTestCase
public abstract class AbstractSerenityTestDetectionWorkaround {

}
