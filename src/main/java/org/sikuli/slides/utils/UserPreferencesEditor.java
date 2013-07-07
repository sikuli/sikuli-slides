/**
Khalid
*/
package org.sikuli.slides.utils;

import java.util.prefs.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPreferencesEditor {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(UserPreferencesEditor.class);
	private Preferences prefs;
	
	public UserPreferencesEditor(){
		prefs = Preferences.userNodeForPackage(UserPreferencesEditor.class);
	}
	/**
	 * Returns the stored user preferences value for maximum wait time in milliseconds for all find target operations.
	 * @return maximum wait time in milliseconds for all find target operations
	 */
	public int getMaxWaitTime(){
		return prefs.getInt(Constants.MAX_WAIT_TIME_MS, Constants.MAX_WAIT_TIME_MS_DEFAULT);
	}
	/**
	 * Set the maximum wait time in milliseconds for all find target operations and store
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
