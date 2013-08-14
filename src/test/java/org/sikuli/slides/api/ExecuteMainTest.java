package org.sikuli.slides.api;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;


import org.sikuli.api.API;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.slides.api.models.Slide;

public class ExecuteMainTest {
	
	private Slide slide2;
	private Slide slide1;
	private Slide slide3;
	private Slide slide4;
	private ExecuteMain main;


	@Before
	public void setUp(){
		slide1 = new Slide();
		slide1.setNumber(1);
		slide2 = new Slide();
		slide2.setNumber(2);
		slide3= new Slide();
		slide3.setNumber(3);
		slide4 = new Slide();
		slide4.setNumber(4);
		
		main = new ExecuteMain();
	}

	@Test
	public void testRange2to3(){ 
		main.parseArgs("helloworld.pptx","-range","2-3");
		assertThat(main.context.getSlideSelector().accept(slide1), equalTo(false));
		assertThat(main.context.getSlideSelector().accept(slide2), equalTo(true));
		assertThat(main.context.getSlideSelector().accept(slide3), equalTo(true));
		assertThat(main.context.getSlideSelector().accept(slide4), equalTo(false));
	}
	
	@Test
	public void testRange2(){ 
		main.parseArgs("helloworld.pptx","-range","2");
		assertThat(main.context.getSlideSelector().accept(slide1), equalTo(false));
		assertThat(main.context.getSlideSelector().accept(slide2), equalTo(true));
		assertThat(main.context.getSlideSelector().accept(slide3), equalTo(false));
		assertThat(main.context.getSlideSelector().accept(slide4), equalTo(false));
	}
	
	@Test
	public void testRange2andLater(){ 
		main.parseArgs("helloworld.pptx","-range","2-");
		assertThat(main.context.getSlideSelector().accept(slide1), equalTo(false));
		assertThat(main.context.getSlideSelector().accept(slide2), equalTo(true));
		assertThat(main.context.getSlideSelector().accept(slide3), equalTo(true));
		assertThat(main.context.getSlideSelector().accept(slide4), equalTo(true));
	}

	
	@Test
	public void testRangeDefaultToAll(){ 
		main.parseArgs("helloworld.pptx");
		assertThat(main.context.getSlideSelector().accept(slide1), equalTo(true));
		assertThat(main.context.getSlideSelector().accept(slide2), equalTo(true));
		assertThat(main.context.getSlideSelector().accept(slide3), equalTo(true));
		assertThat(main.context.getSlideSelector().accept(slide4), equalTo(true));		
	}
	
	@Test
	public void testScreenId(){ 
		main.parseArgs("helloworld.pptx","-screen_id","1");
		assertThat(((DesktopScreen) main.context.getScreenRegion().getScreen()).getScreenId(), equalTo(1));
	}
	
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
		main.parseArgs("-screen_id","1");
	}

	@Test
	public void testRun() throws MalformedURLException{
		main.execute("https://docs.google.com/presentation/d/1RRJuSMFRDc4EnBnqAG6FuJqiafEfGMmXwtFtOwNAKyo/edit?usp=sharing","-range","6");
	}
}
