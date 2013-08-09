package org.sikuli.slides.api.mocks;

import java.util.List;

import org.sikuli.api.DefaultTarget;
import org.sikuli.api.ScreenRegion;

import com.google.common.collect.Lists;

public class IdentiyScreenRegionTarget extends DefaultTarget {

	@Override
	public List<ScreenRegion> doFindAll(ScreenRegion screenRegion) {
		return Lists.newArrayList(screenRegion);
	}

	@Override
	protected List<ScreenRegion> getUnorderedMatches(
			ScreenRegion screenRegion) {
		return Lists.newArrayList(screenRegion);
	}		
}