package org.sikuli.slides.api.actions;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.sikuli.slides.api.Context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ParallelActionTest {
	
	@Test(timeout = 2000)
	public void testCanStopEarlier() throws ActionExecutionException {
		Context context = new Context();
		Action action = mock(Action.class);
		
		final ParallelAction parallel = new ParallelAction();
		parallel.addChild(action);
		parallel.addChild(new SleepAction(5000));
		
		// invoke stop after 1 second
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {				
				parallel.stop();
			}			
		};
		timer.schedule(task,  1000);		
		parallel.execute(context);	
	}
	
	@Test
	public void testCanWaitForLongestActionToFinish() throws ActionExecutionException {
		Context context = new Context();
		Action action = mock(Action.class);
		
		final ParallelAction parallel = new ParallelAction();
		parallel.addChild(action);
		parallel.addChild(new SleepAction(2000));
		parallel.addChild(new SleepAction(1000));
		
		
		long startTime = System.currentTimeMillis();
		parallel.execute(context);		
		long elapsedTime = System.currentTimeMillis() - startTime;
		
		assertThat(elapsedTime,  greaterThanOrEqualTo(1900L));
		assertThat(elapsedTime,  lessThan(2100L));
		
		verify(action).execute(any(Context.class));
	}
	
	@Test(timeout = 2000)
	public void testCanStopBackgroundActionWhenFinishedNormally() throws ActionExecutionException {
		Context context = new Context();
		
		final ParallelAction parallel = new ParallelAction();		
		parallel.addChild(new SleepAction(1000));
		parallel.addChild(new SleepAction(500));
		
		Action background = new SleepAction(10000);
		Action spy = spy(background);		
		parallel.addChildAsBackground(spy);
		
		parallel.execute(context);
				
		verify(spy).execute(any(Context.class));
		verify(spy).stop();
	}
	
	@Test(timeout = 2000)
	public void testCanStopBackgroundActionOnException() throws ActionExecutionException {
		Context context = new Context();
		Action badAction = mock(Action.class);
		ActionExecutionException exception = mock(ActionExecutionException.class);
		doThrow(exception).when(badAction).execute(any(Context.class));

		
		final ParallelAction parallel = new ParallelAction();		
		parallel.addChild(badAction);
		
		Action background = new SleepAction(10000);
		Action backgroundSpy = spy(background);		
		parallel.addChildAsBackground(backgroundSpy);
		
		try{
			parallel.execute(context);
		}catch(ActionExecutionException e){
			
		}
				
		verify(backgroundSpy).execute(any(Context.class));
		verify(backgroundSpy).stop();
	}
	
	@Test
	public void testCanExecuteMultipleActions() throws ActionExecutionException {
		Context context = new Context();
		Action action1 = mock(Action.class);
		Action action2 = mock(Action.class);
		Action action3 = mock(Action.class);
		
		final ParallelAction parallel = new ParallelAction();
		parallel.addChild(action1);
		parallel.addChild(action2);
		parallel.addChild(action3);
		
		parallel.execute(context);		
		
		verify(action1).execute(any(Context.class));
		verify(action2).execute(any(Context.class));
		verify(action3).execute(any(Context.class));
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testCanThrowExecutionException() throws ActionExecutionException {
		Context context = new Context();
		Action badAction = mock(Action.class);
		Action goodAction = mock(Action.class);
		ActionExecutionException exception = mock(ActionExecutionException.class);
		doThrow(exception).when(badAction).execute(any(Context.class));
		
		
		final ParallelAction parallel = new ParallelAction();
		parallel.addChild(badAction);
		parallel.addChild(goodAction);
		
		parallel.execute(context);
	}

}
