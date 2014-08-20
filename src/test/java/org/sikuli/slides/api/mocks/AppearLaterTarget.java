package org.sikuli.slides.api.mocks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Timer;

import org.sikuli.api.DefaultTarget;
import org.sikuli.api.ScreenRegion;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

// A Target that initially returns no match but after some delay (ms)
// returns the whole screen region as the match 
class AppearLaterTarget extends DefaultTarget {

	volatile boolean found = false;
	private ScreenRegion targetRegion;	
	
	AppearLaterTarget(int delay){
		Timer timer = new Timer(delay, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				found = true;				
			}
		});
		timer.start();
	}
	
	AppearLaterTarget(ScreenRegion targetRegion, int delay){
		this.targetRegion = targetRegion;
		Timer timer = new Timer(delay, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				found = true;				
			}
		});
		timer.start();
	}

	@Override	
	protected List<ScreenRegion> getUnorderedMatches(ScreenRegion screenRegion) {
		if (found){
			return Lists.newArrayList(Objects.firstNonNull(targetRegion, screenRegion));
		}else{		
			return Lists.newArrayList();
		}
	}		
}