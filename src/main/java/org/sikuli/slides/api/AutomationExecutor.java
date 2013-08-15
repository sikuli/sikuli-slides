package org.sikuli.slides.api;

import java.util.List;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.actions.BookmarkAction;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.Interpreter;
import org.sikuli.slides.api.models.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class AutomationExecutor implements SlidesExecutor {
	
	static Logger logger = LoggerFactory.getLogger(AutomationExecutor.class);
	
	private Context context;

	public AutomationExecutor(Context context){
		this.context = context;
	}
	
	public AutomationExecutor(){
		context = new Context();
	}

	static int findFirstMatchedBookmarkLocation(String bookmarkName, List<Action> actions){
		for (int i = 0; i < actions.size(); ++i){
			Action action = actions.get(i);
			BookmarkAction bookmarkAction = (BookmarkAction) Actions.select(action).isInstaceOf(BookmarkAction.class).first();			
			if (bookmarkAction != null && bookmarkAction.getName().compareToIgnoreCase(bookmarkName)==0){
				return i;
			}			
		}
		return -1;		
	}
	
	@Override
	public void execute(List<Slide> slides) throws SlideExecutionException {
				
		Interpreter interpreter = new DefaultInterpreter();		
		List<Action> actions = Lists.newArrayList();
		for (Slide slide : slides){
			Action action = interpreter.interpret(slide);
			actions.add(action);
			logger.debug("Action interpreted: {}", action);
		}

		for (int i = 0; i < slides.size(); ++i){
			
			Slide slide = slides.get(i);
			Action action = actions.get(i);			
			SlideExecutionEvent event = new SlideExecutionEvent(slide,action,context);
			
			if (!context.getSlideSelector().accept(event)){
				logger.debug("Slide {} is skipped", slide.getNumber());
				continue;			
				
			}else{
				logger.debug("Slide {} is executed", slide.getNumber());

				try {				
					//				Action action = slideActionMap.get(slide);
					if (action != null){
						action.execute(context);
					}
				} catch (ActionExecutionException e) {
					SlideExecutionException ex = new SlideExecutionException(e);
					ex.setAction(e.getAction());
					ex.setSlide(slide);
					throw ex;
				}
			}
		}
	}

}
