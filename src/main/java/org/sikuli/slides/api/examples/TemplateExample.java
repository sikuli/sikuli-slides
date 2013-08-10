package org.sikuli.slides.api.examples;

import java.io.IOException;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;

public class TemplateExample {

	public static void main(String[] arg) throws SlideExecutionException, IOException{
		
		Context context = new Context();
		context.addParameter("name", "Sikuli");		
		
		Slides.execute(Resources.template_pptx, context);
	}
}
