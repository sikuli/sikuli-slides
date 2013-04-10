/**
Khalid
*/
package org.sikuli.slides.shapes;

import org.sikuli.slides.core.SlideComponent;
import org.sikuli.slides.sikuli.SlideAction;
import org.sikuli.slides.utils.Constants;

public class OvalShape extends SlideShape {
	private int width;
	private int height;
	
	public OvalShape(String name,String id, int offx, int offy, int cx,  int cy, int width, int height,String text, int order){
		super(id,name,offx,offy,cx,cy,text,order);
		this.width=width;
		this.height=height;
	}
	public OvalShape(String id, String name,int order) {
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
		return "Oval info:\n" +super.toString()+
				"\n width:"+width+"\n height:"+height+
				"\n ******************************************";
	}
	/**
	 * perform right click on the target image.
	 * @slideComponent the components of the slide.
	 */
	@Override
	public void doSikuliAction(SlideComponent slideComponent) {
		SlideAction slideAction=new SlideAction(slideComponent);
		slideAction.doSikuliAction(Constants.DesktopEvent.RIGHT_CLICK);
	}
}
