/**
Khalid
*/
package org.sikuli.slides.shapes;

import java.io.File;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.screenshots.SlideTargetRegion;
import org.sikuli.slides.sikuli.SearchMultipleTarget;
import org.sikuli.slides.sikuli.SikuliController;
import org.sikuli.slides.utils.Constants;
/**
 * RoundedRectangle shape to represent drag and drop operation
 * @author Khalid
 *
 */
public class RoundedRectangleShape extends SlideShape {
	
	private int width;
	private int height;

	public RoundedRectangleShape(String name,String id, int offx, int offy, int cx,  int cy, int width, int height, String text, int order){
		super(id,name,offx,offy,cx,cy,text,order);
		this.width=width;
		this.height=height;
	}
	
	public RoundedRectangleShape(String id, String name, int order) {
		super(id,name,0,0,0,0,"", order);
		this.width=0;
		this.height=0;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public String toString(){
		return "RoundedRectangle info:\n" +super.toString()+
				"\n width:"+width+"\n height:"+height+
				"\n ******************************************";
	}
	/**
	 * perform drop or drop action on the target image.
	 * @targetFile the target image file.
	 * @slideTargetRegion the region of the target image in the slide
	 */
	@Override
	public void doSikuliAction(File targetFile, SlideTargetRegion slideTargetRegion) {
		
		final ImageTarget imageTarget=new ImageTarget(targetFile);
		if(imageTarget!=null){
			ScreenRegion fullScreenRegion=new DesktopScreenRegion();
	    	ScreenRegion targetRegion=fullScreenRegion.wait(imageTarget, Constants.MaxWaitTime);
	    	if(targetRegion!=null){
	    		// check if there are more than one occurrence of the target image.
	    		SearchMultipleTarget searchMultipleTarget=new SearchMultipleTarget();
	    		if(searchMultipleTarget.hasMultipleOccurance(imageTarget)){
	    			ScreenRegion newScreenRegion=searchMultipleTarget.findNewScreenRegion(slideTargetRegion, imageTarget);
	    			if(newScreenRegion!=null){
	    				ScreenRegion newtargetRegion=newScreenRegion.find(imageTarget);
	    				performDragDrop(newtargetRegion);
	    			}
	    			else{
	    				System.err.println("Failed to determine the target image among multiple similar targets on the screen."
	    						+"\nTry to resize the shape in slide "+slideTargetRegion.getslideNumber()+".");
	    				System.exit(1);
	    			}
	    		}
	    		else{ // only one occurrence of the target on the screen
	    			performDragDrop(targetRegion);
	    		}
	    	}
			else{
				System.err.println("Failed to find target on the screen. Slide no. "+slideTargetRegion.getslideNumber());
				System.exit(1);
			}
		}
	}
	
	private void performDragDrop(ScreenRegion targetRegion){
		Mouse mouse = new DesktopMouse();
		if(getOrder()==0){
			SikuliController.displayBox(targetRegion);
			mouse.drag(targetRegion.getCenter());
		}
		else if(getOrder()==1){
			SikuliController.displayBox(targetRegion);
			mouse.drop(targetRegion.getCenter());
		}
		else{

			System.err.println("Couldn't find the start and end of the straight arrow connector " +
					"that is used to connect the rounded rectangles. Make sure the arrow is connected to the two rounded rectangles.");
		}
	}
	
}
