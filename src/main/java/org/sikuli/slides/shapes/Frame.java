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

public class Frame extends Shape {
	private int width;
	private int height;
	
	public Frame(String name,String id, int offx, int offy, int cx,  int cy, int width, int height, String text,int order){
		super(id,name,offx,offy,cx,cy,text, order);
		this.width=width;
		this.height=height;
	}
	public Frame(String id, String name,int order) {
		super(id,name,0,0,0,0,"",order);
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
		return "Frame info:\n" +super.toString()+
				"\n width:"+width+"\n height:"+height+
				"\n ******************************************";
	}
	
	/**
	 * perform left click on the target image.
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
	    				performDoubleClick(newtargetRegion);
	    			}
	    			else{
	    				System.out.println("Couldn't uniquely determine the target image among multiple similar targets on the screen.");
	    			}
	    		}
	    		else{
	    			performDoubleClick(targetRegion);
	    		}
	    	}
			else
				System.err.println("Couldn't find target on the screen."+getId());
		}
	}
	
	private void performDoubleClick(ScreenRegion targetRegion){
		Mouse mouse = new DesktopMouse();
		SikuliController.displayBox(targetRegion);
		mouse.doubleClick(targetRegion.getCenter());
	}
	
}
