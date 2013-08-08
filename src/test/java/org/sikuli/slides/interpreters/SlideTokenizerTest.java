package org.sikuli.slides.interpreters;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.interpreters.KeywordInterpreter.InterpretedKeyword;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;

public class SlideTokenizerTest {

	private SlideElementFixtures fixtures;
	private SlideTokenizer tknzr;
	private Slide slide;

	@Before
	public void setUp() throws IOException{
		fixtures = new SlideElementFixtures();
		slide = new Slide();
		
	}

//	@Test
//	public void testGetActionWords_SingleAction(){
//		Slide slide = new Slide();
//		slide.add(fixtures.clickElement);
//		tknzr = new SlideTokenizer(slide);
//		
//		assertThat("has click", tknzr.hasKeyword("click"), equalTo(true));
//		assertThat("# of action words", tknzr.getActionWords().size(), equalTo(1));		
//	}
//	
//	@Test
//	public void testfindKeywordElement(){
//		Slide slide = new Slide();
//		slide.add(fixtures.clickElement);
//		tknzr = new SlideTokenizer(slide);
//
//		assertThat("has click", tknzr.findKeywordElement("click"), equalTo(fixtures.clickElement));
//		assertThat("doesn't have click", tknzr.findKeywordElement("double click"), is(nullValue()));
//		assertThat("bad keyword", tknzr.findKeywordElement("bad keyword"), is(nullValue()));
//	}
//	
//
//	@Test
//	public void testGetImageElements_TwoImages(){
//		Slide slide = new Slide();
//		slide.add(fixtures.imageElement);
//		slide.add(fixtures.imageElement);
//		tknzr = new SlideTokenizer(slide);
//		
//		assertThat("# of images", tknzr.getImageElements().size(), equalTo(2));		
//	}
//	
//	@Test
//	public void testGetImageElements_NoImages(){
//		Slide slide = new Slide();
//		slide.add(fixtures.clickElement);
//		tknzr = new SlideTokenizer(slide);
//		
//		assertThat("# of images", tknzr.getImageElements().size(), equalTo(0));		
//	}
//	
//	@Test
//	public void testGetArgumentStrings_OneArgument(){
//		Slide slide = new Slide();
//		slide.add(fixtures.clickElement);
//		slide.add(fixtures.textElement);
//		tknzr = new SlideTokenizer(slide);		
//		assertThat("# of string arguments", tknzr.getArgumentStrings().size(), equalTo(1));
//		assertThat("first argument", tknzr.getArgumentStrings().get(0), equalToIgnoringCase(fixtures.textElement.getText()));
//	}
//	
//	@Test
//	public void testGetArgumentStrings_TwoArguments(){
//		Slide slide = new Slide();
//		slide.add(fixtures.clickElement);
//		slide.add(fixtures.textElement);
//		slide.add(fixtures.textElement);
//		tknzr = new SlideTokenizer(slide);		
//		assertThat("# of string arguments", tknzr.getArgumentStrings().size(), equalTo(2));		
//	}
//	
//	@Test
//	public void testGetElementsOn(){
//		Slide slide = new Slide();
//		slide.add(fixtures.imageElement);
//		slide.add(fixtures.targetElement);
//		slide.add(fixtures.clickElement);		
//		tknzr = new SlideTokenizer(slide);						
//		assertThat("# of target", tknzr.getElementsOn(fixtures.imageElement).size(), equalTo(1));		
//	}
//	
//	@Test
//	public void testGetNonKeywordTextElements(){
//		Slide slide = new Slide();		
//		slide.add(fixtures.textElement);
//		slide.add(fixtures.textElement);
//		slide.add(fixtures.textElement);
//		slide.add(fixtures.clickElement);		
//		tknzr = new SlideTokenizer(slide);						
//		assertThat("# of target", tknzr.getNonKeywordTextElements().size(), equalTo(3));		
//	}
	
	

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
		
		int n = Selector.select(slide).hasKeyword().all().size();
		assertThat(n, equalTo(3));
		
//		interpreter.interpret(slide);
//		
//		List<InterpretedKeyword> matches = interpreter.getResult().getInterpretedKeywords();
//		assertThat(matches.size(), equalTo(3));
//		assertThat(matches.get(0).getKeyword().getName(), equalToIgnoringCase(KeywordDictionary.DOUBLE_CLICK));
//		assertThat(matches.get(0).getMatchedText(), equalToIgnoringCase("double click"));
//		
//		assertThat(matches.get(1).getKeyword().getName(), equalToIgnoringCase(KeywordDictionary.DOUBLE_CLICK));
//		assertThat(matches.get(1).getMatchedText(), equalToIgnoringCase("double-click"));
//		
//		assertThat(matches.get(2).getKeyword().getName(), equalToIgnoringCase(KeywordDictionary.DOUBLE_CLICK));
//		assertThat(matches.get(2).getMatchedText(), equalToIgnoringCase("doubleclick"));
	}
}
