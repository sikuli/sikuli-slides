package org.sikuli.slides.examples;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.sikuli.slides.api.AutomationExecutor;
import org.sikuli.slides.api.ActionRuntimeException;
import org.sikuli.slides.api.SlideExecutor;
import org.sikuli.slides.models.Slide;

public class AutomationPresenterExample {

	public static void main(String[] args) throws IOException, ActionRuntimeException {
		
		URL url = AutomationPresenterExample.class.getResource("helloworld.pptx");
		
		SlideExecutor executor = new AutomationExecutor();
		
		List<Slide> slides = new ArrayList<Slide>();
		
		
		
		
				
		// execute a list of slides
		executor.execute(slides);
				
		
	}
}
