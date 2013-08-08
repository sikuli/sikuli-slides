package org.sikuli.slides.models;

import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.sikuli.slides.interpreters.Keyword;
import org.sikuli.slides.interpreters.Selector;
import org.sikuli.slides.models.Slide.SlideElementBuilder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class Slide {
	
	List<SlideElement> elements = Lists.newArrayList();
	
	public void add(SlideElement element){
		elements.add(element);
	}
	
	public Collection<SlideElement> getElements(){
		return Lists.newArrayList(elements);		
	}

	public String toString(){
		String txt = "";
		for (SlideElement el : elements){
			txt = txt + el.toString() + "\n";
		}
		return txt;
	}
	
	public Selector select(){
		return Selector.select(this);
	}

	
	
	public SlideElementBuilder newElement() {		
		return new SlideElementBuilder(this);
	}
	
	public ImageElementBuilder newImageElement() {		
		return new ImageElementBuilder(this);
	}
	
	public KeywordElementBuilder newKeywordElement() {		
		return new KeywordElementBuilder(this);
	}
	
	public static class KeywordElementBuilder extends SlideElementBuilder {
		KeywordElementBuilder(Slide slide){
			super(slide);
			this.element = new KeywordElement();
		}

		public KeywordElementBuilder keyword(Keyword keyword){
			((KeywordElement)element).setKeyword(keyword);
			return this;
		}
	}
	
	public static class ImageElementBuilder extends SlideElementBuilder {
		ImageElementBuilder(Slide slide){
			super(slide);
			this.element = new ImageElement();
		}

		public ImageElementBuilder source(URL source) {
			((ImageElement)element).setSource(source);
			return this;
		}
	}
	
	public static class SlideElementBuilder{
		SlideElement element;	
		protected Slide slide;
		SlideElementBuilder(Slide slide){
			this.slide = slide;
			this.element = new SlideElement();
		}		
		public SlideElementBuilder text(String text){
			element.setText(text);
			return this;
		}
		public SlideElementBuilder bounds(int x, int y, int w, int h) {
			element.setOffx(x);
			element.setOffy(y);
			element.setCx(w);
			element.setCy(h);
			return this;
		}
		public SlideElement add(){
			slide.add(element);
			return element;
		}		
	}
	
//	public Builder text(String text){
//		
//	}
	
}
