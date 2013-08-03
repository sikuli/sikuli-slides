package org.sikuli.slides.models;

import java.net.URL;

import com.google.common.base.Objects;

public class ScreenshotElement extends SlideElement {
	private String fileName;
	private URL source;
		
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
//	public Region getTargetRegion() {
//		return targetRegion;
//	}
//	public void setTargetRegion(Region targetRegion) {
//		this.targetRegion = targetRegion;
//	}
	public URL getSource() {
		return source;
	}
	public void setSource(URL source) {
		this.source = source;
	}
	
	public String toString(){
		return Objects.toStringHelper(getClass())
		.add("sourceUrl", source)
		.add("fileName", fileName)
		.add("super", super.toString()).toString();		
	}
}
