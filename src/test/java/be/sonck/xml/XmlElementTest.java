package be.sonck.xml;

import be.sonck.xml.value.StringValue;
import junit.framework.TestCase;

import java.io.PrintWriter;
import java.io.StringWriter;

public class XmlElementTest extends TestCase {

	public void test1() {
		XmlElement xmlElement = new XmlElement("johan");
		
		assertNotNull("getAttributes()", xmlElement.getAttributes());
		assertTrue("getAttributes() is not empty", xmlElement.getAttributes().size() == 0);
		assertNotNull("getChildren()", xmlElement.getChildren());
		assertTrue("getChildren() is not empty", xmlElement.getChildren().size() == 0);
		assertNull("getParent()", xmlElement.getParent());
		assertEquals("getTag()", "johan", xmlElement.getTag());
		assertNull("getValue()", xmlElement.getValue());
	}
	
	public void test2() {
		XmlElement xmlElement = new XmlElement("johan", "sonck");
		
		assertNotNull("getAttributes()", xmlElement.getAttributes());
		assertTrue("getAttributes() is not empty", xmlElement.getAttributes().size() == 0);
		assertNotNull("getChildren()", xmlElement.getChildren());
		assertTrue("getChildren() is not empty", xmlElement.getChildren().size() == 0);
		assertNull("getParent()", xmlElement.getParent());
		assertEquals("getTag()", "johan", xmlElement.getTag());
		
		assertEquals("getValue()", xmlElement.getValue(), new StringValue("sonck"));
	}
	
	public void test3() {
		XmlElement xmlElement = new XmlElement("johan", "& sara");
		
		assertNotNull("getAttributes()", xmlElement.getAttributes());
		assertTrue("getAttributes() is not empty", xmlElement.getAttributes().size() == 0);
		assertNotNull("getChildren()", xmlElement.getChildren());
		assertTrue("getChildren() is not empty", xmlElement.getChildren().size() == 0);
		assertNull("getParent()", xmlElement.getParent());
		assertEquals("getTag()", "johan", xmlElement.getTag());
		
		assertEquals("getValue()", xmlElement.getValue(), new StringValue("& sara"));
	}
	
	public void test4() {
		XmlElement xmlElement = new XmlElement("johan", "sonck");
		
		xmlElement.addAttribute("straat", "Reetsesteenweg");
		xmlElement.addAttribute("huisnummer", "198");
		
		assertNotNull("getChildren()", xmlElement.getChildren());
		assertTrue("getChildren() is not empty", xmlElement.getChildren().size() == 0);
		assertNull("getParent()", xmlElement.getParent());
		assertEquals("getTag()", "johan", xmlElement.getTag());
		assertEquals("getValue()", xmlElement.getValue(), new StringValue("sonck"));
		
		assertNotNull("getAttributes()", xmlElement.getAttributes());
		assertEquals("getAttributes()", 2, xmlElement.getAttributes().size());
		
		assertTrue("attribute 'straat' is missing", xmlElement.getAttributes().containsKey("straat"));
		assertTrue("attribute 'huisnummer' is missing", xmlElement.getAttributes().containsKey("huisnummer"));
		
		assertEquals("attribute 'straat'", "Reetsesteenweg", xmlElement.getAttributes().get("straat"));
		assertEquals("attribute 'huisnummer'", "198", xmlElement.getAttributes().get("huisnummer"));
	}
	
