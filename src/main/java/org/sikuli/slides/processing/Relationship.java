/**
Khalid
*/
package org.sikuli.slides.processing;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.sikuli.slides.utils.Constants;

/* 
 *  
 * @author Khalid
 * This class handles the slide relationship XML file to get the image name in the /media directory.
 */
public class Relationship extends DefaultHandler  {
	
	private String relationshipXMLFile;
	private String imageFileName;
	private String relationshipID;
	
	public Relationship(String projectDirectory,String fileName){
		this.relationshipXMLFile=projectDirectory + Constants.relationshipDirectoryPath + fileName+".rels";
	}
	public String getImageFileName(String relationshipID){
		this.relationshipID=relationshipID;
		parseDocument();
		return imageFileName;
	}
	
	private void parseDocument(){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try{
			SAXParser parser = factory.newSAXParser();
			parser.parse(relationshipXMLFile, this);
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			} 
		catch (SAXException e) {
			e.printStackTrace();
			} 
		catch (IOException e) {
			e.printStackTrace();
			}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException{
		// Part 1: Parsing the original screen shoot info
		// if current element is Relationship, get the target
		if (qName.equalsIgnoreCase("Relationship")) {
			// get the image location
			if(relationshipID.equals(attributes.getValue("Id"))){
					imageFileName=new File(attributes.getValue("Target")).getName();
			}
		}
	}

}
