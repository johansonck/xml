package be.sonck.xml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import be.sonck.xml.XmlElement;
import be.sonck.xml.XmlElementHandler;
import be.sonck.xml.XmlElementToObjectConverter;
import be.sonck.xml.XmlHandlerListener;

public class XmlElementHandlerTest extends TestCase {
	
	private int elementId;

	public void test() throws Exception {
		String inputXml = "<root>" +
				"<person-list>" +
					"<person id='1' person-id='10'>" +
						"<name><first>Johan</first><last>Sonck</last></name>" +
						"<birth>20-10-1972</birth>" +
					"</person>" +
					"<person id='2' person-id='20'>" +
						"<name><first>Sara</first><last>Burm</last></name>" +
						"<birth>05-11-1975</birth>" +
					"</person>" +
				"</person-list>" +
				"<camera-list>" +
					"<camera><brand>Canon</brand><model>Ixus 55</model></camera>" +
					"<camera><brand>Nikon</brand><model>D60</model></camera>" +
				"</camera-list>" +
				"</root>";
		
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		List<String> topElementList = new ArrayList<String>(2);
		topElementList.add("person");
		topElementList.add("camera");
		
		XmlHandlerListener<XmlElement> listener = new XmlHandlerListener<XmlElement>() {
			@Override
			public void start() {}
			
			@Override
			public void end() {}
			
			@Override
			public void newElement(XmlElement element) {
				checkElement(element);
			}
		};
		
		XmlElementToObjectConverter<XmlElement> converter = new XmlElementToObjectConverter<XmlElement>() {
			@Override
			public XmlElement convert(XmlElement element) {
				return element;
			}
		};
		
		XmlElementHandler<XmlElement> handler = new XmlElementHandler<XmlElement>(topElementList, listener, converter);
		xmlReader.setContentHandler(handler);
		xmlReader.setErrorHandler(handler);
		xmlReader.parse(new InputSource(new StringReader(inputXml)));
	}
	
	private void checkElement(XmlElement xmlElement) {
		switch (++this.elementId) {
			case 1:
				checkFirstElement(xmlElement);
				break;
			case 2:
				checkSecondElement(xmlElement);
				break;
			case 3:
				checkThirdElement(xmlElement);
				break;
			case 4:
				checkFourthElement(xmlElement);
				break;
			default:
				fail("more than 4 elements created");
		}
	}
	
	private void checkFirstElement(XmlElement xmlElement) {
		assertEquals("tag name", "person", xmlElement.getTag());
		
		Map<String, String> attributes = xmlElement.getAttributes();
		assertEquals("attribute count", 2, attributes.size());
		assertTrue("attributes doesn't contain id", attributes.containsKey("id"));
		assertEquals("id", "1", attributes.get("id"));
		assertTrue("attributes doesn't contain person-id", attributes.containsKey("person-id"));
		assertEquals("person-id", "10", attributes.get("person-id"));
		
		List<XmlElement> children = xmlElement.getChildren();
		assertEquals("child count", 2, children.size());
		
		XmlElement child = children.get(1);
		assertEquals("second child's attributes", 0, child.getAttributes().size());
		assertEquals("second child's children", 0, child.getChildren().size());
		assertEquals("second child's tag", "birth", child.getTag());
		assertEquals("second child's value", "20-10-1972", child.getValue());
		
		child = children.get(0);
		assertEquals("first child's attributes", 0, child.getAttributes().size());
		assertEquals("first child's children", 2, child.getChildren().size());
		assertEquals("first child's tag", "name", child.getTag());
		
		children = child.getChildren();
		
		child = children.get(0);
		assertEquals("first child's attributes", 0, child.getAttributes().size());
		assertEquals("first child's children", 0, child.getChildren().size());
		assertEquals("first child's tag", "first", child.getTag());
		assertEquals("first child's value", "Johan", child.getValue());

		child = children.get(1);
		assertEquals("first child's attributes", 0, child.getAttributes().size());
		assertEquals("first child's children", 0, child.getChildren().size());
		assertEquals("first child's tag", "last", child.getTag());
		assertEquals("first child's value", "Sonck", child.getValue());
	}
	
