/**
 * 
 */
package be.sonck.xml;


public interface XmlHandlerListener<T> {
	
	/**
	 * This method is called whenever an 
	 */
	public void start();
	public void end();
	public void newElement(T element);
}