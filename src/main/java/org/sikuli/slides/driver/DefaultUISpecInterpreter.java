package org.sikuli.slides.driver;

import java.util.List;

import org.sikuli.api.Target;
import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;
import org.sikuli.slides.api.sikuli.ContextImageTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultUISpecInterpreter implements UISpecInterpreter { 
	
	static Logger log = LoggerFactory.getLogger(DefaultUISpecInterpreter.class);

	@Override
	public UISpec interpret(Slide slide) {
		UISpec page = new UISpec();

		// find all targets
		List<SlideElement> targetElements = slide.select().isTarget().all();

		// for each target element, create a context target			
		for (SlideElement targetElement : targetElements){

			ImageElement imageElement = (ImageElement) slide.select().intersects(targetElement).isImage().first();
			if (imageElement == null)
				continue;

			Target target = interpret(slide, targetElement);
			if (target == null)
				continue;
			
			SlideElement labelElement = slide.select().intersects(targetElement).hasText().first();
			if (labelElement == null)
				continue;
			
			SlideUIElement element = new SlideUIElement(target, labelElement.getText());
			page.add(element);
			
			log.trace("added to spec: " + element);
		}			
		return page;
	}		

	public Target interpret(Slide slide, SlideElement targetElement) {

		ImageElement imageElement = (ImageElement) slide.select().intersects(targetElement).isImage().first();
		if (imageElement == null)
			return null;

		Target target = createTarget(imageElement, targetElement);
		if (target == null)
			return null;
		return target;
	}

	Target createTarget(ImageElement imageElement, SlideElement targetElement){
		if (imageElement == null || targetElement == null)
			return null;

		int w = imageElement.getCx();
		int h = imageElement.getCy();
		if (w <= 0 || h <= 0)
			return null;

		double xmax = 1.0 * (targetElement.getOffx() + targetElement.getCx() - imageElement.getOffx()) / w;
		double ymax = 1.0 * (targetElement.getOffy() + targetElement.getCy() - imageElement.getOffy()) / h;
		double xmin = 1.0 * (targetElement.getOffx() - imageElement.getOffx()) / w;
		double ymin = 1.0 * (targetElement.getOffy() - imageElement.getOffy()) / h;

		xmax = Math.min(1.0, xmax);
		ymax = Math.min(1.0, ymax);
		xmin = Math.max(0, xmin);
		ymin = Math.max(0, ymin);

		return new ContextImageTarget(imageElement.getSource(), xmin, ymin, xmax, ymax); 
	}

}



