package org.sikuli.slides.interpreters;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.sikuli.slides.models.ImageElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;


// Convenient utility class for extracting tokens from a slide object
class SlideTokenizer extends Slide {
	static Logger logger = LoggerFactory.getLogger(SlideTokenizer.class);

	private List<Keyword> actionWords;
	private List<String> argumentStrings;
	private List<ImageElement> imageElements;
	private Slide slide;
//	private Map<String, SlideElement> keywords;

	SlideTokenizer(Slide slide){
		this.slide = slide;			
	}
	
	public boolean hasKeyword(Keyword keyword){
		String actionName = keyword.getName();
		if (getActionWords().size() == 1){
			return getActionWords().get(0).isMatched(actionName);
		}
		return false;
	}
	
	// return a list of slide elements that contain keywords
	public List<SlideElement> getKeywordElements(){
		Collection<SlideElement> ret = Collections2.filter(slide.getElements(), new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement element) {
				String word = element.getText();
				if (word == null || word.isEmpty())
					return false;
				return findMatchedActionWord(word) != null;
			}				    	
		});
		return Lists.newArrayList(ret);
	}
	
	// return the first SlideELement that contains the given keyword
	// return null if the keyword is not valid
	// return null if no SlideElement has the keyword
	public SlideElement findKeywordElement(String keyword) {
		Keyword validWord = null;
		for (Keyword w : KeywordDictionary.WORDS){
			if (w.isMatched(keyword)){
				validWord = w;
			}
		}
		if (validWord != null){
			for (SlideElement element : slide.getElements()){
				if (validWord.isMatched(element.getText())){
					return element;
				}
			}
		}
		return null;
	}

	public List<Keyword> getActionWords() {
		if (actionWords == null){
			actionWords = extractActionWords(slide);
		}
		return actionWords;
	}

	// return all elements on a particular element
	public List<SlideElement> getElementsOn(SlideElement element){
		List<SlideElement> intersectingElements = filterElementsIntersectingWith(slide.getElements(), element);
		return intersectingElements;
	}

	public List<String> getArgumentStrings() {
		if (argumentStrings == null){
			argumentStrings = extractArgumentStrings(slide);
		}
		return argumentStrings;		
	}

	public List<SlideElement> getNonKeywordTextElements() {	
		Collection<SlideElement> els = Collections2.filter(slide.getElements(), new Predicate<SlideElement>(){
				public boolean apply(SlideElement element) {
					String word = element.getText();
					return word != null && word.length() > 0 && findMatchedActionWord(word) == null;					
				}			
		});
		return Lists.newArrayList(els);
	}

	public List<ImageElement> getImageElements(){
		if (imageElements == null){
			Collection<SlideElement> es = Collections2.filter(slide.getElements(), new Predicate<SlideElement>(){
				@Override
				public boolean apply(SlideElement input) {
					return input instanceof ImageElement;
				}			
			});	
			imageElements = Lists.newArrayList(
					Collections2.transform(es, new Function<SlideElement, ImageElement>(){
						@Override
						public ImageElement apply(SlideElement input) {				
							return (ImageElement) input;
						}			
					}));
		}		
		return imageElements;
	}


	static private List<Keyword> extractActionWords(Slide slide) {				
		Collection<SlideElement> elements = slide.getElements();
		Collection<Keyword> actionWords =
				Collections2.filter(
						Collections2.transform(elements, new Function<SlideElement, Keyword>(){
							@Override
							public Keyword apply(SlideElement element) {
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

	static private Keyword findMatchedActionWord(String word){
		for (Keyword validWord : KeywordDictionary.WORDS){
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

	public static List<SlideElement> filterByNonKeywordElements(List<SlideElement> elements) {
		return Lists.newArrayList(
				Collections2.filter(elements, new Predicate<SlideElement>(){
					@Override
					public boolean apply(SlideElement element) {
						String word = element.getText();
						if (word == null)
							return false;
						return findMatchedActionWord(word) != null;
					}			
				}));		
	}

	
}