/**
Khalid
*/
package org.sikuli.slides.v1.processing;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImageProcessing {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(ImageProcessing.class);
	/**
	 * scales the rectangle width to the full size screenshot. It scales the width of the rectangle in the slide to a larger width relative to the full size screenshot.
	 * @param rectangleWidth the width of the rectangle in the slide (*.pptx file)
	 * @param resizedScreenshotWidth the width of the resized screenshot in the slide
	 * @param fullScreenshotWidth the width of the full screenshot
	 * @return the rectangle width relative to the width of the full screenshot.
	 */
	public static double scaleRectangleWidth(int rectangleWidth, int resizedScreenshotWidth, int fullScreenshotWidth){
		return ((double)rectangleWidth/(double)resizedScreenshotWidth)*(double)fullScreenshotWidth;
	}
	
	/**
	 * scales the rectangle height to the full size screenshot. It scales the height of the rectangle in the slide to a larger height relative to the full size screenshot.
	 * @param rectangleHeight the height of the rectangle in the slide (*.pptx file)
	 * @param resizedScreenshotHeight the height of the resized screenshot in the slide
	 * @param fullScreenshotHeight the height of the full screenshot
	 * @return the rectangle height relative to the width of the full screenshot.
	 */
	public static double scaleRectangleHeight(int rectangleHeight, int resizedScreenshotHeight, int fullScreenshotHeight){
		return ((double)rectangleHeight/(double)resizedScreenshotHeight)*(double)fullScreenshotHeight;
	}
	
	public static double getRelativeX(int x,int resizedScreenshotWidth,int fullScreenshotWidth){
		return (double)x*((double)fullScreenshotWidth/(double)resizedScreenshotWidth);
	}
	
	public static double getRelativeY(int y,int resizedScreenshotHeight,int fullScreenshotHeight){
		return (double)y*((double)fullScreenshotHeight/(double)resizedScreenshotHeight);
	}
	//public static double getRelativeX(int )
	
	/**
	 * creates resized copy of an image
	 * @param originalImage the original image to resize
	 * @param scaledWidth the new scaled width
	 * @param scaledHeight the new scaled height
	 * @param preserveAlpha 
	 * @return the resized image.
	 */
	public static BufferedImage createResizedCopy(Image originalImage, 
    		int scaledWidth, int scaledHeight, boolean preserveAlpha)
	{
    	int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    	BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
    	Graphics2D g = scaledBI.createGraphics();
    	if (preserveAlpha) {
    		g.setComposite(AlphaComposite.Src);
    	}
    	g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
    	g.dispose();
    	return scaledBI;
	}
	
	public static BufferedImage cropImage(URL imageUrl, double xmin, double ymin, double xmax, double ymax){
		BufferedImage imageTocrop,croppedImage=null;
		try {
			imageTocrop= ImageIO.read(imageUrl);
			int x = (int) xmin * imageTocrop.getWidth();
			int y = (int) ymin * imageTocrop.getHeight();
			int width = (int) (xmax - xmin) * imageTocrop.getWidth();
			int height = (int) (ymax - ymin) * imageTocrop.getHeight();
			return croppedImage = imageTocrop.getSubimage(x, y, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(RasterFormatException e){
			logger.error("Error: The shape is not contained within the boundaries of the screenshot.");
			System.exit(1);
		}
		return croppedImage;
	}
	
	
	public static BufferedImage cropImage(String imageLocation, int x, int y, int width, int height){
		BufferedImage imageTocrop,croppedImage=null;
		try {
			imageTocrop= ImageIO.read(new File (imageLocation));
			return croppedImage=imageTocrop.getSubimage(x, y, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(RasterFormatException e){
			logger.error("Error: The shape is not contained within the boundaries of the screenshot.");
			System.exit(1);
		}
		return croppedImage;
	}
	
    public static void writeImageToDisk(BufferedImage bufferedImage, String outputFileAbsoluteName){
    	File outputfile = new File(outputFileAbsoluteName);
        try {
			ImageIO.write(bufferedImage, "png", outputfile);
		} 
        catch (IOException e) {
			e.printStackTrace();
		}
    }
}
