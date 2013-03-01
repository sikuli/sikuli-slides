/**
Khalid
*/
package org.sikuli.slides.parsing;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.sikuli.slides.presentation.Presentation;
import org.sikuli.slides.utils.Constants;

public class PresentationParser extends DefaultHandler {
	
	private String xmlFile;
	private Presentation presentation;
	private int slidesCount=0;
	private boolean inSlideIdList=false;
	public PresentationParser(String projectDirectory){
		xmlFile=projectDirectory+Constants.presentationPath;
		presentation=new Presentation();
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
		// Parsing the presentation info
		// get the number of slides in the presentation document
		if(qName.equalsIgnoreCase("p:sldIdLst")){
			inSlideIdList=true;
		}
		else if(qName.equalsIgnoreCase("p:sldId")){
			slidesCount++;
		}
		// if current element is the slide size, create a new presentation		
		else if (qName.equalsIgnoreCase("p:sldSz")) {
			presentation.setCX(Integer.parseInt(attributes.getValue("cx")));
			presentation.setCY(Integer.parseInt(attributes.getValue("cy")));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (inSlideIdList&&qName.equalsIgnoreCase("p:sldIdLst")){
			inSlideIdList=false;
			presentation.setSlidesCount(slidesCount);
		}
	}
	
	/**
	 * returns the presentation object that contains information about the presentation slide.
	 * @return presentation object info that contains info about the slides
	 */
	public Presentation getPresentation(){
		return presentation;
	}
}
