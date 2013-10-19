package org.sikuli.slides.api.slideshow;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.models.Slide;

import com.google.common.collect.Lists;

public class DefaultSlideShowControllerEventTest {

	SlideShowController slideshow;
	private SlideShowListener listener;
		
	void pause(long msecs){
		try {
			Thread.sleep(msecs);
		} catch (InterruptedException e) {
		}
	}
	
	@Before
	public void setUp(){
		Context context = mock(Context.class);
		Slide action1 = mock(Slide.class);
		Slide action2 = mock(Slide.class);
		Slide action3 = mock(Slide.class);
		
		slideshow = new DefaultSlideShowController(context);
		List<Slide> actions = Lists.newArrayList(action1, action2, action3);
		slideshow.setContent(actions);
		
		listener = mock(SlideShowListener.class);
		slideshow.addListener(listener);
	}
	
	@Test
	public void testCanTriggerSlideStartedAndFinished() throws InterruptedException {		
		slideshow.start();
		pause(100);
		verify(listener, times(3)).slideStarted(any(Slide.class));
		verify(listener, times(3)).slideFinished(any(Slide.class));
	}

}
