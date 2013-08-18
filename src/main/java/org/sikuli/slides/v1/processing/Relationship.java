/**
Khalid
*/
package org.sikuli.slides.v1.processing;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.sikuli.slides.v1.utils.Constants;

/* 
 *  
 * @author Khalid
 * This class handles the slide relationship XML file to get media path name in the /media directory.
 */
public class Relationship extends DefaultHandler  {
	
	private String relationshipXMLFile;
	private String mediaFileName;
	private String relationshipID;
	
	public Relationship(String fileName){
		this.relationshipXMLFile=Constants.projectDirectory + Constants.RELATIONSHIP_DIRECTORY + fileName+".rels";
	}
	public String getMediaFileName(String relationshipID){
		this.relationshipID=relationshipID;
		parseDocument();
		return mediaFileName;
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
			// get the media location
			if(relationshipID.equals(attributes.getValue("Id"))){
				mediaFileName=new File(attributes.getValue("Target")).getName();
			}
		}
	}

}