	public void test5() {
		XmlElement parent = new XmlElement("parent");
		XmlElement xmlElement = new XmlElement(parent, "johan", "sonck");
		XmlElement sibling = new XmlElement(parent, "sara", "burm");
		
		xmlElement.addAttribute("straat", "Reetsesteenweg");
		xmlElement.addAttribute("huisnummer", "198");
		
		assertNotNull("getChildren()", xmlElement.getChildren());
		assertTrue("getChildren() is not empty", xmlElement.getChildren().size() == 0);
		assertNotNull("getParent()", xmlElement.getParent());
		assertTrue("getParent() is invalid", xmlElement.getParent() == parent);
		assertEquals("getTag()", "johan", xmlElement.getTag());
		assertEquals("getValue()", xmlElement.getValue(), new StringValue("sonck"));
		assertNotNull("getAttributes()", xmlElement.getAttributes());
		assertEquals("getAttributes()", 2, xmlElement.getAttributes().size());
		assertTrue("attribute 'straat' is missing", xmlElement.getAttributes().containsKey("straat"));
		assertTrue("attribute 'huisnummer' is missing", xmlElement.getAttributes().containsKey("huisnummer"));
		assertEquals("attribute 'straat'", "Reetsesteenweg", xmlElement.getAttributes().get("straat"));
		assertEquals("attribute 'huisnummer'", "198", xmlElement.getAttributes().get("huisnummer"));
		
		assertNotNull("getParent()", sibling.getParent());
		assertTrue("getParent() is invalid", sibling.getParent() == parent);
		
		assertNotNull("parent.getChildren()", parent.getChildren());
		assertEquals("parent.getChildren()", 2, parent.getChildren().size());
		
		assertTrue("parent.getChildren() doesn't contain 'johan'", parent.getChildren().contains(xmlElement));
		assertTrue("parent.getChildren() doesn't contain 'sara'", parent.getChildren().contains(sibling));
	}
	
	public void testNewLineFalse() {
		XmlElement parent = new XmlElement("parent");
		new XmlElement(parent, "johan", "sonck");
		new XmlElement(parent, "sara", "burm");
		
		String expected = "<parent><johan>sonck</johan><sara>burm</sara></parent>";
		
		assertEquals(expected, parent.toString(false));
	}
	
	public void testNewLineTrue() {
		XmlElement parent = new XmlElement("parent");
		new XmlElement(parent, "johan", "sonck");
		new XmlElement(parent, "sara", "burm");
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		printWriter.println("<parent>");
		printWriter.println("<johan>sonck</johan>");
		printWriter.println("<sara>burm</sara>");
		printWriter.println("</parent>");
		
		String expected = stringWriter.toString();
		
		assertEquals(expected, parent.toString());
	}
	
	public void testAddChild() {
		XmlElement parent = new XmlElement("parent");
		
		XmlElement xmlElement = new XmlElement("johan", "sonck");
		parent.addChild(xmlElement);
		
		XmlElement sibling = new XmlElement("sara", "burm");
		parent.addChild(sibling);
		
		xmlElement.addAttribute("straat", "Reetsesteenweg");
		xmlElement.addAttribute("huisnummer", "198");
		
		assertNotNull("getChildren()", xmlElement.getChildren());
		assertTrue("getChildren() is not empty", xmlElement.getChildren().size() == 0);
		assertNotNull("getParent()", xmlElement.getParent());
		assertTrue("getParent() is invalid", xmlElement.getParent() == parent);
		assertEquals("getTag()", "johan", xmlElement.getTag());
		assertEquals("getValue()", xmlElement.getValue(), new StringValue("sonck"));
		assertNotNull("getAttributes()", xmlElement.getAttributes());
		assertEquals("getAttributes()", 2, xmlElement.getAttributes().size());
		assertTrue("attribute 'straat' is missing", xmlElement.getAttributes().containsKey("straat"));
		assertTrue("attribute 'huisnummer' is missing", xmlElement.getAttributes().containsKey("huisnummer"));
		assertEquals("attribute 'straat'", "Reetsesteenweg", xmlElement.getAttributes().get("straat"));
		assertEquals("attribute 'huisnummer'", "198", xmlElement.getAttributes().get("huisnummer"));
		
		assertNotNull("getParent()", sibling.getParent());
		assertTrue("getParent() is invalid", sibling.getParent() == parent);
		
		assertNotNull("parent.getChildren()", parent.getChildren());
		assertEquals("parent.getChildren()", 2, parent.getChildren().size());
		
		assertTrue("parent.getChildren() doesn't contain 'johan'", parent.getChildren().contains(xmlElement));
		assertTrue("parent.getChildren() doesn't contain 'sara'", parent.getChildren().contains(sibling));
	}
}
