package be.sonck.xml.value;

import java.util.Objects;

/**
 * Created by johansonck on 16/03/2017.
 */
public abstract class XmlValue {

    public static XmlValue NON_BLANK_SPACE = new XmlValue() {
        @Override
        public String toString() {
            return "&nbsp;";
        }
    };

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(toString(), o.toString());
    }
}
