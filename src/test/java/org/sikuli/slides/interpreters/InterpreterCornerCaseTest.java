package org.sikuli.slides.interpreters;


import static org.hamcrest.MatcherAssert.assertThat;
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
import org.sikuli.slides.actions.TargetAction;
import org.sikuli.slides.actions.LabelAction;
import org.sikuli.slides.models.Slide;

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

		TargetAction action = (TargetAction) interpreter.interpret(slide);		
		assertNotNull(action);
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotBelow() throws IOException {
		Slide slide = new Slide();
		slide.newImageElement().source(source).bounds(0,0,100,100).add();
		slide.newElement().bounds(50,50,100,100).add();
		slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();

		TargetAction action = (TargetAction) interpreter.interpret(slide);		
		assertNotNull(action);		
	}	
	
	@Test
	public void testInterpretLabelActionWithoutAnyTarget() throws IOException {
		Slide slide = new Slide();
		slide.newElement().text("some label").add();
		
		LabelAction action = (LabelAction) interpreter.interpret(slide);		
		assertThat(action, notNullValue());
		assertThat(action.getText(), equalToIgnoringCase("some label"));
	}	
}
