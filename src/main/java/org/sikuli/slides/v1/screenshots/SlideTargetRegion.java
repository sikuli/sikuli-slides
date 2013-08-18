/**
Khalid
*/
package org.sikuli.slides.v1.screenshots;

public class SlideTargetRegion{
	
	private String name;
	private int slideNumber;
	private String screenshotPath;
	private int x;
	private int y;
	private int width;
	private int height;
	private int maxWidth;
	private int maxHeight;
	
	public SlideTargetRegion(String name, int slideNumber, String screenshotPath, int x, int y, int width, int height, int maxWidth, int maxHeight){
		this.name=name;
		this.slideNumber=slideNumber;
		this.screenshotPath=screenshotPath;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.maxWidth=maxWidth;
		this.maxHeight=maxHeight;
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
	public void setSlideNumber(int slideNumber) {
		this.slideNumber = slideNumber;
	}
	public String getScreenshotPath() {
		return screenshotPath;
	}
	public void setScreenshotPath(String screenshotPath) {
		this.screenshotPath = screenshotPath;
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
	public int getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	public int getMaxHeight() {
		return maxHeight;
	}
	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}
	public String toString(){
		return "Context Region: "+"name= "+name+"; slide number= "+
				"; screenshot path: "+screenshotPath+
				+slideNumber+"\nx= "+x+"; y= "+y+"; width= "+width+"; height= "+height;
	}
}