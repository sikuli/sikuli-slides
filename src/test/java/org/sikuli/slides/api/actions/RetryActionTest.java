package org.sikuli.slides.api.actions;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.sikuli.slides.api.Context;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RetryActionTest {
	
	
	@Test
	public void testCanSucceedNormally() throws ActionExecutionException {
		Context context = new Context();
		Action action = mock(Action.class);
		
		final RetryAction retry = new RetryAction(action, 2000, 500);
		retry.execute(context);	
		
		verify(action).execute(any(Context.class));
	}
	
	@Test
	public void testCanRetryUntilTimeout() throws ActionExecutionException {
		Context context = new Context();
		Action action = mock(Action.class);
		ActionExecutionException exception = mock(ActionExecutionException.class);
		doThrow(exception).when(action).execute(any(Context.class));
		
		final RetryAction retry = new RetryAction(action, 2000, 500);
		try {
			retry.execute(context);
		} catch (ActionExecutionException e) {
		}	
		
		verify(action, atLeast(3)).execute(any(Context.class));
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testCanThrowExceptionWhenTimeout() throws ActionExecutionException {
		Context context = new Context();
		Action action = mock(Action.class);
		ActionExecutionException exception = mock(ActionExecutionException.class);
		doThrow(exception).when(action).execute(any(Context.class));
		
		final RetryAction retry = new RetryAction(action, 1000, 500);
		retry.execute(context);
	}
	
	
	class DelayedSuccessAction extends ConfigAction {
		long startTime;
		long delayTime;
		DelayedSuccessAction(long delayTime){
			this.startTime = System.currentTimeMillis();
			this.delayTime = delayTime;
		}
		@Override
		public void execute(Context context) throws ActionExecutionException {			
			if ((System.currentTimeMillis() - startTime) < delayTime){
				throw new ActionExecutionException("", this);
			}
		} 		
	}
	
	@Test
	public void testCanRetryUntilSuccessful() throws ActionExecutionException {
		Context context = new Context();
		Action action = new DelayedSuccessAction(500);
		Action spy = spy(action);
		
		final RetryAction retry = new RetryAction(spy, 5000, 500);
		try {
			retry.execute(context);
		} catch (ActionExecutionException e) {
		}	
		
		verify(spy, atLeast(1)).execute(any(Context.class));
		verify(spy, atMost(3)).execute(any(Context.class));
	}
	
	@Test(timeout = 2000)
	public void testCanStopBeforeTimeout() throws ActionExecutionException {
		Context context = new Context();
		Action action = mock(Action.class);
		ActionExecutionException exception = mock(ActionExecutionException.class);
		doThrow(exception).when(action).execute(any(Context.class));
		
		final RetryAction retry = new RetryAction(action, 10000, 500);
		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				retry.stop();				
			}			
		};
		Timer timer = new Timer();
		timer.schedule(task, 1000);	
		
		retry.execute(context);
	}

}
