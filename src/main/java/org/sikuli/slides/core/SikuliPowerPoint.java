/**
Khalid
*/
package org.sikuli.slides.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.FilenameUtils;
import org.sikuli.slides.media.Sound;
import org.sikuli.slides.parsing.PresentationParser;
import org.sikuli.slides.parsing.SlideParser;
import org.sikuli.slides.presentation.Presentation;
import org.sikuli.slides.processing.ImageProcessing;
import org.sikuli.slides.processing.Relationship;
import org.sikuli.slides.processing.SlideProcessing;
import org.sikuli.slides.screenshots.SlideTargetRegion;
import org.sikuli.slides.screenshots.Screenshot;
import org.sikuli.slides.shapes.SlideShape;
import org.sikuli.slides.sikuli.TutorialController;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.Constants.DesktopEvent;
import org.sikuli.slides.utils.UnitConverter;
import org.sikuli.slides.utils.Utils;

//TODO: This class needs to be refactored.
public class SikuliPowerPoint {
	
	private File file;
	private Presentation presentation;
	private AtomicInteger counter;
	private List<SikuliAction> tasks;
	
	public SikuliPowerPoint(File file){
		this.file=file;
		counter=new AtomicInteger();
		tasks=new ArrayList<SikuliAction>();
	}
	public void runSikuliPowerPoint(){
		// set the project directory name
		Constants.projectDirectory=Constants.workingDirectoryPath+File.separator+FilenameUtils.removeExtension(file.getName());

		// load the .pptx file
		loadPresentationFile(file);
		// parse the general presentation.xml file
		parsePresentationFile();
		// parse each slide file in the presentation document
		for(int i=1;i<=presentation.getSlidesCount();i++)
			parseSlideFile(i);
		new Thread(new Runnable() {
		    @Override 
		    public void run() {
		    	executeSikuliActions();
		    }}).start();
	}
	

	private void loadPresentationFile(File file) {
		// compress the file.
		Utils.doZipFile(file);
		// decompress the file.
		Utils.doUnZipFile(file);
		// create images directory
		Utils.createSikuliImagesDirectory();
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
		System.out.println("Processing slide: "+slideNumber);
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
		List<SlideShape> labels=mySlideParser.getLabels();
		SlideShape label=null;
		if(labels!=null&&labels.size()>0){
			label=labels.get(0);
		}
		// get targets and desktop events
		DesktopEvent desktopEvent=null;
		List<SlideShape> targetShapes=null;
		
		// running old syntax
		if(Constants.UseOldSyntax){
			if(slideShapes==null||slideShapes.size()==0){
				System.err.println("Failed to process slide "+slideNumber+
						". The slide must contain a predefined shape.");
				System.exit(1);
			}
			else{
				desktopEvent=getOldDesktopEvent(slideShapes);
				if(desktopEvent==null){
					if(slideShapes.size()==1){
						System.err.println("Failed to process slide "+slideNumber+
								". The slide must contain a predefined shape.");
					}
					else{
						System.err.println("Error: Slide "+slideNumber+
								". Multiple targets are not supported in the old syntax. Use the default new syntax option to enable multiple targets per slide.");
					}
                    System.exit(1);
				}
				if(slideShapes.size()==1){
					targetShapes=new ArrayList<SlideShape>();
					targetShapes.add(slideShapes.get(0));
				}
				else if(slideShapes.size()==2 && desktopEvent==DesktopEvent.DRAG_N_DROP){
					targetShapes=new ArrayList<SlideShape>();
					targetShapes.add(slideShapes.get(0));
					targetShapes.add(slideShapes.get(1));
				}
				else{
					System.err.println("Error. Slide "+slideNumber+" contains multiple shapes.\n" +
							"The slide must contain only one input action using the predefined shapes.\n" +
							"You may use the new syntax to enable multiple targets per slide.");
					System.exit(1);
				}
			}
		}
		
		// running new syntax
		else if(!Constants.UseOldSyntax){
			targetShapes=getTargterShapes(slideShapes);
			// get the desktop action
			desktopEvent=getDesktopEvent(slideShapes);
			// if the slide doesn't contain a shape.
			if(desktopEvent==null || slideShapes==null || (slideShapes.size()<2)){
					System.err.println("Failed to process slide "+slideNumber+
							". The slide must contain a shape and textbox that contains the action to be executed.\n" +
							"The text box that descripes the action must contain one of the following actions:\n"+
							"Click, Right Click, Double Click, Type, Drag, Browser, exist, not exist");
					return;
			}
		}
		
		// if the result contains only shape without screenshot, execute just the shape action.
		// for example, the cloud shape means open the default browser and doesn't have screenshot
		if(desktopEvent==DesktopEvent.LAUNCH_BROWSER){
			tasks.add(new SikuliAction(null,targetShapes.get(0),screenshot,null,desktopEvent,sound, label));
			return;
		}

		// set the screenshot image file name
		setScreenshotFileName(screenshot,slideName);
		
		// calculate the target position and process the screenshot
		
		for(SlideShape slideShape:targetShapes)
			startProcessing(screenshot,slideShape, desktopEvent, slideNumber, sound, label);
		
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
		for(SlideShape slideShape:slideShapes){
			// if the shape is the textBox that represents the GUI input action
			if(slideShape.getType().equals("rect") && slideShape.getName().contains("TextBox")){
				String action=slideShape.getText().trim();
				if(action.equalsIgnoreCase("Click") || action.equalsIgnoreCase("Left Click"))
					return DesktopEvent.LEFT_CLICK;
				else if(action.equalsIgnoreCase("Right Click"))
					return DesktopEvent.RIGHT_CLICK;
				else if(action.equalsIgnoreCase("Double Click"))
					return DesktopEvent.DOUBLE_CLICK;
				else if(action.equalsIgnoreCase("Type"))
					return DesktopEvent.KEYBOARD_TYPING;
				else if(action.toLowerCase().contains("drag") || action.toLowerCase().contains("drop"))
					return DesktopEvent.DRAG_N_DROP;
				else if(action.toLowerCase().contains("browser"))
					return DesktopEvent.LAUNCH_BROWSER;
				else if(action.toLowerCase().contains("not exist"))
					return DesktopEvent.NOT_EXIST;
				else if(action.toLowerCase().contains("exist"))
					return DesktopEvent.EXIST;
				else
					continue;
			}
		}
		return null;
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
		File targetFile=saveTargetImage(slideMediaLocation, relativeRectangleX, relativeRectangleY,
				relativeRectangleWidth, relativeRectangleHeight);
		// queue the tasks
		tasks.add(new SikuliAction(targetFile, slideShape, screenshot,slideTargetRegion, desktopEvent,sound, slideLabel));
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
			if(Constants.TUTORIAL_MODE){
				if(Constants.tutorialController==null){
					Constants.tutorialController=TutorialController.getInstance();
					Constants.tutorialController.executeSikuliActions(tasks);
				}
			}
		}
		else{
			for(SikuliAction shapeAction:tasks){
				shapeAction.doSikuliAction();
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
		System.out.println("Finished after "+formattedTime);
		System.exit(0);
	}
	
}
