package org.sikuli.slides.models;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.base.Objects;

public class ImageElement extends SlideElement {
	private String fileName;
	private URL source;
		
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public URL getSource() {
		try {
			return (new File(fileName)).toURI().toURL();
		} catch (MalformedURLException e) {
		}
		return null;
	}
	public void setSource(URL source) {
		fileName = source.getPath();
		this.source = source;
	}
	
	public String toString(){
		return Objects.toStringHelper(getClass())
		.add("sourceUrl", source)
		.add("fileName", fileName)
		.add("super", super.toString()).toString();		
	}
}
