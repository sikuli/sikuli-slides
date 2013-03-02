/**
Khalid
*/
package org.sikuli.slides.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.sikuli.slides.parsing.PresentationParser;
import org.sikuli.slides.parsing.SlideParser;
import org.sikuli.slides.presentation.Presentation;
import org.sikuli.slides.processing.ImageProcessing;
import org.sikuli.slides.processing.Relationship;
import org.sikuli.slides.processing.SlideProcessing;
import org.sikuli.slides.screenshots.Screenshot;
import org.sikuli.slides.shapes.Shape;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.UnitConverter;
import org.sikuli.slides.utils.Utils;

//TODO: This class needs to be refactored.
public class SikuliPowerPoint {
	
	private File file;
	private String projectDirectory;
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
		String fileName=file.getName();
		projectDirectory= Constants.zipDirectoryPath+File.separator+fileName.substring(0, fileName.indexOf('.'));

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
		// finally, run the queued tasks
		
	}
	

	private void loadPresentationFile(File file) {
		// compress the file.
		Utils.doZipFile(file);
		// decompress the file.
		Utils.doUnZipFile(file);
		// create images directory
		Utils.createSikuliImagesDirectory(projectDirectory);
	}
	
	private void parsePresentationFile(){
		// 1) Parse the presentation.xml file
		PresentationParser presentationParser=new PresentationParser(projectDirectory);
		presentationParser.parseDocument();
		presentation=presentationParser.getPresentation();
		//System.out.println("**************************************************");
		//System.out.println(presentation);
	}
	
	private void parseSlideFile(int slideNumber) {
		String slidesDirectory=projectDirectory+Constants.slidesDirectoryPath;
		
		// 2) Parse the slide.xml file
		String slideName=File.separator+"slide"+Integer.toString(slideNumber)+".xml";
		String slidePath=slidesDirectory+slideName;
		System.out.println("Processing slide: "+slideNumber);
		SlideParser mySlideParser=new SlideParser(slidePath);
		mySlideParser.parseDocument();
		
		// Get the results
		// get the screenshot info
		Screenshot screenshot=mySlideParser.getScreenshot();
		Shape shape=mySlideParser.getShape();
		
		// if the slide doesn't contain a shape
		if(shape==null){
			System.err.println("Failed to process slide "+slideNumber+". The slide must contain a predefined shape.");
			return;
		}
		
		// if the result contains only shape without screenshot, execute just the shape action.
		// for example, the cloud shape means open the default browser and doesn't have screenshot
		else if(screenshot==null&&shape!=null){
			tasks.add(new SikuliAction(shape,null));
			return;
		}
		// set the screenshot image file name
		setScreenshotFileName(screenshot,projectDirectory,slideName);
		
		// if the slide contains an arrow, get two targets
		if(mySlideParser.isMultipleShapes()){
			//System.out.println("################################");
			List<Shape> roundedRectnagleShapes=mySlideParser.getShapes();
			//System.out.println("number of shapes: "+roundedRectnagleShapes.size());
			for(Shape roundedRectnagleShape:roundedRectnagleShapes){
				// print the results
				
				//System.out.println(screenshot.toString());
				//System.out.println(roundedRectnagleShape.toString());
				startProcessing(projectDirectory,screenshot,roundedRectnagleShape);
			}
			return;
		}
		
		// print the results
		//System.out.println(screenshot.toString());
		//System.out.println(shape.toString());
		// process the screenshot
		startProcessing(projectDirectory,screenshot,shape);
		
	}

	private void setScreenshotFileName(Screenshot screenshot, String projectDirectory, String slideName) {
		// parse the relationship file and get the image file name
		Relationship relationship=new Relationship(projectDirectory,slideName);
		screenshot.setFileName(relationship.getImageFileName(screenshot.getRelationshipID()));
	}
	
	
	private void startProcessing(String projectDirectory, Screenshot screenshot, Shape shape) {
		// TODO: remove this
		// work on the first slide
		// print the screen resolutions
		//System.out.println("Screen width: "+MyScreen.getScreenDimensions().width);
		//System.out.println("Screen height: "+MyScreen.getScreenDimensions().height);
		// print the original screenshot info that is stored in the first slide
		String slide1MediaLocation=projectDirectory+Constants.mediaDirectoryPath+File.separator+screenshot.getFileName();
		//System.out.println("slide 1 screenshot location: "+slide1MediaLocation);
		
		SlideProcessing slideProcessing1=new SlideProcessing(slide1MediaLocation);
		//System.out.println("Screenshot height: "+slideProcessing1.getScreenshotHeight());
		//System.out.println("Screenshot width: "+slideProcessing1.getScreenshotWidth());
		//System.out.println("***********************************************************");
		int resizedScreenshotWidth=UnitConverter.emuToPixels(screenshot.getCx());
		int resizedScreenshotHeight=UnitConverter.emuToPixels(screenshot.getCy());
		//System.out.println("The width of the resized screenshot in the slide (pixels): " + resizedScreenshotWidth);
		//System.out.println("the height of the resized screenshot in the slide (pixels): " + resizedScreenshotHeight);
		
		//System.out.println("***********************************************************");
		// crop the image
		// get the relative location and dimensions of the rectangle

		double relativeRectangleWidth=ImageProcessing.scaleRectangleWidth(UnitConverter.emuToPixels(shape.getCx()), 
				resizedScreenshotWidth, 
				slideProcessing1.getScreenshotWidth());
		double relativeRectangleHeight=ImageProcessing.scaleRectangleHeight(UnitConverter.emuToPixels(shape.getCy()), 
				resizedScreenshotHeight, 
				slideProcessing1.getScreenshotHeight());
		//System.out.println("Relative rectangle dimensions: RelativeWidth:"+relativeRectangleWidth+
			//	". RelativeHeight: "+relativeRectangleHeight);
		int diffX=shape.getOffx()-screenshot.getOffX();
		int diffY=shape.getOffy()-screenshot.getOffY();

		
		double relativeRectangleX=ImageProcessing.getRelativeX(UnitConverter.emuToPixels(diffX), 
				resizedScreenshotWidth, 
				slideProcessing1.getScreenshotWidth());
		
		double relativeRectangleY=ImageProcessing.getRelativeY(UnitConverter.emuToPixels(diffY), 
				resizedScreenshotHeight, 
				slideProcessing1.getScreenshotHeight());
		
		/*System.out.println("Rectangle location: X="+
				UnitConverter.emuToPixels(shape.getOffx())+
				" pixels. Relative X="+relativeRectangleX+" pixels.");
		
		System.out.println("Rectangle location: Y="+
				UnitConverter.emuToPixels(shape.getOffy())+
				" pixels. Relative Y="+relativeRectangleY+" pixels.");
		*/
		// crop the image
		BufferedImage croppedImage=ImageProcessing.cropImage(slide1MediaLocation, 
				(int)Math.round(relativeRectangleX), (int)Math.round(relativeRectangleY)
				, (int)Math.round(relativeRectangleWidth), 
				(int)Math.round(relativeRectangleHeight));
		
		String croppedImageName=projectDirectory+Constants.sikuliDirectory+
				Constants.imagesDirectory+File.separator+"target"+Integer.toString(counter.incrementAndGet())+".png";
		
		// save the cropped image
		//System.out.println("Cropped image is saved in: "+croppedImageName);
		

		ImageProcessing.writeImageToDisk(croppedImage, croppedImageName);
		// do GUI action according to the shape type
		//System.out.println("GUI Action on: "+croppedImageName);
		
		// queue the tasks
		tasks.add(new SikuliAction(shape, new File(croppedImageName)));
		//shape.doSikuliAction(new File(croppedImageName));
	}
	
	private void executeSikuliActions(){
		for(SikuliAction shapeAction:tasks){
			shapeAction.doSikuliAction();
		}
		
	}
	
	class SikuliAction{
		private Shape shape;
		private File imageTarget;
		
		public SikuliAction(Shape shape, File imageTarget){
			this.shape=shape;
			this.imageTarget=imageTarget;
		}
		public void doSikuliAction(){
			shape.doSikuliAction(imageTarget);
		}
	}
	
}
