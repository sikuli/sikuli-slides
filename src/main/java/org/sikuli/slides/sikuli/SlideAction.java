/**
Khalid
*/
package org.sikuli.slides.sikuli;

import static org.sikuli.api.API.browse;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.core.SlideComponent;
import org.sikuli.slides.media.Sound;
import org.sikuli.slides.screenshots.SlideTargetRegion;
import org.sikuli.slides.shapes.SlideShape;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.UnitConverter;
import org.sikuli.slides.utils.Constants.DesktopEvent;
import org.sikuli.slides.utils.MyScreen;

/**
 * Slide action that performs input events operation specified in each slide.
 * @author Khalid
 *
 */
public class SlideAction {
	private File targetFile; 
	private SlideShape slideShape;
	private SlideTargetRegion slideTargetRegion;
	private Sound sound;
	private SlideShape slideLabel;
	
	public SlideAction(SlideComponent slideComponent){
		this.targetFile=slideComponent.getTargetFile();
		this.slideShape=slideComponent.getSlideShape();
		this.slideTargetRegion=slideComponent.getSlideTargetRegion();
		this.sound=slideComponent.getSound();
		this.slideLabel=slideComponent.getSlideLabel();
	}
	
	public void doSlideAction(DesktopEvent desktopEvent){
		// if the required action is to open the browser, no need to search for target on the screen
		if(desktopEvent==DesktopEvent.LAUNCH_BROWSER){
			performSikuliAction(null, desktopEvent);
		}
		// if the action is to find a target on the screen
		// if the action is to interact with a target, find the target and perform the action
		else{
			ScreenRegion targetRegion=findTargetRegion();
			if(desktopEvent==DesktopEvent.FIND){
				if(targetRegion==null){
					System.err.println("Failed. Couldn't find target on the screen");
					System.exit(1);
				}
				else{
					System.out.println("Pass. Target image was found on the screen");
					return;
				}
			}
			else{
				if(targetRegion==null){
					System.exit(1);
				}
				performSikuliAction(targetRegion, desktopEvent);
			}
		}
	}
	
	private ScreenRegion findTargetRegion(){
		final ImageTarget imageTarget=new ImageTarget(targetFile);
		imageTarget.setMinScore(Constants.MinScore);
		if(imageTarget!=null){
			ScreenRegion fullScreenRegion=SikuliController.getFullScreenRegion();
	    	ScreenRegion targetRegion=fullScreenRegion.wait(imageTarget, Constants.MaxWaitTime);
	    	
	    	if(targetRegion!=null){
	    		// check if there are more than one occurrence of the target image.
	    		SearchMultipleTarget searchMultipleTarget=new SearchMultipleTarget();
	    		if(searchMultipleTarget.hasMultipleOccurance(imageTarget)){
	    			ScreenRegion newScreenRegion=searchMultipleTarget.findNewScreenRegion(slideTargetRegion, imageTarget);
	    			if(newScreenRegion!=null){
	    				ScreenRegion newtargetRegion=newScreenRegion.find(imageTarget);
	    				return newtargetRegion;
	    			}
	    			else{
	    				System.err.println("Failed to determine the target image among multiple similar targets on the screen."
	    						+"\nTry to resize the shape in slide "+slideTargetRegion.getslideNumber()+".");
	    			}
	    		}
	    		else{
	    			return targetRegion;
	    		}
	    	}
			else{
				System.err.println("Failed to find target on the screen. Slide no. "+slideTargetRegion.getslideNumber());
			}
		}
		return null;
	}
	

