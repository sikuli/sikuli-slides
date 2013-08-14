package org.sikuli.slides.api.sikuli;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

interface SearchStrategy {
	public ScreenRegion perform(ScreenRegion screenRegion);
}

public class CrossSearchStrategy implements SearchStrategy {
	private static final Logger logger = LoggerFactory.getLogger(CrossSearchStrategy.class);
	static private final int widthFactor=30;
	static private final int heightFactor=30;
	
	BufferedImage contextImage;
	Rectangle targetRect;
	
	public CrossSearchStrategy(BufferedImage contextImage, Rectangle targetRect) {
		super();
		this.contextImage = contextImage;
		this.targetRect = targetRect;
	}
	
	@Override
	public ScreenRegion perform(ScreenRegion screenRegion){		
		List<Hypothesis> hs = generateHypotheses(contextImage, targetRect);
		logger.info("generated {} hypotheses", hs.size());
		ScreenRegion ret = testHypotheses(screenRegion, hs);		
		return ret;		
	}

	private static List<Hypothesis> generateHypotheses(BufferedImage contextImage, Rectangle targetRect) {
		Rectangle bounds = new Rectangle(0,0,contextImage.getWidth(), contextImage.getHeight());
		List<Rectangle> regions = generateLargerRegions(targetRect, bounds);
		sortBySize(regions);
		
		// make hypotheses
		List<Hypothesis> hs = Lists.newArrayList();
		for (Rectangle r : regions){			
			ContextTargetHypothesis h = new ContextTargetHypothesis(contextImage, targetRect, r);
			hs.add(h);
		}
		return hs;
	}
		
	static public List<Rectangle> generateLargerRegions(Rectangle seed, Rectangle bounds){
		List<Rectangle> result = Lists.newArrayList();
		result.add(seed);
		result.addAll(generateLargerRegionsAbove(seed, bounds));
		result.addAll(generateLargerRegionsBelow(seed, bounds));
		result.addAll(generateLargerRegionsLeft(seed, bounds));
		result.addAll(generateLargerRegionsRight(seed, bounds));
		return result;
	}
		
	// sort a list of rectangles by their sizes in ascending order
	static public List<Rectangle> sortBySize(List<Rectangle> list){
		List<Rectangle> result = Lists.newArrayList();
		Collections.sort(list, new Comparator<Rectangle>(){
			@Override
			public int compare(Rectangle r1, Rectangle r2) {
				return (r1.height * r1.width) - (r2.height * r2.width);
			}			
		});
		return result;
	}
	
	static private ScreenRegion testHypotheses(ScreenRegion screenRegion, List<Hypothesis> hypotheses){
		for(Hypothesis hypothesis : hypotheses){
			
			Target target = hypothesis.getTarget();
			
			logger.info("test: {}" , hypothesis);
			
			List<ScreenRegion> lookupRegion = screenRegion.findAll(target);
			logger.info("found {} matches", lookupRegion.size());
			
			if(lookupRegion.size()>1){
				continue;
			}
			else if(lookupRegion.size()==1){
				ScreenRegion rawResult = lookupRegion.get(0);
				return hypothesis.interpretResult(rawResult);
			}else {
				return null;
			}
		}
		return null;
	}
	
	
	static public List<Rectangle> generateLargerRegionsAbove(Rectangle seed, Rectangle bounds){		
		List<Rectangle> result = new ArrayList<Rectangle>();
		Rectangle r = new Rectangle(seed);
		for(int i=0;i<20;i++){	
			r.y -= heightFactor;
			r.height += heightFactor;
			r = bounds.intersection(r); // keep the rectangle within bounds
			result.add(new Rectangle(r));
			if (r.y <= bounds.y)
				break;
		}
		return result;
	}
	
	static public List<Rectangle> generateLargerRegionsBelow(Rectangle seed, Rectangle bounds){		
		List<Rectangle> result = new ArrayList<Rectangle>();
		Rectangle r = new Rectangle(seed);
		for(int i=0;i<20;i++){
			r.height += heightFactor;
			r = bounds.intersection(r); // keep the rectangle within bounds
			result.add(new Rectangle(r));
			if (r.y + r.height >= bounds.y + bounds.height)
				break;
		}
		return result;
	}
	
	static public List<Rectangle> generateLargerRegionsRight(Rectangle seed, Rectangle bounds){		
		List<Rectangle> result = new ArrayList<Rectangle>();
		Rectangle r = new Rectangle(seed);
		for(int i=0;i<20;i++){
			r.width += widthFactor;
			r = bounds.intersection(r); // keep the rectangle within bounds
			result.add(new Rectangle(r));
			if (r.x + r.width >= bounds.x + bounds.width)
				break;
		}
		return result;
	}
	
	static public List<Rectangle> generateLargerRegionsLeft(Rectangle seed, Rectangle bounds){		
		List<Rectangle> result = new ArrayList<Rectangle>();
		Rectangle r = new Rectangle(seed);
		for(int i=0;i<20;i++){
			r.x -= widthFactor;
			r.width += widthFactor;
			r = bounds.intersection(r); // keep the rectangle within bounds
			result.add(new Rectangle(r));
			if (r.x <= bounds.x)
				break;
		}
		return result;
	}
}
