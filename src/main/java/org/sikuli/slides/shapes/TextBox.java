/**
Khalid
*/
package org.sikuli.slides.shapes;

import java.io.File;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.screenshots.SlideTargetRegion;
import org.sikuli.slides.sikuli.SearchMultipleTarget;
import org.sikuli.slides.sikuli.SikuliController;
import org.sikuli.slides.utils.Constants;

public class TextBox extends Shape {
	private int width;
	private int height;
	
	public TextBox(String name,String id, int offx, int offy, int cx,  int cy, int width, int height, String text, int order){
		super(id,name,offx,offy,cx,cy,text, order);
		this.width=width;
		this.height=height;
	}
	public TextBox(String id, String name, int order) {
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
		return "TextBox info:\n" +super.toString()+
				"\n width:"+width+"\n height:"+height+
				"\n ******************************************";
	}
	// perform typing (click and type text)
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
	    				performTyping(newtargetRegion);
	    			}
	    			else{
	    				System.out.println("Couldn't uniquely determine the target image among multiple similar targets on the screen.");
	    			}
	    		}
	    		else{
	    			performTyping(targetRegion);
	    		}
	    	}
			else
				System.err.println("Couldn't find target on the screen."+getId());
		}
	}
	
	private void performTyping(ScreenRegion targetRegion){
		Mouse mouse = new DesktopMouse();
		Keyboard keyboard=new DesktopKeyboard();
		SikuliController.displayBox(targetRegion);
		mouse.click(targetRegion.getCenter());
		keyboard.type(getText());
	}

}
