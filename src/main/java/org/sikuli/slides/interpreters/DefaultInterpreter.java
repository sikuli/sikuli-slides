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
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.ScreenRegionAction;
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

	@Override
	public Action interpret(Slide slide){

		Collection<ActionWord> words = getActionWords(slide);
		logger.trace("found {} action words", words.size());
		if (words.size() == 1){
			ActionWord singleActionWord = words.iterator().next();			
			if (singleActionWord.isMatched(ActionDictionary.CLICK)){

				//				ScreenRegionFinder finder = null;								
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

					int w = screenshotElement.getOffx() - screenshotElement.getCx();
					int h = screenshotElement.getOffy() - screenshotElement.getCy();

					if (w > 0 && h > 0){

						double xmin = 1.0 * (boundsElement.getCx() - screenshotElement.getCx()) / w;
						double ymin = 1.0 * (boundsElement.getCy() - screenshotElement.getCy()) / h;
						double xmax = 1.0 * (boundsElement.getOffx() - screenshotElement.getCx()) / w;
						double ymax = 1.0 * (boundsElement.getOffy() - screenshotElement.getCy()) / h;					

						Target target = new ContextualImageTarget(screenshotElement.getSource(), xmin, ymin, xmax, ymax); 
						targetScreenRegion = new TargetScreenRegion(target, getScreenRegion());
					}
				}	
				
				if (targetScreenRegion != null)
					return new LeftClickAction(targetScreenRegion);
				else
					return null;
			}else if (singleActionWord.isMatched(ActionDictionary.BROWSER)){

				List<String> arguments = getArgumentStrings(slide);
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
				return null;				
			}

		}
		return null;
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

	static private ActionWord findMatchedActionWord(String word){
		for (ActionWord validWord : ActionDictionary.WORDS){
			if (validWord.isMatched(word)){
				return validWord;
			}
		}
		return null;
	}

	private Collection<SlideElement> getNotActionElements(Slide slide){
		Collection<SlideElement> elements = slide.getElements();		
		return Collections2.filter(elements, new Predicate<SlideElement>(){
			@Override
			public boolean apply(SlideElement input) {
				return findMatchedActionWord(input.getText()) == null;

			}			
		});
	}


	private List<ScreenshotElement> getScreenshotElements(Slide slide){
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


	private List<String> getArgumentStrings(Slide slide){
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


	private Collection<ActionWord> getActionWords(Slide slide) {				
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
		return actionWords;
	}

	public ScreenRegion getScreenRegion() {
		return screenRegion;
	}

	public void setScreenRegion(ScreenRegion screenRegion) {
		this.screenRegion = screenRegion;
	}


}
