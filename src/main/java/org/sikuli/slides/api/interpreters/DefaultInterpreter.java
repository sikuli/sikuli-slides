package org.sikuli.slides.api.interpreters;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.AssertExistAction;
import org.sikuli.slides.api.actions.AssertNotExistAction;
import org.sikuli.slides.api.actions.BookmarkAction;
import org.sikuli.slides.api.actions.BrowserAction;
import org.sikuli.slides.api.actions.CompoundAction;
import org.sikuli.slides.api.actions.DoubleClickAction;
import org.sikuli.slides.api.actions.DragAction;
import org.sikuli.slides.api.actions.DropAction;
import org.sikuli.slides.api.actions.EmptyAction;
import org.sikuli.slides.api.actions.LabelAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.OptionalAction;
import org.sikuli.slides.api.actions.ParallelAction;
import org.sikuli.slides.api.actions.PauseAction;
import org.sikuli.slides.api.actions.RetryAction;
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.SleepAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.actions.TypeAction;
import org.sikuli.slides.api.actions.WaitAction;
import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;
import org.sikuli.slides.api.sikuli.ContextImageTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;



public class DefaultInterpreter implements Interpreter {

	static Logger logger = LoggerFactory.getLogger(DefaultInterpreter.class);
        private final Context context; // The context created by the user for parsing / intepreting slides

        public DefaultInterpreter(Context context){
            this.context = context;
        }
        
	static class RegexActionInterpreter implements Interpreter {

		RegexActionInterpreter(String regex){
			this.regex = regex;
		}

		String regex;			
		String[] arguments;
		SlideElement element;		

		boolean parse(Slide slide){
			element = slide.select().textMatches(regex).first();
			if (element == null)
				return false;

			String text = element.getText();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(text);
			if (!matcher.find())
				return false;

			arguments = new String[matcher.groupCount()];
			for (int i = 0 ; i < matcher.groupCount(); i++){
				// note: groups are 1-indexed
				arguments[i] = matcher.group(i+1);
			}

			return true;
		}			

		@Override
		final public Action interpret(Slide slide) {			
			if (!parse(slide))
				return null;

			Action action = interpret(slide, element, arguments);		
			if (action == null)
				return null;

			slide.remove(element);
			return action;
		}

		/**		 
		 * 
		 * 
		 * @param slide the source slide
		 * @param element the element whose text content matches the regular expression
		 * @param arguments array of string arguments (not including the keyword)
		 * @return
		 */
		protected Action interpret(Slide slide, SlideElement element, String[] arguments){
			return null;
		}
	}

	static class SleepActionInterpreter extends RegexActionInterpreter {

		SleepActionInterpreter(){
			super("(?i)sleep\\s+(\\d+)");
		}

		@Override
		protected Action interpret(Slide slide, SlideElement element, String[] arguments){	
			if (arguments.length != 1)
				return null;

			String durationString = arguments[0];
			Long duration = parseDuration(durationString);		
			if (duration == null)
				return null;			
			return new SleepAction(duration);
		}
	};


	static class BrowseActionInterpreter extends RegexActionInterpreter {

                private final Context context;
                
		BrowseActionInterpreter(Context context){
			super("(?i)(browse|open)\\s+(.+)");
                        this.context = context;
		}

		@Override
		protected Action interpret(Slide slide, SlideElement element, String[] arguments){	
			if (arguments.length != 2)
				return null;
                        // If there is a context, check to see if the URL entered is a context parameter or is plain text
			String urlString;
			if (context != null){
                            urlString = context.render(arguments[1]);
                        } else {
                            urlString = arguments[1];
                        }
                        
                        URL url = null;
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {
				return null;
			}				

			BrowserAction action = new BrowserAction();
			action.setUrl(url);
			return action;
		}		
	}

	static class TypeActionInterpreter extends RegexActionInterpreter {

		TypeActionInterpreter(){
			super("(?i)type\\s+(.+)");
		}

