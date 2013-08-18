/**
Khalid
*/
package org.sikuli.slides.v1.listeners.tutorials;

public interface Observable {
	
	public void register(Observer observer);
	public void unregister(Observer observer);
	public void notifyObserver();
}
