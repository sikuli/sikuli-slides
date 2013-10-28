package org.sikuli.slides.api.actions;

import org.junit.Test;
import org.sikuli.slides.api.Context;

public class LabelActionTest {
		
	@Test
	public void testCanDisplayAndStop() throws InterruptedException, ActionExecutionException {
		final Context context = new Context();		
		LabelAction labelAction = new LabelAction();
		labelAction.setText("This label should display for 2 seconds");
		labelAction.setFontSize(15);		
		labelAction.execute(context);
		Thread.sleep(2000);
		labelAction.stop();
	}
	
	@Test
	public void testLabelInRelativeLocation() throws ActionExecutionException{
		Context context = new Context();
		
		LabelAction labelAction1 = new LabelAction();
		labelAction1.setText("Lower Right");
		labelAction1.setFontSize(15);
		
		LabelAction labelAction2 = new LabelAction();
		labelAction2.setText("Upper Right");
		labelAction2.setFontSize(15);

		RelativeAction relativeAction1 = new RelativeAction(0.5,0.5,0.8,0.8, labelAction1);		
		RelativeAction relativeAction2 = new RelativeAction(0.5,0.2,0.8,0.5, labelAction2);		

		ParallelAction parallelAction = new ParallelAction();
		parallelAction.addChild(relativeAction1);
		parallelAction.addChild(relativeAction2);		
		parallelAction.addChild(new SleepAction(3000));
		
		parallelAction.execute(context);			
	}
}
