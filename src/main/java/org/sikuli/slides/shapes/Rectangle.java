/**
Khalid
*/
package org.sikuli.slides.shapes;

import java.io.File;
import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.screenshots.ContextRegion;
import org.sikuli.slides.sikuli.SikuliController;
import org.sikuli.slides.utils.Constants;

public class Rectangle extends Shape {
	private int width;
	private int height;
	
	public Rectangle(String name,String id, int offx, int offy, int cx,  int cy, int width, int height, String text,int order){
		super(id,name,offx,offy,cx,cy,text, order);
		this.width=width;
		this.height=height;
	}
	public Rectangle(String id, String name,int order) {
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
		return "Rectangle info:\n" +super.toString()+
				"\n width:"+width+"\n height:"+height+
				"\n ******************************************";
	}
	/**
	 * perform left click on the target image.
	 * targetFile the target image file.
	 */
	@Override
	public void doSikuliAction(File targetFile, ContextRegion contextRegion) {
		final ImageTarget imageTarget=new ImageTarget(targetFile);
		if(imageTarget!=null){
			ScreenRegion fullScreenRegion=new DesktopScreenRegion();
	    	ScreenRegion targetRegion=fullScreenRegion.wait(imageTarget, Constants.MaxWaitTime);
	    	if(targetRegion!=null){
	    		// check for multiple targets:
	    		// check if there are more than one occurrence of the target image.
	    		
	    		//imageTarget.setMinScore(1);	
	    		List<ScreenRegion> targetList=fullScreenRegion.findAll(imageTarget);
	    		if(targetList.size()>1){
	    			System.out.println("multiple targets on the screen.");
	    		}
	    		else{
	    			Mouse mouse = new DesktopMouse();
	    			mouse.click(targetRegion.getCenter());
	    			SikuliController.displayBox(targetRegion);
	    		}
	    	}
			else
				System.err.println("Couldn't find target on the screen."+getId());
		}
	}
	
}
