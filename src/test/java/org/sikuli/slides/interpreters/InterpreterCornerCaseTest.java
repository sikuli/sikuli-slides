package org.sikuli.slides.interpreters;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.StaticImageScreenRegion;
import org.sikuli.slides.actions.TargetAction;
import org.sikuli.slides.actions.LabelAction;
import org.sikuli.slides.models.Slide;

public class InterpreterCornerCaseTest {

	private DefaultInterpreter interpreter;
	private SlideElementFixtures fixtures;	

	@Before
	public void setUp() throws IOException{		
		interpreter = new DefaultInterpreter();		
		fixtures = new SlideElementFixtures();
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotAbove() throws IOException {
		Slide slide = new Slide();
		slide.add(fixtures.clickElement);
		slide.add(fixtures.imageElement);
		slide.add(fixtures.belowTargetElement);

		TargetAction action = (TargetAction) interpreter.interpret(slide);		
		assertNotNull(action);
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotBelow() throws IOException {
		Slide slide = new Slide();
		slide.add(fixtures.clickElement);
		slide.add(fixtures.imageElement);
		slide.add(fixtures.aboveTargetElement);

		TargetAction action = (TargetAction) interpreter.interpret(slide);		
		assertNotNull(action);		
	}	
	
	@Test
	public void testInterpretLabelActionWithoutAnyTarget() throws IOException {
		Slide slide = new Slide();
		slide.add(fixtures.textElement);
		LabelAction action = (LabelAction) interpreter.interpret(slide);		
		assertThat(action, notNullValue());
		assertThat(action.getText(), equalToIgnoringCase(fixtures.textElement.getText()));
	}	
}
