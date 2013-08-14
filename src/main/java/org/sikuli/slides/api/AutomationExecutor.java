package org.sikuli.slides.api;

import java.util.List;
import java.util.Map;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.Interpreter;
import org.sikuli.slides.api.models.Slide;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AutomationExecutor implements SlidesExecutor {
	
	
	private Context context;

	public AutomationExecutor(Context context){
		this.context = context;
	}
	
	public AutomationExecutor(){
		context = new Context();
	}

	@Override
	public void execute(List<Slide> slides) throws SlideExecutionException {
				
		Interpreter interpreter = new DefaultInterpreter();
		
		List<Action> actions = Lists.newArrayList();
		Map<Slide,Action> slideActionMap = Maps.newHashMap();
		for (Slide slide : slides){
			Action action = interpreter.interpret(slide);
			actions.add(action);
			slideActionMap.put(slide, action);
		}		
		
		for (Slide slide : slides) {
			if (!context.getSlideSelector().accept(slide))
				continue;			
			
			try {				
				Action action = slideActionMap.get(slide);
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
