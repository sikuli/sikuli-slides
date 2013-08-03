package org.sikuli.slides.interpreters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.StaticImageScreenRegion;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.BrowserAction;
import org.sikuli.slides.actions.DoubleClickAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.RightClickAction;
import org.sikuli.slides.models.ScreenshotElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;

public class InterpreterTest {

	private StaticImageScreenRegion screenRegion;

	@Test
	public void testInterpretBrowserAction() throws MalformedURLException{
		URL url = new URL("http://slides.sikuli.org");
		
		Slide slide = new Slide();
		SlideElement actionElement = new SlideElement();
		actionElement.setText("browser");

		SlideElement argumentElement = new SlideElement();
		argumentElement.setText(url.toString());
		
		slide.add(actionElement);
		slide.add(argumentElement);
		
		Interpreter interpreter = new DefaultInterpreter(new DesktopScreenRegion());
		Action action = interpreter.interpret(slide);
		
		assertNotNull(action);
		assertEquals("browser action", BrowserAction.class, action.getClass());
		
		BrowserAction browserAction = (BrowserAction) action;
		assertNotNull(browserAction.getUrl());
		assertEquals("browser url", url, browserAction.getUrl());
	}
	
	
	private void addTarget(Slide slide){
		ScreenshotElement screenshotElement = new ScreenshotElement();
		screenshotElement.setSource(getClass().getResource("sikuli_context.png"));
		screenshotElement.setCx(100);
		screenshotElement.setCy(100);
		screenshotElement.setOffx(1100);
		screenshotElement.setOffy(1100);
		slide.add(screenshotElement);
		
		SlideElement targetElement = new SlideElement(); 
		targetElement.setCx(348);
		targetElement.setCy(223);		
		targetElement.setOffx(548);
		targetElement.setOffy(423);
		slide.add(targetElement);
	}
	
	private Slide createClickSlide(){		
		Slide slide = new Slide();
		SlideElement actionElement = new SlideElement();
		actionElement.setText("click");
		slide.add(actionElement);
		
		addTarget(slide);		
		return slide;
	}
	
	private Slide createRightClickSlide(){		
		Slide slide = new Slide();
		SlideElement actionElement = new SlideElement();
		actionElement.setText("right click");
		slide.add(actionElement);
		
		addTarget(slide);		
		return slide;
	}	
	
	private Slide createDoubleClickSlide(){		
		Slide slide = new Slide();
		SlideElement actionElement = new SlideElement();
		actionElement.setText("double click");
		slide.add(actionElement);
		
		addTarget(slide);		
		return slide;
	}	
	
	
	@Before
	public void setUp() throws IOException{
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli_context.png"));
		screenRegion = new StaticImageScreenRegion(image);				
	}
	
	@Test
	public void testInterpretLeftClickAction() throws IOException {
		Slide slide = createClickSlide();
		
		Interpreter interpreter = new DefaultInterpreter(screenRegion);
		Action action = interpreter.interpret(slide);
		
		assertNotNull(action);
		assertEquals("leftclick action", LeftClickAction.class, action.getClass());
		LeftClickAction leftClickAction = (LeftClickAction) action;
		
		ScreenRegion targetScreenRegion = leftClickAction.getTargetScreenRegion();
		assertNotNull(targetScreenRegion);
		
		Rectangle b = targetScreenRegion.getBounds();	
		assertEquals(129, b.x);
		assertEquals(43, b.y);
	}
	
	@Test
	public void testInterpretRightClickAction() throws IOException {
		Slide slide = createRightClickSlide();
		
		Interpreter interpreter = new DefaultInterpreter(screenRegion);
		Action action = interpreter.interpret(slide);
		
		assertNotNull(action);
		assertEquals("right-click action", RightClickAction.class, action.getClass());
	}
	
	@Test
	public void testInterpretDoubleClickAction() throws IOException {
		Slide slide = createDoubleClickSlide();
		
		Interpreter interpreter = new DefaultInterpreter(screenRegion);
		Action action = interpreter.interpret(slide);
		
		assertNotNull(action);
		assertEquals("double-click action", DoubleClickAction.class, action.getClass());
	}
	
}
