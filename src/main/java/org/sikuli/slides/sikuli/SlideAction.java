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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
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
import org.sikuli.slides.utils.Constants.DesktopEvent;
import org.sikuli.slides.utils.MyScreen;
import org.sikuli.slides.utils.UnitConverter;
import org.sikuli.slides.utils.UserPreferencesEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Slide action that performs input events operation specified in each slide.
 * @author Khalid
 *
 */
public class SlideAction {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SlideAction.class);
	private UserPreferencesEditor prefsEditor = new UserPreferencesEditor();
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
		
		// if the required action is one of the following actions, no need to search for target on the screen
		// #1 Wait action
		if(desktopEvent==DesktopEvent.WAIT){
			performNonSikuliAction(null);
			performSikuliAction(null, desktopEvent);
		}
		// #2 Open default browser
		else if(desktopEvent==DesktopEvent.LAUNCH_BROWSER){
			performNonSikuliAction(null);
			performSikuliAction(null, desktopEvent);
		}

		// if the action is to find a target on the screen
		// if the action is to interact with a target, find the target and perform the action
		else{
			ScreenRegion targetRegion=findTargetRegion();
			
			performNonSikuliAction(targetRegion);
			
			if(desktopEvent==DesktopEvent.EXIST){
				logger.info("Checking whether the target image is visible on the screen.");
				if(targetRegion==null){
					logger.error("Test failed. Target image was not found on the screen.");
					System.exit(1);
				}
				else{
					logger.info("Test passed. Target image was found on the screen.");
					return;
				}
			}
			else if(desktopEvent==DesktopEvent.NOT_EXIST){
				logger.info("Checking whether the target image is invisible on the screen.");
				if(targetRegion==null){
					logger.info("Test passed. Target image was invisible on the screen.");
					return;
				}
				else{
					logger.error("Test failed. Target image was visible on the screen.");
					System.exit(1);
				}
			}
			else{
				/*if(targetRegion==null){
					if(Constants.TUTORIAL_MODE){
						logger.error("Couldn't find target on the screen.");
					}
				}*/
				if(Constants.HELP_MODE){
					new SlideTutorial(targetRegion, slideShape, desktopEvent).performTutorialSlideAction();
				}
				else if(Constants.TUTORIAL_MODE){
					new SlideTutorial(targetRegion, slideShape, desktopEvent).performTutorialSlideAction();
				}
				else{
					performSikuliAction(targetRegion, desktopEvent);
				}
			}
		}
	}
	

	private ScreenRegion findTargetRegion(){
		final ImageTarget imageTarget=new ImageTarget(targetFile);
		imageTarget.setMinScore(Constants.MinScore);
		if(imageTarget!=null){
			ScreenRegion fullScreenRegion=SikuliController.getFullScreenRegion();
	    	ScreenRegion targetRegion=fullScreenRegion.wait(imageTarget, prefsEditor.getMaxWaitTime());
	    	
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
	    				logger.error("Failed to determine the target image among multiple similar targets on the screen."
	    						+"\nTry to resize the shape in slide number "+slideTargetRegion.getslideNumber() + " or use the precision option to make the search more accurate.");
	    			}
	    		}
	    		else{
	    			return targetRegion;
	    		}
	    	}
			else{
				logger.error("Failed to find target on the screen. Slide no. "+slideTargetRegion.getslideNumber());
			}
		}
		return null;
	}
	
	/**
	 * Performs non sikuli actions such as playing background sound and displaying annotations
	 * @param targetRegion
	 */
	
	private void performNonSikuliAction(ScreenRegion targetRegion){
		// if the slide contains a sound, play it first
		if(sound!=null){
			sound.playSound();
		}
		
		if(slideLabel!=null){
			displayLabel(targetRegion);
		}
	}
	
	private void performSikuliAction(ScreenRegion targetRegion,Constants.DesktopEvent desktopEvent){
		
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
		else if(desktopEvent==Constants.DesktopEvent.WAIT){
			performWaitAction();
		}
	}
	/**
	 * display a label on a target region
	 * @param targetRegion the target region to display the label on
	 */
	private void displayLabel(ScreenRegion targetRegion) {
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
		if(fontSize==0){
			fontSize=18;
		}
		int x=(dimension.width-width)/2;
		int y=(dimension.height-height)/2;
		ScreenRegion canvasRegion=new DesktopScreenRegion(x, y, width, height);
		canvas.addLabel(canvasRegion, slideLabel.getText()).
			withColor(Color.black).withFontSize((int)fontSize).withLineWidth(prefsEditor.getCanvasWidthSize());
		canvas.display(prefsEditor.getLabelDisplayTime());
	}

	/**
	 * perform left click
	 * @param targetRegion the region to perform left click input event on.
	 */
	private void performLeftClick(ScreenRegion targetRegion){
		logger.info("performing left click event on target...");
		Mouse mouse = new DesktopMouse();
		SikuliController.displayBox(targetRegion);
		mouse.click(targetRegion.getCenter());
	}
	
	/**
	 * perform right click
	 * @param targetRegion the region to perform right click input event on.
	 */
	private void performRightClick(ScreenRegion targetRegion){
		logger.info("performing right click event on target...");
		Mouse mouse = new DesktopMouse();
		SikuliController.displayBox(targetRegion);
		mouse.rightClick(targetRegion.getCenter());
	}
	/**
	 * perform double click
	 * @param targetRegion the region to perform double click input event on.
	 */
	private void performDoubleClick(ScreenRegion targetRegion){
		logger.info("performing double click event on target...");
		Mouse mouse = new DesktopMouse();
		SikuliController.displayBox(targetRegion);
		mouse.doubleClick(targetRegion.getCenter());
	}
	
	/**
	 * perform drag and drop
	 * @param targetRegion the region to perform drag or drop input event on.
	 */
	private void performDragDrop(ScreenRegion targetRegion){
		logger.info("performing drag and drop event on targets...");
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
			logger.error("Couldn't find the start and end of the straight arrow connector " +
					"that is used to connect the rounded rectangles. Make sure the arrow is connected to the two rounded rectangles.");
		}
	}
	
	/**
	 * perform keyboard typing
	 * @param targetRegion the region to perform keyboard typing input event on.
	 */
	private void performKeyboardTyping(ScreenRegion targetRegion){
		logger.info("performing keyboard typing event on target...");
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
		logger.info("launching the default browser...");
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
			logger.error("The text body of the Cloud shape doesn't contain a valid URL.");
			System.exit(1);
		}
	}
	
	/**
	 * Perform wait action. It waits for the specified time in seconds
	 */
	private void performWaitAction(){
		logger.info("Performing wait operation...");
		// extract the time unit
		TimeUnit timeUnit=UnitConverter.extractTimeUnitFromString(slideShape.getText());
		// if the time unit was not specified, default to seconds
		if(timeUnit==null){
			timeUnit=TimeUnit.SECONDS;
		}
		// extract the wait time string value, replace all non digits with blank
		String waitTimeString=slideShape.getText().replaceAll("\\D", "");
		if(waitTimeString==null){
			logger.error("Error: Please enter the wait time value in a shape."
					+" Valid examples include: 10 seconds, 10 minutes, 10 hours, or even 2 days.");
			return;
		}
		try {
			long timeout=Long.parseLong(waitTimeString);
			if(Constants.TUTORIAL_MODE){
				// Disable controllers UI buttons to prevent tutorial mode from navigating through steps.
				Constants.IsWaitAction=true;
			}
			// display a label
			Dimension dimension=MyScreen.getScreenDimensions();
			ScreenRegion canvasRegion=new DesktopScreenRegion(0, dimension.height-200,50,200);
			Canvas canvas=new DesktopCanvas();
			String readyTime=getReadyDate(timeUnit, timeout);
			String waitMessage="Please wait for "+timeout+" "+timeUnit.toString().toLowerCase()+"...."+
					"This might end at "+readyTime;
			logger.info(waitMessage);
			
			canvas.addLabel(canvasRegion, waitMessage).withFontSize(prefsEditor.getInstructionHintFontSize());
			canvas.show();
			
			timeUnit.sleep(timeout);
			Constants.IsWaitAction=false;
			
			canvas.hide();
			logger.info("Waking up...");
		} 
		catch(NumberFormatException e){
			logger.error("Error: Invalid wait time.");
		}
		catch (InterruptedException e) {
			logger.error("Error in wait operation");
		}
		
	}

	private String getReadyDate(TimeUnit timeUnit,long timeout) {
		if(timeUnit != null){
			// set timeout format
			String dateFormat="HH:mm:ss";
			if(timeUnit == TimeUnit.DAYS){
				dateFormat="yyyy-MM-dd HH:mm:ss";
			}
			
			// get the end time in milli seconds
			long waitTimeInMilliSeconds=timeUnit.toMillis(timeout);
			logger.info("Wait time in milli seconds: "+waitTimeInMilliSeconds);
			
			Calendar nowCalendar=Calendar.getInstance();
			Calendar timeoutCalendar = Calendar.getInstance();
			timeoutCalendar.setTimeInMillis(waitTimeInMilliSeconds+nowCalendar.getTimeInMillis());

			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return sdf.format(timeoutCalendar.getTime()).toString();
		}
		return null;
	}
}
