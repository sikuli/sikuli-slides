package org.sikuli.slides.api.actions;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.jnativehook.mouse.NativeMouseEvent;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.recorder.detector.EventDetector;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.concurrent.Latch;
import org.sikuli.slides.api.concurrent.ScreenRegionHoverLatch;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.*;

public class PauseAction extends ChainedAction {

	private Latch latch;

	@Override
	public void execute(Context context) throws ActionExecutionException {

		// set up a canvas to display a button in the middle of the screen
		ScreenRegion r = context.getScreenRegion();
		ScreenRegion centerRegion = Relative.to(r).region(0.4,0.4,0.6,0.6).getScreenRegion();		

		Canvas canvas = new ScreenRegionCanvas(r);		
		canvas.addBox(centerRegion).withColor(Color.black).withTransparency(0.7f);
		canvas.addLabel(context.getScreenRegion().getCenter(), "  Mouse over to Continue ")
		.withBackgroundColor(Color.black)
		.withColor(Color.white)
		.withTransparency(0.7f)
		.withFontSize(20)
		.withHorizontalAlignmentCenter()		
		.withVerticalAlignmentMiddle();
		canvas.show();

		// wait for a click event on the button
		latch = new ScreenRegionHoverLatch(centerRegion);
		latch.await();
				
		// hide the button
		canvas.hide();
		
		if (getChild() != null)
			getChild().execute(context);
	}
	
	public void stop(){
		if (latch != null)
			latch.release();
		super.stop();
	}

}
