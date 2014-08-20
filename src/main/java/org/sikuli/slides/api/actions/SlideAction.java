package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.models.Slide;

public class SlideAction extends ChainedAction {
	
	private Slide slide;
	public SlideAction(Slide slide){
		this.setSlide(slide);
	}
		
	@Override
	public void execute(Context context) throws ActionExecutionException {
		Action child = getChild();
		if (child != null){
			child.execute(context);
		}
	}

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}
	
}
