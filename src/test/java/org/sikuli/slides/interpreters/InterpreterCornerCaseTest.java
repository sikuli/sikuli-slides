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
import org.sikuli.slides.models.ScreenshotElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;

public class InterpreterCornerCaseTest {

	private static final String TEST_TEXT = "some text";
	private StaticImageScreenRegion screenRegion;
	private DefaultInterpreter interpreter;
	private ScreenshotElement screenshotElement;
	private SlideElement targetElement;
	private SlideElement belowTargetElement;
	private SlideElement clicklement;
	private SlideElement aboveTargetElement;

	@Before
	public void setUp() throws IOException{
		
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli_context.png"));
		screenRegion = new StaticImageScreenRegion(image);		
		interpreter = new DefaultInterpreter(screenRegion);
		
		clicklement = new SlideElement();
		clicklement.setText("click");
		
		screenshotElement = new ScreenshotElement();
		screenshotElement.setSource(getClass().getResource("sikuli_context.png"));
		screenshotElement.setOffx(100);
		screenshotElement.setOffy(100);
		screenshotElement.setCx(1000);
		screenshotElement.setCy(1000);
		
		targetElement = new SlideElement(); 
		targetElement.setOffx(348);
		targetElement.setOffy(223);
		targetElement.setCx(200);
		targetElement.setCy(200);		
		targetElement.setTextSize(3600);
		targetElement.setText(TEST_TEXT);
		
		belowTargetElement = new SlideElement();
		belowTargetElement.setOffx(800);
		belowTargetElement.setOffy(800);
		belowTargetElement.setCx(350);
		belowTargetElement.setCy(350);
		
		
		aboveTargetElement = new SlideElement();
		aboveTargetElement.setOffx(50);
		aboveTargetElement.setOffy(50);
		aboveTargetElement.setCx(350);
		aboveTargetElement.setCy(350);				
	}
	
	@Test
	public void testInterpretLeftClickActionWhenTargetExceedsScreenshotAbove() throws IOException {
		Slide slide = new Slide();
		slide.add(clicklement);
		slide.add(screenshotElement);
		slide.add(belowTargetElement);

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
		slide.add(clicklement);
		slide.add(screenshotElement);
		slide.add(aboveTargetElement);

		LeftClickAction action = (LeftClickAction) interpreter.interpret(slide);		
		assertNotNull(action);
		
		ScreenRegion targetScreenRegion = action.getTargetScreenRegion();
		Rectangle b = targetScreenRegion.getBounds();	
		assertEquals(0, b.x);
		assertEquals(0, b.y);
	}	
}
