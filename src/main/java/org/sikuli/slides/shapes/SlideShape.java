/**
Khalid
*/
package org.sikuli.slides.shapes;

import org.sikuli.slides.core.SlideComponent;
import org.sikuli.slides.sikuli.SlideAction;
import org.sikuli.slides.utils.Constants.DesktopEvent;

public class SlideShape implements Comparable<SlideShape>{
	private String id;
	private String name;
	private String type; 
	private int offx;
	private int offy;
	private int cx;
	private int cy;
	private int width;
	private int height;
	private String text;
	private int order;
	private Integer targetOrder; /* the order in which the target is sorted*/
	private int textSize;
	private String backgroundColor;
	private int lineWidth;
	private String lineColor;
	public SlideShape(){
		this.id = "";
		this.name = "";
		this.order = 0;
		this.type = "";
		this.offx = 0;
		this.offy = 0;
		this.cx = 0;
		this.cy = 0;
		this.backgroundColor = "";
		this.lineWidth = 0;
		this.lineColor = "";
		this.targetOrder = 0;
	}
	public SlideShape(String id, String name, int order, String type, 
			int offx, int offy, int cx, int cy, String backgroundColor,int lineWidth, String lineColor){
		this.id = id;
		this.name = name;
		this.order = order;
		this.type = type;
		this.offx = offx;
		this.offy = offy;
		this.cx = cx;
		this.cy = cy;
		this.backgroundColor = backgroundColor;
		this.lineWidth = lineWidth;
		this.lineColor = lineColor;
		this.setTargetOrder(0);
	}
	/**
	 * 
	 * @return shape id
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * returns the shape type or the geometry of the shape. In Office Open XML
	 * , the pre-defined shape type is specified with an attribute "prst" 
	 * on the <a:prstGeom> element in each slide file.
	 * @return the geometry or form of a shape 
	 */
	public String getType() {
		return type;
	}
	/**
	 * sets the geometry or form of a shape.
	 * @param type the pre-defined shape type
	 */
	public void setType(String type) {
		this.type = type;
	}
	public int getOffx() {
		return offx;
	}
	public void setOffx(int offx) {
		this.offx = offx;
	}
	public int getOffy() {
		return offy;
	}
	public void setOffy(int offy) {
		this.offy = offy;
	}
	public int getCx() {
		return cx;
	}
	public void setCx(int cx) {
		this.cx = cx;
	}
	public int getCy() {
		return cy;
	}
	public void setCy(int cy) {
		this.cy = cy;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public int getTextSize() {
		return textSize;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	public int getTargetOrder() {
		return targetOrder;
	}
	public void setTargetOrder(int targetOrder) {
		this.targetOrder =new Integer(targetOrder);
	}
	public int getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
	public String getLineColor() {
		return lineColor;
	}
	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
	public void doSikuliAction(SlideComponent slideComponent,DesktopEvent desktopEvent){
		SlideAction slideAction=new SlideAction(slideComponent);
		slideAction.doSlideAction(desktopEvent);
	}
	
	@Override
	public String toString(){
		return "id="+id+" name="+name+
				"\n offx="+offx+"\n offy="+offy+"\n cx="+cx+"\n cy="+cy
				+"\n"+"width="+width+"\n"+"height="+height
				+"\n"+"text: "+text+"\n order="+order+"\n"+
				"background color= "+backgroundColor+
				"font size="+textSize;
	}
	@Override
	public int compareTo(SlideShape slideShape) {
		return targetOrder.compareTo(slideShape.targetOrder);
	}
}
