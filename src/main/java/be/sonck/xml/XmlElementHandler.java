package be.sonck.xml;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates XmlElement objects based on the parsed data.
 * @author JoSo
 */
public class XmlElementHandler<T> extends DefaultHandler {

	private Logger logger = Logger.getLogger(XmlElementHandler.class);
	private List<String> topElementsToParse;
	private XmlElement currentTopElement;
	private XmlElement currentElement;
	
	private XmlHandlerListener<T> listener;
	private XmlElementToObjectConverter<T> converter;

	/**
	 * For each name in the topElementsToParse argument, when the parser encounters an element with that name,
	 * an {@link XmlElement} will be created that holds all content in that element.
	 * @param topElementsToParse the names of all elements for which an {@link XmlElement} should be created 
	 */
	public XmlElementHandler(List<String> topElementsToParse, XmlHandlerListener<T> listener, 
			XmlElementToObjectConverter<T> converter) {
		
		this.topElementsToParse = new ArrayList<String>(topElementsToParse);
		this.listener = listener;
		this.converter = converter;
	}

	@Override
	public final void characters(char[] ch, int start, int length) throws SAXException {
		String value = String.valueOf(Arrays.copyOfRange(ch, start, start + length)).trim();
		logger.debug("characters(" + value + ")");
		
		if (currentElement != null && value.length() > 0) {
			currentElement.setValue(value);
		}
	}
	
	@Override
	public final void endDocument() throws SAXException {
		listener.end();
	}

	@Override
	public final void startDocument() throws SAXException {
		listener.start();
	}

	@Override
	public final void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		logger.debug("startElement(" + localName + ")");
		
		if (currentTopElement != null) {
			// An element is currently being parsed.  Create a child for the current element
			// and attach it.
			XmlElement newElement = new XmlElement(currentElement, localName);
			currentElement = newElement;
			
		} else if (topElementsToParse.contains(localName)) {
			// A requested top element was encountered.  Create a new top element.
			currentTopElement = new XmlElement(localName);
			currentElement = currentTopElement;
		}
		
		if (currentElement != null && attributes != null) {
			int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				String attributeName = attributes.getLocalName(i);
				String attributeValue = attributes.getValue(i);
				
				currentElement.setAttribute(attributeName, attributeValue);
			}
		}
	}

	@Override
	public final void endElement(String uri, String localName, String name) throws SAXException {
		logger.debug("endElement(" + localName + ")");
		
		// No requested element is being parsed.  Nothing to do.
		if (currentElement == null) return;
		
		if (currentElement == currentTopElement) {
			// The end of the currently processing top element has been reached.  Notify the subclass
			// that a new XmlElement has been created and remove the currently processing element.
			newTopElement(currentTopElement);
			currentElement = null;
			currentTopElement = null;
			
		} else {
			currentElement = currentElement.getParent();
		}
	}
	
	/**
	 * This method is invoked whenever the handler has created an {@link XmlElement} matching the criteria
	 * provided in the constructor.
	 * @param element the new created {@link XmlElement}
	 */
	public final void newTopElement(XmlElement element) {
		listener.newElement(converter.convert(element));
	}
}
