package org.sikuli.slides.api.models;

import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.sikuli.slides.api.interpreters.Keyword;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class SlideEditor {
		
	private Slide slide;
	public SlideEditor(Slide slide){
		this.slide = slide;
	}	
	
	public static SlideEditor on(Slide slide){
		return new SlideEditor(slide);
	}
	
	public class Insert{
		public ImageElementSetter image(){
			ImageElement e = new ImageElement();
			slide.add(e);
			return new ImageElementSetter(e);
			
		}
		public void keyword(){
			
		}
		public SlideElementSetter element(){
			SlideElement e = new SlideElement();
			slide.add(e);			
			return new SlideElementSetter(e);
		}
	}
	
	public Insert insert(){
		return new Insert();
	}
	
//	public SlideElementBuilder newElement() {		
//		return new SlideElementBuilder(this);
//	}
//	
//	public ImageElementBuilder newImageElement() {		
//		return new ImageElementBuilder(this);
//	}
//	
//	public KeywordElementBuilder newKeywordElement() {		
//		return new KeywordElementBuilder(this);
//	}
//	
//	public static class KeywordElementBuilder extends SlideElementBuilder {
//		KeywordElementBuilder(SlideBuilder slide){
//			super(slide);
//			this.element = new KeywordElement();
//		}
//
//		public KeywordElementBuilder keyword(Keyword keyword){
//			((KeywordElement)element).setKeyword(keyword);
//			return this;
//		}
//
//	}
//	
	public static class ImageElementSetter extends SlideElementSetter {		
		ImageElementSetter(ImageElement element){
			super(element);
		}
		public ImageElementSetter source(URL source) {
			((ImageElement)element).setSource(source);
			return this;
		}
	}
//	
	
//	public static class SlideElementSetter<T>{
//		SlideElement element;		
//		SlideElementSetter(SlideElement element){
//			this.element = element;
//		}		
//		public T text(String text){
//			element.setText(text);
//			return this;
//		}
////		public SlideElementBuilder bounds(int x, int y, int w, int h) {
//			element.setOffx(x);
//			element.setOffy(y);
//			element.setCx(w);
//			element.setCy(h);
//			return this;
//		}
//		public SlideElement add(){
//			slide.add(element);
//			return element;
//		}		
//		
//		public SlideElementBuilder geom(String geom) {
//			element.setGeom(geom);
//			return this;
//		}
//
//	}
	
	public static class SlideElementSetter{
		SlideElement element;		
		SlideElementSetter(SlideElement element){
			this.element = element;
		}		
		public SlideElementSetter text(String text){
			element.setText(text);
			return this;
		}		
		public SlideElementSetter textSize(int size){
			element.setTextSize(size);
			return this;
		}
		public SlideElementSetter backgroundColor(String colorString){
			element.setBackgroundColor(colorString);
			return this;
		}
		
		public SlideElementSetter bounds(int x, int y, int w, int h) {
			element.setOffx(x);
			element.setOffy(y);
			element.setCx(w);
			element.setCy(h);
			return this;
		}
		
		public SlideElement get(){
			return element;
		}
//		public SlideElement add(){
//			slide.add(element);
//			return element;
//		}		
//		
//		public SlideElementBuilder geom(String geom) {
//			element.setGeom(geom);
//			return this;
//		}
		public SlideElementSetter lineColor(String colorString) {
			element.setLineColor(colorString);
			return this;
		}

	}
}
