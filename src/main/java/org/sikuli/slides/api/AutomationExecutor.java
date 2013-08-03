package org.sikuli.slides.api;

import java.util.List;

import org.sikuli.slides.models.Slide;

public class AutomationExecutor implements SlideExecutor {

	
	// For backward compatibility.
	// This allows users to use the pre-1.0 syntax
	// The default is always the most current syntax
	public void setSyntax(String version){
		
	}

	@Override
	public void execute(List<Slide> slides) throws ActionRuntimeException {
		// TODO Auto-generated method stub
					
		// Translate the content of a slide into an action
	}

}
