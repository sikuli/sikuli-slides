/**
Khalid
*/
package org.sikuli.slides.v1.utils;

import java.util.prefs.Preferences;

import org.sikuli.api.robot.desktop.DesktopScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPreferencesEditor {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(UserPreferencesEditor.class);
	private Preferences prefs;
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	public UserPreferencesEditor(){
		prefs = Preferences.userNodeForPackage(UserPreferencesEditor.class);
	}
	/**
	 * Returns the stored user preferences value for fuzziness score value that controls how fuzzy the image 
	 * search is. It returns a value between 0.1 and 1, where 0.1 is the least precise search and 1 is the most 
	 * precise search (default is 0.7).
	 * @return the fuzziness score value that controls how fuzzy the image search is.
	 */
	public double getPreciseSearchScore(){
		return prefs.getDouble(Constants.PRECISE_SEARCH_SCORE, Constants.PRECISE_SEARCH_SCORE_DEFAULT);
	}
	/**
	 * Sets the fuzziness score value that controls how fuzzy the image. It accepts a value between 1 and 10, where 1 
	 * is the least precise search and 10 is the most precise search.
	 * precise search (default is 7). The new value is stored in user preferences.
	 * @param PRECISE_SEARCH_SCORE_VALUE the fuzziness score value that controls how fuzzy the image search is.
	 */
	public void putPreciseSearchScore(int PRECISE_SEARCH_SCORE_VALUE){
		if(PRECISE_SEARCH_SCORE_VALUE > 0 && PRECISE_SEARCH_SCORE_VALUE <= 10){
			double score = (double) PRECISE_SEARCH_SCORE_VALUE / 10.0;
			prefs.putDouble(Constants.PRECISE_SEARCH_SCORE, score);
		}
		else{
			logger.error("Invalid precision score value. Please enter a valid score value.{}Accepted values are between " +
					"1 and 10, where 1 is the least precise search and 10 is the most precise search.",NEW_LINE);
		}
	}
	/**
	 * Returns the stored user preferences value for maximum wait time in milliseconds for all find target operations.
	 * @return the maximum wait time in milliseconds for all find target operations
	 */
	public int getMaxWaitTime(){
		return prefs.getInt(Constants.MAX_WAIT_TIME_MS, Constants.MAX_WAIT_TIME_MS_DEFAULT);
	}
	/**
	 * Sets the maximum wait time in milliseconds for all find target operations and store
	 * the new value in user preferences 
	 * @param MAX_WAIT_TIME_MS_NEW_VALUE maximum wait time in milliseconds for all find target operations
	 */
	public void putMaxWaitTime(int MAX_WAIT_TIME_MS_NEW_VALUE){
		if(MAX_WAIT_TIME_MS_NEW_VALUE < 0){
			logger.error("Invalid maximum wait time value. Please enter the maximum wait time in milliseconds.");
			return;
		}
		else{
			prefs.putInt(Constants.MAX_WAIT_TIME_MS, MAX_WAIT_TIME_MS_NEW_VALUE);
		}
	}
	/**
	 * Returns the stored user preferences value for the display/monitor id.
	 * @return stored user preferences value for the display/monitor id
	 */
	public int getDisplayId(){
		return prefs.getInt(Constants.DISPLAY_ID, Constants.DISPLAY_ID_DEFAULT);
	}
	/**
	 * Sets the display/monitor id
	 * the new value is stored in user preferences 
	 * @param DISPLAY_ID_VALUE the new display/monitor id
	 */
	public void putDisplayId(int DISPLAY_ID_VALUE){
		if(DISPLAY_ID_VALUE < 0 || DISPLAY_ID_VALUE >= DesktopScreen.getNumberScreens()){
			logger.error("Invalid display id. Please enter a valid display id. " +
					"Example: 0 refers to the main display, 1 is the secondary display, etc.");
			logger.info("resetting default screen to main display, screen 0");
			prefs.putInt(Constants.DISPLAY_ID, 0);
			Constants.ScreenId = 0;
			return;
		}
		else{
			prefs.putInt(Constants.DISPLAY_ID, DISPLAY_ID_VALUE);
			Constants.ScreenId = DISPLAY_ID_VALUE;
		}
	}
	/**
	 * Returns the stored user preferences value for the time, in seconds, to display a label on the screen
	 * @return the time in seconds to display a label on the screen.
	 */
	public int getLabelDisplayTime(){
		return prefs.getInt(Constants.LABEL_DISPLAY_TIME_SEC, Constants.LABEL_DISPLAY_TIME_SEC_DEFAULT);
	}
	/**
	 * Set the time, in seconds, to display a label on the screen.
	 * The new value is stored in user preferences.
	 * @param LABEL_DISPLAY_TIME_SEC_NEW_VALUE the time, in seconds, to display a label on the screen
	 */
	public void putLabelDisplayTime(int LABEL_DISPLAY_TIME_SEC_NEW_VALUE){
		if(LABEL_DISPLAY_TIME_SEC_NEW_VALUE < 0){
			logger.error("Invalid label display time value. Please enter the time, in seconds, to display" +
					" a label on the screen.");
			return;
		}
		else{
			prefs.putInt(Constants.LABEL_DISPLAY_TIME_SEC, LABEL_DISPLAY_TIME_SEC_NEW_VALUE);
		}
	}
	/**
	 * Returns the stored user preferences value for the instruction hint (tooltip) font size.
	 * @return the stored user preferences value for the instruction hint (tooltip) font size.
	 */
	public int getInstructionHintFontSize(){
		return prefs.getInt(Constants.INSTRUCTION_HINT_FONT_SIZE, Constants.INSTRUCTION_HINT_FONT_SIZE_DEFAULT);
	}
	/**
	 * Set the instruction hint (tooltip) font size.
	 * The new value is stored in user preferences.
	 * @param INSTRUCTION_HINT_FONT_SIZE_NEW_VALUE the instruction hint (tooltip) font size, in points.
	 */
	public void putInstructionHintFontSize(int INSTRUCTION_HINT_FONT_SIZE_NEW_VALUE){
		if(INSTRUCTION_HINT_FONT_SIZE_NEW_VALUE < 0){
			logger.error("Invalid instruction hint (tooltip) font size. Please enter a valid font size, in points.");
			return;
		}
		else{
			prefs.putInt(Constants.INSTRUCTION_HINT_FONT_SIZE, INSTRUCTION_HINT_FONT_SIZE_NEW_VALUE);
		}
	}
	/**
	 * Returns the stored user preferences value for the canvas width size.
	 * @return the stored user preferences value for the canvas width size.
	 */
	public int getCanvasWidthSize(){
		return prefs.getInt(Constants.CANVAS_WIDTH_SIZE, Constants.CANVAS_WIDTH_SIZE_DEFAULT);
	}
	/**
	 * Set the canvas width size.
	 * The new value is stored in user preferences.
	 * @param INSTRUCTION_HINT_FONT_SIZE_NEW_VALUE the canvas width size, in points.
	 */
	public void putCanvasWidthSize(int CANVAS_WIDTH_SIZE_NEW_VALUE){
		if(CANVAS_WIDTH_SIZE_NEW_VALUE < 0){
			logger.error("Invalid canvas width size. Please enter a valid width size, in points.");
			return;
		}
		else{
			prefs.putInt(Constants.CANVAS_WIDTH_SIZE, CANVAS_WIDTH_SIZE_NEW_VALUE);
		}
	}
}
