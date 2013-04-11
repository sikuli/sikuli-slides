/**
Khalid
*/
package org.sikuli.slides.shapes;

import org.sikuli.slides.core.SlideComponent;
import org.sikuli.slides.sikuli.SlideAction;
import org.sikuli.slides.utils.Constants;
/**
 * RoundedRectangle shape to represent drag and drop operation
 * @author Khalid
 *
 */
public class RoundedRectangleShape extends SlideShape {

	public RoundedRectangleShape(String name,String id, int offx, int offy, int cx,  int cy, int width, int height, String text, int order){
		super(id,name,offx,offy,cx,cy,width,height,text,order);
	}
	
	public RoundedRectangleShape(String id, String name, int order) {
		super(id,name,0,0,0,0,0,0,"", order);
	}
	
	public String toString(){
		return "RoundedRectangle info:\n" +super.toString()+
				"\n ******************************************";
	}
	/**
	 * perform drag and drop click on the target image.
	 * @slideComponent the components of the slide.
	 */
	@Override
	public void doSikuliAction(SlideComponent slideComponent) {
		SlideAction slideAction=new SlideAction(slideComponent);
		slideAction.doSikuliAction(Constants.DesktopEvent.DRAG_N_DROP);
	}
	
}
