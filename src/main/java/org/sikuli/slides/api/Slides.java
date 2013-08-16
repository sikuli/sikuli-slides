package org.sikuli.slides.api;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.io.SlidesReader;
import org.sikuli.slides.api.models.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slides {
	
	static Logger logger = LoggerFactory.getLogger(Slides.class);

	static public void execute(URL url) throws SlideExecutionException {		
		ScreenRegion screenRegion = new DesktopScreenRegion();
		Context context = new Context(screenRegion);
		execute(url, context);		
	}

	static public void execute(URL url, Context context) throws SlideExecutionException {	
		logger.debug("execute slides with context {}", context);
		SlidesReader reader = new PPTXSlidesReader();		
		List<Slide> slides;
		try {
			slides = reader.read(url);
		} catch (IOException e) {
			throw new SlideExecutionException(e);		
		}

		SlidesExecutor executor = new AutomationExecutor(context);
		executor.execute(slides);
	}

	static public void execute(File file) throws SlideExecutionException {
		try {
			execute(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new SlideExecutionException(e);
		}
	}

	static public void execute(File file, Context context) throws SlideExecutionException {		
		try {
			execute(file.toURI().toURL(), context);
		} catch (MalformedURLException e) {
			throw new SlideExecutionException(e);
		}
	}

}
