/**
Khalid
*/
package org.sikuli.slides.utils;

import java.io.File;
/**
 * This class contains constants.
 * @author Khalid Alharbi
 *
 */
public class Constants {
	/**
	 * The working directory absolute path. 
	 * This is the sikuli slides working directory in the operating system's temp directory. 
	 */
	public static String workingDirectoryPath;
	/**
	 * The current project directory name that contains the imported and extracted .pptx file. 
	 */
	public static String projectDirectory;
	/**
	 * The name of the directory that sikuli-slides creates in the operating system's tmp directory.
	 */
	public static final String SIKULI_SLIDES_ROOT_DIRECTORY="org.sikuli.SikuliSlides";
	/**
	 * The name of the directory that contains all sikuli related files in the .pptx file.
	 */
	public static final String SIKULI_DIRECTORY=File.separator+"sikuli";
	/**
	 * The name of the images directory that contains the cropped target images.
	 */
	public static final String IMAGES_DIRECTORY=File.separator+"images";
	/**
	 * The slides directory that contains the XML files for each slide in the .pptx file.
	 */
	public static final String SLIDES_DIRECTORY=File.separator+"ppt"+File.separator+"slides";
	/**
	 * The slides directory that contains the media files for each slide in the .pptx file.
	 */
	public static final String MEDIA_DIRECTORY=File.separator+"ppt"+File.separator+"media";
	/**
	 * The presentaion.xml absolute path name. 
	 */
	public static final String PRESENTATION_DIRECTORY=File.separator+"ppt"+File.separator+"presentation.xml";
	/**
	 * The relationship directory that contains info about the duplicated media files in the presentation slides.
	 */
	public static final String RELATIONSHIP_DIRECTORY=File.separator+"ppt"+File.separator+"slides"+File.separator+"_rels";
	/**
	 * The maximum time to wait in milliseconds
	 */
	public static int MaxWaitTime=15000;
	/**
	 *  The duration time in seconds to display annotation (canvas) around the target on the screen. 
	 */
	public static final int CANVAS_DURATION=1;
}
