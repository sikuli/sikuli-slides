/**
Khalid
*/
package org.sikuli.slides.utils;

import java.io.File;

public class Constants {

	public static final String zipDirectoryPath= System.getProperty("user.home")+File.separator+
			"Documents"+File.separator+"sikuli-slides";
	public static final String sikuliDirectory=File.separator+"sikuli";
	public static final String imagesDirectory=File.separator+"images";
	public static final String screenRegionImagesDirectory=imagesDirectory+File.separator+"regions";
	public static final String slidesDirectoryPath=File.separator+"ppt"+File.separator+"slides";
	public static final String mediaDirectoryPath=File.separator+"ppt"+File.separator+"media";
	public static final String presentationPath=File.separator+"ppt"+File.separator+"presentation.xml";
	public static final String relationshipDirectoryPath=File.separator+"ppt"+File.separator+"slides"+File.separator+"_rels";
	
	// the maximum time to wait in milliseconds
	public static int MaxWaitTime=15000;
	// annotation (canvas) display duration time in seconds
	public static final int CANVAS_DURATION=1;
}
