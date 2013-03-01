/**
Khalid
*/
package org.sikuli.slides.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

public class MyScreen {
	
	public static Dimension getScreenDimensions(){
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
}
