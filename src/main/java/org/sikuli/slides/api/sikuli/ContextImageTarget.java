package org.sikuli.slides.api.sikuli;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Slides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class ContextImageTarget implements Target {

	static Logger logger = LoggerFactory.getLogger(ContextImageTarget.class);
	
	private URL contextImageURL;
	private BufferedImage contextImage;

	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	private int limit = 1;

	private boolean isPixels =false;

	private int x;
	private int y;
	private int width;
	private int height;

	public ContextImageTarget(URL contextImageURL, double xmin, double ymin, double xmax, double ymax){
		this.contextImageURL = contextImageURL;
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.isPixels = false;
	}		

	public ContextImageTarget(URL contextImageURL, int x, int y, int width, int height){
		this.contextImageURL = contextImageURL;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isPixels = true;
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
	
	public Rectangle getTargetBounds(){
		BufferedImage image = getContextImage();		
		Rectangle r = new Rectangle();
		if (isPixels){
			r.x = Math.max(0,x);
			r.y = Math.max(0,y);
			r.width = Math.min(width + x, image.getWidth()) - x;
			r.height = Math.min(height + y, image.getHeight()) - y;
		}else{			
			r.x = (int) Math.round(xmin * image.getWidth());
			r.y = (int) Math.round(ymin * image.getHeight());
			r.width = (int) ((xmax - xmin) * image.getWidth());
			r.height = (int) ((ymax - ymin) * image.getHeight());
		}
		return r;
	}
	
	public BufferedImage getTargetImage(){
		Rectangle bounds = getTargetBounds();
		BufferedImage image = getContextImage();
		return image.getSubimage(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public List<ScreenRegion> doFindAll(ScreenRegion screenRegion) {
		SearchStrategy strategy = new CrossSearchStrategy(getContextImage(), getTargetBounds());
		ScreenRegion ret = strategy.perform(screenRegion);			
		if (ret == null){
			return Lists.newArrayList();
		}else{
			return Lists.newArrayList(ret);
		}
	}

	public BufferedImage getContextImage() {
		if (contextImage == null){
			try {
				contextImage = ImageIO.read(contextImageURL);
			} catch (IOException e) {
			}
		}
		return contextImage;
	}

	public String toString(){
		if (isPixels){
			return Objects.toStringHelper(this)
					.add("image", contextImageURL)
					.add("x", x)
					.add("y", y)
					.add("width", width)
					.add("height", height)
					.toString();
			}else{
		return Objects.toStringHelper(this)
				.add("image", contextImageURL)
				.add("x", String.format("(%.2f,%.2f)", xmin, xmax))
				.add("y", String.format("(%.2f,%.2f)", ymin, ymax))
				.toString();
			}
	}


}
