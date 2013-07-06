/**
Khalid
*/
package org.sikuli.slides.sikuli;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.UserPreferencesEditor;

public class SikuliController {
	private static UserPreferencesEditor prefsEditor = new UserPreferencesEditor();
	
	/**
	 * display a box around a screen region
	 * @param screenRegion the region to draw a box around
	 */
	public static void displayBox(ScreenRegion screenRegion){
		Canvas canvas=new DesktopCanvas();
		// Display the canvas around the target
		canvas.addBox(screenRegion);
		canvas.display(prefsEditor.getCanvasDisplayTime());
	}
	
	public static ScreenRegion getFullScreenRegion(){
		ScreenRegion fullScreenRegion=new DesktopScreenRegion();
		fullScreenRegion.setScreen(new DesktopScreen(Constants.ScreenId));
		return fullScreenRegion;
	}
}
