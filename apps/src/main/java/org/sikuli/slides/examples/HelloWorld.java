package org.sikuli.slides.examples;

import java.net.MalformedURLException;
import java.net.URL;

import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;

	public class HelloWorld {
	
		public static void main(String[] arg) throws SlideExecutionException, MalformedURLException {		
			//Slides.execute(Resources.hello_world_pptx);
			Slides.execute(new URL("http://slides-dev.sikuli.org/helloworld.pptx"));
		}
	}
