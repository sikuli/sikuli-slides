/**
Khalid
*/
package org.sikuli.slides.utils;

import java.io.File;

import org.sikuli.slides.sikuli.TutorialController;
/**
 * This class contains constants.
 * @author Khalid Alharbi
 *
 */
public class Constants {
	/**
	 * The working directory absolute path. 
	 * This holds the sikuli slides working directory in the operating system's temp directory. 
	 */
	public static String workingDirectoryPath;
	/**
	 * The current project directory name that contains the imported and extracted .pptx file. 
	 */
	public static String projectDirectory;
	/**
	 * The relative directory path to the embedding resources icons for the GUIs.
	 */
	public static final String RESOURCES_ICON_DIR="/org/sikuli/slides/gui/icons/";
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
	 * The slides directory that contains the slide notes.
	 */
	public static final String SLIDE_NOTES_DIRECTORY=File.separator+"ppt"+File.separator+"notesSlides";
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
	/**
	 * Screen id. Use this constant to run the test on a secondary monitor, 
	 * 0 means the default monitor and 1 means the secondary monitor.
	 */
	public static int ScreenId=0;
	/**
	 * This control how fuzzy the image search is. A value of 1 means the search
	 * is very precise and less fuzzy
	 */
	public static double MinScore=0.7;
	/**
	 * 
	 */
	public static boolean UseOldSyntax=false;
	/**
	 * Desktop input events
	 */
	public enum DesktopEvent {
		LEFT_CLICK, RIGHT_CLICK, DOUBLE_CLICK, DRAG_N_DROP, KEYBOARD_TYPING, LAUNCH_BROWSER, 
		EXIST, NOT_EXIST, WAIT
	}
	/**
	 * Running modes
	 */
	public static boolean ACTION_MODE=false;
	public static boolean HELP_MODE=false;
	public static boolean TUTORIAL_MODE=false;
	public static boolean DEVELOPMENT_MODE=false;
	/**
	 * Execution start time in milliseconds.
	 */
	public static long Execution_Start_Time;
	/**
	 * Tutorial mode previous step.
	 */
	public static boolean IsPreviousStep=false;
	/**
	 * Tutorial mode next step.
	 */
	public static boolean IsNextStep=false;
	/**
	 * Tutorial mode return back to previous step.
	 */
	public static boolean IsReturnToPreviousStep=false;
	/**
	 * Tutorial Controller
	 */
	public static TutorialController tutorialController=null;
	/**
	 * Wait action is running.
	 */
	public static boolean IsWaitActionRunning=false;
}
