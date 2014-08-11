package org.sikuli.slides.api.mocks;

import java.util.List;

import org.sikuli.api.DefaultTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

import com.google.common.collect.Lists;

public class MockTargetFactory {
	
	static public Target canBeFound(){		
		return new DefaultTarget() {
			@Override
			protected List<ScreenRegion> getUnorderedMatches(ScreenRegion screenRegion) {
				return Lists.newArrayList(screenRegion);
			}		
		};
	};
	
	static public Target canNotBeFound(){		
		return new DefaultTarget() {
			@Override
			protected List<ScreenRegion> getUnorderedMatches(ScreenRegion screenRegion) {
				return Lists.newArrayList();
			}		
		};
	};
	
	static public Target canBeFoundAfter(int msecs){
		return new AppearLaterTarget(msecs);
	}

}
