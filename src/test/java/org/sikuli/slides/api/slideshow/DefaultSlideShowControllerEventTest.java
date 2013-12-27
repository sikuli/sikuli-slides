package org.sikuli.slides.api.slideshow;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.models.Slide;

import com.google.common.collect.Lists;

public class DefaultSlideShowControllerEventTest {

	SlideShowController slideshow;
	private SlideShowListener listener;
	private Slide slide1;
	private Slide slide2;
	private Slide slide3;
		
	void pause(long msecs){
		try {
			Thread.sleep(msecs);
		} catch (InterruptedException e) {
		}
	}
	
	@Before
	public void setUp(){
		Context context = mock(Context.class);
	    slide1 = mock(Slide.class);
		slide2 = mock(Slide.class);
		slide3 = mock(Slide.class);
		List<Slide> slides = Lists.newArrayList(slide1, slide2, slide3);
		slideshow = new DefaultSlideShowController(context, slides);
		
		listener = mock(SlideShowListener.class);
		slideshow.addListener(listener);
	}
	
	@Test
	public void testSlideSelected() throws InterruptedException {		
		slideshow.start();
		pause(1000);
		verify(listener).slideSelected(slide1);
		verify(listener).slideSelected(slide2);
		verify(listener).slideSelected(slide3);
	}
	
	@Test
	public void testDuringSlide1_NextNext() throws ActionExecutionException {
		slideshow.start();
		
		new Thread(){
			public void run(){
				slideshow.next();
				pause(100);
				slideshow.next();
				pause(100);
			}
		}.start();
		
		pause(800);
				
		verify(listener).slideSelected(slide1);
		verify(listener).slideSelected(slide2);
		verify(listener).slideSelected(slide3);
	}

}
