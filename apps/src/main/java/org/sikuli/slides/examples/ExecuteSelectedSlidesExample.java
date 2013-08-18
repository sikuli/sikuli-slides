package org.sikuli.slides.examples;

import java.io.IOException;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.ExecutionEvent;
import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.ExecutionFilter;
import org.sikuli.slides.api.Slides;
import org.sikuli.slides.api.models.Slide;

public class ExecuteSelectedSlidesExample {

	public static void main(String[] arg) throws SlideExecutionException, IOException{
		
		Context context = new Context();
		// select the 1st, 3rd, 4th, and 5th slides to execute 
		context.setExecutionFilter(new ExecutionFilter(){
			@Override
			public boolean accept(ExecutionEvent event) {
				Slide slide = event.getSlide();
				return slide.getNumber() == 1 || (slide.getNumber() >= 3 && slide.getNumber() <= 5);
			}			
		});
		Slides.execute(Resources.seven_pptx, context);
	}
}
