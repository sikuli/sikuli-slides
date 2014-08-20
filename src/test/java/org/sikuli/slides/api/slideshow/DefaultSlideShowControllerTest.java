package org.sikuli.slides.api.slideshow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.models.Slide;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class DefaultSlideShowControllerTest {

	
	List<Slide> slides;
	DefaultSlideShowController slideshow;
	private Slide slide1;
	private Slide slide2;
	private Slide slide3;
	private Context context;
		
	void pause(long msecs){
		try {
			Thread.sleep(msecs);
		} catch (InterruptedException e) {
		}
	}
	
	static class TestSlide extends Slide {
		private long duration;
		private int number;
		private boolean throwException = false;
		
		public TestSlide(long waitDuration, int number) {
			this.duration = waitDuration;
			this.number = number;
		}
		
		public void setThrowException(boolean throwException){
			this.throwException = throwException;
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
			
			if (throwException){
				throw new ActionExecutionException("exception", this);
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
		
		public String toString(){
			return Objects.toStringHelper(getClass()).add("number", number).toString();
		}
	}

	@Before
	public void setUp(){
		context = new Context();
		slide1 = spy(new TestSlide(500,1));
		slide2 = spy(new TestSlide(500,2));
		slide3 = spy(new TestSlide(500,3));
		slides = Lists.newArrayList(slide1, slide2, slide3);
		slideshow = new DefaultSlideShowController(context, slides);
	}
	
	@Test
	public void testStartExecuteAllThreeSlides() throws ActionExecutionException {
		slideshow.start();
		pause(1600);
		verify(slide1).execute(any(Context.class));
		verify(slide2).execute(any(Context.class));
		verify(slide3).execute(any(Context.class));
	}
	
	@Test
	public void testAfterSlide3_JumpToSlide1AndPlay() throws ActionExecutionException {
		slideshow.start();
				
		new Thread(){
			public void run(){
				pause(1600);
				slideshow.jumpTo(0);
				slideshow.play();
			}
		}.start();		
		
		pause(2000);
		verify(slide1, times(2)).execute(any(Context.class));
		verify(slide2).execute(any(Context.class));
		verify(slide3).execute(any(Context.class));
	}
	
	@Test
	public void testAfterSlide3_JumpToSlide1() throws ActionExecutionException {
		slideshow.start();
				
		new Thread(){
			public void run(){
				pause(1600);
				slideshow.jumpTo(0);
			}
		}.start();		
		
		pause(2000);
		verify(slide1).execute(any(Context.class));
		verify(slide2).execute(any(Context.class));
		verify(slide3).execute(any(Context.class));
	}
	
	
	@Test
	public void testDuringSlide2_Pause() throws ActionExecutionException {
		slideshow.start();		
		
		new Thread(){
			public void run(){
				pause(600);
				slideshow.pause();
			}
		}.start();		
		
		pause(1000);
		verify(slide1).execute(any(Context.class));
		verify(slide2).execute(any(Context.class));
		verify(slide2).stop();
		verify(slide3,never()).execute(any(Context.class));
	}

	@Test
	public void testDuringSlide1_Quit() throws ActionExecutionException, InterruptedException {
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
	public void testDuringSlide1_Next() throws ActionExecutionException {
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
	public void testDuringSlide1_NextNext() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.next();
				pause(100);
				slideshow.next();
			}
		}.start();
		
		pause(500);
		
		// should stop slide1
		verify(slide1).stop();
		// should execute slide 3
		verify(slide3).execute(any(Context.class));
	}
	
	@Test
	public void testDuringSlide1_Previous() throws ActionExecutionException {
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
	public void testDuringSlide1_NextNextNext() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.next();
				pause(100);
				slideshow.next();
				pause(100);
				slideshow.next();
			}
		}.start();
		
		pause(500);
		
		verify(slide1).stop();	
		verify(slide3).execute(any(Context.class));	
	}
	
	@Test
	public void testDuringSlide2_Previous() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(600);
				slideshow.previous();
			}
		}.start();
		
		pause(800);
		
		verify(slide2).execute(any(Context.class));
		verify(slide2).stop();
		
		// should go back to execute slide 1
		verify(slide1, times(2)).execute(any(Context.class));
		// should not have already executed slide 3
		verify(slide3, never()).execute(any(Context.class));
		
//		pause(1500);
//		// should continue to execute slide 2
//		verify(slide2, times(2)).execute(any(Context.class));
//		verify(slide3).execute(any(Context.class));
//		
	}
	
	
	@Test
	public void testDuringSlide1_JumpToSlide3() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.jumpTo(2);
			}
		}.start();
		
		pause(1000);
		
		verify(slide1).stop();
		verify(slide3).execute(any(Context.class));
	}
	
	@Test
	public void testDuringSlide3_JumpToSlide1() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.next();
				pause(100);
				slideshow.next();
				pause(100);
				slideshow.jumpTo(0);
			}
		}.start();
		
		pause(500);
		
		verify(slide1).stop();
		verify(slide3).execute(any(Context.class));
	}
	
	@Test
	public void testDuringSlide1_PauseNext() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.pause();
				pause(100);
				slideshow.next();
			}
		}.start();
		
		pause(1000);
		
		// slide 1 should stop
		verify(slide1, atLeast(1)).stop();
		
		// slide 2 should never execute
		verify(slide2,never()).execute(any(Context.class));
	}
	
	@Test
	public void testDuringSlide1_PauseNextPlay() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.pause();
				pause(100);
				slideshow.next();
				pause(100);
				slideshow.play();
			}
		}.start();
		
		pause(500);
				
		// slide 1 should stop		
		verify(slide1,atLeast(1)).stop();
		
		// slide 1 should execute only once (didn't get re-executed)
		verify(slide1).execute(any(Context.class));
		
		// slide 2 should execute
		verify(slide2).execute(any(Context.class));

	}
	
	@Test
	public void testDuringSlide1_PausePlay() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.pause();
				pause(100);
				slideshow.play();
			}
		}.start();
		
		pause(800);
				
		// slide 1 should stop		
		verify(slide1).stop();
		
		// slide 1 should execute two times
		verify(slide1, times(2)).execute(any(Context.class));

		verify(slide2).execute(any(Context.class));
	}
	
	@Test
	public void testDuringSlide1_PausePause() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				pause(100);
				slideshow.pause();
				pause(100);
				slideshow.pause();
			}
		}.start();
		
		pause(800);
				
		// slide 1 should stop		
		verify(slide1).stop();
		// slide should execute only once
		verify(slide1).execute(any(Context.class));
	}
	
	@Test
	public void testDuringSlide1_ThrowException() throws ActionExecutionException {
		((TestSlide) slide1).setThrowException(true);
		slideshow.start();
		pause(800);				
		verify(slide1).execute(any(Context.class));
		verify(slide2, never()).execute(any(Context.class));
	}
	
	@Test
	public void testDuringSlide2_ThrowException() throws ActionExecutionException {
		((TestSlide) slide2).setThrowException(true);
		slideshow.start();
		pause(1200);	
		verify(slide1).execute(any(Context.class));
		verify(slide2).execute(any(Context.class));
		verify(slide3, never()).execute(any(Context.class));
	}
	
	@Test
	public void testStartWithNoSlides(){
		slideshow = new DefaultSlideShowController(context, new ArrayList<Slide>());
		assertThat(slideshow.hasNext(), equalTo(false));
		assertThat(slideshow.hasPrevious(), equalTo(false));
		assertThat(slideshow.isPaused(), equalTo(false));
	}
}
