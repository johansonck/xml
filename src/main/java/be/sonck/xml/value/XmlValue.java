package be.sonck.xml.value;

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
}
