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
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.models.Slide;

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

		LeftClickAction action = (LeftClickAction) interpreter.interpret(slide);		
		assertNotNull(action);
		
		ScreenRegion targetScreenRegion = action.getTargetScreenRegion();
		Rectangle b = targetScreenRegion.getBounds();	
		assertEquals(363, b.x);
		assertEquals(243, b.y);
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotBelow() throws IOException {
		Slide slide = new Slide();
		slide.add(fixtures.clickElement);
		slide.add(fixtures.imageElement);
		slide.add(fixtures.aboveTargetElement);

		LeftClickAction action = (LeftClickAction) interpreter.interpret(slide);		
		assertNotNull(action);
		
		ScreenRegion targetScreenRegion = action.getTargetScreenRegion();
		Rectangle b = targetScreenRegion.getBounds();	
		assertEquals(0, b.x);
		assertEquals(0, b.y);
	}	
}
