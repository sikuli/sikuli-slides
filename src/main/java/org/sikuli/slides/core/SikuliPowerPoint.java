/**
Khalid
*/
package org.sikuli.slides.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.UnitConverter;
import org.sikuli.slides.utils.Utils;

//TODO: This class needs to be refactored.
public class SikuliPowerPoint {
	
	private File file;
	//private String projectDirectory;
	private Presentation presentation;
	private AtomicInteger counter;
	private List<SikuliAction> tasks;
	
	public SikuliPowerPoint(File file){
		this.file=file;
		counter=new AtomicInteger();
		tasks=new ArrayList<SikuliPowerPoint.SikuliAction>();
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
		SlideShape slideShape=mySlideParser.getShape();
		// get the sound info
		Sound sound=mySlideParser.getSound();
		if(sound!=null){
			setSoundFileName(sound, slideName);
		}
		// if the slide doesn't contain a shape.
		if(slideShape==null){
			System.err.println("Failed to process slide "+slideNumber+". The slide must contain a predefined shape.");
			return;
		}
		
		// if the result contains only shape without screenshot, execute just the shape action.
		// for example, the cloud shape means open the default browser and doesn't have screenshot
		else if(screenshot==null&&slideShape!=null){
			tasks.add(new SikuliAction(null,slideShape,screenshot,null,sound));
			return;
		}
		// set the screenshot image file name
		setScreenshotFileName(screenshot,slideName);
		
		// if the slide contains an arrow, get two targets
		if(mySlideParser.isMultipleShapes()){
			List<SlideShape> roundedRectnagleShapes=mySlideParser.getShapes();
			for(SlideShape roundedRectnagleShape:roundedRectnagleShapes){
				startProcessing(screenshot,roundedRectnagleShape, slideNumber, sound);
			}
			return;
		}
		
		// calculate the target position and process the screenshot
		startProcessing(screenshot,slideShape, slideNumber, sound);
		
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
	
	
	private void startProcessing(Screenshot screenshot, SlideShape slideShape, int slideNumber, Sound sound) {
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
		tasks.add(new SikuliAction(targetFile, slideShape, screenshot,slideTargetRegion, sound));
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
		for(SikuliAction shapeAction:tasks){
			shapeAction.doSikuliAction();
		}
		
	}
	
	class SikuliAction{
		private SlideComponent slideComponent;
		private SlideShape slideShape;
		public SikuliAction(File imageTargetFile, SlideShape slideShape, Screenshot screenshot, 
				SlideTargetRegion slideTargetRegion, Sound sound){
			this.slideShape=slideShape;
			slideComponent=new SlideComponent(imageTargetFile, slideShape, screenshot, slideTargetRegion, sound);
		}
		public void doSikuliAction(){
			slideShape.doSikuliAction(slideComponent);
		}
	}
	
}
