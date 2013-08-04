package org.sikuli.slides.interpreters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.BrowserAction;
import org.sikuli.slides.actions.DoubleClickAction;
import org.sikuli.slides.actions.ExistAction;
import org.sikuli.slides.actions.LabelAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.NotExistAction;
import org.sikuli.slides.actions.RightClickAction;
import org.sikuli.slides.actions.TypeAction;
import org.sikuli.slides.actions.WaitAction;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;
import org.sikuli.slides.utils.UnitConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultInterpreter implements Interpreter {

	Logger logger = LoggerFactory.getLogger(DefaultInterpreter.class);

	// the screen region the actions will be applied to
	private ScreenRegion screenRegion;
	public DefaultInterpreter(ScreenRegion screenRegion){
		this.screenRegion = screenRegion;
	}	

	Action interpretAsClick(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.CLICK)){
			ScreenRegion targetScreenRegion = tknzr.getTargetScreenRegion(screenRegion);
			if (targetScreenRegion != null)
				return new LeftClickAction(targetScreenRegion);			
		}
		return null;
	}


	Action interpretAsRightClick(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.RIGHT_CLICK)){
			ScreenRegion targetScreenRegion = tknzr.getTargetScreenRegion(screenRegion);
			if (targetScreenRegion != null)
				return new RightClickAction(targetScreenRegion);			
		}
		return null;
	}

	Action interpretAsDoubleClick(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.DOUBLE_CLICK)){
			ScreenRegion targetScreenRegion = tknzr.getTargetScreenRegion(screenRegion);
			if (targetScreenRegion != null)
				return new DoubleClickAction(targetScreenRegion);			
		}
		return null;
	}
	
	Action interpretAsExist(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.EXIST)){
			ScreenRegion targetScreenRegion = tknzr.getTargetScreenRegion(screenRegion);
			if (targetScreenRegion != null)
				return new ExistAction(targetScreenRegion);			
		}
		return null;
	}
	
	Action interpretAsNotExist(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.NOT_EXIST)){
			ScreenRegion targetScreenRegion = tknzr.getTargetScreenRegion(screenRegion);
			if (targetScreenRegion != null)
				return new NotExistAction(targetScreenRegion);			
		}
		return null;
	}
	
	
	Action interpretAsLabel(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.getActionWords().size() == 0){		
			ScreenRegion targetScreenRegion = tknzr.getTargetScreenRegion(screenRegion);
			SlideElement targetElement = tknzr.getTargetSlideElement();
			if (targetScreenRegion != null){
				LabelAction action = new LabelAction(targetScreenRegion);
				action.setText(targetElement.getText());
				double fontSize = UnitConverter.WholePointsToPoints(targetElement.getTextSize());
				action.setFontSize((int)fontSize);
				return action;
			}
		}
		return null;
	}
	
	
	Action interpretAsType(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.TYPE)){
			ScreenRegion targetScreenRegion = tknzr.getTargetScreenRegion(screenRegion);
			SlideElement targetElement = tknzr.getTargetSlideElement();
			if (targetScreenRegion != null){
				TypeAction typeAction = new TypeAction(targetScreenRegion);
				typeAction.setText(targetElement.getText());
				return typeAction;
			}
		}
		return null;
	}


	Action interpretAsBrowser(SlideTokenizer tknzr){
		if (tknzr.hasActionWord(ActionDictionary.BROWSER)){			
			List<String> arguments = tknzr.getArgumentStrings();
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

	Action interpretAsWait(SlideTokenizer tknzr){
		if (tknzr.hasActionWord(ActionDictionary.WAIT)){			
			List<String> arguments = tknzr.getArgumentStrings();
			if (arguments.size() > 0){				
				WaitAction a = new WaitAction();
				// find the first string argument that provides the wait duration
				for (String arg : arguments){
					// extract the time unit
					TimeUnit timeUnit = UnitConverter.extractTimeUnitFromString(arg);
					// TODO: "2 hours" doesn't get extracted correctly
					// if the time unit was not specified, default to seconds
					if(timeUnit==null){
						timeUnit=TimeUnit.SECONDS;
					}
					// extract the wait time string value, replace all non digits with blank
					String waitTimeString = arg.replaceAll("[^0-9.]", "");
					if(waitTimeString != null){
						double duration = Double.parseDouble(waitTimeString);
						double durationInMilliSeconds = 0;
						if (timeUnit == TimeUnit.SECONDS){
							durationInMilliSeconds = duration * 1000;
						}else if (timeUnit == TimeUnit.MINUTES){
							durationInMilliSeconds = duration * 1000 * 60;
						}else if (timeUnit == TimeUnit.HOURS){
							durationInMilliSeconds = duration * 1000 * 60 * 60;
						}
						a.setDuration((long) durationInMilliSeconds);
						return a;
					}			
				}
				logger.error("Error: Please enter the wait time value in a shape."
						+" Valid examples include: 10 seconds, 10 minutes, 10 hours, or even 2 days.");
			}			
		}
		return null;
	}	


	@Override
	public Action interpret(Slide slide){

		SlideTokenizer tknzr = new SlideTokenizer(slide);
		Action action = null;
		if ((action = interpretAsClick(tknzr, screenRegion)) != null){			

		}else if ((action = interpretAsRightClick(tknzr, screenRegion)) != null){			

		}else if ((action = interpretAsDoubleClick(tknzr, screenRegion)) != null){			

		}else if ((action = interpretAsType(tknzr, screenRegion)) != null){
			
		}else if ((action = interpretAsLabel(tknzr, screenRegion)) != null){
			
		}else if ((action = interpretAsExist(tknzr, screenRegion)) != null){
		
		}else if ((action = interpretAsNotExist(tknzr, screenRegion)) != null){		
			
		}else if ((action = interpretAsBrowser(tknzr)) != null){		

		}else if ((action = interpretAsWait(tknzr)) != null){

		}
		return action;
	}
}