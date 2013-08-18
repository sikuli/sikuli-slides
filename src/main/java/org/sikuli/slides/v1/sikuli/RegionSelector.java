/**
Khalid
*/
package org.sikuli.slides.v1.sikuli;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.v1.processing.ImageProcessing;
import org.sikuli.slides.v1.screenshots.SlideTargetRegion;
import org.sikuli.slides.v1.utils.Constants;
import org.sikuli.slides.v1.utils.UserPreferencesEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A region selector that finds the screen region of a target when similar targets exist on the screen.
 * @author Khalid
 *
 */
public class RegionSelector {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RegionSelector.class);
	private static UserPreferencesEditor prefsEditor = new UserPreferencesEditor();
	private SlideTargetRegion slideTargetRegion;
	private final int widthFactor=30;
	private final int heightFactor=30;
	private static AtomicInteger counter;
	private final int TOP=0;
	private final int BOTTOM=1;
	private final int RIGHT=2;
	private final int LEFT=3;
	
	public RegionSelector(SlideTargetRegion slideTargetRegion){
		this.slideTargetRegion=slideTargetRegion;
		counter=new AtomicInteger(0);
	}
	/**
	 * Find the screen region that uniquely identifies image target on the screen 
	 * @param imageTarget the target image to search for
	 * @param slideTargetRegion the region of the target image in the presentation slide
	 * @return
	 */
	public ScreenRegion findScreenRegion(ImageTarget imageTarget,SlideTargetRegion slideTargetRegion){
		
		// Search to the right of the target
		logger.info("Searching to the right of the target to identify the target image.");
		ScreenRegion s=doDirectionalSearch(imageTarget, slideTargetRegion, RIGHT);
		if(s!=null){
			return s;
		}
		logger.info("Searching to the top of the target to identify the target image.");
		// search to the top of the target
		s=doDirectionalSearch(imageTarget, slideTargetRegion, TOP);
		if(s!=null){
			return s;
		}
		logger.info("Searching to the left of the target to identify the target image.");
		// search to the left of the target
		s=doDirectionalSearch(imageTarget, slideTargetRegion, LEFT);
		if(s!=null){
			return s;
		}
		logger.info("Searching to the bottom of the target to identify the target image.");
		// search to the bottom of the target
		s=doDirectionalSearch(imageTarget, slideTargetRegion, BOTTOM);
		if(s!=null){
			return s;
		}
		return null;
	}
	
	/**
	 * Calculates and returns a list of screen regions scaled in the x direction by going to the left, from the location of the shape to the left edge of the screenshot.
	 * It moves to the right by 30 pixels. 
	 * @return a list of screen regions scaled in the x direction by going to the left, from the location of the shape to the left edge of the screenshot.
	 */
	private List<ScreenRegion> getLeftScreenRegionList(){
		List<ScreenRegion> screenRegions=new ArrayList<ScreenRegion>();
		int regionWidth=slideTargetRegion.getWidth();
		int leftEdge, newWidth;
		for(int i=0;i<5;i++){
			// set the new region width
			regionWidth+=widthFactor;
			// check if the boundaries of the region are not outside of the original screenshot
			leftEdge=slideTargetRegion.getX()-regionWidth;
			newWidth=regionWidth+slideTargetRegion.getWidth();
			if(leftEdge<0){
				break;
			}
			else{
				ScreenRegion screenRegion=new DesktopScreenRegion(Constants.ScreenId, leftEdge, 
				slideTargetRegion.getY(), newWidth, slideTargetRegion.getHeight());
				screenRegions.add(screenRegion);
			}
		}
		return screenRegions;
	}
	
	/**
	 * Calculates and returns a list of screen regions scaled in the x direction by going to the right, from the location of the shape to the right edge of the screenshot.
	 * It moves to the right by 30 pixels. 
	 * @return a list of screen regions scaled in the x direction by going to the right, from the location of the shape to the right edge of the screenshot.
	 */
	private List<ScreenRegion> getRightScreenRegionList(){
		List<ScreenRegion> screenRegions=new ArrayList<ScreenRegion>();
		int regionWidth=slideTargetRegion.getWidth();
		int rightEdge;
		for(int i=0;i<5;i++){
			// set the new region width
			regionWidth+=widthFactor;
			// check if the boundaries of the region are not outside of the original screenshot
			rightEdge=slideTargetRegion.getX()+regionWidth;
			if(rightEdge>slideTargetRegion.getMaxWidth()){
				break;
			}
			else{
				ScreenRegion screenRegion=new DesktopScreenRegion(Constants.ScreenId, slideTargetRegion.getX(), 
				slideTargetRegion.getY(), regionWidth, slideTargetRegion.getHeight());
				screenRegions.add(screenRegion);
			}
		}
		return screenRegions;
	}
	
	/**
	 * Calculates and returns a list of screen regions scaled in the y direction by going up, from the location of the shape to the top of the screenshot.
	 * It moves to the top by 30 pixels. 
	 * @return a list of screen regions scaled in the y direction by going up, from the location of the shape to the top of the screenshot.
	 */
	private List<ScreenRegion> getTopScreenRegionList(){
		List<ScreenRegion> screenRegions=new ArrayList<ScreenRegion>();
		int regionHeight=slideTargetRegion.getHeight();
		int upperEdge, newHeight;
		for(int i=0;i<5;i++){
			// set the new region height
			regionHeight+=heightFactor;
			// check if the boundaries of the region are not outside of the original screenshot
			upperEdge=slideTargetRegion.getY()-regionHeight;
			newHeight=regionHeight+slideTargetRegion.getHeight();
			if(upperEdge<0){
				break;
			}
			else{
				ScreenRegion screenRegion=new DesktopScreenRegion(Constants.ScreenId, slideTargetRegion.getX(), 
						upperEdge, slideTargetRegion.getWidth(),newHeight);
				screenRegions.add(screenRegion);
			}
		}
		return screenRegions;
	}
	
	/**
	 * Calculates and returns a list of screen regions scaled in the y direction by going down, from the location of the shape to the bottom of the screenshot.
	 * It moves to down by 30 pixels. 
	 * @return a list of screen regions scaled in the y direction by going down, from the location of the shape to the bottom of the screenshot.
	 */
	private List<ScreenRegion> getBottomScreenRegionList(){
		List<ScreenRegion> screenRegions=new ArrayList<ScreenRegion>();
		int regionHeight=slideTargetRegion.getHeight();
		int lowerEdge;
		for(int i=0;i<5;i++){
			// set the new region height
			regionHeight+=heightFactor;
			// check if the boundaries of the region are not outside of the original screenshot
			lowerEdge=slideTargetRegion.getY()+regionHeight;
			if(lowerEdge>slideTargetRegion.getMaxHeight()){
				break;
			}
			else{
				ScreenRegion screenRegion=new DesktopScreenRegion(Constants.ScreenId, slideTargetRegion.getX(), 
						slideTargetRegion.getY(), slideTargetRegion.getWidth(),regionHeight);
				screenRegions.add(screenRegion);
			}
		}
		return screenRegions;
	}
	
	
	/**
	 * Searches in the specified direction for the target image. 
	 * @param imageTarget the target image to search for
	 * @param slideTargetRegion the target region in the presentation slide
	 * @param direction the direction to which to search; @value {@link #TOP}, {@link #BOTTOM},
	 * {@link #RIGHT}, or {@link #LEFT}
	 * @return the screen region that only contains one occurrence of the target image
	 */
	private ScreenRegion doDirectionalSearch(ImageTarget imageTarget,
			SlideTargetRegion slideTargetRegion, int direction) {
		
		ScreenRegion fullScreenRegion = new DesktopScreenRegion(Constants.ScreenId);
		List<ScreenRegion> directionScreenRegions;
		if(direction==TOP){
			directionScreenRegions=getTopScreenRegionList();
		}
		else if(direction==BOTTOM){
			directionScreenRegions=getBottomScreenRegionList();
		}
		else if(direction==RIGHT){
			directionScreenRegions=getRightScreenRegionList();
		}
		else if(direction==LEFT){
			directionScreenRegions=getLeftScreenRegionList();
		}
		else{
			logger.error("Unknown search direction.");
			return null;
		}
		
		for(ScreenRegion screenRegion:directionScreenRegions){
			BufferedImage croppedRegionImage=ImageProcessing.cropImage(slideTargetRegion.getScreenshotPath(), screenRegion.getBounds().x, 
			screenRegion.getBounds().y, screenRegion.getBounds().width, 
			screenRegion.getBounds().height);
			
			logger.info("Attempt no. "+counter.incrementAndGet());
			/*TODO: Write region images for debugging purposes. Remove this later.
			// save the cropped region image on the disk
			String croppedImageName=Constants.projectDirectory+Constants.SIKULI_DIRECTORY+
					Constants.IMAGES_DIRECTORY+File.separator+"region"+Integer.toString(counter.get())+".png";
			ImageProcessing.writeImageToDisk(croppedRegionImage, croppedImageName);
			*/
			// check if there are more than one occurrence of the region itself
			ImageTarget newImageTarget=new ImageTarget(croppedRegionImage);
			newImageTarget.setMinScore(prefsEditor.getPreciseSearchScore());
			List<ScreenRegion>lookupRegion=fullScreenRegion.findAll(newImageTarget);
			if(lookupRegion.size()>1){
				continue;
			}
			else if(lookupRegion.size()==1){
				// search for the target image within the screen region
				List<ScreenRegion> targetList=lookupRegion.get(0).findAll(imageTarget);
				if(targetList.size()>1){
					continue;
				}
				else if(targetList.size()==1){
					logger.info("Target found.");
					return lookupRegion.get(0);
				}
			}
		}
		return null;
	}
}
