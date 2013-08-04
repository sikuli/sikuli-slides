package org.sikuli.slides.api;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.sikuli.slides.models.Slide;

public class Slides {

	static public void exeute(URL url) throws SlideExecutionException, IOException {		
		SlidesReader reader = new PPTXSlidesReader();		
		List<Slide> slides;
		slides = reader.read(url);
		SlidesExecutor executor = new AutomationExecutor();
		executor.execute(slides);
	}


}
