package org.sikuli.slides.api.actions;

import org.junit.Test;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.DelayAction;
import org.sikuli.slides.api.mocks.AppearLaterTarget;
import org.sikuli.slides.api.mocks.NeverFoundTarget;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ActionTest {
	
	@Test
	public void testDelayAction(){
		DelayAction waitAction = new DelayAction();
		waitAction.setDuration(1000);
		
		Context context = new Context();
				
		long startTime = System.currentTimeMillis();
		waitAction.execute(context);
		long elapsedTime = System.currentTimeMillis() - startTime;
		
		assertThat(elapsedTime,  greaterThanOrEqualTo(1000L));
		assertThat(elapsedTime,  lessThan(1100L));
		
		waitAction.setDuration(500);
		
		startTime = System.currentTimeMillis();
		waitAction.execute(context);
		elapsedTime = System.currentTimeMillis() - startTime;
		
		assertThat(elapsedTime,  greaterThanOrEqualTo(500L));
		assertThat(elapsedTime,  lessThan(600L));
	}
	
	@Test
	public void testParallelAction() throws ActionExecutionException{
		
		Context context = new Context();
	
		// thread 1: display a label for 5 seconds
		LabelAction labelAction = new LabelAction();
		labelAction.setText("Click here after about 3 seconds");
		labelAction.setFontSize(15);
		labelAction.setDuration(5000);
		
		// thread 2: click on a taget that will appear after about 3 seconds
		LeftClickAction clickAction = new LeftClickAction();
		Target target = new AppearLaterTarget(3000);
		TargetAction targetAction = new TargetAction(target, clickAction);
		
		ParallelAction parallelAction = new ParallelAction();
		parallelAction.addAction(targetAction);
		parallelAction.addAction(labelAction);
		parallelAction.execute(context);		
	}
	
	
	@Test(expected = ActionExecutionException.class)
	public void testParallelActionFailed() throws ActionExecutionException {
		
		Context context = new Context();
	
		// thread 1: display a label for 5 seconds
		LabelAction labelAction = new LabelAction();
		labelAction.setText("Click here after about 3 seconds");
		labelAction.setFontSize(15);
		labelAction.setDuration(5000);
		
		// thread 2: click on a target that will not be found, which should throw a
		// ActionExecutionException
		LeftClickAction clickAction = new LeftClickAction();
		Target target = new NeverFoundTarget();
		TargetAction targetAction = new TargetAction(target, clickAction);
		
		ParallelAction parallelAction = new ParallelAction();
		parallelAction.addAction(targetAction);
		parallelAction.addAction(labelAction);
		parallelAction.execute(context);
	
	}

}
