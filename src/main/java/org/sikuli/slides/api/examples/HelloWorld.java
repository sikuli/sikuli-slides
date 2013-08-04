package org.sikuli.slides.api.examples;

import java.io.IOException;

import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;

public class HelloWorld {

	public static void main(String[] arg) throws SlideExecutionException, IOException{		
		Slides.execute(HelloWorld.class.getResource("helloworld.pptx"));
	}
}
