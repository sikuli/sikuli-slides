package org.sikuli.slides.api.examples;

import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;

public class ExceptionHandlingExample {

	public static void main(String[] arg) {
		try {
			Slides.execute(Resources.fail_pptx);
		}catch (SlideExecutionException e) {
			System.err.println("Failed to execute slide no. " + e.getSlide().getNumber());
			System.err.println(" because " + e.getMessage());			
			System.err.println(" action: " + e.getAction());
		} 
	}
}
