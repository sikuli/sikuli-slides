package org.sikuli.slides.api;

import java.util.List;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.Interpreter;
import org.sikuli.slides.api.models.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

class AutomationExecutor implements SlidesExecutor {
	
	static Logger logger = LoggerFactory.getLogger(AutomationExecutor.class);
	
	private Context context;

	public AutomationExecutor(Context context){
		this.context = context;
	}
	
	public AutomationExecutor(){
		context = new Context();
	}

	@Override
	public void execute(List<Slide> slides) throws SlideExecutionException {
				
		logger.trace("Execute {} slide(s)", slides.size());
		
		for (int i = 0; i < slides.size(); ++i){
			
			Slide slide = slides.get(i);
			ExecutionEvent event = new ExecutionEvent(slide ,context);
			
			
			if (!context.getExecutionFilter().accept(event)){
				logger.info("Slide {} of {} is skipped", slide.getNumber(), slides.size());				
				continue;							
			}else{ 
				logger.info("Execute slide {} of {}", slide.getNumber(), slides.size());				
				try {				
					slide.execute(context);
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
