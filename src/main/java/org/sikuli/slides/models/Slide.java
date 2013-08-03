package org.sikuli.slides.models;

import java.util.Collection;
import java.util.List;

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
	
	public Collection<ScreenshotElement> getScreenshotElements(){
		Collection<SlideElement> es = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement input) {
				return input instanceof ScreenshotElement;
			}			
		});	
		return Collections2.transform(es, new Function<SlideElement, ScreenshotElement>(){
			@Override
			public ScreenshotElement apply(SlideElement input) {				
				return (ScreenshotElement) input;
			}			
		});
	}

	public String toString(){
		String txt = "";
		for (SlideElement el : elements){
			txt = txt + el.toString() + "\n";
		}
		return txt;
	}
	
	
}
