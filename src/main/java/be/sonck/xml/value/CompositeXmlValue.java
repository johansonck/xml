package be.sonck.xml.value;

import lombok.Value;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Created by johansonck on 16/03/2017.
 */
@Value
public class CompositeXmlValue extends XmlValue {

    private List<XmlValue> values;

    @Override
    public String toString() {
        return values.stream()
                .map(XmlValue::toString)
                .collect(joining());
    }
}
