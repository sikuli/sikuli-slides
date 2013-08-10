package org.sikuli.slides.api.examples;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.sikuli.api.DefaultLocation;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;

public class EmulatorExample {

	public static void main(String[] arg) throws SlideExecutionException, IOException{
		
		// display a mock image of an emulator
		Canvas canvas = new DesktopCanvas();
		canvas.addImage(new DefaultLocation(100,100), ImageIO.read(EmulatorExample.class.getResource("emulator.png")));
		canvas.show();
		
		// execute a series of click operations defined in "emulator.pptx"
		Slides.execute(EmulatorExample.class.getResource("emulator.pptx"));
		
		// hide the mock image
		canvas.hide();
	}
}
