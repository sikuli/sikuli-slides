/**
 * @author Khalid Alharbi
 */
package org.sikuli.slides.v1.utils;

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
	 * The name of the directory that contains the downloaded files.
	 */
	public static final String SIKULI_DOWNLOAD_DIRECTORY=File.separator+"download";
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
	 * The key name for the score value that controls how fuzzy the image search is. A value of 1 means the search
	 * is very precise and less fuzzy. This is defined as minscore in the API
	 */
	public static final String PRECISE_SEARCH_SCORE = "precise_search_score";
	/**
	 * The default key value for the score value that controls how fuzzy the image search is
	 */
	public static final double PRECISE_SEARCH_SCORE_DEFAULT = 0.7;
	/**
	 *  The key name for the time to display a label on the screen.
	 */
	public static final String LABEL_DISPLAY_TIME_SEC = "label_display_time_sec";
	/**
	 *  The default key value for the time, in seconds, to display a label on the screen.
	 */
	public static final int LABEL_DISPLAY_TIME_SEC_DEFAULT = 3;
	/**
	 *  The key name for the instruction hint (tooltip) font size.
	 */
	public static final String INSTRUCTION_HINT_FONT_SIZE="instruction_hint_font_size";
	/**
	 *  The default key value of the instruction hint (tooltip) font size.
	 */
	public static int INSTRUCTION_HINT_FONT_SIZE_DEFAULT=18;
	/**
	 *  The key name for the canvas width size.
	 */
	public static final String CANVAS_WIDTH_SIZE="canvas_width_size";
	/**
	 *  The default key value of the canvas width size.
	 */
	public static int CANVAS_WIDTH_SIZE_DEFAULT=5;
	/**
	 *  The key name for the display id.
	 */
	public static final String DISPLAY_ID="display_id";
	/**
	 *  The default key value of the canvas width size.
	 */
	public static int DISPLAY_ID_DEFAULT = 0;
	
	/**
	 * Screen id. Use this constant to run the test on a secondary monitor, 
	 * 0 means the default monitor and 1 means the secondary monitor.
	 */
	public static int ScreenId = 0;
	/**
	 * Flag for the old syntax. Prior to version 1.2.0, we use special shapes to represent each action.
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
	public static boolean AUTOMATION_MODE=false;
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
	 * Tutorial mode navigation status
	 */
	public enum NavigationStatus {
		NEXT, PREVIOUS, DONE
	}
	/**
	 * The total screen width of the connected screens. This value is used by the global mouse listeners to
	 * find the location of the x mouse events.
	 */
	public static int Total_Screen_Width = 0;
}
