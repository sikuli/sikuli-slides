package org.sikuli.slides.api;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.sikuli.api.DefaultLocation;
import org.sikuli.api.DefaultScreenLocation;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;

public class SlidesTest {	
	
	@Test
	public void testExecute() throws IOException{
		Canvas canvas = new DesktopCanvas();
				
		BufferedImage image = ImageIO.read(getClass().getResource("sikuli.png"));
		canvas.addImage(new DefaultLocation(150,150), image);
		canvas.show();
		
		URL url = getClass().getResource("click.pptx");		
		Slides.exeute(url);
	}
}
