package org.sikuli.slides.api;

import java.util.List;

import org.sikuli.api.DefaultTarget;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class RelativeScreenRegionTarget extends DefaultTarget {
	
	private double xmin = 0;
	private double xmax = 1;
	private double ymin = 0;
	private double ymax = 1;
	
	public RelativeScreenRegionTarget(double xmin, double ymin, double xmax, double ymax){
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}	

	@Override
	protected List<ScreenRegion> getUnorderedMatches(ScreenRegion screenRegion) {
		ScreenRegion targetRegion = Relative.to(screenRegion).region(xmin, ymin, xmax, ymax).getScreenRegion();
		return Lists.newArrayList(targetRegion);
	}
	
	public String toString(){
		return Objects.toStringHelper(this)
		.add("x",String.format("[%.2f,%.2f]", xmin, xmax))
		.add("y",String.format("[%.2f,%.2f]", ymin, ymax)).toString();
	}

}
