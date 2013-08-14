package org.sikuli.slides.api.io;

import java.io.File;
import java.io.IOException;
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

	static Logger logger = LoggerFactory.getLogger(PPTXSlidesReader.class);

	@Override
	public List<Slide> read(URL url) throws IOException {
		
		File pptxFile = new File(url.getFile());
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
