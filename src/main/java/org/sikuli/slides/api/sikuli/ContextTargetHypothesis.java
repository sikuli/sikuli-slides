package org.sikuli.slides.api.sikuli;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

import com.google.common.base.Objects;

class ContextTargetHypothesis implements Hypothesis {		
	public ContextTargetHypothesis(BufferedImage contextImage, Rectangle originalTargetRect, Rectangle contextTargetRect) {
		super();
		this.contextImage = contextImage;
		this.originalTargetRect = originalTargetRect;
		this.contextTargetRect = contextTargetRect;
	}

	final BufferedImage contextImage;
	final Rectangle originalTargetRect;
	final Rectangle contextTargetRect;
	
	@Override
	public Target getTarget(){
		Rectangle r = contextTargetRect;
		BufferedImage targetImage = contextImage.getSubimage(r.x,r.y,r.width,r.height);
		Target target = new ImageTarget(targetImage);
		return target; 
	}
	
	@Override
	public ScreenRegion interpretResult(ScreenRegion rawResult){
		int xoffset = originalTargetRect.x - contextTargetRect.x;
		int yoffset = originalTargetRect.y - contextTargetRect.y;
		ScreenRegion finalResult = rawResult.getRelativeScreenRegion(xoffset, yoffset, originalTargetRect.width, originalTargetRect.height);				
		return finalResult;
	}
	
	public String toString(){
		Rectangle r = contextTargetRect;
		return Objects.toStringHelper(this).add("rect", String.format("%d,%d,%d,%d", r.x, r.y, r.width, r.height)).toString();
	}
}