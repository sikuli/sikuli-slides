/**
Khalid Alharbi
*/
package org.sikuli.slides.v1.sikuli;

import java.util.List;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.v1.screenshots.SlideTargetRegion;
import org.sikuli.slides.v1.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Search for multiple occurrences of a target on the screen.
 * 
 * @author Khalid Alharbi
 */
public class SearchMultipleTarget {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SearchMultipleTarget.class);
	
	/**
	 * Check if the screen has multiple occurrences of the Target image.
	 * @param imageTarget the target image to search for.
	 * @return returns true if the screen contains multiple occurrences of the target image (similar targets).
	 */
	public boolean hasMultipleOccurance(ImageTarget imageTarget){
		ScreenRegion fullScreenRegion=new DesktopScreenRegion(Constants.ScreenId);
		List<ScreenRegion> targetList=fullScreenRegion.findAll(imageTarget);
		if(targetList!=null && targetList.size()>1){
			logger.info("Found "+targetList.size()+" similar targets on the screen.");
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * finds the new screen region that only contains the target image.
	 * @return the screen region that only contains the target image.
	 */
	public ScreenRegion findNewScreenRegion(SlideTargetRegion slideTargetRegion,ImageTarget imageTarget){
		RegionSelector regionSelector=new RegionSelector(slideTargetRegion);
		ScreenRegion newScreenRegion=regionSelector.findScreenRegion(imageTarget,slideTargetRegion);
		if(newScreenRegion!=null){
			return newScreenRegion.find(imageTarget);
		}
		else{
			return null;
		}
	}
}
