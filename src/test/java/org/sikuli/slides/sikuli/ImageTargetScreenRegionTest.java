package org.sikuli.slides.sikuli;


import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.sikuli.api.DefaultLocation;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;


public class ImageTargetScreenRegionTest {
		
	@Test
	public void testFirstLazyEvalFind() throws IOException{
		Canvas canvas = new DesktopCanvas();
		BufferedImage targetImage = ImageIO.read(ImageTargetScreenRegionTest.class.getResource("sikuli.png"));
		ImageTarget imageTarget = new ImageTarget(targetImage);

		int x = 140;
		int y = 140;
		canvas.addImage(new DefaultLocation(x,y), targetImage);
		canvas.show();
		
		ScreenRegion imageTargetScreenRegion = new TargetScreenRegion(imageTarget, new DesktopScreenRegion());		
		Rectangle r = imageTargetScreenRegion.getBounds();
		canvas.hide();
		
		assertEquals("x", x, r.x);
		assertEquals("y", y, r.y);		
	}
	
	
}
