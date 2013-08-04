package org.sikuli.slides.interpreters;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.models.ImageElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;
import org.sikuli.slides.sikuli.ContextualImageTarget;
import org.sikuli.slides.sikuli.TargetScreenRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;


// Convenient utility class for extracting tokens from a slide object
class SlideTokenizer extends Slide {
	private List<ActionWord> actionWords;
	private List<String> argumentStrings;
	private Slide slide;

	SlideTokenizer(Slide slide){
		this.slide = slide;
		actionWords = extractActionWords(slide);
		argumentStrings = extractArgumentStrings(slide);
	}

	public boolean hasActionWord(String actionName) {
		if (actionWords.size() == 1){
			return actionWords.get(0).isMatched(actionName);
		}
		return false;
	}

	public List<ActionWord> getActionWords() {
		return actionWords;
	}
	public ScreenRegion getTargetScreenRegion(ScreenRegion screenRegion) {
		return extractTargetScreenRegion(slide, screenRegion);	
	}
	public SlideElement getTargetSlideElement() {
		return extractTargetSlideElement(slide);	
	}	

	public List<String> getArgumentStrings() {
		return argumentStrings;
	}	

	public List<ImageElement> getImageElements(){
		Collection<SlideElement> es = Collections2.filter(slide.getElements(), new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement input) {
				return input instanceof ImageElement;
			}			
		});	
		return Lists.newArrayList(
				Collections2.transform(es, new Function<SlideElement, ImageElement>(){
					@Override
					public ImageElement apply(SlideElement input) {				
						return (ImageElement) input;
					}			
				}));
	}

	static Logger logger = LoggerFactory.getLogger(SlideTokenizer.class);

	static private List<ActionWord> extractActionWords(Slide slide) {				
		Collection<SlideElement> elements = slide.getElements();
		Collection<ActionWord> actionWords =
				Collections2.filter(
						Collections2.transform(elements, new Function<SlideElement, ActionWord>(){
							@Override
							public ActionWord apply(SlideElement element) {
								String word = element.getText();
								if (word == null)
									return null;
								return findMatchedActionWord(word);
							}				    	
						}),
						Predicates.notNull()
						);
		return Lists.newArrayList(actionWords);
	}

	SlideElement extractTargetSlideElement(Slide slide){
		List<ImageElement> screenshots = getImageElements();
		Collection<SlideElement> otherElements = getNotActionElements(slide);

		ImageElement screenshotElement = null;
		SlideElement boundsElement = null;
		if (screenshots.size() > 0) {		
			screenshotElement = screenshots.get(0);				
			List<SlideElement> elementsInsideScreenshot = filterElementsContainedBy(otherElements, screenshotElement);					
			if (elementsInsideScreenshot.size() > 0){
				boundsElement = elementsInsideScreenshot.get(0);
			}
		}
		return boundsElement;
	}

	ScreenRegion extractTargetScreenRegion(Slide slide, ScreenRegion screenRegion){
		List<ImageElement> screenshots = getImageElements();
		Collection<SlideElement> otherElements = getNotActionElements(slide);

		ImageElement screenshotElement = null;
		SlideElement boundsElement = null;
		if (screenshots.size() == 0) {
			// parsing error: no picture
			return null;
		}

		// TODO: handle multiple screenshots
		screenshotElement = screenshots.get(0);				
		List<SlideElement> intersectingElements = filterElementsIntersectingWith(otherElements, screenshotElement);					
		if (intersectingElements.size() == 0){
			// parsing error: no bounds
			return null;
		}

		// TODO: handle intersecting elements
		boundsElement = intersectingElements.get(0);


		ScreenRegion targetScreenRegion = null;
		if (screenshotElement != null && boundsElement != null){

			int w = screenshotElement.getCx();
			int h = screenshotElement.getCy();

			if (w > 0 && h > 0){

				double xmax = 1.0 * (boundsElement.getOffx() + boundsElement.getCx() - screenshotElement.getOffx()) / w;
				double ymax = 1.0 * (boundsElement.getOffy() + boundsElement.getCy() - screenshotElement.getOffy()) / h;
				double xmin = 1.0 * (boundsElement.getOffx() - screenshotElement.getOffx()) / w;
				double ymin = 1.0 * (boundsElement.getOffy() - screenshotElement.getOffy()) / h;

				xmax = Math.min(1.0, xmax);
				ymax = Math.min(1.0, ymax);
				xmin = Math.max(0, xmin);
				ymin = Math.max(0, ymin);

				logger.trace("x: {}-{} y: {}-{}", xmin, xmax, ymin, ymax);				

				Target target = new ContextualImageTarget(screenshotElement.getSource(), xmin, ymin, xmax, ymax); 
				targetScreenRegion = new TargetScreenRegion(target, screenRegion);
			}
		}	
		return targetScreenRegion;
	}

	static private List<SlideElement> filterElementsContainedBy(Collection<SlideElement> elements, final SlideElement container){
		final Rectangle r = container.getBounds();
		return Lists.newArrayList(Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement element) {				
				return r.contains(element.getBounds()) && element != container;				
			}			
		}));		
	}

	static private List<SlideElement> filterElementsIntersectingWith(Collection<SlideElement> elements, final SlideElement container){
		final Rectangle r = container.getBounds();
		return Lists.newArrayList(Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement element) {				
				return r.intersects(element.getBounds()) && element != container;				
			}			
		}));		
	}

	static private ActionWord findMatchedActionWord(String word){
		for (ActionWord validWord : ActionDictionary.WORDS){
			if (validWord.isMatched(word)){
				return validWord;
			}
		}
		return null;
	}

	static private Collection<SlideElement> getNotActionElements(Slide slide){
		Collection<SlideElement> elements = slide.getElements();		
		return Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement input) {
				return findMatchedActionWord(input.getText()) == null;

			}			
		});
	}

	static private List<String> extractArgumentStrings(Slide slide){
		Collection<SlideElement> elements = slide.getElements();		
		Collection<String> strings = Collections2.filter(
				Collections2.transform(elements, new Function<SlideElement, String>(){
					@Override
					public String apply(SlideElement element) {
						String word = element.getText();
						// TODO: modify here to accept argument strings in the same element						
						if (findMatchedActionWord(word) == null){
							return word;
						}else{
							return null;
						}						
					}				    	
				}),
				Predicates.notNull()
				);
		return Lists.newArrayList(strings);
	}

}