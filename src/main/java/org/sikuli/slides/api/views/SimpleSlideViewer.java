package org.sikuli.slides.api.views;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.sikuli.slides.api.models.Slide;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;

@SuppressWarnings("serial")
public class SimpleSlideViewer extends JFrame {

	private final PCanvas canvas;
	public SimpleSlideViewer() {
		canvas = new PCanvas();
		getContentPane().add(canvas);
		getContentPane().setPreferredSize(new Dimension(400,300));
		pack();
		setAlwaysOnTop(true);		
		
	}

	public void setSlide(Slide slide){
		canvas.getLayer().removeAllChildren();

		PNode pSlide = new PSlide(slide);

		// scale to fit the display area
		double xScale = 1.0 * getContentPane().getWidth()/ pSlide.getWidth();
		double yScale = 1.0 * getContentPane().getHeight() / pSlide.getHeight();
		pSlide.scale(Math.min(xScale,yScale));
		pSlide.setTransparency(0.8f);

		canvas.getLayer().addChild(pSlide);
		canvas.repaint();
	}

}