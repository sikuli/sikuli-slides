package org.sikuli.slides.interpreters;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.models.Slide;

public class SlideTokenizerTest {

	private SlideElementFixtures fixtures;
	private SlideTokenizer tknzr;

	@Before
	public void setUp() throws IOException{
		fixtures = new SlideElementFixtures();
		
	}

	@Test
	public void testGetActionWords_SingleAction(){
		Slide slide = new Slide();
		slide.add(fixtures.clickElement);
		tknzr = new SlideTokenizer(slide);
		
		assertThat("has click", tknzr.hasActionWord("click"), equalTo(true));
		assertThat("# of action words", tknzr.getActionWords().size(), equalTo(1));		
	}
	

	@Test
	public void testGetImageElements_TwoImages(){
		Slide slide = new Slide();
		slide.add(fixtures.imageElement);
		slide.add(fixtures.imageElement);
		tknzr = new SlideTokenizer(slide);
		
		assertThat("# of images", tknzr.getImageElements().size(), equalTo(2));		
	}
	
	@Test
	public void testGetImageElements_NoImages(){
		Slide slide = new Slide();
		slide.add(fixtures.clickElement);
		tknzr = new SlideTokenizer(slide);
		
		assertThat("# of images", tknzr.getImageElements().size(), equalTo(0));		
	}
}
