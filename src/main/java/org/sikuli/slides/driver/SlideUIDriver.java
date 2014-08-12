package org.sikuli.slides.driver;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.models.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlideUIDriver implements UIDriver {
		
	static Logger log = LoggerFactory.getLogger(SlideUIDriver.class);
	
	private UISpec spec;
	
	public SlideUIDriver(Slide slide){
		interpretSlide(slide);
	}
	
	public SlideUIDriver(){
		spec = new UISpec();
	}

	void interpretSlide(Slide slide){
		spec = new DefaultUISpecInterpreter().interpret(slide);
	}
	
	@Override
	public UIElement findElement(String label) {
				
		UIElement element = spec.findElementByLabel(label);
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
