package org.sikuli.slides.api.actions;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.mockito.InOrder;
import org.sikuli.slides.api.Context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SequentialActionTest {
	
	@Test
	public void testCanExecuteSleepActionsInSequence() throws ActionExecutionException {
		Context context = new Context();
		final SequentialAction seq = new SequentialAction();
		seq.addChild(new SleepAction(1000));
		seq.addChild(new SleepAction(1000));
		seq.addChild(new SleepAction(1000));		
		
		long startTime = System.currentTimeMillis();
		seq.execute(context);		
		long elapsedTime = System.currentTimeMillis() - startTime;
		
		assertThat(elapsedTime,  greaterThanOrEqualTo(2800L));
		assertThat(elapsedTime,  lessThan(3200L));
	}
	
	@Test
	public void testCanExecuteActionsInSequence() throws ActionExecutionException {
		Context context = new Context();
		final SequentialAction seq = new SequentialAction();
		Action action1 = mock(Action.class);
		Action action2 = mock(Action.class);
		Action action3 = mock(Action.class);
		
		seq.addChild(action1);
		seq.addChild(action2);
		seq.addChild(action3);
		
		seq.execute(context);		
		
		InOrder inOrder = inOrder(action1,action2,action3);
		inOrder.verify(action1).execute(any(Context.class));
		inOrder.verify(action2).execute(any(Context.class));
		inOrder.verify(action3).execute(any(Context.class));
	}
	
	
	@Test(timeout = 2000)
	public void testCanStopEarlier() throws ActionExecutionException {
		Context context = new Context();
		Action action1 = mock(Action.class);
		Action action2 = mock(Action.class);
		
		final SequentialAction seq = new SequentialAction();
		seq.addChild(action1);
		seq.addChild(new SleepAction(5000));
		seq.addChild(action2);
		
		// invoke stop after 1 second
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {				
				seq.stop();
			}			
		};
		timer.schedule(task,  1000);				
		seq.execute(context);

		verify(action1).execute(any(Context.class));
		verify(action2, never()).execute(any(Context.class));		
	}
	
	
	@Test(expected = ActionExecutionException.class)
	public void testCanThrowExecutionException() throws ActionExecutionException {
		Context context = new Context();
		Action badAction = mock(Action.class);
		Action goodAction = mock(Action.class);
		ActionExecutionException exception = mock(ActionExecutionException.class);
		doThrow(exception).when(badAction).execute(any(Context.class));
		
		
		final SequentialAction seq = new SequentialAction();
		seq.addChild(goodAction);
		seq.addChild(badAction);
		seq.execute(context);
	}
	
	@Test
	public void testCanSkipSubsequentActionsOnException() throws ActionExecutionException {
		Context context = new Context();
		Action badAction = mock(Action.class);
		Action goodAction = mock(Action.class);
		Action nextAction = mock(Action.class);
		ActionExecutionException exception = mock(ActionExecutionException.class);
		doThrow(exception).when(badAction).execute(any(Context.class));
		
		
		final SequentialAction seq = new SequentialAction();
		seq.addChild(goodAction);
		seq.addChild(badAction);
		seq.addChild(nextAction);
		
		try{
			seq.execute(context);
		}catch(ActionExecutionException e){		
		}
		
		verify(goodAction).execute(any(Context.class));
		verify(badAction).execute(any(Context.class));
		verify(nextAction, never()).execute(any(Context.class));
	}

}
