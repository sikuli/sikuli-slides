package org.sikuli.slides.api.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.sikuli.api.DefaultRegion;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.DelayAction;
import org.sikuli.slides.api.mocks.AppearLaterTarget;
import org.sikuli.slides.api.mocks.NeverFoundTarget;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ActionTest {
	
	@Test(timeout = 2000)
	public void testPauseActionCanBeUnpausedByClick() throws ActionExecutionException{
		final Context context = new Context();
		
		// this will try to click in the middle of the screen after 1 sec.
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				Mouse mouse = new DesktopMouse();
				mouse.click(context.getScreenRegion().getLowerRightCorner());
			}			
		};
		timer.schedule(task,  1000);
		
		PauseAction pauseAction = new PauseAction();
		pauseAction.execute(context);		
	}
	
	@Test
	public void testDelayAction() throws ActionExecutionException{
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
		
		// thread 2: click on a target that will appear after about 3 seconds
		LeftClickAction clickAction = new LeftClickAction();
		Target target = new AppearLaterTarget(3000);
		TargetAction targetAction = new TargetAction(target, clickAction);
		
		ParallelAction parallelAction = new ParallelAction();
		parallelAction.addChild(targetAction);
		parallelAction.addChild(labelAction);
		parallelAction.execute(context);		
	}
	
	@Test
	public void testParallelActionMultiLabels() throws ActionExecutionException{
		ScreenRegion desktop = new DesktopScreenRegion();
		Context context = new Context(desktop);
	
		// thread 1: display a label for 5 seconds
		LabelAction labelAction1 = new LabelAction();
		labelAction1.setText("First label appears in the screen center right away");
		labelAction1.setFontSize(15);
		labelAction1.setDuration(5000);
		
		
		// thread 2: display another label for 2 seconds
		LabelAction labelAction2 = new LabelAction();
		labelAction2.setText("Second label appears at a targe that appears after 2 seconds");
		labelAction2.setFontSize(15);
		labelAction2.setDuration(5000);
		ScreenRegion targetRegion = Relative.to(desktop).region(new DefaultRegion(500,100,100,100)).getScreenRegion();
		Target target = new AppearLaterTarget(targetRegion, 2000);
		TargetAction targetAction = new TargetAction(target, labelAction2);
				
		ParallelAction parallelAction = new ParallelAction();
		parallelAction.addChild(targetAction);
		parallelAction.addChild(labelAction1);
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
		parallelAction.addChild(targetAction);
		parallelAction.addChild(labelAction);
		parallelAction.execute(context);
	
	}
	
	@Test
	public void testLabelInRelativeLocation() throws ActionExecutionException{
		Context context = new Context();
		
		LabelAction labelAction1 = new LabelAction();
		labelAction1.setText("First Quadrant");
		labelAction1.setFontSize(15);
		labelAction1.setDuration(2000);

		
		LabelAction labelAction2 = new LabelAction();
		labelAction2.setText("Forth Quadrant");
		labelAction2.setFontSize(15);
		labelAction2.setDuration(2000);

		RelativeAction relativeAction1 = new RelativeAction(0.5,0.5,0.8,0.8, labelAction1);		
		RelativeAction relativeAction2 = new RelativeAction(0.5,0.2,0.8,0.5, labelAction2);		

		ParallelAction parallelAction = new ParallelAction();
		parallelAction.addChild(relativeAction1);
		parallelAction.addChild(relativeAction2);		
		
		parallelAction.execute(context);
		
		assertThat(Actions.select(parallelAction).isInstanceOf(LabelAction.class).all().size(), equalTo(2));
	}
	

}
