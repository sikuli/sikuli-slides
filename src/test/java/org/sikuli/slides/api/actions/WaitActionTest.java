package org.sikuli.slides.api.actions;



import org.jnativehook.NativeHookException;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.mocks.MockTargetFactory;


public class WaitActionTest {

	private Context context;

	@Before
	public void setUp() throws NativeHookException{
		context = new Context();
	}

	@Test(timeout = 2500)
	public void testCanFinishWhenTargetAppearsAfter2Seconds() throws ActionExecutionException {	
		Target target = MockTargetFactory.canBeFoundAfter(2000);
		Action action = new WaitAction(target);
		action.execute(context);
	}
	
	@Test(timeout = 2500, expected = ActionExecutionException.class)
	public void testCanThrowExceptionWhenTargetDoesNotAppearsAfter2Seconds() throws ActionExecutionException {	
		Target target = MockTargetFactory.canBeFoundAfter(3000);
		WaitAction action = new WaitAction(target);
		action.setDuration(1000);
		action.execute(context);
	}
	
	@Test(timeout = 1500)
	public void testCanStopEarlierBeforeTimeout() throws ActionExecutionException {		

		Target target = MockTargetFactory.canBeFoundAfter(10000);
		final WaitAction action = new WaitAction(target);
		action.setDuration(5000);		
		
		new Thread(){
			public void run(){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				action.stop();
			}
		}.start();
		
		action.execute(context);
	}
	

}
