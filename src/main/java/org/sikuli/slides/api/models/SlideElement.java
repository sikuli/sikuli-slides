package org.sikuli.slides.api.models;

import java.awt.Rectangle;

import com.google.common.base.Objects;


public class SlideElement {
	private String id;
	private String name;
	private String type; 
	private int offx;
	private int offy;
	private int cx;
	private int cy;
	private String geom;
	private int width;
	private int height;
	private String text;
	private int order;
	private int textSize;
	private String backgroundColor;
	private int lineWidth;
	private String lineColor;
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
	
	public Rectangle getBounds(){
		return new Rectangle(getOffx(),getOffy(),getCx(),getCy());
	}
	
	@Override
	public String toString(){
		return Objects.toStringHelper(SlideElement.class)
				.add("id", id).add("name",name)
				.add("offx",offx).add("offy",offy)
				.add("cx",cx).add("cy",cy)
				.add("width",width).add("height",height)
				.add("geom",geom)
				.add("text",text)
				.add("fontSize", textSize)
				.add("order", order)
				.add("lineColor", lineColor)
				.add("backgroundColor", backgroundColor).toString();
	}
	public String getGeom() {
		return geom;
	}
	public void setGeom(String geom) {
		this.geom = geom;
	}
}
