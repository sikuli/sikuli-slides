package org.sikuli.slides.interpreters;

import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.BrowserAction;
import org.sikuli.slides.actions.DoubleClickAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.RightClickAction;
import org.sikuli.slides.models.ScreenshotElement;
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

public class DefaultInterpreter implements Interpreter {

	Logger logger = LoggerFactory.getLogger(DefaultInterpreter.class);
		
	// the screen region the actions will be applied to
	private ScreenRegion screenRegion;
	public DefaultInterpreter(ScreenRegion screenRegion){
		this.screenRegion = screenRegion;
	}	
	
	Action interpretAsClick(ParsedSlide parsedSlide, ScreenRegion screenRegion){
		if (parsedSlide.isAction(ActionDictionary.CLICK)){
			ScreenRegion targetScreenRegion = parsedSlide.getTargetScreenRegion(screenRegion);
			if (targetScreenRegion != null)
				return new LeftClickAction(targetScreenRegion);			
		}
		return null;
	}
	
	
	Action interpretAsRightClick(ParsedSlide parsedSlide, ScreenRegion screenRegion){
		if (parsedSlide.isAction(ActionDictionary.RIGHT_CLICK)){
			ScreenRegion targetScreenRegion = parsedSlide.getTargetScreenRegion(screenRegion);
			if (targetScreenRegion != null)
				return new RightClickAction(targetScreenRegion);			
		}
		return null;
	}
	
	Action interpretAsDoubleClick(ParsedSlide parsedSlide, ScreenRegion screenRegion){
		if (parsedSlide.isAction(ActionDictionary.DOUBLE_CLICK)){
			ScreenRegion targetScreenRegion = parsedSlide.getTargetScreenRegion(screenRegion);
			if (targetScreenRegion != null)
				return new DoubleClickAction(targetScreenRegion);			
		}
		return null;
	}

	
	Action interpretAsBrowser(ParsedSlide parsedSlide){
		if (parsedSlide.isAction(ActionDictionary.BROWSER)){			
			List<String> arguments = parsedSlide.getArgumentStrings();
			if (arguments.size() > 0){				
				BrowserAction a = new BrowserAction();
				// find the first string that is a valid URL
				for (String arg : arguments){
					URL url;
					try {
						url = new URL(arg);
						a.setUrl(url);
						return a;
					} catch (MalformedURLException e) {
					}							
				}					
			}			
		}
		return null;
	}	
	
	
	@Override
	public Action interpret(Slide slide){

		ParsedSlide parsedSlide = new ParsedSlide(slide);
		Action action = null;
		if ((action = interpretAsClick(parsedSlide, screenRegion)) != null){			
		
		}else if ((action = interpretAsRightClick(parsedSlide, screenRegion)) != null){			
			
		}else if ((action = interpretAsDoubleClick(parsedSlide, screenRegion)) != null){			
			
		}else if ((action = interpretAsBrowser(parsedSlide)) != null){
			
		}		
		return action;
	}
}


class ParsedSlide extends Slide {
	private List<ActionWord> actionWords;
	private List<String> argumentStrings;
	private Slide slide;
		
	ParsedSlide(Slide slide){
		this.slide = slide;
		actionWords = extractActionWords(slide);
		argumentStrings = extractArgumentStrings(slide);

	}
	
	public boolean isAction(String actionName) {
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
	public List<String> getArgumentStrings() {
		return argumentStrings;
	}	
	
	
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
	
	
	static ScreenRegion extractTargetScreenRegion(Slide slide, ScreenRegion screenRegion){
		List<ScreenshotElement> screenshots = getScreenshotElements(slide);
		Collection<SlideElement> otherElements = getNotActionElements(slide);

		ScreenshotElement screenshotElement = null;
		SlideElement boundsElement = null;
		if (screenshots.size() > 0) {		
			screenshotElement = screenshots.get(0);				
			List<SlideElement> elementsInsideScreenshot = filterElementsContainedBy(otherElements, screenshotElement);					
			if (elementsInsideScreenshot.size() > 0){
				boundsElement = elementsInsideScreenshot.get(0);
			}
		}

		ScreenRegion targetScreenRegion = null;
		if (screenshotElement != null && boundsElement != null){

			int w = screenshotElement.getCx();
			int h = screenshotElement.getCy();

			if (w > 0 && h > 0){

				double xmax = 1.0 * (boundsElement.getOffx() + boundsElement.getCx() - screenshotElement.getOffx()) / w;
				double ymax = 1.0 * (boundsElement.getOffy() + boundsElement.getCy() - screenshotElement.getOffy()) / h;
				double xmin = 1.0 * (boundsElement.getOffx() - screenshotElement.getOffx()) / w;
				double ymin = 1.0 * (boundsElement.getOffy() - screenshotElement.getOffy()) / h;					

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
				boolean ret = (r.contains(element.getBounds()) && element != container);
				System.out.println(element.getBounds() + "<->" + container.getBounds() + " " + ret);
				return r.contains(element.getBounds()) && element != container;				
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


	static private List<ScreenshotElement> getScreenshotElements(Slide slide){
		Collection<SlideElement> elements = slide.getElements();		
		Collection<ScreenshotElement> ret = Collections2.filter(
				Collections2.transform(elements, new Function<SlideElement, ScreenshotElement>(){
					@Override
					public ScreenshotElement apply(SlideElement element) {
						if (element instanceof ScreenshotElement)
							return (ScreenshotElement) element;
						else
							return null;

					}
				}),
				Predicates.notNull()
				);
		return Lists.newArrayList(ret);
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