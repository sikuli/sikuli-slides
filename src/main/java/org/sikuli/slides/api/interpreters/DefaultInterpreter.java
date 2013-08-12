package org.sikuli.slides.api.interpreters;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.sikuli.api.API;
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
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.actions.TypeAction;
import org.sikuli.slides.api.actions.DelayAction;
import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;
import org.sikuli.slides.sikuli.ContextualImageTarget;
import org.sikuli.slides.utils.UnitConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DefaultInterpreter implements Interpreter {

	Logger logger = LoggerFactory.getLogger(DefaultInterpreter.class);

	Action interpretAsClick(Slide slide){				
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.CLICK).first();
		if (keywordElement == null)
			return null;		
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

		Target target = interpretAsTarget(slide);
		if (target == null)
			return null;

		return new ExistAction(target);
	}

	Action interpretAsNotExist(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.NOT_EXIST).first();
		if (keywordElement == null)
			return null;		

		Target target = interpretAsTarget(slide);
		if (target == null)
			return null;

		
		return new NotExistAction(target);
	}

	Action interpretAsLabel(Slide slide){

		// if there's a keyword on the slide, do not interpret text as labels
		if (slide.select().isKeyword().exist())
			return null;

		SlideElement textElement = slide.select().hasText().first();
		if (textElement == null)
			return null;

		LabelAction action = new LabelAction();
		action.setText(textElement.getText());
		double fontSize = UnitConverter.WholePointsToPoints(textElement.getTextSize());
		action.setFontSize((int)fontSize);
		
		try {
			Color bgColor = Color.decode("#" + textElement.getBackgroundColor());		
			action.setBackgroundColor(bgColor);
		}catch(NumberFormatException e){
			
		}

		// check if the text intersects with an image
		ImageElement image = (ImageElement) slide.select().intersects(textElement).isImage().first();
		if (image != null){			
			Target target = createTarget(image, textElement);			
			return new TargetAction(target, action, null);						
		}else{			
			return action;
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

	Target interpretAsTarget(Slide slide){
		SlideElement targetElement = slide.select().isTarget().first();
		if (targetElement == null)
			return null;

		ImageElement imageElement = (ImageElement) slide.select().intersects(targetElement).isImage().first();
		if (imageElement == null)
			return null;

		return createTarget(imageElement, targetElement);		
	}

	Action interpretAsTargetAction(Slide slide, Action doAction){
		Target target = interpretAsTarget(slide);			
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

	Action interpretAsWait(Slide slide){
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.DELAY).first();
		if (keywordElement == null)
			return null;

		SlideElement textElement = slide.select().hasText().first();
		if (textElement == null)
			return null;


		String arg = textElement.getText();
		
		DelayAction a = new DelayAction();

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

		logger.error("Error: Please enter the wait time value in a shape."
				+" Valid examples include: 10 seconds, 10 minutes, 10 hours, or even 2 days.");


		return null;
	}	

	@Override
	public Action interpret(Slide slide){

		Action action = null;
		if ((action = interpretAsClick(slide)) != null){			

		}else if ((action = interpretAsExist(slide)) != null){

		}else if ((action = interpretAsRightClick(slide)) != null){			

		}else if ((action = interpretAsDoubleClick(slide)) != null){			

		}else if ((action = interpretAsDoubleClick(slide)) != null){
			
		}else if ((action = interpretAsDrag(slide)) != null){		

		}else if ((action = interpretAsDrop(slide)) != null){		

		}else if ((action = interpretAsBrowser(slide)) != null){		

		}else if ((action = interpretAsLabel(slide)) != null){

		}else if ((action = interpretAsNotExist(slide)) != null){		
		
		}else if ((action = interpretAsWait(slide)) != null){
		
		}
		return action;		
	}
}
