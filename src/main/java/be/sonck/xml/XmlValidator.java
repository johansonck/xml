package be.sonck.xml;

import java.io.IOException;
import java.io.Reader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlValidator {

	private class ErrorHandler extends DefaultHandler {
		private boolean validationError = false;
		private SAXParseException saxParseException = null;

		public void error(SAXParseException exception) throws SAXException {
			validationError = true;
			saxParseException = exception;
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			validationError = true;
			saxParseException = exception;
		}

		public void warning(SAXParseException exception) throws SAXException {
		}

		public boolean isValidationError() {
			return validationError;
		}

		public SAXParseException getSaxParseException() {
			return saxParseException;
		}
	}


	public void validateXml(Reader xmlReader, Reader xsdReader) {
		try {
			ErrorHandler errorHandler = new ErrorHandler();

			Validator validator = getSchema(xsdReader).newValidator();
			validator.setErrorHandler(errorHandler);
			validator.validate(new StreamSource(xmlReader));

			if (errorHandler.getSaxParseException() != null) throw errorHandler.getSaxParseException();
			
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Schema getSchema(Reader xsdReader) throws SAXException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		return factory.newSchema(new StreamSource(xsdReader));
	}
}
