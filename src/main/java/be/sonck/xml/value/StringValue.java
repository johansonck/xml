package be.sonck.xml.value;

import lombok.Value;

import static org.apache.commons.lang3.StringUtils.replaceAll;

@Value
public class StringValue extends XmlValue {

    private String value;

    public String toString() {
        String temporary = value;

        temporary = replaceAll(temporary, "&", "&amp;");
        temporary = replaceAll(temporary, ">", "&gt;");
        temporary = replaceAll(temporary, "<", "&lt;");
        temporary = replaceAll(temporary, "\"", "&quot;");
        temporary = replaceAll(temporary, "'", "&#39;");
        temporary = replaceAll(temporary, "¬", "&#172;");
        //temporary = replace(temporary, "", "&#128;");
        //temporary = replace(temporary, "\u20AC", "&#8364;");

        return temporary;
    }
}
