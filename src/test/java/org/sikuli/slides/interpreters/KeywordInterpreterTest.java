package org.sikuli.slides.interpreters;


import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.interpreters.KeywordInterpreter.Result;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;

public class KeywordInterpreterTest {
	private KeywordInterpreter interpreter;
	private SlideElementFixtures fixtures;
	private Slide slide;	

	@Before
	public void setUp() throws IOException{		
		slide = new Slide();
		interpreter = new KeywordInterpreter(slide);		
		fixtures = new SlideElementFixtures();
	}
	
	@Test
	public void testInterpretSingleKeyword(){
		slide.add(fixtures.clickElement);
		interpreter.interpret();
		
		List<Result> matches = interpreter.getResults();
		assertThat(matches.size(), equalTo(1));
		assertThat(matches.get(0).getSlideElement(), equalTo(fixtures.clickElement));
		assertThat(matches.get(0).getMatchedText(), equalToIgnoringCase("click"));
	}
	
	@Test
	public void testInterpretTwoWordKeyword(){
		slide.add(fixtures.doubleClickElement);
		interpreter.interpret();
		
		List<Result> matches = interpreter.getResults();
		assertThat(matches.get(0).getMatchedText(), equalToIgnoringCase("double click"));
	}
	
	@Test
	public void testInterpretSingleKeywordWithTrailingArugments(){
		SlideElement e = new SlideElement();
		e.setText("type hello world");
		slide.add(e);
		interpreter.interpret();
		
		List<Result> matches = interpreter.getResults();
		assertThat(matches.get(0).getKeyword().getName(), equalToIgnoringCase(KeywordDictionary.TYPE));	
		assertThat(matches.get(0).getMatchedText(), equalToIgnoringCase("type"));
	}
	
	@Test
	public void testInterpretRemainingMatchedText(){
		SlideElement e = new SlideElement();
		e.setText("type hello world");
		slide.add(e);
		interpreter.interpret();
		
		List<Result> matches = interpreter.getResults();
		assertThat(matches.get(0).getRemainingText(), containsString("hello world"));		
	}
	
	@Test
	public void testInterpretDoubleClickAliases(){
				
		SlideElement doubleClickElement1 = new SlideElement();
		doubleClickElement1.setText("double click");

		SlideElement doubleClickElement2 = new SlideElement();
		doubleClickElement2.setText("double-click");

		SlideElement doubleClickElement3 = new SlideElement();
		doubleClickElement3.setText("doubleclick");

		slide.add(doubleClickElement1);
		slide.add(doubleClickElement2);
		slide.add(doubleClickElement3);
		interpreter.interpret();
		
		List<Result> matches = interpreter.getResults();
		assertThat(matches.size(), equalTo(3));
		assertThat(matches.get(0).getKeyword().getName(), equalToIgnoringCase(KeywordDictionary.DOUBLE_CLICK));
		assertThat(matches.get(0).getMatchedText(), equalToIgnoringCase("double click"));
		
		assertThat(matches.get(1).getKeyword().getName(), equalToIgnoringCase(KeywordDictionary.DOUBLE_CLICK));
		assertThat(matches.get(1).getMatchedText(), equalToIgnoringCase("double-click"));
		
		assertThat(matches.get(2).getKeyword().getName(), equalToIgnoringCase(KeywordDictionary.DOUBLE_CLICK));
		assertThat(matches.get(2).getMatchedText(), equalToIgnoringCase("doubleclick"));
	}

	
	@Test
	public void testInterpretTwoKeywords(){
		slide.add(fixtures.clickElement);
		slide.add(fixtures.clickElement);
		interpreter.interpret();
		
		assertThat(interpreter.getResults().size(), equalTo(2));
	}
	
	@Test
	public void testInterpretNoKeyword(){
		slide.add(fixtures.targetElement);
		slide.add(fixtures.targetElement);
		slide.add(fixtures.targetElement);
		interpreter.interpret();
		
		assertThat(interpreter.getResults().size(), equalTo(0));
	}
}
