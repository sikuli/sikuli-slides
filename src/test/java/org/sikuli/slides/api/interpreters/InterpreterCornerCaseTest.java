package org.sikuli.slides.api.interpreters;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.StaticImageScreenRegion;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.actions.LabelAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.KeywordDictionary;
import org.sikuli.slides.api.models.Slide;

public class InterpreterCornerCaseTest {

	private DefaultInterpreter interpreter;
	private URL source;	

	@Before
	public void setUp() throws IOException{		
		interpreter = new DefaultInterpreter();		
		source = getClass().getResource("sikuli_context.png");
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotAbove() throws IOException {
		Slide slide = new Slide();
		slide.newImageElement().source(source).bounds(20,20,200,200).add();
		slide.newElement().bounds(0,0,100,100).add();
		slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();

		Action action = interpreter.interpret(slide);		
		assertNotNull(action);
		assertThat(Actions.select(action).isInstanceOf(TargetAction.class).all().size(), equalTo(1));
		assertThat(Actions.select(action).isInstanceOf(LeftClickAction.class).all().size(), equalTo(1));
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotBelow() throws IOException {
		Slide slide = new Slide();
		slide.newImageElement().source(source).bounds(0,0,100,100).add();
		slide.newElement().bounds(50,50,100,100).add();
		slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();

		Action action = interpreter.interpret(slide);		
		assertNotNull(action);
		assertThat(Actions.select(action).isInstanceOf(TargetAction.class).all().size(), equalTo(1));
		assertThat(Actions.select(action).isInstanceOf(LeftClickAction.class).all().size(), equalTo(1));
	}	
	
}
