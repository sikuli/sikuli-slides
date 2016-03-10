package org.sikuli.recorder.html;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.sikuli.api.API;
import org.sikuli.recorder.event.ClickEvent;
import org.sikuli.recorder.event.ClickEventGroup;
import org.sikuli.recorder.event.Event;
import org.sikuli.recorder.event.Events;
import org.sikuli.recorder.event.ScreenShotEvent;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.stringtemplate.v4.*;

public class HTMLGenerator {

	public static void generate(File inputDir, File outputDir){
		List<Event> events = Events.readEventsFrom(inputDir);

		if (!outputDir.exists()){
			outputDir.mkdir();
		}

		File imageDir = new File(outputDir, "images");
		if (!imageDir.exists()){
			imageDir.mkdir();
		}


		ST pageListST = stg.getInstanceOf("page_list");
		ST indexST = stg.getInstanceOf("index");
		String firstPageUrl = null;

		int no = 1;
		List<ClickEventGroup> slideDataList = Events.getClickEventGroups(events);

		for (ClickEventGroup data : slideDataList){

			ClickEvent clickEvent = (ClickEvent) data.getClickEvent();				
			ScreenShotEvent screenShotEventBefore = data.getScreenShotEventBefore();

			// copy image file
			try {
				File src = screenShotEventBefore.getFile();
				File dest = new File(imageDir, src.getName());
				Files.copy(src, dest);
			} catch (IOException e2) {
				e2.printStackTrace();
			}				

			ST pageST = stg.getInstanceOf("page");
			pageST.add("x", clickEvent.getX()-25);
			pageST.add("y", clickEvent.getY()-25);
			pageST.add("xc", clickEvent.getX()-5);
			pageST.add("yc", clickEvent.getY()-5);
			pageST.add("imgurl", "images/" + screenShotEventBefore.getFile().getName());
			
			// TODO DRY this clickEvent --> command
			String command = "";			
			if (clickEvent.getButton() == MouseEvent.BUTTON1){
				if (clickEvent.getCount() == 1){
					command = "Click";
				}else if (clickEvent.getCount() == 2){
					command = "Double Click";
				}
			} else if (clickEvent.getButton() == MouseEvent.BUTTON3){
				command = "Right Click";
			}			
			pageST.add("command", command);

			String pageName = "" + no;
			String pageUrl = pageName + ".html";

			File outFile = new File(outputDir, pageUrl);
			try {
				Files.write(pageST.render(), outFile, Charsets.UTF_8);
			} catch (IOException e1) {				

			}

			pageListST.addAggr("pages.{url,name}", pageUrl, pageName);
 
			if (firstPageUrl == null){
				firstPageUrl = pageUrl;
				indexST.add("firstPageUrl", firstPageUrl);
			}				

			// increment the page counter
			no++;
		}

		File indexFile = new File(outputDir, "index.html");				
		File pageListFile = new File(outputDir, "page-list.html");
		try {			
			Files.write(pageListST.render(), pageListFile, Charsets.UTF_8);
			Files.write(indexST.render(), indexFile, Charsets.UTF_8);
		} catch (IOException e1) {				

		}

	}

	static STGroup stg = new STGroupFile("org/sikuli/recorder/html/html.stg", "utf-8", '$', '$');

	public static void main(String[] args) throws MalformedURLException {

		File inputDir = new File("output/2013-06-06-15-14-21");		
		File outputDir = new File("html");

		HTMLGenerator g = new HTMLGenerator();
		g.generate(inputDir, outputDir);

		URI uri = new File(outputDir, "index.html").toURI();
		URL url = uri.toURL();
		API.browse(url);
	}

}