package org.sikuli.slides.api.views;

import java.awt.Color;

import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;

import edu.umd.cs.piccolo.PNode;

public class PSlide extends PNode {
		
	public PSlide(Slide slide){
		for (SlideElement element : slide.select().all()){
			PNode aNode = new PSlideElement(element);
			addChild(aNode);
		}
		
		setWidth(slide.getWidth());
		setHeight(slide.getHeight());
		setPaint(Color.white);
	}
	
}