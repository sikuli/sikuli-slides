package org.sikuli.slides.interpreters;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.StaticImageScreenRegion;
import org.sikuli.slides.actions.FindDoAction;
import org.sikuli.slides.actions.LabelAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.models.Slide;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InterpreterCornerCaseTest {


	private StaticImageScreenRegion screenRegion;
	private DefaultInterpreter interpreter;
	private SlideElementFixtures fixtures;	

	@Before
	public void setUp() throws IOException{
		
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli_context.png"));
		screenRegion = new StaticImageScreenRegion(image);		
		interpreter = new DefaultInterpreter(screenRegion);
		
		fixtures = new SlideElementFixtures();
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotAbove() throws IOException {
		Slide slide = new Slide();
		slide.add(fixtures.clickElement);
		slide.add(fixtures.imageElement);
		slide.add(fixtures.belowTargetElement);

		FindDoAction action = (FindDoAction) interpreter.interpret(slide);		
		assertNotNull(action);
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotBelow() throws IOException {
		Slide slide = new Slide();
		slide.add(fixtures.clickElement);
		slide.add(fixtures.imageElement);
		slide.add(fixtures.aboveTargetElement);

		FindDoAction action = (FindDoAction) interpreter.interpret(slide);		
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
