/**
Khalid
*/
package org.sikuli.slides.sikuli;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.processing.ImageProcessing;
import org.sikuli.slides.screenshots.SlideTargetRegion;
/**
 * A region selector that finds the screen region of a target when similar targets exist on the screen.
 * @author Khalid
 *
 */
public class RegionSelector {
	
	private SlideTargetRegion slideTargetRegion;
	private final int widthFactor=30;
	private final int heightFactor=30;
	
	public RegionSelector(SlideTargetRegion slideTargetRegion){
		this.slideTargetRegion=slideTargetRegion;
	}
	
	/**
	 * returns a list of screen regions scaled in the x direction by 40 pixels
	 * @return
	 */
	private List<ScreenRegion> getXScreenRegionList(){
		List<ScreenRegion> screenRegions=new ArrayList<ScreenRegion>();
		int regionWidth=slideTargetRegion.getWidth();
		for(int i=0;i<5;i++){
			// set the new region width
			regionWidth+=widthFactor;
			// check if the boundaries of the region are not outside of the original screenshot
			if(regionWidth>slideTargetRegion.getMaxWidth()){
				break;
			}
			else{
				ScreenRegion screenRegion=new DesktopScreenRegion(slideTargetRegion.getX(), 
				slideTargetRegion.getY(), regionWidth, slideTargetRegion.getHeight());
				screenRegions.add(screenRegion);
			}
		}
		return screenRegions;
	}
	
	/**
	 * returns a list of screen regions scaled in the y direction by 40 pixels
	 * @return
	 */
	private List<ScreenRegion> getYScreenRegionList(){
		List<ScreenRegion> screenRegions=new ArrayList<ScreenRegion>();
		int regionHeight=slideTargetRegion.getHeight();
		for(int i=0;i<5;i++){
			// set the new region height
			regionHeight+=heightFactor;
			// check if the boundaries of the region are not outside of the original screenshot
			if(regionHeight>slideTargetRegion.getMaxHeight()){
				break;
			}
			else{
				ScreenRegion screenRegion=new DesktopScreenRegion(slideTargetRegion.getX(), 
				slideTargetRegion.getY(), regionHeight, slideTargetRegion.getHeight());
				screenRegions.add(screenRegion);
			}
		}
		return screenRegions;
	}
	
	/**
	 * returns a list of screen regions scaled in the x and y directions by 40 pixels
	 * @return
	 */
	private List<ScreenRegion> getXYScreenRegionList(){
		List<ScreenRegion> screenRegions=new ArrayList<ScreenRegion>();
		int regionWidth=slideTargetRegion.getWidth();
		int regionHeight=slideTargetRegion.getHeight();
		for(int i=0;i<5;i++){
			regionWidth+=widthFactor;
			regionHeight+=heightFactor;
			// check if the boundaries of the region are not outside of the original screenshot
			if(regionWidth>slideTargetRegion.getMaxWidth()||regionHeight>slideTargetRegion.getMaxHeight()){
				break;
			}
			else{
				ScreenRegion screenRegion=new DesktopScreenRegion(slideTargetRegion.getX(), 
				slideTargetRegion.getY(), regionWidth, regionHeight);
				screenRegions.add(screenRegion);
			}
		}
		return screenRegions;
	}
	
	
	public ScreenRegion findScreenRegion(ImageTarget imageTarget,SlideTargetRegion contextRegion){
		System.out.println("Found similar targets on the screen. Trying to identify the target image...");
		// Search in the x direction
		ScreenRegion s=doXDirectionSearch(imageTarget,contextRegion);
		if(s!=null){
			return s;
		}
		// search in the y direction
		s=doYDirectionSearch(imageTarget, contextRegion);
		if(s!=null){
			return s;
		}
		// search in the x and y directions
		s=doXYSearch(imageTarget, contextRegion);
		if(s!=null){
			return s;
		}
		else{
			System.out.println("Failed to identify target image...");
		}
		return null;
	}
	
	/**
	 * Search in the x direction to find the target on the full screenshot image 
	 * @param imageTarget the target image to search for
	 * @param slideTargetRegion the target region in the presentation slide 
	 * @return the screen region that only contains one occurrence of the target image
	 */
	private ScreenRegion doXDirectionSearch(ImageTarget imageTarget,
			SlideTargetRegion slideTargetRegion) {

		ScreenRegion fullScreenRegion=new DesktopScreenRegion();
		List<ScreenRegion> xDirectionScreenRegions=getXScreenRegionList();
		for(ScreenRegion screenRegion:xDirectionScreenRegions){
			BufferedImage croppedImage=ImageProcessing.cropImage(slideTargetRegion.getScreenshotPath(), screenRegion.getBounds().x, 
			screenRegion.getBounds().y, screenRegion.getBounds().width, 
			screenRegion.getBounds().height);
			
			ScreenRegion lookupRegion=fullScreenRegion.find(new ImageTarget(croppedImage));
			if(lookupRegion!=null){
				List<ScreenRegion> targetList=lookupRegion.findAll(imageTarget);
				if(targetList.size()>1){
					continue;
				}
				else if(targetList.size()==1){
					return lookupRegion;
				}
			}
		}
		return null;
	}
	/**
	 * Search in the y direction to find the target on the full screenshot image 
	 * @param imageTarget the target image to search for
	 * @param slideTargetRegion the target region in the presentation slide 
	 * @return the screen region that only contains one occurrence of the target image
	 */
	private ScreenRegion doYDirectionSearch(ImageTarget imageTarget,
			SlideTargetRegion slideTargetRegion) {

		ScreenRegion fullScreenRegion=new DesktopScreenRegion();
		List<ScreenRegion> yDirectionScreenRegions=getYScreenRegionList();
		for(ScreenRegion screenRegion:yDirectionScreenRegions){
			BufferedImage croppedImage=ImageProcessing.cropImage(slideTargetRegion.getScreenshotPath(), screenRegion.getBounds().x, 
			screenRegion.getBounds().y, screenRegion.getBounds().width, 
			screenRegion.getBounds().height);
			
			ScreenRegion lookupRegion=fullScreenRegion.find(new ImageTarget(croppedImage));
			if(lookupRegion!=null){
				List<ScreenRegion> targetList=lookupRegion.findAll(imageTarget);
				if(targetList.size()>1){
					continue;
				}
				else if(targetList.size()==1){
					return lookupRegion;
				}
			}
		}
		return null;
	}
	/**
	 * Search in the x and y directions to find the target on the full screenshot image 
	 * @param imageTarget the target image to search for
	 * @param slideTargetRegion the target region in the presentation slide 
	 * @return the screen region that only contains one occurrence of the target image
	 */
	private ScreenRegion doXYSearch(ImageTarget imageTarget,
			SlideTargetRegion slideTargetRegion) {

		ScreenRegion fullScreenRegion=new DesktopScreenRegion();
		List<ScreenRegion> xyDirectionScreenRegions=getXYScreenRegionList();
		for(ScreenRegion screenRegion:xyDirectionScreenRegions){
			BufferedImage croppedImage=ImageProcessing.cropImage(slideTargetRegion.getScreenshotPath(), screenRegion.getBounds().x, 
			screenRegion.getBounds().y, screenRegion.getBounds().width, 
			screenRegion.getBounds().height);
			
			ScreenRegion lookupRegion=fullScreenRegion.find(new ImageTarget(croppedImage));
			if(lookupRegion!=null){
				List<ScreenRegion> targetList=lookupRegion.findAll(imageTarget);
				if(targetList.size()>1){
					continue;
				}
				else if(targetList.size()==1){
					return lookupRegion;
				}
			}
		}
		return null;
	}
}
