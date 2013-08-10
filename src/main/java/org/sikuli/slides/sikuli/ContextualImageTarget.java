package org.sikuli.slides.sikuli;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

import com.google.common.base.Objects;

public class ContextualImageTarget implements Target {
	
	private URL contextImage;
	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	private int limit = 1;
	
	public ContextualImageTarget(URL contextImage, double xmin, double ymin, double xmax, double ymax){
		this.setContextImage(contextImage);
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}		

	@Override
	public double getMinScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMinScore(double minScore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
		
	}

	@Override
	public Ordering getOrdering() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOrdering(Ordering ordering) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ScreenRegion> doFindAll(ScreenRegion screenRegion) {
		BufferedImage image = getTargetImage();
		Target imageTarget = new ImageTarget(image);
		return imageTarget.doFindAll(screenRegion);
	}
	
	private static BufferedImage crop(URL imageUrl, double xmin, double ymin, double xmax, double ymax){
		
		BufferedImage imageTocrop;
		try {
			imageTocrop= ImageIO.read(imageUrl);
			int x = (int) Math.round(xmin * imageTocrop.getWidth());
			int y = (int) Math.round(ymin * imageTocrop.getHeight());
			int width = (int) ((xmax - xmin) * imageTocrop.getWidth());
			int height = (int) ((ymax - ymin) * imageTocrop.getHeight());
			return imageTocrop.getSubimage(x, y, width, height);
		} catch (IOException e) {
		}
		return null;
	}

	private BufferedImage getTargetImage() {
		return crop(getContextImage(), xmin, ymin, xmax, ymax);
	}

	public URL getContextImage() {
		return contextImage;
	}

	public void setContextImage(URL contextImage) {
		this.contextImage = contextImage;
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("image", contextImage).add("xmin",xmin).add("xmax",xmax)
				.add("ymin",ymin).add("ymax",ymax).toString();
	}


}
