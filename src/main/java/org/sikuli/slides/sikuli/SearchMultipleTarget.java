/**
Khalid Alharbi
*/
package org.sikuli.slides.sikuli;

import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.screenshots.SlideTargetRegion;

/**
 * Search for multiple occurrences of a target on the screen.
 * 
 * @author Khalid Alharbi
 */
public class SearchMultipleTarget {

	
	/**
	 * Check if the screen has multiple occurrences of the Target image.
	 * @param imageTarget the target image to search for.
	 * @return returns true if the screen contains multiple occurrences of the target image (similar targets).
	 */
	public boolean hasMultipleOccurance(ImageTarget imageTarget){
		ScreenRegion fullScreenRegion=new DesktopScreenRegion();
		List<ScreenRegion> targetList=fullScreenRegion.findAll(imageTarget);
		if(targetList!=null && targetList.size()>1){
			System.out.println("Found "+targetList.size()+" similar targets on the screen.");
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
