package org.sikuli.slides.api.io;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.parsers.SlideParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import java.net.URLDecoder;
import org.sikuli.slides.api.Context;

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
//				logger.debug("Slide {} of {} parsed: {}", i, n, slide);
			}
		}		
		return slides;
	}

	@Override
	public List<Slide> read(URL url) throws IOException {

		File pptxFile = null;
		if (url.getProtocol().toLowerCase().startsWith("http")){
			url = resolveDownloadURL(url);
			pptxFile = downloadFile(url);
			if (pptxFile == null){
				throw new IOException("Unable to download from " + url);
			}
		}else if (url.getProtocol().compareToIgnoreCase("file") == 0){
			// Need to unencode the URL because otherwise spaces in the name will cause the file not to be found.
                        String filename = URLDecoder.decode(url.getFile(), "UTF-8");
                        pptxFile = new File(filename);
		}else{			
			throw new IOException("Unable to deal with " + url);
		}

		return read(pptxFile);
	}
        
	public static File downloadFile(URL downloadURL){
		logger.info("Download file from {} ... ", downloadURL);
		File destination;
		try {
			String name = "download";
			destination = File.createTempFile(name, "");
			FileUtils.copyURLToFile(downloadURL, destination, 300000, 30000);
			logger.info("Download complete. Saved as {}", destination);
		} catch (IOException e) {
			logger.error("Unable to download {}", e.getMessage());
			destination = null;
		}    	
		return destination;
	}

	private static URL resolveDownloadURL(URL url){
    	// Example input: https://docs.google.com/presentation/d/1-qXEu7jYvm1Oql-hBcjgXU5zLQUGWd_uGH6mc8buRkI/export/pptx
    	//https://docs.google.com/presentation/d/1-qXEu7jYvm1Oql-hBcjgXU5zLQUGWd_uGH6mc8buRkI/export/pptx
    	if(url != null){
    		try {
				URI uri = url.toURI();
				
				// check if the domain is Google.com
				String domain = uri.getHost();
				if(domain == null){
					return null;
				}
				String domainName = domain.startsWith("www.")? domain.substring(4) : domain;
				// download the remote .pptx file
				// 1) The file is hosted on google drive
				if(domainName.equalsIgnoreCase("docs.google.com")){
					
					try{
						String urlString = url.toString();
						int startIndex = urlString.indexOf("/d/") + 3;
						int lastIndex = urlString.indexOf("/edit");
						String documentId = urlString.substring( startIndex, lastIndex);
						
						// construct GoogleDrive download link
						String gDriveDownloadLink = "https://docs.google.com/presentation/d/" + 
								documentId + "/export/pptx";
						return new URL(gDriveDownloadLink);
					}
					catch(StringIndexOutOfBoundsException e){
						logger.error("ERROR: Invalid Google Drive link.");
					}
				}
				// 2) The file is hosted on a remote server.
				else{
					return url;
				}
			} catch (URISyntaxException e) {
				logger.error("ERROR: Invalid share link.");
			}
    		catch (MalformedURLException e){
    			logger.error("ERROR: Invalid share link.");
    		}
    	}
    	return null;
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
