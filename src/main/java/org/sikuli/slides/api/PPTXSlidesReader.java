package org.sikuli.slides.api;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.parsers.SlideParser;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public class PPTXSlidesReader implements SlidesReader {

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


	@Override
	public List<Slide> read(URL url) throws IOException {

		// create the project working directory directory
		if(!Utils.createWorkingDirectory()){
			logger.error("Failed to create sikuli-slides working directory in +"+System.getProperty("java.io.tmpdir"));
			throw new IOException();
		}
		if (url == null || !loadPresentationFile(url.getPath())){
			throw new IOException("ERROR: Failed to open " + url);
		}

		String slidesDirectory = Constants.projectDirectory+Constants.SLIDES_DIRECTORY;		
		int numSlides = (new File(slidesDirectory)).list().length - 1;
		logger.debug("Found {} slides", numSlides);

		SlideParser parser = new SlideParser();	
		List<Slide> slides = Lists.newArrayList();
		for (int i = 1; i <= numSlides; ++i){
			Slide slide = parser.parse(getSlideXML(i), getSlideXMLRel(i));
			if (slide != null){
				slide.setNumber(i);
				slides.add(slide);
				logger.debug("Slide " + i + ":\n" + slide);
			}
		}		
		return slides;
	}


	// returns the project directory
	static private boolean loadPresentationFile(String pptxSourceName) {
		File file = null;
		// if the file is remotely stored in the cloud
		if(pptxSourceName.startsWith("http")){
			file = Utils.downloadFile(pptxSourceName);
		}else{
			file = new File(pptxSourceName);
		}
		
		if(file !=null && file.exists() && FilenameUtils.isExtension(file.getName(), "pptx")){
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
			return true;
		}
		else{
			return false;
		}
	}
}
