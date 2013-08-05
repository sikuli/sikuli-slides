package org.sikuli.slides.interpreters;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
import org.sikuli.slides.actions.ExistAction;
import org.sikuli.slides.actions.FindDoAction;
import org.sikuli.slides.actions.LabelAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.NotExistAction;
import org.sikuli.slides.actions.RightClickAction;
import org.sikuli.slides.actions.TypeAction;
import org.sikuli.slides.actions.WaitAction;
import org.sikuli.slides.models.ImageElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;



public class InterpreterTest {

	private static final String TEST_TEXT = "some text";
	private StaticImageScreenRegion screenRegion;
	private DefaultInterpreter interpreter;

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
		ImageElement screenshotElement = new ImageElement();
		screenshotElement.setSource(getClass().getResource("sikuli_context.png"));
		screenshotElement.setOffx(100);
		screenshotElement.setOffy(100);
		screenshotElement.setCx(1000);
		screenshotElement.setCy(1000);
		slide.add(screenshotElement);
		
		SlideElement targetElement = new SlideElement(); 
		targetElement.setOffx(348);
		targetElement.setOffy(223);
		targetElement.setCx(200);
		targetElement.setCy(200);		
		targetElement.setTextSize(3600);
		targetElement.setText(TEST_TEXT);
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
	
	private Slide createWaitSlide(String text){		
		Slide slide = new Slide();
		SlideElement actionElement = new SlideElement();
		actionElement.setText("wait");
		slide.add(actionElement);

		SlideElement argumentElement = new SlideElement();
		argumentElement.setText(text);
		slide.add(argumentElement);
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
	
	private Slide createLabelSlide(){		
		Slide slide = new Slide();		
		addTarget(slide);		
		return slide;
	}
	
	private Slide createExistSlide(){		
		Slide slide = new Slide();
		SlideElement actionElement = new SlideElement();
		actionElement.setText("exist");
		slide.add(actionElement);
		
		addTarget(slide);		
		return slide;
	}
	
	private Slide createNotExistSlide(){		
		Slide slide = new Slide();
		SlideElement actionElement = new SlideElement();
		actionElement.setText("not exist");
		slide.add(actionElement);
		
		addTarget(slide);		
		return slide;
	}	
	
	
	private Slide createTypeSlide() {
		Slide slide = new Slide();
		SlideElement actionElement = new SlideElement();
		actionElement.setText("type");
		slide.add(actionElement);
		
		addTarget(slide);		
		return slide;
	}

	
	
	@Before
	public void setUp() throws IOException{
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli_context.png"));
		screenRegion = new StaticImageScreenRegion(image);		
		interpreter = new DefaultInterpreter(screenRegion);
	}
	
	@Test
	public void testInterpretLeftClickAction() throws IOException {
		Slide slide = createClickSlide();		
		FindDoAction action = (FindDoAction) interpreter.interpret(slide);
		
		assertNotNull(action);
		assertThat(action, instanceOf(FindDoAction.class));
		assertThat(action.getTargetAction(), instanceOf(LeftClickAction.class));
		//LeftClickAction leftClickAction = (LeftClickAction) action;
		
//		ScreenRegion targetScreenRegion = leftClickAction.getTargetScreenRegion();
//		assertNotNull(targetScreenRegion);
//		action.execute(context);
//		Rectangle b = targetScreenRegion.getBounds();	
//		assertEquals(129, b.x);
//		assertEquals(43, b.y);
	}
	
	@Test
	public void testInterpretRightClickAction() throws IOException {
		Slide slide = createRightClickSlide();		
		FindDoAction action = (FindDoAction) interpreter.interpret(slide);
		
		assertNotNull(action);
		assertThat(action, instanceOf(FindDoAction.class));
		assertThat(action.getTargetAction(), instanceOf(RightClickAction.class));
	}
	
	@Test
	public void testInterpretDoubleClickAction() {
		Slide slide = createDoubleClickSlide();
		FindDoAction action = (FindDoAction) interpreter.interpret(slide);
		
		assertNotNull(action);
		assertThat(action.getTargetAction(), instanceOf(DoubleClickAction.class));
	}
	
	@Test
	public void testInterpretLabelAction() {
		Slide slide = createLabelSlide();
		FindDoAction action = (FindDoAction) interpreter.interpret(slide);		
		assertNotNull(action);
		
		assertThat(action.getTargetAction(), instanceOf(LabelAction.class));
		LabelAction label = (LabelAction) action.getTargetAction();
		assertEquals(TEST_TEXT, label.getText());
		assertEquals(36, label.getFontSize());
	}	
	
	@Test
	public void testInterpretExistAction() {
		Slide slide = createExistSlide();
		ExistAction action = (ExistAction) interpreter.interpret(slide);		
		assertNotNull(action);
	}	

	@Test
	public void testInterpretNotExistAction() {
		Slide slide = createNotExistSlide();
		NotExistAction action = (NotExistAction) interpreter.interpret(slide);		
		assertNotNull(action);
	}	
	
	
	@Test
	public void testInterpretTypeAction() {
		Slide slide = createTypeSlide();
		FindDoAction action = (FindDoAction) interpreter.interpret(slide);		
		assertNotNull(action);
		assertThat(action.getTargetAction(), instanceOf(TypeAction.class));
		
		TypeAction typeAction = (TypeAction) action.getTargetAction();
		assertThat(typeAction.getText(), equalToIgnoringCase(TEST_TEXT));
	}	

	@Test
	public void testInterpretWaitAction() {
		Slide slide = createWaitSlide("2 seconds");		
		Action action = interpreter.interpret(slide);
		
		assertNotNull(action);
		assertEquals("wait action", WaitAction.class, action.getClass());
		WaitAction waitAction = (WaitAction) action;				
		assertEquals(2000, waitAction.getDuration());
		
		slide = createWaitSlide("2");		
		waitAction = (WaitAction) interpreter.interpret(slide);
		assertEquals(2000, waitAction.getDuration());
		
		slide = createWaitSlide("1 minute");		
		waitAction = (WaitAction) interpreter.interpret(slide);
		assertEquals(1000 * 60, waitAction.getDuration());

		slide = createWaitSlide("0.5 second");		
		waitAction = (WaitAction) interpreter.interpret(slide);
		assertEquals(500, waitAction.getDuration());

		slide = createWaitSlide("0.5");		
		waitAction = (WaitAction) interpreter.interpret(slide);
		assertEquals(500, waitAction.getDuration());

		slide = createWaitSlide("0");		
		waitAction = (WaitAction) interpreter.interpret(slide);
		assertEquals(0, waitAction.getDuration());
		
	}
	
}
