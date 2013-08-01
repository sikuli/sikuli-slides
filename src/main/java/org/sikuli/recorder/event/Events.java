package org.sikuli.recorder.event;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Events {
	
	static private Logger logger = LoggerFactory.getLogger(Events.class);
	
	static public List<Event> readEventsFrom(File inputDir){

		List<Event> events = Lists.newArrayList();


		File[] files = inputDir.listFiles();

		// sort the files so that they are chronologically ordered
		// we assume that the name ordering reflects its chronological order 
		Arrays.sort(files);

		for (File f : files){

			String s = f.getPath();

			if (s.contains("click.txt")){
				try {
					String jsonString = Files.toString(f, Charsets.US_ASCII);
					ClickEvent clickEvent = ClickEvent.createFromJSON(jsonString);
					events.add(clickEvent);
					logger.trace("read event:" + clickEvent);
				} catch (IOException e1) {
				}

			}else if (s.contains("screenshot.png")){								
				ScreenShotEvent screenShotEvent = ScreenShotEvent.createFromFile(f);				
				events.add(screenShotEvent);				
			}
		}
		return events;
	}

	static public List<ClickEventGroup> getClickEventGroups(List<Event> events){

		List<ClickEventGroup >slideDataList = Lists.newArrayList(); 
		for (int i = 0; i < events.size(); ++i) {
			Event e = events.get(i);
			if (e instanceof ClickEvent){

				ClickEvent clickEvent = (ClickEvent) e;				
				ScreenShotEvent screenShotEventBefore = Events.findScreenShotEventBefore(events, i);
				boolean isClickEventNext = (i < events.size() - 1) && events.get(i+1) instanceof ClickEvent;
				if (isClickEventNext){
					// if it's immediately followed by a click event, it is probably
					// the first click event of a double-click event, so we ignore this one
					// and wait to process the next one
					continue;
				}
				if (screenShotEventBefore != null){					

					ClickEventGroup data = new ClickEventGroup();				
					data.setClickEvent(clickEvent);
					data.setScreenShotEventBefore(screenShotEventBefore);
					
					slideDataList.add(data);
				}
			}
		}

		return slideDataList;
	}
	
	static private ScreenShotEvent findScreenShotEventBefore(List<Event> events, int start) {
		for (int i = start; i >= 0; i--){
			Event e = events.get(i);
			if (e instanceof ScreenShotEvent)
				return (ScreenShotEvent) e;
		}
		return null;
	}
	



}