package org.sikuli.slides.api.slideshow;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jnativehook.mouse.NativeMouseEvent;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.actions.SleepAction;

import com.google.common.collect.Lists;
import static org.mockito.Mockito.*;

public class SlideShowControllerTest {

	
	List<Action> actions;
	DefaultSlideShowController slideshow;
	private Action action1;
	private Action action2;
	private Action action3;
		
	void pause(long msecs){
		try {
			Thread.sleep(msecs);
		} catch (InterruptedException e) {
		}
	}
	
	@Before
	public void setUp(){
		Context context = new Context();
		action1 = spy(new SleepAction(500));
		action2 = spy(new SleepAction(500));
		action3 = spy(new SleepAction(500));
		actions = Lists.newArrayList(action1, action2, action3);
		slideshow = new DefaultSlideShowController(context);
		slideshow.setContent(actions);		
	}
	
	@Test
	public void testInvokeStartCanExecuteAllActions() throws ActionExecutionException {
		slideshow.start();
		pause(1600);
		verify(action1).execute(any(Context.class));
		verify(action2).execute(any(Context.class));
		verify(action3).execute(any(Context.class));
	}

	@Test
	public void testInvokeQuitDuringAction1() throws ActionExecutionException, InterruptedException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.quit();
			}
		}.start();
		
		pause(150);

		// should cause action1 to stop
		verify(action1).stop();
		// should never execute action 2
		verify(action2, never()).execute(any(Context.class));
		// should never execute action 3
		verify(action3, never()).execute(any(Context.class));
	}
	
	@Test
	public void testInvokeNextDuringAction1() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.next();
			}
		}.start();
		
		pause(200);
		
		verify(action1).stop();
		verify(action2).execute(any(Context.class));
		verify(action3, never()).execute(any(Context.class));
	}
	
	@Test
	public void testInvokeNextTwiceDuringAction1() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.next();
				slideshow.next();
			}
		}.start();
		
		pause(150);
		
		// should stop action1
		verify(action1).stop();
		// should execute action 3
		verify(action3).execute(any(Context.class));
	}
	
	@Test
	public void testInvokePreviousDuringAction1() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.previous();
			}
		}.start();
		
		pause(150);
		
		// should not cause action1 to stop
		verify(action1,never()).stop();
		// should not start executing action2
		verify(action2,never()).execute(any(Context.class));
	}
	
	@Test
	public void testInvokeNextThreeTimesDuringAction1() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.next();
				slideshow.next();
				slideshow.next();
			}
		}.start();
		
		pause(200);
		
		verify(action1).stop();	
		verify(action3).execute(any(Context.class));	
	}
	
	@Test
	public void testInvokePreviousDuringAction2() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(600);
				slideshow.previous();
			}
		}.start();
		
		pause(700);
		
		verify(action2).execute(any(Context.class));
		verify(action2).stop();
		
		// should go back to execute action 1
		verify(action1, times(2)).execute(any(Context.class));
		// should not have already executed action 3
		verify(action3, never()).execute(any(Context.class));
		
		pause(1000);
		
		verify(action2, times(2)).execute(any(Context.class));
		verify(action3).execute(any(Context.class));
		
	}
}
