package org.sikuli.slides.api.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.io.Files;

class PPTXBundle {

	static Logger logger = LoggerFactory.getLogger(PPTXBundle.class);

	// the root directory of the decompressed bundle on the file system
	private File rootDir;		
	// the name of the pptx file
	private String name = "";
	// number of slides 
	private int slideCount = 0;

	public String toString(){
		return Objects.toStringHelper(this)
				.add("count", slideCount)
				.add("rootDir", rootDir).toString();
	}


	public static PPTXBundle createFrom(File pptxFile) throws IOException{
		if (pptxFile == null || !pptxFile.exists()){
			logger.error("File does not exist: {}", pptxFile);
			throw new IOException();
		}

		File workingDir = Files.createTempDir();
		if (workingDir == null){
			logger.error("Failed to create sikuli-slides working directory in " + System.getProperty("java.io.tmpdir"));
			throw new IOException();
		}

		PPTXBundle bundle = new PPTXBundle(pptxFile, workingDir);
		if (!bundle.isValid()){
			logger.error("File is not a valid pptx: {}", pptxFile);
			throw new IOException();			
		}		
		return bundle;
	}

	// takes pptxFile as the input input and tmpDir to unzip the file to
	PPTXBundle(File pptxFile, File tmpDir){
		name = pptxFile.getName();
		rootDir = new File(tmpDir, FilenameUtils.removeExtension(name));

		doUnZipFile(pptxFile, rootDir);

		if (isValid()){			
			slideCount = getSlidesDirectory().list().length - 1;			
		}
	}

	boolean isValid(){
		return rootDir.exists() && getSlidesDirectory().exists() && getRelationshipsDirectory().exists();
	}

	int getSlideCount(){
		return slideCount;
	}

	File getRelationshipsDirectory(){
		return new File(rootDir, "ppt" + File.separator + "slides" + File.separator + "_rels");
	}

	File getSlidesDirectory(){
		return new File(rootDir, "ppt" + File.separator + "slides");
	}

	// Get the file of the slide rel for a slide number
	File getSlideXMLRel(int slideNumber){
		String filename = String.format("slide%d.xml.rels", slideNumber);
		return new File(getRelationshipsDirectory(), filename);
	}		

	File getSlideXML(int slideNumber){
		String filename = String.format("slide%d.xml", slideNumber);
		return new File(getSlidesDirectory(), filename);
	}

	static void doUnZipFile(File file, File dest){		
		try {
	         ZipFile zipFile = new ZipFile(file);
	         if (zipFile.isEncrypted()) {
//	            zipFile.setPassword(password);
	         }
	         zipFile.extractAll(dest.getAbsolutePath());
	    } catch (ZipException e) {
	    	logger.error(e.getMessage());
	    }
	}

}