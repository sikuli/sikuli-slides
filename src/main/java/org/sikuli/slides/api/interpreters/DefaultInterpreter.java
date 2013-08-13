package org.sikuli.slides.api.interpreters;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.sikuli.api.Relative;
import org.sikuli.api.Target;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.BrowserAction;
import org.sikuli.slides.api.actions.DoubleClickAction;
import org.sikuli.slides.api.actions.DragAction;
import org.sikuli.slides.api.actions.DropAction;
import org.sikuli.slides.api.actions.ExistAction;
import org.sikuli.slides.api.actions.LabelAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.NotExistAction;
import org.sikuli.slides.api.actions.ParallelAction;
import org.sikuli.slides.api.actions.RelativeAction;
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.actions.TypeAction;
import org.sikuli.slides.api.actions.DelayAction;
import org.sikuli.slides.api.actions.WaitAction;
import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;
import org.sikuli.slides.sikuli.ContextualImageTarget;
import org.sikuli.slides.utils.UnitConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DefaultInterpreter implements Interpreter {

	Logger logger = LoggerFactory.getLogger(DefaultInterpreter.class);
	
	class ClickInterpreter {
		
		Action interpret(Slide slide){
			SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.CLICK).first();
			if (keywordElement == null)
				return null;		
			Action action = new LeftClickAction();
			return interpretAsTargetAction(slide, action);
		}
	}

	Action interpretAsClick(Slide slide){				
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.CLICK).first();
		if (keywordElement == null)
			return null;		
		slide.remove(keywordElement);
		Action action = new LeftClickAction();
		return interpretAsTargetAction(slide, action);
	}

	Action interpretAsRightClick(Slide slide){				
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.RIGHT_CLICK).first();
		if (keywordElement == null)
			return null;

		Action action = new RightClickAction();
		return interpretAsTargetAction(slide, action);
	}


	Action interpretAsDoubleClick(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.DOUBLE_CLICK).first();
		if (keywordElement == null)
			return null;

		Action action = new DoubleClickAction();
		return interpretAsTargetAction(slide, action);
	}

	Action interpretAsDrag(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.DRAG).first();
		if (keywordElement == null)
			return null;

		Action action = new DragAction();
		return interpretAsTargetAction(slide, action);
	}

	Action interpretAsDrop(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.DROP).first();
		if (keywordElement == null)
			return null;

		Action action = new DropAction();
		return interpretAsTargetAction(slide, action);
	}

	
	Action interpretAsExist(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.EXIST).first();
		if (keywordElement == null)
			return null;

		Target target = interpretAsImageTarget(slide);
		if (target == null)
			return null;

		return new ExistAction(target);
	}

	Action interpretAsNotExist(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.NOT_EXIST).first();
		if (keywordElement == null)
			return null;		

		Target target = interpretAsImageTarget(slide);
		if (target == null)
			return null;

		
		return new NotExistAction(target);
	}

	Action interpretAsLabel(Slide slide, SlideElement textElement){
		LabelAction action = new LabelAction();
		action.setText(textElement.getText());
		double fontSize = UnitConverter.WholePointsToPoints(textElement.getTextSize());
		action.setFontSize((int)fontSize);
		
		try {
			Color bgColor = Color.decode("#" + textElement.getBackgroundColor());		
			action.setBackgroundColor(bgColor);
		}catch(NumberFormatException e){
			
		}		
		// check if there is an image target nearby
		SlideElement targetElement = slide.select().isTarget().near(textElement, 200000).first();
		if (targetElement == null){	
			double xmin = 1.0 * textElement.getOffx() / 9144000;
			double ymin = 1.0 * textElement.getOffy() / 6858000;
			double xmax = 1.0 * (textElement.getOffx()  + textElement.getCx()) / 9144000;
			double ymax = 1.0 * (textElement.getOffy()  + textElement.getCy()) / 6858000;
			
			RelativeAction relativeAction = new RelativeAction(xmin, ymin, xmax, ymax, action);			
			return relativeAction;
		}else{			
			Target imageTarget = interpretAsImageTarget(slide, targetElement);		
			int offsetX = UnitConverter.emuToPixels(textElement.getOffx() - targetElement.getOffx());
			int offsetY = UnitConverter.emuToPixels(textElement.getOffy() - targetElement.getOffy());
			int width = UnitConverter.emuToPixels(textElement.getCx());
			int height = UnitConverter.emuToPixels(textElement.getCy());
						
			RelativeAction relativeAction = new RelativeAction(offsetX, offsetY, width, height, action);
			TargetAction targetAction = new TargetAction(imageTarget, relativeAction);
			return targetAction;
		}
	}
	
	Action interpretAsLabel(Slide slide){

		// if there's a keyword on the slide, do not interpret text as labels
		if (slide.select().isKeyword().exist())
			return null;

		List<SlideElement> textElements = slide.select().hasText().all();
		if (textElements.size() == 0)
			return null;

		if (textElements.size() == 1){
			return interpretAsLabel(slide, textElements.get(0));
		}else{
			ParallelAction pa = new ParallelAction();
			for (SlideElement textElement : textElements){
				Action labelAction = interpretAsLabel(slide, textElement);
				pa.addAction(labelAction);
			}
			return pa;
		}
	}

	Target createTarget(ImageElement imageElement, SlideElement targetElement){
		if (imageElement == null || targetElement == null)
			return null;

		int w = imageElement.getCx();
		int h = imageElement.getCy();
		if (w <= 0 || h <= 0)
			return null;

		double xmax = 1.0 * (targetElement.getOffx() + targetElement.getCx() - imageElement.getOffx()) / w;
		double ymax = 1.0 * (targetElement.getOffy() + targetElement.getCy() - imageElement.getOffy()) / h;
		double xmin = 1.0 * (targetElement.getOffx() - imageElement.getOffx()) / w;
		double ymin = 1.0 * (targetElement.getOffy() - imageElement.getOffy()) / h;

		xmax = Math.min(1.0, xmax);
		ymax = Math.min(1.0, ymax);
		xmin = Math.max(0, xmin);
		ymin = Math.max(0, ymin);

		return new ContextualImageTarget(imageElement.getSource(), xmin, ymin, xmax, ymax); 
	}

	Target interpretAsImageTarget(Slide slide){
		SlideElement targetElement = slide.select().isTarget().first();
		if (targetElement == null)
			return null;
		return interpretAsImageTarget(slide, targetElement);
	}
	
	Target interpretAsImageTarget(Slide slide, SlideElement targetElement){
		ImageElement imageElement = (ImageElement) slide.select().intersects(targetElement).isImage().first();
		if (imageElement == null)
			return null;

		Target target = createTarget(imageElement, targetElement);
		if (target == null)
			return null;
		return target;
	}

	Action interpretAsTargetAction(Slide slide, Action doAction){
		Target target = interpretAsImageTarget(slide);		
		if (target == null)
			return doAction;				
		return new TargetAction(target, doAction);
	}

	Action interpretAsType(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.TYPE).first();
		if (keywordElement == null)
			return null;

		SlideElement textElement = slide.select().hasText().first();
		if (textElement == null)
			return null;

		TypeAction typeAction = new TypeAction();
		typeAction.setText(textElement.getText());

		return interpretAsTargetAction(slide, typeAction);
	}

	Action interpretAsBrowser(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.BROWSER).first();
		if (keywordElement == null)
			return null;

		SlideElement textElement = slide.select().hasText().first();
		if (textElement == null)
			return null;

		URL url = null;
		try {
			url = new URL(textElement.getText());
		} catch (MalformedURLException e) {
			return null;
		}				

		BrowserAction a = new BrowserAction();
		a.setUrl(url);
		return a;		
	}	
	
	
	
	Long interpretAsDuration(Slide slide){
		SlideElement textElement = slide.select().hasText().first();
		if (textElement == null)
			return null;

		String arg = textElement.getText();
		
		// extract the time unit
		TimeUnit timeUnit = UnitConverter.extractTimeUnitFromString(arg);
		// if the time unit was not specified, default to seconds
		if(timeUnit==null){
			timeUnit=TimeUnit.SECONDS;
		}
		// extract the wait time string value, replace all non digits with blank
		String waitTimeString = arg.replaceAll("[^0-9.]", "");
		if(waitTimeString != null){
			double duration = Double.parseDouble(waitTimeString);
			Double durationInMilliSeconds = 0.0;
			if (timeUnit == TimeUnit.SECONDS){
				durationInMilliSeconds = duration * 1000;
			}else if (timeUnit == TimeUnit.MINUTES){
				durationInMilliSeconds = duration * 1000 * 60;
			}else if (timeUnit == TimeUnit.HOURS){
				durationInMilliSeconds = duration * 1000 * 60 * 60;
			}			
			return durationInMilliSeconds.longValue();
		}else{			
			
			logger.error("Error: Please write a valid time string."
					+" Valid examples include: 10 milliseconds, 10 seconds, 10 minutes.");

			return null;
		}
	}

	Action interpretAsDelay(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.DELAY).first();
		if (keywordElement == null)
			return null;

		Long duration = interpretAsDuration(slide);
		if (duration == null)
			return null;		

		DelayAction a = new DelayAction();
		a.setDuration(duration);
		return a;
	}	
	
	Action interpretAsWait(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.WAIT).first();
		if (keywordElement == null)
			return null;

		Target target = interpretAsImageTarget(slide);
		if (target == null)
			return null;

		Long duration = interpretAsDuration(slide);
		if (duration == null)
			return null;		

		WaitAction a = new WaitAction(target);
		a.setDuration(duration);
		return a;
	}	

	@Override
	public Action interpret(Slide inputSlide){
		
		// make a copy of the slide to work on
		Slide slide = new Slide(inputSlide);

		Action keywordAction = null;		
		if ((keywordAction = interpretAsClick(slide)) != null){			

		}else if ((keywordAction = interpretAsExist(slide)) != null){

		}else if ((keywordAction = interpretAsRightClick(slide)) != null){			

		}else if ((keywordAction = interpretAsDoubleClick(slide)) != null){			

		}else if ((keywordAction = interpretAsDoubleClick(slide)) != null){

		}else if ((keywordAction = interpretAsDrag(slide)) != null){		

		}else if ((keywordAction = interpretAsDrop(slide)) != null){		

		}else if ((keywordAction = interpretAsBrowser(slide)) != null){		

		}else if ((keywordAction = interpretAsNotExist(slide)) != null){		

		}else if ((keywordAction = interpretAsDelay(slide)) != null){

		}else if ((keywordAction = interpretAsWait(slide)) != null){
		
		}else if ((keywordAction = interpretAsType(slide)) != null){
			
			}
		
		Action labelAction = interpretAsLabel(slide);		
		if (labelAction != null && keywordAction != null){
			ParallelAction parallelAction = new ParallelAction();
			parallelAction.addAction(labelAction);
			parallelAction.addAction(keywordAction);
			return parallelAction; 
		}else if (labelAction != null){
			return labelAction;
		}else if (keywordAction != null){
			return keywordAction;
		}
		return null;
	}
}
