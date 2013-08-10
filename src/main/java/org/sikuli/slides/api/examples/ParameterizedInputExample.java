package org.sikuli.slides.api.examples;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;

public class ParameterizedInputExample {

	public static void main(String[] arg) throws SlideExecutionException {
		
		Context context = new Context();
		context.addParameter("username", "user2142");
		context.addParameter("password", "cdads2ev");
		
		Slides.execute(Resources.login_pptx, context);
	}
}
