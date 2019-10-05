package net.serenitybdd.junit5;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

@Deprecated // Will be removed once serenity-core and serenity-model don't have a dependency to JUnit4 anymore
public class SerenityNoOpRunner extends BlockJUnit4ClassRunner {

    public SerenityNoOpRunner(Class<?> klass) throws InitializationError {
        super(TestClass.class);
    }

    public static class TestClass {

        @Ignore
        @Test
        public void ignoreJunit4Dummy() {

        }

    }
}
