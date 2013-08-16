package org.sikuli.slides.examples;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;

public class TemplateExample {

	public static void main(String[] arg) throws SlideExecutionException {
		
		Context context = new Context();
		context.addParameter("name", "Sikuli");		
		
		Slides.execute(Resources.template_pptx, context);
	}
}
