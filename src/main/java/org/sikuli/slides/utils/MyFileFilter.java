/**
Khalid
*/
package org.sikuli.slides.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class MyFileFilter  extends FileFilter {
 
    //Accept all directories and all .pptx files.
    public boolean accept(File f) {
    	
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.pptx)) {
                    return true;
            } else {
                return false;
            }
        }
        return false;
    }

	@Override
	public String getDescription() {
		return "PowerPoint Presentation (*.pptx)";
	}
}
