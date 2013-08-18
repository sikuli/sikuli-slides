/**
Khalid
*/
package org.sikuli.slides.v1.screenshots;

public class CroppedImage {
	private String name;
	private String path;
	private int width;
	private int height;
	
	public CroppedImage(String name, String path, int width, int height){
		this.name=name;
		this.path=path;
		this.width=width;
		this.height=height;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
}
