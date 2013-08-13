package org.sikuli.slides.api.examples;

import java.io.IOException;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.SlideSelector;
import org.sikuli.slides.api.Slides;
import org.sikuli.slides.api.models.Slide;

public class ExecuteSelectedSlidesExample {

	public static void main(String[] arg) throws SlideExecutionException, IOException{
		
		Context context = new Context();
		// select the 1st, 3rd, 4th, and 5th slides to execute 
		context.setSlideSelector(new SlideSelector(){
			@Override
			public boolean accept(Slide slide) {
				return slide.getNumber() == 1 || (slide.getNumber() >= 3 && slide.getNumber() <= 5);
			}			
		});
		Slides.execute(Resources.seven_pptx, context);
	}
}
