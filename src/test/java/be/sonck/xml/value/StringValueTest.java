package be.sonck.xml.value;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by johansonck on 16/03/2017.
 */
public class StringValueTest {

    @Test
    public void test() {
        String value = new StringValue("blah & > < \" ' ¬ blah").toString();
        assertThat(value).isEqualTo("blah &amp; &gt; &lt; &quot; &#39; &#172; blah");
    }
}