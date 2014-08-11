/**
Khalid
*/
package org.sikuli.slides.v1.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.FilenameUtils;
import org.sikuli.slides.v1.media.Sound;
import org.sikuli.slides.v1.parsing.PresentationParser;
import org.sikuli.slides.v1.parsing.SlideParser;
import org.sikuli.slides.v1.presentation.Presentation;
import org.sikuli.slides.v1.processing.ImageProcessing;
import org.sikuli.slides.v1.processing.Relationship;
import org.sikuli.slides.v1.processing.SlideProcessing;
import org.sikuli.slides.v1.screenshots.Screenshot;
import org.sikuli.slides.v1.screenshots.SlideTargetRegion;
import org.sikuli.slides.v1.shapes.SlideShape;
import org.sikuli.slides.v1.utils.Constants;
import org.sikuli.slides.v1.utils.UnitConverter;
import org.sikuli.slides.v1.utils.Utils;
import org.sikuli.slides.v1.utils.Constants.DesktopEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: This class needs to be refactored.
public class SikuliPowerPoint {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SikuliPowerPoint.class);
	private Presentation presentation;
	private String pptxSourceName;
	private AtomicInteger counter;
	private List<SikuliAction> tasks;
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	public SikuliPowerPoint(String pptxSourceName){
		this.pptxSourceName = pptxSourceName;
		counter=new AtomicInteger();
		tasks=new ArrayList<SikuliAction>();
	}
	
	public void runSikuliPowerPoint(int start, int end){
		// load the .pptx file
		loadPresentationFile();
		// parse the general presentation.xml file
		parsePresentationFile();
		
		if (end == -1){
			end = presentation.getSlidesCount();
		}		
		
		// parse each slide file in the presentation document
		for(int i=start;i<=end;i++){
			parseSlideFile(i);
		}
		new Thread(new Runnable() {
			@Override 
			public void run() {
				executeSikuliActions();
			}}).start();
	}
	
	public void runSikuliPowerPoint(){
		runSikuliPowerPoint(1, presentation.getSlidesCount());
	}
	

	private void loadPresentationFile() {
		File file = null;
		// if the file is remotely stored in the cloud
		if(pptxSourceName.startsWith("http")){
			file = Utils.downloadFile(pptxSourceName);
		}
		else{
			file = new File(pptxSourceName);
		}
		if(file !=null){
			// set the project directory name
			Constants.projectDirectory=Constants.workingDirectoryPath + 
					File.separator + 
					FilenameUtils.removeExtension(file.getName());
			// compress the file.
			Utils.doZipFile(file);
			// decompress the file.
			Utils.doUnZipFile(file);
			// create images directory
			Utils.createSikuliImagesDirectory();
		}
		else{
			logger.error("ERROR: Failed to find the .pptx file");
			System.exit(0);
		}
	}
	
	private void parsePresentationFile(){
		// 1) Parse the presentation.xml file
		PresentationParser presentationParser=new PresentationParser();
		presentationParser.parseDocument();
		presentation=presentationParser.getPresentation();
	}
	
	private void parseSlideFile(int slideNumber) {
		String slidesDirectory=Constants.projectDirectory+Constants.SLIDES_DIRECTORY;
		// 2) Parse the slide.xml file
		String slideName=File.separator+"slide"+Integer.toString(slideNumber)+".xml";
		String slidePath=slidesDirectory+slideName;
		logger.info("Processing slide: "+slideNumber);
		SlideParser mySlideParser=new SlideParser(slidePath);
		mySlideParser.parseDocument();
		
		// Get the results
		// get the screenshot info
		Screenshot screenshot=mySlideParser.getScreenshot();
		// get the shape info
		List<SlideShape> slideShapes=mySlideParser.getShapes();
		// get the sound info
		Sound sound=mySlideParser.getSound();
		if(sound!=null){
			setSoundFileName(sound, slideName);
		}
		// get the label to be displayed on the screen
		List<SlideShape> labels = mySlideParser.getLabels();
		SlideShape label = null;
		if(labels != null && labels.size() > 0){
			label = labels.get(0);
		}
		// get targets and desktop events
		DesktopEvent desktopEvent = null;
		List<SlideShape> targetShapes = null;
		
		// running old syntax
		if(Constants.UseOldSyntax){
			if(slideShapes==null||slideShapes.size()==0){
				logger.error("Failed to process slide {}. The slide must contain a predefined shape.",slideNumber);
				System.exit(1);
			}
			else{
				desktopEvent = getOldDesktopEvent(slideShapes);
				if(desktopEvent == null){
					if(slideShapes.size() == 1){
						logger.error("Failed to process slide {}. The slide must contain a predefined shape.",slideNumber);
					}
					else{
						logger.error("Error: Slide {}. Multiple targets are not supported in the old syntax." +
								"{}. Use the default new syntax option to enable multiple targets per slide.",
								slideNumber, NEW_LINE);
					}
                    System.exit(1);
				}
				if(slideShapes.size() == 1 ){
					targetShapes = new ArrayList<SlideShape>();
					targetShapes.add(slideShapes.get(0));
				}
				else if(slideShapes.size()==2 && desktopEvent==DesktopEvent.DRAG_N_DROP){
					targetShapes=new ArrayList<SlideShape>();
					targetShapes.add(slideShapes.get(0));
					targetShapes.add(slideShapes.get(1));
				}
				else{
//					logger.error("Error. Slide {} contains multiple shapes.{}" +
//							"The slide must contain only one input action using the predefined shapes.{}"+
//							"You may use the new syntax to enable multiple targets per slide."
//							, slideNumber, NEW_LINE, NEW_LINE);
					System.exit(1);
				}
			}
		}
		
		// running new syntax
		else if(!Constants.UseOldSyntax){
			// get the desktop action
			desktopEvent=getDesktopEvent(slideShapes);
			targetShapes=getTargterShapes(slideShapes);

			// if the slide doesn't contain a shape.
			if((labels == null && desktopEvent == null && sound == null) || slideShapes == null){
//				logger.error("Failed to process slide {}." +
//				"The slide must contain a shape and textbox that contains the action to be executed.{}" +
//				"The text box that descripes the action must contain one of the following actions:{}" +
//				"Click, Right Click, Double Click, Type, Drag, Browser, Exist, Not Exist, Wait", slideNumber, NEW_LINE, NEW_LINE);
				return;
			}
		}
		
		// The slide only contains shapes and no  screenshot images
		// If the result contains only shape without screenshot, execute just the shape action.
		// #1 open the default browser action
		if(desktopEvent==DesktopEvent.LAUNCH_BROWSER){
			tasks.add(new SikuliAction(null, targetShapes.get(0), screenshot, null, desktopEvent, sound, label, null, null));
			return;
		}
		// #2 Wait action
		else if(desktopEvent==DesktopEvent.WAIT){
			tasks.add(new SikuliAction(null, targetShapes.get(0), screenshot, null, desktopEvent, sound, label, null, null));
			return;
		}
		// if the slide only contains an audio clip
		else if(desktopEvent == null && sound != null){
			tasks.add(new SikuliAction(null, new SlideShape(), null, null, null, sound, null, null, null));
			return;
		}
		else if(screenshot == null){
			logger.info("Error in slide {}.", slideNumber);
			return;
		}
		// set the screenshot image file name
		setScreenshotFileName(screenshot,slideName);
		
		// The slide contains only a label/caption to be displayed on the screen
		if(desktopEvent == null && label != null){
			startProcessing(screenshot, label, desktopEvent, slideNumber, sound, label);
		}
		
		// calculate the target position and process the screenshot
		
		for(SlideShape slideShape:targetShapes){
			startProcessing(screenshot,slideShape, desktopEvent, slideNumber, sound, label);
		}

		
	}
	
	// return only the target shapes excluding the text box that represents the action.
	private List<SlideShape> getTargterShapes(List<SlideShape> slideShapes) {
		List<SlideShape> targetShapes=new ArrayList<SlideShape>();
		for(SlideShape slideShape:slideShapes){
			// if the shape is the textBox that represents the GUI input action
			if(slideShape.getType().equals("rect") && slideShape.getName().contains("TextBox")){
				continue;
			}
			else{
				targetShapes.add(slideShape); 
			}
		}
		return targetShapes;
	}
	// return the GUI desktop event or action to be executed in the slide
	private DesktopEvent getDesktopEvent(List<SlideShape> slideShapes){
		DesktopEvent desktopEvent=null;
		for(SlideShape slideShape:slideShapes){
			// Get the shape that represents the desktop action and remove it from the shapes list
			if(slideShape.getType().equals("rect")) {//&& slideShape.getName().contains("TextBox")){
				String action = slideShape.getText().trim();
				if(action.equalsIgnoreCase("Click") || action.equalsIgnoreCase("Left Click")){
					desktopEvent = DesktopEvent.LEFT_CLICK;
					slideShapes.remove(slideShape);
					break;
				}
				else if(action.equalsIgnoreCase("Right Click")){
					desktopEvent = DesktopEvent.RIGHT_CLICK;
					slideShapes.remove(slideShape);
					break;
				}
				else if(action.equalsIgnoreCase("Double Click")){
					desktopEvent = DesktopEvent.DOUBLE_CLICK;
					slideShapes.remove(slideShape);
					break;
				}
				else if(action.equalsIgnoreCase("Type")){
					desktopEvent = DesktopEvent.KEYBOARD_TYPING;
					slideShapes.remove(slideShape);
					break;
				}
				else if(action.toLowerCase().contains("drag") || action.toLowerCase().contains("drop")){
					desktopEvent = DesktopEvent.DRAG_N_DROP;
					slideShapes.remove(slideShape);
					break;
				}
				else if(action.toLowerCase().contains("browser")){
					desktopEvent = DesktopEvent.LAUNCH_BROWSER;
					slideShapes.remove(slideShape);
					break;
				}
				else if(action.toLowerCase().contains("not exist")){
					desktopEvent = DesktopEvent.NOT_EXIST;
					slideShapes.remove(slideShape);
					break;
				}
				else if(action.toLowerCase().contains("exist")){
					desktopEvent = DesktopEvent.EXIST;
					slideShapes.remove(slideShape);
					break;
				}
				else if(action.equalsIgnoreCase("wait") || action.equalsIgnoreCase("delay")){
					desktopEvent = DesktopEvent.WAIT;
					slideShapes.remove(slideShape);
					break;
				}
				else
					continue;
			}
		}
		return desktopEvent;
	}
	
	// return the GUI desktop event or action to be executed in the slide based on the old syntax
	private DesktopEvent getOldDesktopEvent(List<SlideShape> slideShapes){
		
		String shapeType=slideShapes.get(0).getType();
		String shapeName=slideShapes.get(0).getName();
		
		if(slideShapes.size()==2){
			if(shapeType.equals("roundRect") && shapeName.contains("Rounded Rectangle")){
				return DesktopEvent.DRAG_N_DROP;
			}
		}
		else if(shapeType.equals("rect") && shapeName.contains("Rectangle")){
			return DesktopEvent.LEFT_CLICK;
		}
		else if(shapeType.equals("frame") && shapeName.contains("Frame")){
			return DesktopEvent.DOUBLE_CLICK;
		}
		else if(shapeType.equals("ellipse") && shapeName.contains("Oval")){
			return DesktopEvent.RIGHT_CLICK;
		}
		else if(shapeType.equals("cloud") && shapeName.contains("Cloud")){
			return DesktopEvent.LAUNCH_BROWSER;
		}
		else if(shapeType.equals("rect") && shapeName.contains("TextBox")){
			return DesktopEvent.KEYBOARD_TYPING;
		}
		return null;
		
	}
	
	// set the file path of the image screenshot
	private void setScreenshotFileName(Screenshot screenshot, String slideName) {
		// parse the relationship file and get the image file name
		Relationship relationship=new Relationship(slideName);
		screenshot.setFileName(relationship.getMediaFileName(screenshot.getRelationshipID()));
	}
	// set the file path of the sound file
	private void setSoundFileName(Sound sound, String slideName) {
		// parse the relationship file and get the image file name
		Relationship relationship=new Relationship(slideName);
		sound.setFileName(relationship.getMediaFileName(sound.getRelationshipId()));
	}
	
	
	private void startProcessing(Screenshot screenshot, SlideShape slideShape,DesktopEvent desktopEvent, int slideNumber, Sound sound, SlideShape slideLabel) {
		SlideTargetRegionContainer slideTarget = getSlideShapeRegion(screenshot, slideShape, slideNumber);
		File targetFile = slideTarget.getSlideTargetFile();
		SlideTargetRegion slideTargetRegion = slideTarget.getSlideTargetRegion();
		
		File labelTargetFile = null;
		SlideTargetRegion slideLabelTargetRegion = null;
		if(slideLabel != null){
			SlideTargetRegionContainer slideLabelTarget = getSlideShapeRegion(screenshot, slideLabel, slideNumber);
			labelTargetFile = slideLabelTarget.getSlideTargetFile();
			slideLabelTargetRegion = slideLabelTarget.getSlideTargetRegion();
		}
		// queue the tasks
		tasks.add(new SikuliAction(targetFile, slideShape, screenshot,slideTargetRegion,
				desktopEvent,sound, slideLabel,labelTargetFile, slideLabelTargetRegion));
	}
	
	private SlideTargetRegionContainer getSlideShapeRegion(Screenshot screenshot, SlideShape slideShape, int slideNumber){
		String slideMediaLocation=Constants.projectDirectory+Constants.MEDIA_DIRECTORY+File.separator+screenshot.getFileName();		
		SlideProcessing slideProcessing=new SlideProcessing(slideMediaLocation);
		
		int resizedScreenshotWidth=UnitConverter.emuToPixels(screenshot.getCx());
		int resizedScreenshotHeight=UnitConverter.emuToPixels(screenshot.getCy());
		// get the relative location and dimensions of the rectangle

		int relativeRectangleWidth=(int)Math.round(ImageProcessing.scaleRectangleWidth(UnitConverter.emuToPixels(slideShape.getCx()), 
				resizedScreenshotWidth, 
				slideProcessing.getScreenshotWidth()));
		int relativeRectangleHeight=(int)Math.round(ImageProcessing.scaleRectangleHeight(UnitConverter.emuToPixels(slideShape.getCy()), 
				resizedScreenshotHeight, 
				slideProcessing.getScreenshotHeight()));
		
		int diffX=slideShape.getOffx()-screenshot.getOffX();
		int diffY=slideShape.getOffy()-screenshot.getOffY();

		int relativeRectangleX=(int)Math.round(ImageProcessing.getRelativeX(UnitConverter.emuToPixels(diffX), 
				resizedScreenshotWidth, 
				slideProcessing.getScreenshotWidth()));
		
		int relativeRectangleY=(int)Math.round(ImageProcessing.getRelativeY(UnitConverter.emuToPixels(diffY), 
				resizedScreenshotHeight, 
				slideProcessing.getScreenshotHeight()));
		
		// get the target region (the shape area)
		SlideTargetRegion slideTargetRegion=new SlideTargetRegion("",slideNumber,slideMediaLocation,
				relativeRectangleX, relativeRectangleY
				, relativeRectangleWidth, 
				relativeRectangleHeight,
				slideProcessing.getScreenshotWidth(),slideProcessing.getScreenshotHeight());
		
		// crop and save the target image
		File targetRegionFile=saveTargetImage(slideMediaLocation, relativeRectangleX, relativeRectangleY,
				relativeRectangleWidth, relativeRectangleHeight);
		
		return new SlideTargetRegionContainer(slideTargetRegion, targetRegionFile);
		
	}
	private File saveTargetImage(String slideMediaLocation,int relativeRectangleX, int relativeRectangleY 
			, int relativeRectangleWidth, int relativeRectangleHeight){
		BufferedImage croppedImage=ImageProcessing.cropImage(slideMediaLocation, 
				(int)Math.round(relativeRectangleX), (int)Math.round(relativeRectangleY)
				, (int)Math.round(relativeRectangleWidth), 
				(int)Math.round(relativeRectangleHeight));
		
		String croppedImageName=Constants.projectDirectory+Constants.SIKULI_DIRECTORY+
				Constants.IMAGES_DIRECTORY+File.separator+"target"+Integer.toString(counter.incrementAndGet())+".png";
		// save the target image to the disk
		ImageProcessing.writeImageToDisk(croppedImage, croppedImageName);
		return new File(croppedImageName);	
	}
	
	
	private void executeSikuliActions(){
		if(Constants.TUTORIAL_MODE){
			// display Tutorial Controller UI when tutorial mode is running, which will run the tasks in a new worker thread
			logger.info("Running in tutorial mode...");
			//TutorialConrollerUI.runTutorialUIAndTasks(tasks);
		}
		else{
			for(SikuliAction sikuliAction:tasks){
				sikuliAction.doSikuliAction();
			}
		}
		printExecutionTime();
	}
	
	private void printExecutionTime() {
		long endTime = System.currentTimeMillis();	
		long elapsedTime = endTime - Constants.Execution_Start_Time;
		long hr=TimeUnit.MILLISECONDS.toHours(elapsedTime);
		long min=TimeUnit.MILLISECONDS.toMinutes(elapsedTime-TimeUnit.HOURS.toMillis(hr));
		long sec=TimeUnit.MILLISECONDS.toSeconds(elapsedTime-TimeUnit.HOURS.toMillis(hr)
				-TimeUnit.MINUTES.toMillis(min));
		long ms=TimeUnit.MILLISECONDS.toMillis(elapsedTime-TimeUnit.HOURS.toMillis(hr)
				-TimeUnit.MINUTES.toMillis(min)-TimeUnit.SECONDS.toMillis(sec));
		String formattedTime=String.format("%02d:%02d:%02d.%02d", hr, min, sec,ms);
		logger.info("Finished after "+formattedTime);
	}
	
	class SlideTargetRegionContainer{
		private SlideTargetRegion slideTargetRegion;
		private File slideTargetFile;
		
		public SlideTargetRegionContainer(SlideTargetRegion slideTargetRegion, File slideTargetFile){
			this.slideTargetRegion = slideTargetRegion;
			this.slideTargetFile = slideTargetFile;
		}
		public SlideTargetRegion getSlideTargetRegion(){
			return this.slideTargetRegion;
		}
		public File getSlideTargetFile(){
			return this.slideTargetFile;
		}
		
	}
}
