package org.sikuli.slides.api.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.parsers.SlideParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class PPTXSlidesReader implements SlidesReader {

	static Logger logger = LoggerFactory.getLogger(PPTXSlidesReader.class);

	@Override
	public List<Slide> read(File pptxFile) throws IOException {

		PPTXBundle bundle = PPTXBundle.createFrom(pptxFile);
		logger.debug("PPTX bundle created: {}", bundle);

		SlideParser parser = new SlideParser();	
		List<Slide> slides = Lists.newArrayList();
		int n = bundle.getSlideCount();
		for (int i = 1; i <= n; ++i){
			Slide slide = parser.parse(bundle.getSlideXML(i), bundle.getSlideXMLRel(i));
			if (slide != null){
				slide.setNumber(i);
				slides.add(slide);
				logger.debug("Slide {} of {} parsed: {}", i, n, slide);
			}
		}		
		return slides;
	}

	@Override
	public List<Slide> read(URL url) throws IOException {

		File pptxFile = null;
		if (url.getProtocol().compareToIgnoreCase("http") == 0){
			pptxFile = downloadFile(url);
		}else if (url.getProtocol().compareToIgnoreCase("file") == 0){
			pptxFile = new File(url.getFile());
		}else{			
			throw new IOException("Unable to deal with " + url);
		}

		return read(pptxFile);
	}

	public static File downloadFile(URL downloadURL){
		File destination;
		try {
			String name = downloadURL.getFile();
			destination = File.createTempFile(name, "");
			logger.info("Download file from {} ... ", downloadURL);
			FileUtils.copyURLToFile(downloadURL, destination, 300000, 30000);
			logger.info("Download complete. Saved as {}", destination);
		} catch (IOException e) {
			logger.error("Unable to download {}", e.getMessage());
			destination = null;
		}    	
		return destination;
	}



	//	// returns the project directory
	//	static private boolean loadPresentationFile(String pptxSourceName) {
	//		File file = null;
	//		// if the file is remotely stored in the cloud
	//		if(pptxSourceName.startsWith("http")){
	//			file = Utils.downloadFile(pptxSourceName);
	//		}else{
	//			file = new File(pptxSourceName);
	//		}
	//
	//		if(file !=null && file.exists() && FilenameUtils.isExtension(file.getName(), "pptx")){
	//			// set the project directory name
	//			Constants.projectDirectory=Constants.workingDirectoryPath + 
	//					File.separator + 
	//					FilenameUtils.removeExtension(file.getName());
	//			// compress the file.
	//			Utils.doZipFile(file);
	//			// decompress the file.
	//			Utils.doUnZipFile(file);
	//			// create images directory
	//			Utils.createSikuliImagesDirectory();
	//			return true;
	//		}
	//		else{
	//			return false;
	//		}
	//	}
}
