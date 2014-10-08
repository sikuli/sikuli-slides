package org.sikuli.recorder;

import org.sikuli.recorder.event.Event;

public interface EventWriter {
	public void write(Event event);
	//public void write(NativeKeyEvent event);
}