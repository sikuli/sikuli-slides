package org.sikuli.slides.api.slideshow;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.models.Slide;

import com.google.common.collect.Lists;
import static org.mockito.Mockito.*;

public class DefaultSlideShowControllerTest {

	
	List<Slide> slides;
	DefaultSlideShowController slideshow;
	private Slide slide1;
	private Slide slide2;
	private Slide slide3;
		
	void pause(long msecs){
		try {
			Thread.sleep(msecs);
		} catch (InterruptedException e) {
		}
	}
	
	static class SleepSlide extends Slide {
		private long duration;
		
		public SleepSlide(long waitDuration) {
			this.duration = waitDuration;
		}

		/**
		 * Execute and wait for execution to finish
		 */
		public void execute(Context context) throws ActionExecutionException{
			synchronized (this){
				try {
					this.wait(duration);
				} catch (InterruptedException e) {
				}
			}
		}
		
		public void stop(){
			synchronized(this){
				this.notify();
			}
		}

		public long getDuration() {		
			return duration;
		}
	}
	
	@Before
	public void setUp(){
		Context context = new Context();
		slide1 = spy(new SleepSlide(500));
		slide2 = spy(new SleepSlide(500));
		slide3 = spy(new SleepSlide(500));
		slides = Lists.newArrayList(slide1, slide2, slide3);
		slideshow = new DefaultSlideShowController(context);
		slideshow.setContent(slides);		
	}
	
	@Test
	public void testInvokeStartCanExecuteAllSlides() throws ActionExecutionException {
		slideshow.start();
		pause(1600);
		verify(slide1).execute(any(Context.class));
		verify(slide2).execute(any(Context.class));
		verify(slide3).execute(any(Context.class));
	}

	@Test
	public void testInvokeQuitDuringSlide1() throws ActionExecutionException, InterruptedException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.quit();
			}
		}.start();
		
		pause(150);

		// should cause slide1 to stop
		verify(slide1).stop();
		// should never execute slide 2
		verify(slide2, never()).execute(any(Context.class));
		// should never execute slide 3
		verify(slide3, never()).execute(any(Context.class));
	}
	
	@Test
	public void testInvokeNextDuringSlide1() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.next();
			}
		}.start();
		
		pause(200);
		
		verify(slide1).stop();
		verify(slide2).execute(any(Context.class));
		verify(slide3, never()).execute(any(Context.class));
	}
	
	@Test
	public void testInvokeNextTwiceDuringSlide1() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.next();
				slideshow.next();
			}
		}.start();
		
		pause(150);
		
		// should stop slide1
		verify(slide1).stop();
		// should execute slide 3
		verify(slide3).execute(any(Context.class));
	}
	
	@Test
	public void testInvokePreviousDuringSlide1() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.previous();
			}
		}.start();
		
		pause(150);
		
		// should not cause slide1 to stop
		verify(slide1,never()).stop();
		// should not start executing slide2
		verify(slide2,never()).execute(any(Context.class));
	}
	
	@Test
	public void testInvokeNextThreeTimesDuringSlide1() throws ActionExecutionException {
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
		
		verify(slide1).stop();	
		verify(slide3).execute(any(Context.class));	
	}
	
	@Test
	public void testInvokePreviousDuringSlide2() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(600);
				slideshow.previous();
			}
		}.start();
		
		pause(700);
		
		verify(slide2).execute(any(Context.class));
		verify(slide2).stop();
		
		// should go back to execute slide 1
		verify(slide1, times(2)).execute(any(Context.class));
		// should not have already executed slide 3
		verify(slide3, never()).execute(any(Context.class));
		
		pause(1000);
		
		verify(slide2, times(2)).execute(any(Context.class));
		verify(slide3).execute(any(Context.class));
		
	}
}
