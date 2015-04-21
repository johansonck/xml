package be.sonck.xml;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

/**
 * This class facilitates the construction of a nested XML document.
 *
 * @author Johan Sonck
 */
public class XmlElement {
    private String tag;
    private String value;
    private Map<String, String> attributes;
    private List<XmlElement> children;
    private XmlElement parent;

    public static final String HEADER = "<?xml version='1.0' encoding='utf-8'?>";

    /**
     * Create an new XmlElement with the given tag.
     * @param tag String
     */
    public XmlElement(String tag) {
        this(null, tag, null);
    }

    /**
     * Create an new XmlElement with the given tag and assign the given value
     * to it.
     * @param tag String
     * @param value String
     */
    public XmlElement(String tag, String value) {
    	this(null, tag, value);
    }

    public XmlElement(XmlElement parent, String tag) {
    	this(parent, tag, null);
    }
    
    public XmlElement(XmlElement parent, String tag, String value) {
    	this.tag = tag;
    	this.value = value;
        this.attributes = new HashMap<String, String>();
        this.children = new ArrayList<XmlElement>();
    	
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Add an attribute to the XmlElement with the given name and value.
     * @param name String
     * @param value String
     * @return A reference to self.
     */
    public XmlElement addAttribute(String name, String value) {
        attributes.put(name, value);

        return this;
    }

    /**
     * Add a child element to this element.
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
		
		for (Iterator<XmlElement> childIter = children.iterator(); childIter.hasNext(); ) {
			XmlElement nextChild = (XmlElement)childIter.next();
			
			printWriter.print(nextChild.prettyPrint(indentation, startIndent + 1, writeNewLine));
		}
	}

	private void writeValue(PrintWriter printWriter) {
		if (getValue() != null) {
            printWriter.print(setXml(getValue()));
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
		if (attributes != null) {
            for (Iterator<String> attrIter = attributes.keySet().iterator(); attrIter.hasNext(); ) {
                String key = (String)attrIter.next();

                if (key != null) {
                    String value = (String)attributes.get(key);

                    printWriter.print(" ");
                    printWriter.print(key);
                    printWriter.print("=\"");
                    printWriter.print(value == null ? "" : value);
                    printWriter.print('"');
                }
            }
        }
	}

    /**
     * Construct a String representation of this XmlElement and its children.
     * @return String
     */
    public String toString() {
        return toString(true);
    }
    
    public String toString(boolean writeNewLine) {
    	return prettyPrint(0, writeNewLine);
    }

	private static String setXml(final String textString) {
		String temporary = textString;
		temporary = replace(temporary, "&", "&amp;");
		temporary = replace(temporary, ">", "&gt;");
		temporary = replace(temporary, "<", "&lt;");
		temporary = replace(temporary, "\"", "&quot;");
		temporary = replace(temporary, "'", "&#39;");
		temporary = replace(temporary, "¬", "&#172;");
		//temporary = replace(temporary, "€", "&#128;");
		//temporary = replace(temporary, "\u20AC", "&#8364;");

		return temporary;
	}
	
	private static String replace(String source, String find, String replace) {

		if (find == null) {
			throw new IllegalArgumentException("replace(...) doesn't accept null as argument.");
		}
		
		String returnvalue = null;
		
		if (source == null) {
			returnvalue = source;
		}
		else {
			StringBuffer buffer = new StringBuffer();
			int findLength = find.length();
	
			int previous = 0;
			int index = source.indexOf(find);
			while (index >= 0) {
				buffer.append(source.substring(previous, index));
				buffer.append(replace);
				previous = index + findLength;
				index = source.indexOf(find, previous);
			}
			buffer.append(source.substring(previous));
	
			returnvalue = buffer.toString();
		}
		return returnvalue;
	}

    /**
     * Adds the padding chars to the left of the source. If the maxLengthOfString is smaller than
     * the length of the source String no chars are added. The paddingChars String is always added
     * completely (not chopped) until the maxLengthOfString is reached.
     * @param source String
     * @param paddingChars String
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

