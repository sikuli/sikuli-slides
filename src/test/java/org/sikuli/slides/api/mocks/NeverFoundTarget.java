package org.sikuli.slides.api.mocks;

import java.util.List;

import org.sikuli.api.DefaultTarget;
import org.sikuli.api.ScreenRegion;

import com.google.common.collect.Lists;

public class NeverFoundTarget extends DefaultTarget {

	@Override
	protected List<ScreenRegion> getUnorderedMatches(
			ScreenRegion screenRegion) {
		return Lists.newArrayList();
	}		
}