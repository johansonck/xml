package be.sonck.xml;

import be.sonck.xml.value.StringValue;
import be.sonck.xml.value.XmlValue;
import org.apache.commons.collections.CollectionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * This class facilitates the construction of a nested XML document.
 *
 * @author Johan Sonck
 */
public class XmlElement {
    private String tag;
    private XmlValue xmlValue;
    private Map<String, String> attributes;
    private List<XmlElement> children;
    private XmlElement parent;

    public static final String HEADER = "<?xml version='1.0' encoding='utf-8'?>";

    public XmlElement(String tag) {
        this(null, tag, (XmlValue) null);
    }

    public XmlElement(String tag, String value) {
        this(null, tag, value);
    }

    public XmlElement(String tag, XmlValue xmlValue) {
        this(null, tag, xmlValue);
    }

    public XmlElement(XmlElement parent, String tag) {
        this(parent, tag, (XmlValue) null);
    }

    public XmlElement(XmlElement parent, String tag, String value) {
        this(parent, tag, new StringValue(value));
    }

    public XmlElement(XmlElement parent, String tag, XmlValue xmlValue) {
        this.tag = tag;
        this.xmlValue = xmlValue;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();

        if (parent != null) {
            parent.addChild(this);
        }
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    public List<XmlElement> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public XmlElement getParent() {
        return this.parent;
    }

    public String getTag() {
        return tag;
    }

    public XmlValue getValue() {
        return xmlValue;
    }

    public void setValue(XmlValue xmlValue) {
        this.xmlValue = xmlValue;
    }

    public void setValue(String value) {
        setValue(new StringValue(value));
    }

    /**
     * Add an attribute to the XmlElement with the given name and value.
     *
     * @param name  String
     * @param value String
     * @return A reference to self.
     */
    public XmlElement addAttribute(String name, String value) {
        attributes.put(name, value);

        return this;
    }

    /**
     * Add a child element to this element.
     *
     * @param child XmlElement
     * @return A reference to self.
     */
    public XmlElement addChild(XmlElement child) {
        if (child.getParent() != null) {
            throw new IllegalArgumentException("the child already has a parent (" + child.getParent().getTag() + ")");
        }

        child.parent = this;
        children.add(child);

        return this;
    }

    public String prettyPrint(int indentation) {
        return prettyPrint(indentation, true);
    }

    public String prettyPrint(int indentation, boolean writeNewLine) {
        return prettyPrint(indentation, 0, writeNewLine);
    }

    private String prettyPrint(int indentation, int startIndent, boolean writeNewLine) {
        int currentIndent = indentation * startIndent;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        writeBeginTag(currentIndent, writeNewLine, printWriter);
        writeValue(printWriter);
        writeChildren(indentation, startIndent, writeNewLine, printWriter);
        writeEndTag(currentIndent, writeNewLine, printWriter);

        return stringWriter.toString();
    }

    private void writeChildren(int indentation, int startIndent, boolean writeNewLine, PrintWriter printWriter) {
        if (CollectionUtils.isEmpty(children)) return;

        if (writeNewLine) printWriter.println();

        for (XmlElement nextChild : children) {
            printWriter.print(nextChild.prettyPrint(indentation, startIndent + 1, writeNewLine));
        }
    }

    private void writeValue(PrintWriter printWriter) {
        if (getValue() != null) {
            printWriter.print(xmlValue.toString());
        }
    }

    private void writeEndTag(int currentIndent, boolean writeNewLine, PrintWriter printWriter) {
        if (getValue() == null && CollectionUtils.isEmpty(children)) return;

        if (!CollectionUtils.isEmpty(children)) {
            printWriter.print(padLeft("", " ", currentIndent));
        }

        printWriter.print("</");
        printWriter.print(getTag());
        printWriter.print(">");

        if (writeNewLine) printWriter.println();
    }

    private void writeBeginTag(int currentIndent, boolean writeNewLine, PrintWriter printWriter) {
        printWriter.print(padLeft("", " ", currentIndent));
        printWriter.print("<");
        printWriter.print(getTag());

        writeAttributes(printWriter);

        if (getValue() == null && CollectionUtils.isEmpty(children)) {
            printWriter.print("/>");
            if (writeNewLine) printWriter.println();
        } else {
            printWriter.print(">");
        }
    }

    private void writeAttributes(PrintWriter printWriter) {
        if (attributes == null) return;

        for (String key : attributes.keySet()) {
            if (key == null) continue;

            String value = attributes.get(key);

            printWriter.print(" ");
            printWriter.print(key);
            printWriter.print("=\"");
            printWriter.print(value == null ? "" : value);
            printWriter.print('"');
        }
    }

    /**
     * Construct a String representation of this XmlElement and its children.
     *
     * @return String
     */
    public String toString() {
        return toString(true);
    }

    public String toString(boolean writeNewLine) {
        return prettyPrint(0, writeNewLine);
    }

    /**
     * Adds the padding chars to the left of the source. If the maxLengthOfString is smaller than
     * the length of the source String no chars are added. The paddingChars String is always added
     * completely (not chopped) until the maxLengthOfString is reached.
     *
     * @param source            String
     * @param paddingChars      String
     * @param maxLengthOfString int
     * @return String
     */
    public static String padLeft(String source, String paddingChars, int maxLengthOfString) {
        StringBuffer buffer = new StringBuffer(source == null ? "" : source);
        while (buffer.length() + paddingChars.length() <= maxLengthOfString) {
            buffer.insert(0, paddingChars);
        }
        return buffer.toString();

    }
}

