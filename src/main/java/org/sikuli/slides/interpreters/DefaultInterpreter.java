package org.sikuli.slides.interpreters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.BrowserAction;
import org.sikuli.slides.actions.DoubleClickAction;
import org.sikuli.slides.actions.ExistAction;
import org.sikuli.slides.actions.FindDoAction;
import org.sikuli.slides.actions.LabelAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.NotExistAction;
import org.sikuli.slides.actions.RightClickAction;
import org.sikuli.slides.actions.TypeAction;
import org.sikuli.slides.actions.WaitAction;
import org.sikuli.slides.models.ImageElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;
import org.sikuli.slides.sikuli.ContextualImageTarget;
import org.sikuli.slides.utils.UnitConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultInterpreter implements Interpreter {

	Logger logger = LoggerFactory.getLogger(DefaultInterpreter.class);

	static class TargetIterpreter {
		private SlideElement targetElement;
		private ImageElement imageElement;
		private Target target;
		boolean interpret(SlideTokenizer tknzr, ScreenRegion screenRegion){
			List<ImageElement> images = tknzr.getImageElements();
			if (images.isEmpty()){
				return false;
			}
			imageElement = images.get(0);
			List<SlideElement> possibleTargetElements = tknzr.getElementsOn(getImageElement());
			SlideTokenizer.filterByNonKeywordElements(possibleTargetElements);
			if (possibleTargetElements.isEmpty()){
				return false;
			}
			targetElement = possibleTargetElements.get(0);			
			if (imageElement != null && targetElement != null){

				int w = imageElement.getCx();
				int h = imageElement.getCy();

				if (w > 0 && h > 0){

					double xmax = 1.0 * (targetElement.getOffx() + targetElement.getCx() - imageElement.getOffx()) / w;
					double ymax = 1.0 * (targetElement.getOffy() + targetElement.getCy() - imageElement.getOffy()) / h;
					double xmin = 1.0 * (targetElement.getOffx() - imageElement.getOffx()) / w;
					double ymin = 1.0 * (targetElement.getOffy() - imageElement.getOffy()) / h;

					xmax = Math.min(1.0, xmax);
					ymax = Math.min(1.0, ymax);
					xmin = Math.max(0, xmin);
					ymin = Math.max(0, ymin);

					//				logger.trace("x: {}-{} y: {}-{}", xmin, xmax, ymin, ymax);				

					target = new ContextualImageTarget(imageElement.getSource(), xmin, ymin, xmax, ymax); 
					return true;
				}
			}

			return false;		
		}
		public SlideElement getTargetElement() {
			return targetElement;
		}
		public ImageElement getImageElement() {
			return imageElement;
		}
		public Target getTarget() {
			return target;
		}
	}

	// the screen region the actions will be applied to
	private ScreenRegion screenRegion;
	public DefaultInterpreter(ScreenRegion screenRegion){
		this.screenRegion = screenRegion;
	}	

	Action interpretAsClick(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.CLICK)){
			TargetIterpreter targetIterpreter = new TargetIterpreter();			
			if (targetIterpreter.interpret(tknzr, screenRegion)){
				return new FindDoAction(targetIterpreter.getTarget(), new LeftClickAction());
			}
		}
		return null;
	}

	Action interpretAsRightClick(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.RIGHT_CLICK)){
			TargetIterpreter targetIterpreter = new TargetIterpreter();			
			if (targetIterpreter.interpret(tknzr, screenRegion)){
				return new FindDoAction(targetIterpreter.getTarget(), new RightClickAction());	
			}
		}
		return null;
	}

	Action interpretAsDoubleClick(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.DOUBLE_CLICK)){
			TargetIterpreter targetIterpreter = new TargetIterpreter();			
			if (targetIterpreter.interpret(tknzr, screenRegion)){
				return new FindDoAction(targetIterpreter.getTarget(), new DoubleClickAction());
			}
		}
		return null;
	}



	Action interpretAsExist(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.EXIST)){
			TargetIterpreter targetIterpreter = new TargetIterpreter();			
			if (targetIterpreter.interpret(tknzr, screenRegion)){
				return new ExistAction(targetIterpreter.getTarget());
			}
		}
		return null;
	}

	Action interpretAsNotExist(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.NOT_EXIST)){
			TargetIterpreter targetIterpreter = new TargetIterpreter();			
			if (targetIterpreter.interpret(tknzr, screenRegion)){
				return new NotExistAction(targetIterpreter.getTarget());
			}
		}
		return null;
	}

	Action interpretAsLabel(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.getActionWords().size() == 0){	
			// if there is a valid target
			TargetIterpreter targetIterpreter = new TargetIterpreter();			
			if (targetIterpreter.interpret(tknzr, screenRegion)){		
				// show the label over that target
				SlideElement targetElement = targetIterpreter.getTargetElement();
				LabelAction action = new LabelAction();
				action.setText(targetElement.getText());
				double fontSize = UnitConverter.WholePointsToPoints(targetElement.getTextSize());
				action.setFontSize((int)fontSize);
				return new FindDoAction(targetIterpreter.getTarget(), action, null);
				
			}else{
				// show the label without a specific target
				List<SlideElement> textElements = tknzr.getNonKeywordTextElements();
				if (textElements.size() > 0){
					LabelAction action = new LabelAction();
					SlideElement textElement = textElements.get(0);
					action.setText(textElement.getText());
					double fontSize = UnitConverter.WholePointsToPoints(textElement.getTextSize());
					action.setFontSize((int)fontSize);		
					return action;
				}
			}
		}
		return null;
	}


	Action interpretAsType(SlideTokenizer tknzr, ScreenRegion screenRegion){
		if (tknzr.hasActionWord(ActionDictionary.TYPE)){
			TargetIterpreter targetIterpreter = new TargetIterpreter();			
			if (targetIterpreter.interpret(tknzr, screenRegion)){
				SlideElement targetElement = targetIterpreter.getTargetElement();
				TypeAction typeAction = new TypeAction();
				typeAction.setText(targetElement.getText());
				return new FindDoAction(targetIterpreter.getTarget(), typeAction);
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