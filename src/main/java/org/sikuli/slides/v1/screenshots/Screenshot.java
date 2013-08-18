/**
Khalid
*/
package org.sikuli.slides.v1.screenshots;

public class Screenshot {
	private String name;
	private String fileName;
	private String relationshipID;
	private int cx;
	private int cy;
	private int offX;
	private int offY;
	private int width;
	private int height;
	
	public Screenshot(){
		
	}
	
	public Screenshot(String name, String fileName, int cx ,int cy, int offX, int offY, int width, int height){
		this.name=name;
		this.fileName=fileName;
		this.cx=cx;
		this.cy=cy;
		this.offX=offX;
		this.offY=offY;
		this.width=width;
		this.height=height;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getRelationshipID() {
		return relationshipID;
	}

	public void setRelationshipID(String relationshipID) {
		this.relationshipID = relationshipID;
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

	public int getOffX() {
		return offX;
	}

	public void setOffX(int offX) {
		this.offX = offX;
	}

	public int getOffY() {
		return offY;
	}

	public void setOffY(int offY) {
		this.offY = offY;
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
		return "Screenshot info:\n" +"name:"+name+"\n file name:"+fileName+
				"\n relationshipID="+relationshipID+
				"\n offX="+offX+"\n offY="+offY+"\n CX="+cx+"\n CY="+cy+
				"\n width:"+width+"\n height:"+height+
				"\n ******************************************";
	}
}
