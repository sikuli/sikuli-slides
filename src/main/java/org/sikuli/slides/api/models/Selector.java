package org.sikuli.slides.api.models;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Rectangle;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sikuli.slides.api.interpreters.Keyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

// Convenient utility class for selecting slide elements
public class Selector {
	static Logger logger = LoggerFactory.getLogger(Selector.class);
	private Collection<SlideElement> elements;
	private boolean ignoreCase = false;

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

	
	public Selector below(final SlideElement element){	
		checkNotNull(element);
		final Rectangle r = element.getBounds();
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {				
				return e != element && e.getBounds().y >= r.y;								
			}		
		});
		return this;
	}
	
	public Selector toRightOf(final SlideElement element){	
		checkNotNull(element);
		final Rectangle r = element.getBounds();
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {				
				return e != element && e.getBounds().x >= r.x;								
			}		
		});
		return this;
	}

	public Selector intersects(final SlideElement element){		
		checkNotNull(element);
		final Rectangle r = element.getBounds();
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {			
				return e != element && r.intersects(e.getBounds());								
			}		
		});
		return this;
	}
	
	public Selector near(final SlideElement element, int radius){	
		checkNotNull(element);
		final Rectangle r = element.getBounds();
		r.x -= radius;
		r.y -= radius;
		r.width += (2*radius);
		r.height += (2*radius);
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {			
				return e != element && r.intersects(e.getBounds());								
			}		
		});
		return this;
	}
	
	// vertical center line is within each other's y range
	// namely, if they have the same x range, they would intersect
	public Selector overlapVerticallyWith(final SlideElement element, final float minOverlapRatio){	
		checkNotNull(element);
		final Rectangle r1 = element.getBounds();
		r1.x = 0; r1.width = 1;		
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				if (e == element){
					return false;
				}				
				if (r1.height == 0){
					return false;
				}
				Rectangle r2 =e.getBounds();
				r2.x = 0; r2.width = 1;
				Rectangle intersection = r1.intersection(r2);
				float yOverlapRatio = 1f * intersection.height / r1.height;			
				return yOverlapRatio > minOverlapRatio;
			}		
		});		
		return this;
	}
	
	public Selector orderByY(){
		List<SlideElement> list = Lists.newArrayList(elements);
		Collections.sort(list, new Comparator<SlideElement>(){
			@Override
			public int compare(SlideElement a, SlideElement b) {				
				return a.getOffy() - b.getOffy();
			}			
		});
		elements = list;
		return this;
	}
	
	public Selector orderByX(){
		List<SlideElement> list = Lists.newArrayList(elements);
		Collections.sort(list, new Comparator<SlideElement>(){
			@Override
			public int compare(SlideElement a, SlideElement b) {				
				return a.getOffx() - b.getOffx();
			}			
		});
		elements = list;
		return this;
	}
	
	public Selector print(PrintStream out){
		for (SlideElement element : elements){
			out.println(element);
		}
		return this;
	}
	
	public Selector print(){
		for (SlideElement element : elements){
			System.out.println(element);
		}
		return this;
	}
	
	
	public Selector hasText(){
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				return hasText(e);
			}		
		});
		return this;
	}
	
	public Selector hasNoText(){
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				return !hasText(e);
			}		
		});
		return this;
	}
	
	public Selector textContains(final String str){
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				if (!ignoreCase){
					return e.getText() != null && e.getText().contains(str);
				}else{
					return e.getText() != null && e.getText().toLowerCase().contains(str.toLowerCase());
				}
			}		
		});
		return this;
	}
	
	public Selector textStartsWith(final String str){
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				if (!ignoreCase){
					return e.getText() != null && e.getText().startsWith(str);
				}else{
					return e.getText() != null && e.getText().toLowerCase().startsWith(str.toLowerCase());
				}
			}		
		});
		return this;
	}
	
	public Selector textMatches(final String regex){		
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {				
				return e.getText() != null && e.getText().matches(regex);
			}	
		});
		return this;
	}
	
	public Selector ignoreCase(){
		ignoreCase = true;
		return this;
	}
	
	public Selector nameStartsWith(final String prefix){
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				return e.getName().startsWith(prefix);
			}		
		});
		return this;
	}

	static public boolean hasText(SlideElement e){
		return e.getText() != null && ! e.getText().isEmpty();
	}
	
	public Selector isTarget(){
		final List<SlideElement> images = Selector.select(elements).isImage().all();		
		// find elements that intersect at least one image 
		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement e) {
				return !hasText(e) && Selector.select(images).intersects(e).exist();
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

