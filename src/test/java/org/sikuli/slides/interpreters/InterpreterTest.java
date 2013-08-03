package org.sikuli.slides.interpreters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.StaticImageScreenRegion;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.BrowserAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.models.ScreenshotElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;

public class InterpreterTest {

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
	
	@Test
	public void testInterpretLeftClickAction() throws IOException {
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli_context.png"));
		ScreenRegion screenRegion = new StaticImageScreenRegion(image);				
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
}
