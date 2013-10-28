package org.sikuli.slides.api;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DefaultLocation;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.actions.InputDetector;
import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.models.Slide;

public class AutomationExecutorTest {	
	
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
		canvas.show();		
	}
	
	public List<Slide> readSlidesFromResource(String resourceName) throws IOException{
		return (new PPTXSlidesReader()).read(getClass().getResource(resourceName));
	}

	@After
	public void tearDown(){
		detector.stop();
		canvas.hide();
	}
	
	@Test
	public void testExecute() throws IOException, SlideExecutionException{		
		List<Slide> slides = readSlidesFromResource("click.pptx");		
		executor.execute(slides);		
		NativeMouseEvent ev = detector.getLastMouseEvent();
		assertEquals("x", 330, ev.getX());
	}
	
	
	@Test
	public void testExecuteFiveStepsPPTX() throws IOException, SlideExecutionException{
		List<Slide> slides = readSlidesFromResource("fivesteps.pptx");		
		executor.execute(slides);
		assertEquals("mouse events", 6, detector.getNumMouseEvents());
	}
	
	@Test
	public void testExecuteDragDropPPTX() throws IOException, SlideExecutionException{
		List<Slide> slides = readSlidesFromResource("dragdrop.pptx");		
		executor.execute(slides);		
	}
}