	private void checkSecondElement(XmlElement xmlElement) {
		assertEquals("tag name", "person", xmlElement.getTag());
		
		Map<String, String> attributes = xmlElement.getAttributes();
		assertEquals("attribute count", 2, attributes.size());
		assertTrue("attributes doesn't contain id", attributes.containsKey("id"));
		assertEquals("id", "2", attributes.get("id"));
		assertTrue("attributes doesn't contain person-id", attributes.containsKey("person-id"));
		assertEquals("person-id", "20", attributes.get("person-id"));
		
		List<XmlElement> children = xmlElement.getChildren();
		assertEquals("child count", 2, children.size());
		
		XmlElement child = children.get(1);
		assertEquals("second child's attributes", 0, child.getAttributes().size());
		assertEquals("second child's children", 0, child.getChildren().size());
		assertEquals("second child's tag", "birth", child.getTag());
		assertEquals("second child's value", "05-11-1975", child.getValue());
		
		child = children.get(0);
		assertEquals("first child's attributes", 0, child.getAttributes().size());
		assertEquals("first child's children", 2, child.getChildren().size());
		assertEquals("first child's tag", "name", child.getTag());
		
		children = child.getChildren();
		
		child = children.get(0);
		assertEquals("first child's attributes", 0, child.getAttributes().size());
		assertEquals("first child's children", 0, child.getChildren().size());
		assertEquals("first child's tag", "first", child.getTag());
		assertEquals("first child's value", "Sara", child.getValue());
		
		child = children.get(1);
		assertEquals("first child's attributes", 0, child.getAttributes().size());
		assertEquals("first child's children", 0, child.getChildren().size());
		assertEquals("first child's tag", "last", child.getTag());
		assertEquals("first child's value", "Burm", child.getValue());
	}
	
	private void checkThirdElement(XmlElement xmlElement) {
		assertEquals("tag name", "camera", xmlElement.getTag());
		
		Map<String, String> attributes = xmlElement.getAttributes();
		assertEquals("attribute count", 0, attributes.size());
		
		List<XmlElement> children = xmlElement.getChildren();
		assertEquals("child count", 2, children.size());
		
		XmlElement child = children.get(0);
		assertEquals("first child's attributes", 0, child.getAttributes().size());
		assertEquals("first child's children", 0, child.getChildren().size());
		assertEquals("first child's tag", "brand", child.getTag());
		assertEquals("first child's value", "Canon", child.getValue());
		
		child = children.get(1);
		assertEquals("second child's attributes", 0, child.getAttributes().size());
		assertEquals("second child's children", 0, child.getChildren().size());
		assertEquals("second child's tag", "model", child.getTag());
		assertEquals("second child's value", "Ixus 55", child.getValue());
	}
	
	private void checkFourthElement(XmlElement xmlElement) {
		assertEquals("tag name", "camera", xmlElement.getTag());
		
		Map<String, String> attributes = xmlElement.getAttributes();
		assertEquals("attribute count", 0, attributes.size());
		
		List<XmlElement> children = xmlElement.getChildren();
		assertEquals("child count", 2, children.size());
		
		XmlElement child = children.get(0);
		assertEquals("first child's attributes", 0, child.getAttributes().size());
		assertEquals("first child's children", 0, child.getChildren().size());
		assertEquals("first child's tag", "brand", child.getTag());
		assertEquals("first child's value", "Nikon", child.getValue());
		
		child = children.get(1);
		assertEquals("second child's attributes", 0, child.getAttributes().size());
		assertEquals("second child's children", 0, child.getChildren().size());
		assertEquals("second child's tag", "model", child.getTag());
		assertEquals("second child's value", "D60", child.getValue());
	}
}
