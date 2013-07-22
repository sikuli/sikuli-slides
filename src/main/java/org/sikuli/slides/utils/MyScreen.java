/**
Khalid
*/
package org.sikuli.slides.utils;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class MyScreen {
	
	public static Dimension getScreenDimensions(){
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	public static Rectangle getScreenBounds(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		return defaultScreen.getDefaultConfiguration().getBounds();
	}
	
	/**
	 * Returns the total width of the connected screens.
	 * @param currentScreen the screen id for the current screen to get the total width for.
	 * @return the total width of the connected screens.
	 */
	public static int getTotalScreenWidth(int currentScreen){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice [] devices = ge.getScreenDevices();
		int baseWidth = 0;
		for(int i=0; i<currentScreen; i++){
			baseWidth += devices[i].getDisplayMode().getWidth();
		}
		return baseWidth;
	}
}
