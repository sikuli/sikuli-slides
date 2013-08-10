package org.sikuli.slides.api;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.models.Slide;

public class Slides {

	static public void execute(URL url) throws SlideExecutionException {		
		ScreenRegion screenRegion = new DesktopScreenRegion();
		Context context = new Context(screenRegion);
		execute(url, context);		
	}
	
	static public void execute(URL url, Context context) throws SlideExecutionException {		
		SlidesReader reader = new PPTXSlidesReader();		
		List<Slide> slides;
		try {
			slides = reader.read(url);
		} catch (IOException e) {
			SlideExecutionException ex = new SlideExecutionException(e.getMessage());		
			throw ex;
		}
		
		SlidesExecutor executor = new AutomationExecutor(context);
		executor.execute(slides);
	}


}
