package org.sikuli.slides.apps;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.slides.api.ExecutionEvent;
import org.sikuli.slides.api.models.Slide;

import static org.mockito.Mockito.*;

public class ExecuteMainTest {
	
	private Slide slide2;
	private Slide slide1;
	private Slide slide3;
	private Slide slide4;
	private ExecuteMain main;
	private ExecutionEvent event1;
	private ExecutionEvent event4;
	private ExecutionEvent event3;
	private ExecutionEvent event2;


	@Before
	public void setUp(){
		
		slide1 = new Slide();
		slide1.setNumber(1);
		event1 = mock(ExecutionEvent.class);
		when(event1.getSlide()).thenReturn(slide1);
		
		slide2 = new Slide();
		slide2.setNumber(2);
		event2 = mock(ExecutionEvent.class);
		when(event2.getSlide()).thenReturn(slide2);
		
		slide3= new Slide();
		slide3.setNumber(3);
		event3 = mock(ExecutionEvent.class);
		when(event3.getSlide()).thenReturn(slide3);
		
		slide4 = new Slide();
		slide4.setNumber(4);		
		event4 = mock(ExecutionEvent.class);
		when(event4.getSlide()).thenReturn(slide4);
		
		main = new ExecuteMain();		
		
	}
	
	@Test
	public void testRange2to3(){ 
		main.parseArgs("helloworld.pptx","-range","2-3");
		assertThat(main.context.getExecutionFilter().accept(event1), equalTo(false));
		assertThat(main.context.getExecutionFilter().accept(event2), equalTo(true));
		assertThat(main.context.getExecutionFilter().accept(event3), equalTo(true));
		assertThat(main.context.getExecutionFilter().accept(event4), equalTo(false));
	}
	
	@Test
	public void testRange2(){ 
		main.parseArgs("helloworld.pptx","-range","2");
		assertThat(main.context.getExecutionFilter().accept(event1), equalTo(false));
		assertThat(main.context.getExecutionFilter().accept(event2), equalTo(true));
		assertThat(main.context.getExecutionFilter().accept(event3), equalTo(false));
		assertThat(main.context.getExecutionFilter().accept(event4), equalTo(false));
	}
	
	@Test
	public void testRange2andLater(){ 
		main.parseArgs("helloworld.pptx","-range","2-");
		assertThat(main.context.getExecutionFilter().accept(event1), equalTo(false));
		assertThat(main.context.getExecutionFilter().accept(event2), equalTo(true));
		assertThat(main.context.getExecutionFilter().accept(event3), equalTo(true));
		assertThat(main.context.getExecutionFilter().accept(event4), equalTo(true));
	}

	
	@Test
	public void testRangeDefaultToAll(){ 
		main.parseArgs("helloworld.pptx");
		assertThat(main.context.getExecutionFilter().accept(event1), equalTo(true));
		assertThat(main.context.getExecutionFilter().accept(event2), equalTo(true));
		assertThat(main.context.getExecutionFilter().accept(event3), equalTo(true));
		assertThat(main.context.getExecutionFilter().accept(event4), equalTo(true));		
	}
	
	@Test
	public void testScreenId(){ 
		main.parseArgs("helloworld.pptx","-screen","0");
		assertThat(((DesktopScreen) main.context.getScreenRegion().getScreen()).getScreenId(), equalTo(0));
	}
	// TODO: test case with bad screen ids
	
	@Test
	public void testWaitTime(){ 
		main.parseArgs("helloworld.pptx","-wait","5000");
		assertThat(main.context.getWaitTime(), equalTo(5000L));
	}
	
	@Test
	public void testMinScore(){ 
		main.parseArgs("helloworld.pptx","-min_score","0.9");
		assertThat((double) main.context.getMinScore(), closeTo(0.9,0.05));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMinScoreTooBig(){ 
		main.parseArgs("helloworld.pptx","-min_score","100");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBadOptionName(){ 
		main.parseArgs("helloworld.pptx","-not_a_good_optionname","100");
	}
	
	@Test
	public void testInputIsAFilePath(){ 
		main.parseArgs("helloworld.pptx");
		assertThat(main.url.toString(), containsString("helloworld.pptx"));
		assertThat(main.url.getProtocol(), containsString("file"));
	}
	
	@Test
	public void testInputIsAnURL(){ 
		main.parseArgs("http://some.url");
		assertThat(main.url.toString(), containsString("some.url"));
		assertThat(main.url.getProtocol(), containsString("http"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNoInput(){ 
		main.parseArgs("-screen","1");
	}
	
}
