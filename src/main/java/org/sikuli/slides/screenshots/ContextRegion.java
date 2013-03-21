/**
Khalid
*/
package org.sikuli.slides.screenshots;

public class ContextRegion{
	
	private String name;
	private int slideNumber;
	private int x;
	private int y;
	private int width;
	private int height;
	
	public ContextRegion(String name, int slideNumber, int x, int y, int width, int height){
		this.name=name;
		this.slideNumber=slideNumber;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getslideNumber() {
		return slideNumber;
	}
	public void setFileName(int slideNumber) {
		this.slideNumber = slideNumber;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
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
		return "Context Region: "+"name= "+name+"; slide number= "
				+slideNumber+"\nx= "+x+"; y= "+y+"; width= "+width+"; height= "+height;
	}
}