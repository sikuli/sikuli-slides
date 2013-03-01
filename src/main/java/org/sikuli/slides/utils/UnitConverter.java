/**
Khalid
*/
package org.sikuli.slides.utils;
/**
 * This class converts between various units that OpenXML uses.
 * @author Khalid
 *
 */

public class UnitConverter {
	
	/**
	 * Converts from English Metric Units (EMUs) to pixels
	 * @param emu  Size in English Metric Unit (EMU)
	 * @return Size in pixels
	 * 
	 */
    public static int emuToPixels(int emu)
    {
        if (emu != 0)
        {
        	// assuming the shape is not a child, so we divide by 9525.
            return (int)Math.round((double)emu / 9525);
        }
        else
        {
            return 0;
        }
    }
}
