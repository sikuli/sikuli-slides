package org.sikuli.slides.examples;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.sikuli.api.DefaultLocation;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;

public class SecondMonitorExample {

	public static void main(String[] arg) throws SlideExecutionException, IOException{
		
		ScreenRegion secondDesktop = new DesktopScreenRegion(1);		
		
		// display a mock image of an emulator
		Canvas canvas = new ScreenRegionCanvas(secondDesktop);
		canvas.addImage(new DefaultLocation(100,100), ImageIO.read(Resources.emulator_png));
		canvas.show();
		
		Context context = new Context();
		context.setScreenRegion(secondDesktop);
		
		// execute a series of click operations defined in "emulator.pptx"
		Slides.execute(Resources.emulator_pptx, context);
		
		// hide the mock image
		canvas.hide();
	}
}
