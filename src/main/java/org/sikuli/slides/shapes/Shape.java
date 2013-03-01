/**
Khalid
*/
package org.sikuli.slides.shapes;

import java.io.File;

public abstract class Shape {
	private String id;
	private String name;
	private int offx;
	private int offy;
	private int cx;
	private int cy;
	private String text;
	private int order;
	public Shape(String id, String name, int order){
		this.id=id;
		this.name=name;
		this.setOrder(order);
	}
	public Shape(String id, String name,int offx, int offy, int cx, int cy, String text, int order){
		this.id=id;
		this.name=name;
		this.offx=offx;
		this.offy=offy;
		this.cx=cx;
		this.cy=cy;
		this.text=text;
		this.setOrder(order);
	}
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
	
	public abstract void doSikuliAction(File targetFile);
	
	@Override
	public String toString(){
		return "id="+id+" name="+name+
				"\n offx="+offx+"\n offy="+offy+"\n cx="+cx+"\n cy="+cy
				+"\n"+"text: "+text+"\n order="+order;
	}


	
}
