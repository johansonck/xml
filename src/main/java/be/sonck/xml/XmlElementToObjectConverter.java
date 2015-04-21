package be.sonck.xml;

public interface XmlElementToObjectConverter<T> {
	
	public T convert(XmlElement element);
}