	private void performSikuliAction(ScreenRegion targetRegion,Constants.DesktopEvent desktopEvent){
		// if the slide contains a sound, play it first
		if(sound!=null){
			sound.playSound();
		}
		
		if(slideLabel!=null){
			displayToolTip(targetRegion);
		}
		
		if(desktopEvent==Constants.DesktopEvent.LEFT_CLICK){
			performLeftClick(targetRegion);
		}
		else if(desktopEvent==Constants.DesktopEvent.RIGHT_CLICK){
			performRightClick(targetRegion);
		}
		else if(desktopEvent==Constants.DesktopEvent.DOUBLE_CLICK){
			performDoubleClick(targetRegion);
		}
		else if(desktopEvent==Constants.DesktopEvent.DRAG_N_DROP){
			performDragDrop(targetRegion);
		}
		else if(desktopEvent==Constants.DesktopEvent.KEYBOARD_TYPING){
			performKeyboardTyping(targetRegion);
		}
		else if(desktopEvent==Constants.DesktopEvent.LAUNCH_BROWSER){
			performLaunchWebBrowser();
		}
	}
	/**
	 * display tool tip around the target region
	 * @param targetRegion the target region to display tool tip around
	 */
	private void displayToolTip(ScreenRegion targetRegion) {
		/* if the target region is null or ther's no target to work on, 
		 * use the default desktop region.
		   this is important in case of opening the default browser
		*/
		if(targetRegion==null){
			targetRegion=new DesktopScreenRegion();
		}
		Canvas canvas = new DesktopCanvas();
		Dimension dimension=MyScreen.getScreenDimensions();
		// TODO: Fix this
		int width=UnitConverter.emuToPixels(slideLabel.getCx());
		int height=UnitConverter.emuToPixels(slideLabel.getCy());
		double fontSize=UnitConverter.WholePointsToPoints(slideLabel.getTextSize());
		int x=(dimension.width-width)/2;
		int y=(dimension.height-height)/2;
		ScreenRegion canvasRegion=new DesktopScreenRegion(x, y, width, height);
		canvas.addLabel(canvasRegion, slideLabel.getText()).
			withColor(Color.black).withFontSize((int)fontSize);
		canvas.display(Constants.CANVAS_DURATION);
	}

	/**
	 * perform left click
	 * @param targetRegion the region to perform left click input event on.
	 */
	private void performLeftClick(ScreenRegion targetRegion){
		System.out.println("performing left click event on target...");
		Mouse mouse = new DesktopMouse();
		SikuliController.displayBox(targetRegion);
		mouse.click(targetRegion.getCenter());
	}
	
	/**
	 * perform right click
	 * @param targetRegion the region to perform right click input event on.
	 */
	private void performRightClick(ScreenRegion targetRegion){
		System.out.println("performing right click event on target...");
		Mouse mouse = new DesktopMouse();
		SikuliController.displayBox(targetRegion);
		mouse.rightClick(targetRegion.getCenter());
	}
	/**
	 * perform double click
	 * @param targetRegion the region to perform double click input event on.
	 */
	private void performDoubleClick(ScreenRegion targetRegion){
		System.out.println("performing double click event on target...");
		Mouse mouse = new DesktopMouse();
		SikuliController.displayBox(targetRegion);
		mouse.doubleClick(targetRegion.getCenter());
	}
	
	/**
	 * perform drag and drop
	 * @param targetRegion the region to perform drag or drop input event on.
	 */
	private void performDragDrop(ScreenRegion targetRegion){
		System.out.println("performing drag and drop event on targets...");
		Mouse mouse = new DesktopMouse();
		if(slideShape.getOrder()==0){
			SikuliController.displayBox(targetRegion);
			mouse.drag(targetRegion.getCenter());
		}
		else if(slideShape.getOrder()==1){
			SikuliController.displayBox(targetRegion);
			mouse.drop(targetRegion.getCenter());
		}
		else{
			System.err.println("Couldn't find the start and end of the straight arrow connector " +
					"that is used to connect the rounded rectangles. Make sure the arrow is connected to the two rounded rectangles.");
		}
	}
	
	/**
	 * perform keyboard typing
	 * @param targetRegion the region to perform keyboard typing input event on.
	 */
	private void performKeyboardTyping(ScreenRegion targetRegion){
		System.out.println("performing keyboard typing event on target...");
		Mouse mouse = new DesktopMouse();
		Keyboard keyboard=new DesktopKeyboard();
		SikuliController.displayBox(targetRegion);
		mouse.click(targetRegion.getCenter());
		keyboard.type(slideShape.getText());
	}
	
	/**
	 * launch the default browser
	 */
	private void performLaunchWebBrowser(){
		System.out.println("launching the default browser...");
		try {
			String userURL=slideShape.getText();
			URL url=null;
			if(userURL.startsWith("http://")){
				url=new URL(userURL);
			}
			else{
				url=new URL("http://"+userURL);
			}
			browse(url);
		} catch (MalformedURLException e) {
			System.err.println("The text body of the Cloud shape doesn't contain a valid URL.");
			System.exit(1);
		}
	}
	

}
