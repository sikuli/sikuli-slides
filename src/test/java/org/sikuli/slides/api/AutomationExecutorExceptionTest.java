package org.sikuli.slides.api;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DefaultLocation;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.actions.InputDetector;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.models.Slide;

public class AutomationExecutorExceptionTest {	
	
	private InputDetector detector;
	private SlidesExecutor executor;
	private DesktopCanvas canvas;

	@Before
	public void setUp() throws NativeHookException, IOException{
		executor = new AutomationExecutor();
		detector = new InputDetector();
		detector.start();		
		
		canvas = new DesktopCanvas();
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli_context.png"));
		canvas.addImage(new DefaultLocation(150,150), image);
	
	}
	
	public List<Slide> readSlidesFromResource(String resourceName) throws IOException{
		return (new PPTXSlidesReader()).read(getClass().getResource(resourceName));
	}

	@After
	public void tearDown(){
		detector.stop();
		canvas.hide();
	}
	
	@Test(expected = SlideExecutionException.class)
	public void testExecuteTargetNotFoundOnFirstSlide() throws IOException, SlideExecutionException{
		List<Slide> slides = readSlidesFromResource("badFirstStep.pptx");	
		executor.execute(slides);		
	}
	
	@Test
	public void testExecuteTargetNotFoundOnFirstSlideExeptionData() throws IOException {
		List<Slide> slides = readSlidesFromResource("badFirstStep.pptx");	
		try {
			executor.execute(slides);
		} catch (SlideExecutionException e) {
			assertEquals("slide", e.getSlide(), slides.get(0));
			assertEquals("action", e.getAction().getClass(), TargetAction.class);
		}		
	}
	
	@Test
	public void testExecuteTargetNotFoundOnSecondSlide() throws IOException {
		canvas.show();	
		List<Slide> slides = readSlidesFromResource("badSecondStep.pptx");
		SlideExecutionException exception = null;
		try {
			executor.execute(slides);
		} catch (SlideExecutionException e) {	
			exception = e;
		}
		assertNotNull(exception);
		// check if the 2nd slide is returned as the bad slide
		assertEquals("slide", exception.getSlide(), slides.get(1));
		canvas.hide();
	}
	
}
