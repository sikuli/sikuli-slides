package org.sikuli.slides.interpreters;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;

import org.sikuli.slides.models.ImageElement;
import org.sikuli.slides.models.KeywordElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

// Convenient utility class for selecting slide elements
public class Selector {
	static Logger logger = LoggerFactory.getLogger(Selector.class);
	private Collection<SlideElement> elements;

	Selector(Collection<SlideElement> elements){
		this.elements = elements;
	}

	public List<SlideElement> all(){
		return Lists.newArrayList(elements);
	}

	public SlideElement first(){
		if (elements.size()>=1){
			return elements.iterator().next();
		}else{
			return null;
		}
	}

	public boolean exist() {
		return elements.size() > 0;
	}

	public static Selector select(Slide slide){
		return new Selector(slide.getElements());
	}

	public static Selector select(Collection<SlideElement> elements){
		return new Selector(elements);
	}


	public Selector intersects(final SlideElement element){		
		if (element == null){
			elements = Lists.newArrayList();
			return this;
		}
		final Rectangle r = element.getBounds();
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {			
				return e != element && r.intersects(e.getBounds());								
			}		
		});
		return this;
	}

	public Selector hasText(){
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				return e.getText() != null && ! e.getText().isEmpty();
			}		
		});
		return this;
	}

	public Selector isTarget(){
		final List<SlideElement> images = Selector.select(elements).isImage().all();		
		// find elements that intersect at least one image 
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				return Selector.select(images).intersects(e).exist();
			}		
		});
		return this;
	}
	
	public Selector isKeyword() {
		List<SlideElement> images = Lists.newArrayList();
		for (SlideElement e : elements){
			if (e instanceof KeywordElement){
				images.add((KeywordElement) e);
			}
		}
		elements = images;
		return this;
	}
	
	public Selector isKeyword(Keyword keyword) {
		List<SlideElement> newList = Lists.newArrayList();
		for (SlideElement e : elements){
			if (e instanceof KeywordElement && ((KeywordElement) e).getKeyword() == keyword){
				newList.add((KeywordElement) e);
			}
		}
		elements = newList;
		return this;
	}

	public Selector isImage() {
		List<SlideElement> images = Lists.newArrayList();
		for (SlideElement e : elements){
			if (e instanceof ImageElement){
				images.add((ImageElement) e);
			}
		}
		elements = images;
		return this;
	}

	public Selector isNotKeyword() {
		List<SlideElement> newList = Lists.newArrayList();
		for (SlideElement e : elements){
			if (!(e instanceof KeywordElement)){
				newList.add(e);
			}
		}
		elements = newList;
		return this;	
	}

}

