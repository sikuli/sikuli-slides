package org.sikuli.slides.driver;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.models.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSlideDriver implements SlideDriver {
		
	static Logger log = LoggerFactory.getLogger(DefaultSlideDriver.class);
	
	private SlideSpec spec;
	
	public DefaultSlideDriver(Slide slide){
		interpretSlide(slide);
	}
	
	public DefaultSlideDriver(){
		spec = new SlideSpec();
	}

	void interpretSlide(Slide slide){
		spec = new DefaultUISpecInterpreter().interpret(slide);
	}
	
	@Override
	public Widget findElement(String label) {
				
		Widget element = spec.findElementByLabel(label);
		if (element == null)
			// element of the given label does not exist
			return null;
		
		log.debug("attempt to find: "+ element);
					
		Target target = element.getTarget();
		ScreenRegion screen = getScreenRegion();		
		ScreenRegion found = screen.find(target);
			
		if (found != null){
			log.debug("target is found at: " + found);		
			element.setScreenRegion(found);
		}else{
			log.debug("target is not found!");
		}
		
		return element;
	}

	private ScreenRegion getScreenRegion() {
		return new DesktopScreenRegion();
	}

}
