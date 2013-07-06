/**
 * @author Khalid Alharbi
 */
package org.sikuli.slides.utils;

import java.io.File;

/**
 * This class contains constants.
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
	 * The key name for maximum wait time to find target on the screen.
	 */
	public static final String MAX_WAIT_TIME_MS = "max_wait_time_ms";
	/**
	 * The default key value for maximum wait time in milliseconds to find target on the screen. 
	 */
	public static int MAX_WAIT_TIME_MS_DEFAULT = 15000;
	/**
	 *  The key name for the time to display canvas around the target on the screen.
	 */
	public static final String CANVAS_DISPLAY_TIME_SEC = "canvas_display_time_sec";
	/**
	 *  The default key value for the time, in seconds, to display canvas around the target on the screen.
	 */
	public static final int CANVAS_DISPLAY_TIME_SEC_DEFAULT = 3;
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
	 * Wait action.
	 */
	public static boolean IsWaitAction=false;
	/**
	 *  Total steps in the tutorial mode
	 */
	public static int Steps_Total=0;
	/**
	 *  The font size of the label in the tutorial mode
	 */
	public static int Label_Font_Size=18;
	/**
	 * Tutorial mode navigation status
	 */
	public enum NavigationStatus {
		NEXT, PREVIOUS
	}
}
