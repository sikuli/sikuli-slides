package org.sikuli.recorder.detector;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.recorder.DefaultEventWriter;
import org.sikuli.recorder.EventWriter;
import org.sikuli.recorder.event.Event;

public class EventDetector {
	private EventWriter writer;
	private ScreenRegion regionOfInterest;
		
	public EventDetector(){
		writer = new DefaultEventWriter();
		regionOfInterest = new DesktopScreenRegion();		
	}
	
	public void eventDetected(Event event){
		if (writer != null)
			writer.write(event);
	}
	
	public void start(){		
	}

	public void stop(){		
	}
	
	public void setWriter(EventWriter writer) {
		this.writer = writer;
	}
	
	
	// set the screen region to detect events
	// events occurring outside  the region should be discarded
	public void setRegionOfInterest(ScreenRegion region){
		regionOfInterest = region;
	}
	
	public ScreenRegion getRegionOfInterest(){
		return regionOfInterest;
	}

}