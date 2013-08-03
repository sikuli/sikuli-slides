package org.sikuli.slides.api;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.interpreters.DefaultInterpreter;
import org.sikuli.slides.interpreters.Interpreter;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.parsers.SlideParser;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Slides {

	static Logger logger = LoggerFactory.getLogger(Slides.class);

	static URL getSlideXML(int slideNumber){
		String slidesDirectory = Constants.projectDirectory+Constants.SLIDES_DIRECTORY;
		String slideName=File.separator+"slide"+Integer.toString(slideNumber)+".xml";
		String slidePath=slidesDirectory+slideName;
		try {
			return (new File(slidePath)).toURI().toURL();
		} catch (MalformedURLException e) {
		}
		return null;
	}

	static URL getSlideXMLRel(int slideNumber){
		String slideName=File.separator+"slide"+Integer.toString(slideNumber)+".xml";
		String relationshipXMLFile=Constants.projectDirectory + Constants.RELATIONSHIP_DIRECTORY + slideName+".rels";
		try {
			return (new File(relationshipXMLFile)).toURI().toURL();
		} catch (MalformedURLException e) {
		}
		return null;		
	}
	
	static URL getFileName(String fileName){
		String slidesDirectory = Constants.projectDirectory+Constants.SLIDES_DIRECTORY;
		String filePath = slidesDirectory + File.separator + fileName;
		try {
			return (new File(filePath)).toURI().toURL();
		} catch (MalformedURLException e) {
		}
		return null;				
	}

	static public void exeute(URL url){		
		// create the project working directory directory
		if(!Utils.createWorkingDirectory()){
			logger.error("Failed to create sikuli-slides working directory in +"+System.getProperty("java.io.tmpdir"));
			return;
		}
		loadPresentationFile(url.getPath());

		String slidesDirectory = Constants.projectDirectory+Constants.SLIDES_DIRECTORY;		
		int numSlides = (new File(slidesDirectory)).list().length - 1;
		logger.debug("extracted {} slides", numSlides);

		SlideParser parser = new SlideParser();	
		List<Slide> slides = Lists.newArrayList();
		for (int i = 1; i <= numSlides; ++i){
			Slide slide = parser.parse(getSlideXML(i), getSlideXMLRel(i));
			if (slide != null){
				slides.add(slide);
				logger.debug("Slide " + 1 + " :\n" + slide);
			}
		}
		
		ScreenRegion screenRegion = new DesktopScreenRegion();
		
		Interpreter interpreter = new DefaultInterpreter(screenRegion);
		
		List<Action> actions = Lists.newArrayList();
		for (Slide slide : slides){
			Action action = interpreter.interpret(slide);
			if (action != null){
				actions.add(action);
				logger.debug("action: " + action);
			}			
		}				
		
		for (Action action : actions){
			action.perform();
		}
		
	}


	// returns the project directory
	static private void loadPresentationFile(String pptxSourceName) {
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
}
