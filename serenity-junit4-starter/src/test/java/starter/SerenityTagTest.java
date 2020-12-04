package starter;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.core.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
@WithTag("tagOnClassLevel")
public class SerenityTagTest {

    @Test
    public void noTag() {

    }

    @Test
    @WithTag("tagName1")
    public void tagWithJustAName() {

    }

    @Test
    @WithTag(name = "tagName2", value = "value1ForTag2")
    public void tagWithNameAndValue() {

    }

    @Test
    @WithTags({
            @WithTag("tagName1"),
            @WithTag(name = "tagName2", value = "value1ForTag2"),
            @WithTag(name = "tagName2", value = "value2ForTag2"),
            @WithTag(type = "tagType1", name = "tagName3"),
            @WithTag(type = "tagType1", name = "tagName4", value = "tagValue1ForTag3")
    })
    public void WithTags() {

    }


    @Test
    @WithTagValuesOf({"tagType1:tagName3", "tagType1=tagName5"})
    public void WithTagValuesOf() {

    }

}

