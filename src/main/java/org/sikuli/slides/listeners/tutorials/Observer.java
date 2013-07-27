/**
Khalid
*/
package org.sikuli.slides.listeners.tutorials;

import org.sikuli.slides.utils.Constants.NavigationStatus;

public interface Observer {
	/*
	 * Update the observers when the observable changes
	 * @param navigationStatus  tutorial mode navigation status.
	 */
	public void update(NavigationStatus navigationStatus);
}
