package org.sikuli.slides.api;

import java.util.List;
import java.util.Map;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.ActionExecutionException;
import org.sikuli.slides.interpreters.DefaultInterpreter;
import org.sikuli.slides.interpreters.Interpreter;
import org.sikuli.slides.models.Slide;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AutomationExecutor implements SlidesExecutor {

	@Override
	public void execute(List<Slide> slides) throws SlideExecutionException {
		ScreenRegion screenRegion = new DesktopScreenRegion();		
		Interpreter interpreter = new DefaultInterpreter(screenRegion);
		
		List<Action> actions = Lists.newArrayList();
		Map<Slide,Action> slideActionMap = Maps.newHashMap();
		for (Slide slide : slides){
			Action action = interpreter.interpret(slide);
			actions.add(action);
			slideActionMap.put(slide, action);
		}				
		
		Context context = new Context();
		context.setScreenRegion(screenRegion);
		
		for (Slide slide : slides) {
			try {				
				Action action = slideActionMap.get(slide);
				if (action != null){
					action.execute(context);
				}
			} catch (ActionExecutionException e) {
				throw new SlideExecutionException(slide, e.getAction());
			}
		}
	}

}
