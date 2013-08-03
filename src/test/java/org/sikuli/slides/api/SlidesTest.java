package org.sikuli.slides.api;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DefaultLocation;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.actions.InputDetector;

public class SlidesTest {	
	
	private InputDetector detector;

	@Before
	public void setUp() throws NativeHookException{
		detector = new InputDetector();
		detector.start();		
	}

	@After
	public void tearDown(){
		detector.stop();
	}
	
	@Test
	public void testExecute() throws IOException{
		Canvas canvas = new DesktopCanvas();
				
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli.png"));
		canvas.addImage(new DefaultLocation(150,150), image);
		canvas.show();
		
		URL url = getClass().getResource("click.pptx");		
		Slides.exeute(url);
		
		NativeMouseEvent ev = detector.getLastMouseEvent();
		assertEquals("x", 200, ev.getX());
		
		canvas.hide();
	}
	
	@Test
	public void testExecuteFiveStepsPPTX() throws IOException{
		Canvas canvas = new DesktopCanvas();
				
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli_context.png"));
		canvas.addImage(new DefaultLocation(150,150), image);
		canvas.show();
		
		URL url = getClass().getResource("fivesteps.pptx");		
		Slides.exeute(url);
				
		canvas.hide();
		
		assertEquals("mouse events", 4, detector.getNumMouseEvents());
	}
}
