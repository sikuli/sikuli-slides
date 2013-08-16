/**
Khalid
*/
package org.sikuli.slides.v1.parsing;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sikuli.slides.v1.utils.Constants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NoteParser extends DefaultHandler{
	private String xmlFile;
	private boolean inTextBody=false;
	private boolean inShapePlaceHolder=false;
	private boolean inNotePlaceHolder=false;
	private String noteBody="";
	
	public NoteParser(int slideNumber){
		xmlFile=Constants.projectDirectory+Constants.SLIDE_NOTES_DIRECTORY+
				File.separator+"notesSlide"+Integer.toString(slideNumber)+".xml";
	}
	public void parseDocument(){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try{
			SAXParser parser = factory.newSAXParser();
			parser.parse(xmlFile, this);
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
		if(qName.equalsIgnoreCase("p:sp")){
			inShapePlaceHolder=true;
		}
		if(inShapePlaceHolder&&qName.equalsIgnoreCase("p:ph")){
			String type=attributes.getValue("type");
			if(type.equals("body")){
				inNotePlaceHolder=true;
			}
		}
		else if(inNotePlaceHolder&&qName.equalsIgnoreCase("p:txBody")){
			inTextBody=true;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(inNotePlaceHolder&&qName.equalsIgnoreCase("p:sp")){
			inNotePlaceHolder=false;
			inShapePlaceHolder=false;
		}
		else if(inTextBody && qName.equalsIgnoreCase("p:txBody")){
			inTextBody=false;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length){
		if(inTextBody){
			noteBody+=new String(ch, start, length);
		}
	}
	
	public String getNote(){
		return noteBody;
	}
	
}
