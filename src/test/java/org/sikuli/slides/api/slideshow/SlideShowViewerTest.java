package org.sikuli.slides.api.slideshow;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.jnativehook.NativeHookException;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DefaultLocation;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.TestResources;
import org.sikuli.slides.api.models.Slide;

public class SlideShowViewerTest {
	
	void pause(long msecs){
		try {
			Thread.sleep(msecs);
		} catch (InterruptedException e) {
		}
	}
	
	private DesktopCanvas canvas;

	@Before
	public void setUp() throws NativeHookException, IOException{
//		canvas = new DesktopCanvas();
//		BufferedImage image = ImageIO.read(TestResources.get("sikuli_context.png"));
//		canvas.addImage(new DefaultLocation(400,150), image);
//		canvas.show();		
	}
	
	@Test
	public void test() throws IOException{
				
//		List<Slide> slides = TestResources.readSlides("fivesteps.pptx");
		
		
//		Context context = new Context();
//		SlideShowController slideshow = new DefaultSlideShowController(context, slides);
////		slideshow.setContent(slides);
		
		SlideShowViewer viewer = new SlideShowViewer();
		viewer.setVisible(true);
//		viewer.open(TestResources.get("fivesteps.pptx"));
		viewer.invokeOpen(new File("/Users/tomyeh/Desktop/Testtest.pptx"));
		
//		slideshow.start();
		
//		SlideShowHotkeyManager hotkeys = new SlideShowHotkeyManager(slideshow);
//		hotkeys.start();
		
		pause(100000);
		
	}

}
