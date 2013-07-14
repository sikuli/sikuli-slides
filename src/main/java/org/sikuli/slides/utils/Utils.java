/**
Khalid
*/
package org.sikuli.slides.utils;

import java.awt.Desktop;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(Utils.class);
	
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    public static boolean createWorkingDirectory(){
    	File tmpDirectory, workingDirectory;
    	try{
    		tmpDirectory=FileUtils.getTempDirectory();
    	}
    	catch(IllegalStateException e){
    		return false;
    	}
    	if (tmpDirectory.exists()) {
    		workingDirectory=new File(tmpDirectory.getAbsoluteFile()+File.separator+Constants.SIKULI_SLIDES_ROOT_DIRECTORY);
    		Constants.workingDirectoryPath=workingDirectory.getAbsolutePath();
    		if(workingDirectory.exists()){
    			return true;
    		}
    		else if(workingDirectory.mkdir()){
    			return true;
    		}
    		else{
    			return false;
    		}
    	}
    	else{
    		return false;
    	}
    }
    
    public static void doZipFile(File file){
       	byte[] buffer = new byte[1024];
    	try{
 
    		FileOutputStream fos = new FileOutputStream(Constants.workingDirectoryPath+File.separator+file.getName()+".zip");
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		ZipEntry ze= new ZipEntry(file.getName());
    		zos.putNextEntry(ze);
    		FileInputStream in = new FileInputStream(file.getAbsolutePath());
 
    		int len;
    		while ((len = in.read(buffer)) > 0) {
    			zos.write(buffer, 0, len);
    		}
 
    		in.close();
    		zos.closeEntry();
    		zos.close();

 
    	}catch(IOException ex){
    	   ex.printStackTrace();
    	}
    }
    

    public static void doUnZipFile(File file){
    	
    	
     byte[] buffer = new byte[1024];
 
     try{
 
    	//create output directory
    	File folder = new File(Constants.workingDirectoryPath+File.separator+file.getName().substring(0, file.getName().indexOf('.')));
    	// if the directory doesn't exist, create it
    	if(!folder.exists()){
    		folder.mkdir();
    	}
    	// if the directory already exists, delete it and recreate it.
    	else{
    		FileUtils.deleteDirectory(folder);
    		folder.mkdir();
    	}
    	
    	// Next, unzip it.
    	//get the zip file content
    	ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(file.getAbsoluteFile()));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	while(ze!=null){
 
    	   String fileName = ze.getName();
           File newFile = new File(folder + File.separator + fileName);
 
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
 
            FileOutputStream fos = new FileOutputStream(newFile);             
 
            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
 
            fos.close();   
            ze = zis.getNextEntry();
    	}
 
        zis.closeEntry();
    	zis.close();
    	
    	//delete the zip file since we no longer need it.
    	File zipFile = new File(Constants.workingDirectoryPath+File.separator+file.getName()+".zip");
    	if(zipFile.delete())
    		return;
    	else
    		logger.error("Couldn't delete zip: "+Constants.workingDirectoryPath+File.separator+file.getName()+".zip");
     }
     catch(IOException ex){
       ex.printStackTrace(); 
     }
    }
    
    public static void createSikuliImagesDirectory(){
    	//create sikuli directory
    	File folder = new File(Constants.projectDirectory+File.separator+Constants.SIKULI_DIRECTORY);
    	//create sikuli images directory
    	File imagesFolder = new File(Constants.projectDirectory+File.separator+Constants.SIKULI_DIRECTORY+File.separator+Constants.IMAGES_DIRECTORY);
    	// if the directory doesn't exist, create it
    	if(!folder.exists()){
    		folder.mkdir();
    		imagesFolder.mkdir();
    	}
    }
    /**
     * Open a new URL in the default web browser
     * @param uri
     */
    public static void openURLInBrowser(String URLString) {
    	try {
    		URI uri = new URI(URLString);
    		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
    			desktop.browse(uri);
    		}
    	}
    	catch (Exception e) {
    		logger.error("Failed to open the following URL: "+ URLString);
    	}
    }
    
    /**
     * Gets number of connected displays
     * 
     * @return Returns the number of connected displays
     */
    public static int getConnectedDisplays(){
    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	GraphicsDevice sd[] = ge.getScreenDevices();
    	if(sd != null){
    		return sd.length;
    	}
    	return 0;
    }
}
