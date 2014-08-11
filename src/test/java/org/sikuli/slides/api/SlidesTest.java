package org.sikuli.slides.api;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jnativehook.mouse.NativeMouseEvent;
import org.junit.Test;
import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.io.SlidesReader;
import org.sikuli.slides.api.models.Slide;

public class SlidesTest {

	@Test
	public void testExecuteCiscoDemo() throws IOException, SlideExecutionException{
		//Slides.execute();
		//List<Slide> slides = readSlidesFromResource("ciscodemo.pptx");		
		//executor.execute(slides);		
		//NativeMouseEvent ev = detector.getLastMouseEvent();
		//assertEquals("x", 330, ev.getX());
		
		Context context = new Context();
		File file = new File("ciscodemo.pptx"); 
		
		SlidesReader reader = new PPTXSlidesReader();		
		List<Slide> slides;
		try {
			slides = reader.read(file);
		} catch (IOException e) {
			throw new SlideExecutionException(e);		
		}

		SlidesExecutor executor = new SlideShowExecutor(context);
		executor.execute(slides);
	}

}
