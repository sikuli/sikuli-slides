package org.sikuli.slides.api.models;

import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.interpreters.ConfigInterpreter;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.Interpreter;
import org.sikuli.slides.api.interpreters.Keyword;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Slide implements Action {
	
	List<SlideElement> elements = Lists.newArrayList();
	private int width = 9144000;
	private int height = 6858000;
	private int number; // one-based	

	
	// Construct a blank slide
	public Slide(){		
	}
	
	// Construct a new slide by copying the content from a given slide
	public Slide(Slide slide){
		elements = Lists.newArrayList(slide.elements);
		number = slide.number;
	}
		
	public void add(SlideElement element){
		elements.add(element);
	}
	
	public void remove(SlideElement element){
		elements.remove(element);
	}
	
	public Collection<SlideElement> getElements(){
		return Lists.newArrayList(elements);		
	}

	public String getText(){
		List<String> elementNames = Lists.newArrayList();
		for (SlideElement el : elements){		
			String text = el.getText();
			if (text != null && !text.isEmpty()){
				elementNames.add(text);
			}
		}		
		String txt = Joiner.on(",").join(elementNames);
		return txt;
	}
	
	public String toString(){		
		return Objects.toStringHelper(this)
				.add("number", number)
				.add("text", getText())
				.add("elements", elements).toString();		
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
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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
		
		public SlideElementBuilder geom(String geom) {
			element.setGeom(geom);
			return this;
		}

	}
	
	private Action action;
	
	
	@Override
	public void execute(Context context) throws ActionExecutionException {
		
		List<Interpreter> interpreters = Lists.newArrayList(
				new ConfigInterpreter(),
				new DefaultInterpreter(context)
		);
		
		for (Interpreter interpreter : interpreters){
			action = interpreter.interpret(this);
			if (action != null){
				action.execute(context);
				return;
			}
		}		
		throw new ActionExecutionException("Unable to interpret this slide", this); 
	}

	@Override
	public void stop() {
		if (action != null)
			action.stop();		
	}
}