		@Override
		protected Action interpret(Slide slide, SlideElement element, String[] arguments){	
			if (arguments.length != 1)
				return null;

			TypeAction action = new TypeAction();
			action.setText(arguments[0]);
			return action;
		}		
	}

	static class WaitActionInterpreter extends RegexActionInterpreter {

		WaitActionInterpreter(){
			super("(?i)wait\\s*(\\d*)");
		}

		@Override
		protected Action interpret(Slide slide, SlideElement element, String[] arguments){
			Long duration;

			String durationString = arguments[0];
			if (durationString.length() == 0){
				duration = Long.MAX_VALUE;
			}else{
				duration = parseDuration(durationString);					
			}

			if (duration == null)
				return null;		


			Target target = (new ContextImageTargetInterpreter()).interpret(slide);
			if (target == null)
				return null;
			WaitAction action = new WaitAction(target);
			action.setDuration(duration);
			return action;
		}		
	}	

	static class LeftClickActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {
			SlideElement keywordElement = slide.select().ignoreCase().textStartsWith("click").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);
			Action action = new LeftClickAction();
			return action;
		}		
	}

	static class RightClickActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {
			SlideElement keywordElement = slide.select().ignoreCase().textStartsWith("rightclick").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);
			Action action = new RightClickAction();
			return action;
		}		
	}

	static class DoubleClickActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {
			SlideElement keywordElement = slide.select().ignoreCase().textStartsWith("doubleclick").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);
			Action action = new DoubleClickAction();
			return action;
		}		
	}

	static class DragActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {
			SlideElement keywordElement = slide.select().ignoreCase().textStartsWith("drag").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);
			Action action = new DragAction();
			return action;
		}		
	}

	static class DropActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {
			SlideElement keywordElement = slide.select().ignoreCase().textStartsWith("drop").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);
			Action action = new DropAction();
			return action;
		}		
	}

	static class ExistActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {			
			SlideElement keywordElement = slide.select().ignoreCase().textStartsWith("exist").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);
			
			Target target = (new ContextImageTargetInterpreter()).interpret(slide);
			if (target == null)
				return null;
			return new AssertExistAction(target);
		}
	}

	static class NotExistActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {			

			SlideElement keywordElement = slide.select().ignoreCase().textStartsWith("not exist").first();
			if (keywordElement == null)
				return null;
			slide.remove(keywordElement);

			Target target = (new ContextImageTargetInterpreter()).interpret(slide);
			if (target == null)
				return null;
			return new AssertNotExistAction(target);
		}
	}

	static class TargetActionInterpreter implements Interpreter {

		private List<Interpreter> actionInterpreters;

		TargetActionInterpreter(){
			actionInterpreters = Lists.newArrayList();
			actionInterpreters.add(new LeftClickActionInterpreter());	
			actionInterpreters.add(new DoubleClickActionInterpreter());	
			actionInterpreters.add(new RightClickActionInterpreter());
			actionInterpreters.add(new DragActionInterpreter());
			actionInterpreters.add(new DropActionInterpreter());		
			actionInterpreters.add(new TypeActionInterpreter());
		}

		@Override
		public Action interpret(Slide slide) {			

			Action action = null;
			Iterator<Interpreter> iter = actionInterpreters.iterator();
			while (action == null && iter.hasNext()){
				action = iter.next().interpret(slide);		
			}

			if (action == null)
				return null;

			Target target = (new ContextImageTargetInterpreter()).interpret(slide);
			if (target == null)
				return null;
			return new TargetAction(target, action);
		}
	}
	


	public static class ContextImageTargetInterpreter implements TargetInterpreter {

		public Target interpret(Slide slide, SlideElement targetElement) {

			ImageElement imageElement = (ImageElement) slide.select().intersects(targetElement).isImage().first();
			if (imageElement == null)
				return null;

			Target target = createTarget(imageElement, targetElement);
			if (target == null)
				return null;
			return target;
		}	

		@Override
		public Target interpret(Slide slide) {

			SlideElement targetElement = slide.select().isTarget().first();
			if (targetElement == null)
				return null;

			return interpret(slide, targetElement);
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

			return new ContextImageTarget(imageElement.getSource(), xmin, ymin, xmax, ymax); 
		}
	}



	static class ScreenLocationInterpreter implements SpatialRelationshipInterpreter{

		@Override
		public SpatialRelationship interpret(Slide slide, SlideElement element) {			
			final double xmin = 1.0 * element.getOffx() / 9144000;
			final double ymin = 1.0 * element.getOffy() / 6858000;
			final double xmax = 1.0 * (element.getOffx()  + element.getCx()) / 9144000;
			final double ymax = 1.0 * (element.getOffy()  + element.getCy()) / 6858000;			
			return new SpatialRelationship(){
				@Override
				public ScreenRegion apply(Context input) {
					ScreenRegion screenRegion = input.getScreenRegion();
					return Relative.to(screenRegion).region(xmin, ymin, xmax, ymax).getScreenRegion();						
				}				
			};
		}
	}

	static class TargetLocationInterpreter implements SpatialRelationshipInterpreter{

		@Override
		public SpatialRelationship interpret(Slide slide, SlideElement element) {		

			SlideElement targetElement = slide.select().isTarget().near(element, 200000).first();
			if (targetElement == null)
				return null;

			final Target target = (new ContextImageTargetInterpreter()).interpret(slide, targetElement);
			if (target == null)
				return null;

			final int x = UnitConverter.emuToPixels(element.getOffx() - targetElement.getOffx());
			final int y = UnitConverter.emuToPixels(element.getOffy() - targetElement.getOffy());
			final int width = UnitConverter.emuToPixels(element.getCx());
			final int height = UnitConverter.emuToPixels(element.getCy());

			return new SpatialRelationship(){
				@Override
				public ScreenRegion apply(Context context) {
					ScreenRegion region = context.getScreenRegion().find(target);
					if (region != null)
						return Relative.to(region).region(x,y,width,height).getScreenRegion();
					else
						return null;
				}
			};
		}
	}

	static class LabelInterpreter implements Interpreter{
		@Override
		public Action interpret(Slide slide) {
			SlideElement textElement = slide.select().isNotKeyword().hasText().first();
			if (textElement == null)
				return null;

			LabelAction action = new LabelAction();
			action.setSlideElement(textElement);
			action.setText(textElement.getText());
			double fontSize = UnitConverter.WholePointsToPoints(textElement.getTextSize());
			action.setFontSize((int)fontSize);

			try {
				Color bgColor = Color.decode("#" + textElement.getBackgroundColor());		
				action.setBackgroundColor(bgColor);
			}catch(NumberFormatException e){

			}

			List<SpatialRelationshipInterpreter> locations =
					Lists.newArrayList(new TargetLocationInterpreter(), new ScreenLocationInterpreter());

			SpatialRelationship spatial = null;
			Iterator<SpatialRelationshipInterpreter> iter = locations.iterator();
			while (iter.hasNext() && spatial == null){
				spatial = iter.next().interpret(slide, textElement);
			}

			if (spatial != null)
				action.setSpatialRelationship(spatial);
			slide.remove(textElement);			
			return action;
		}
	}

	static class SkipActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {
			SlideElement keywordElement = slide.select().textMatches("(?i)skip").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);
			return new EmptyAction();
		}		
	}

	static class OptionalActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {
			SlideElement keywordElement = slide.select().textMatches("(?i)optional").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);
			return new OptionalAction();
		}		
	}

	static class PauseActionInterpreter implements Interpreter {
		@Override
		public Action interpret(Slide slide) {			
			SlideElement keywordElement = slide.select().textMatches("(?i)pause").first();
			if (keywordElement == null)
				return null;		
			slide.remove(keywordElement);			
			return new PauseAction();
		}		
	}

	static class BookmarkActionInterpreter extends RegexActionInterpreter {

		BookmarkActionInterpreter(){
			super("(?i)bookmark\\s+([\\S]+)");
		}

		@Override
		protected Action interpret(Slide slide, SlideElement element, String[] arguments){	
			if (arguments.length != 1)
				return null;

			BookmarkAction action = new BookmarkAction();
			action.setName(arguments[0]);
			return action;
		}		
	}


	//	Action interpretAsLabel(Slide slide){
	//		List<SlideElement> textElements = slide.select().isNotKeyword().hasText().all();
	//		if (textElements.size() == 0)
	//			return null;
	//
	//		if (textElements.size() == 1){
	//			return interpretAsLabel(slide, textElements.get(0));
	//		}else{
	//			ParallelAction pa = new ParallelAction();
	//			for (SlideElement textElement : textElements){
	//				Action labelAction = interpretAsLabel(slide, textElement);
	//				pa.addChild(labelAction);
	//			}
	//			return pa;
	//		}
	//	}



	//	Target interpretAsImageTarget(Slide slide){
	//		SlideElement targetElement = slide.select().isTarget().first();
	//		if (targetElement == null)
	//			return null;
	//		return interpretAsImageTarget(slide, targetElement);
	//	}

	//	Target interpretAsImageTarget(Slide slide, SlideElement targetElement){
	//		ImageElement imageElement = (ImageElement) slide.select().intersects(targetElement).isImage().first();
	//		if (imageElement == null)
	//			return null;
	//
	//		Target target = createTarget(imageElement, targetElement);
	//		if (target == null)
	//			return null;
	//		return target;
	//	}

	//	Action interpretAsTargetAction(Slide slide, Action doAction){
	//		Target target = interpretAsImageTarget(slide);		
	//		if (target == null)
	//			return doAction;				
	//		return new RetryAction(new TargetAction(target, doAction),5000, 500);
	//	}

	//	Action interpretAsType(Slide slide){
	//		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.TYPE).first();
	//		if (keywordElement == null)
	//			return null;
	//
	//		SlideElement textElement = slide.select().hasText().first();
	//		if (textElement == null)
	//			return null;
	//
	//		TypeAction typeAction = new TypeAction();
	//		typeAction.setText(textElement.getText());
	//
	//		slide.remove(keywordElement);
	//		slide.remove(textElement);
	//
	//		return interpretAsTargetAction(slide, typeAction);
	//	}
	//
	//	Action interpretAsBrowser(Slide slide){
	//		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.BROWSER).first();
	//		if (keywordElement == null)
	//			return null;
	//
	//		SlideElement textElement = slide.select().hasText().first();
	//		if (textElement == null)
	//			return null;
	//
	//		URL url = null;
	//		try {
	//			url = new URL(textElement.getText());
	//		} catch (MalformedURLException e) {
	//			return null;
	//		}				
	//
	//		slide.remove(keywordElement);
	//		slide.remove(textElement);
	//
	//		BrowserAction a = new BrowserAction();
	//		a.setUrl(url);
	//		return a;		
	//	}	
	//


	Long interpretAsDuration(SlideElement textElement){

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

	static Long parseDuration(String inputString){

		// extract the time unit
		TimeUnit timeUnit = UnitConverter.extractTimeUnitFromString(inputString);
		// if the time unit was not specified, default to seconds
		if(timeUnit==null){
			timeUnit=TimeUnit.SECONDS;
		}
		// extract the wait time string value, replace all non digits with blank
		String waitTimeString = inputString.replaceAll("[^0-9.]", "");
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



	//	Action interpretAsSleep(Slide slide){
	//		SlideElement keywordElement = slide.select().ignoreCase().textStartsWith("sleep").first();
	//		if (keywordElement == null)
	//			return null;
	//		
	//		String[] toks = keywordElement.getText().split(" ");
	//		if (toks.length != 2)
	//			return null;
	//		
	//		String durationString = toks[1];
	//		Long duration = parseDuration(durationString);		
	//		if (duration == null)
	//			return null;
	//		
	//		slide.remove(keywordElement);
	//		
	//		SleepAction a = new SleepAction(duration);
	//		return a;
	//	}	

	//	Action interpretAsWait(Slide slide){
	//		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.WAIT).first();
	//		if (keywordElement == null)
	//			return null;
	//
	//		Target target = interpretAsImageTarget(slide);
	//		if (target == null)
	//			return null;
	//
	//		SlideElement textElement = slide.select().hasText().first();
	//		if (textElement == null)
	//			return null;
	//
	//		Long duration = interpretAsDuration(textElement);
	//		if (duration == null)
	//			return null;
	//
	//		slide.remove(keywordElement);
	//		slide.remove(textElement);
	//
	//		WaitAction a = new WaitAction(target);
	//		a.setDuration(duration);
	//		return a;
	//	}

	private Action interpretAsBookmark(Slide slide) {
		SlideElement keywordElement = slide.select().isKeyword(KeywordDictionary.BOOKMARK).first();
		if (keywordElement == null)
			return null;		

		String text = keywordElement.getText(); 
		if (text.isEmpty()){
			logger.error("No name is specified for the bookmark keyword");
			return null;
		}

		slide.remove(keywordElement);
		BookmarkAction action = new BookmarkAction();
		action.setName(text);				
		return action;
	}	

	@Override
	public Action interpret(Slide inputSlide){

		ParallelAction parallelAction = new ParallelAction();

		// make a copy of the slide to work on
		Slide slide = new Slide(inputSlide);		

		List<Interpreter> interpreters = Lists.newArrayList(
				new TargetActionInterpreter(),
				new ExistActionInterpreter(),
				new NotExistActionInterpreter(),
				new BrowseActionInterpreter(context),
				new SleepActionInterpreter(),
				new WaitActionInterpreter()
				);

		List<Interpreter> controlActionInterpreters = Lists.newArrayList(
				new SkipActionInterpreter(),
				new OptionalActionInterpreter(),
				new PauseActionInterpreter(),
				new BookmarkActionInterpreter()
				);


		Action keywordAction = null;
		Iterator<Interpreter> iter = interpreters.iterator();
		while (keywordAction == null && iter.hasNext()){
			keywordAction = iter.next().interpret(slide);		
		}

		if (keywordAction instanceof TargetAction){
			keywordAction = new RetryAction(keywordAction, 10000, 500);
		}

		if (keywordAction != null){
			parallelAction.addChild(keywordAction);
		}

		Action controlAction = null;
		iter = controlActionInterpreters.iterator();
		while (controlAction == null && iter.hasNext()){
			controlAction = iter.next().interpret(slide);
		}

		Interpreter labelInterpreter = new LabelInterpreter();
		Action labelAction;
		while ((labelAction = labelInterpreter.interpret(slide)) != null){
			
			// if there is no foreground action, create a sleep action as the foreground action,
			// this way, the label actions can stop on when the sleep action terminates
			if (!parallelAction.hasForegroundAction()){				
				// TODO: makes the sleep duration customizable
				parallelAction.addChild(new SleepAction(5000));
			}			
			
			labelAction = new RetryAction(labelAction, Long.MAX_VALUE, 500);
			parallelAction.addChildAsBackground(labelAction);
		}


		Action action = parallelAction;


		//		Action controlAction = null;		
		//		if ((controlAction = interpretAsSkip(slide)) != null){			
		//
		//		}else if ((controlAction = interpretAsOptional(slide)) != null){
		//
		//		}else if ((controlAction = interpretAsPause(slide)) != null){
		//
		//		}else if ((controlAction = interpretAsBookmark(slide)) != null){
		//
		//		}

		if (controlAction != null){			
			((CompoundAction) controlAction).addChild(action);
			action = controlAction;
		}		
		
		
		logger.debug("result:" + action);		
		return action;
	}

}
