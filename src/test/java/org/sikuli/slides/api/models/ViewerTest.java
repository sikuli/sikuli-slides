package org.sikuli.slides.api.models;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.sikuli.slides.api.TestResources;
import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.io.SlidesReader;
import org.sikuli.slides.api.views.SimpleSlideViewer;

public class ViewerTest {

	@Test
	public void testSimpleSlideViewer() throws IOException{
		SlidesReader reader = new PPTXSlidesReader();
		URL url = TestResources.get("fiveSteps.pptx");
		List<Slide> slides = reader.read(url);
		SimpleSlideViewer window = new SimpleSlideViewer();
		window.setVisible(true);		

		for (Slide slide : slides){
			window.setSlide(slide);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

}
