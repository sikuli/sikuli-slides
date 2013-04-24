/**
Khalid
*/
package org.sikuli.slides.parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.sikuli.slides.media.Sound;
import org.sikuli.slides.screenshots.Screenshot;
import org.sikuli.slides.shapes.SlideShape;

public class SlideParser extends DefaultHandler {
	private Screenshot originalScreenshot;
	private Sound mSound;
	private String xmlFile;
	private boolean inScreenshot=false;
	private boolean inPictureElement=false;
	private boolean inSound=false;
	private boolean inShapeProperties=false;
	private boolean inShapeBackgroundColor=false;
	private boolean inShape=false;
	private boolean inArrowShape=false;
	private SlideShape slideShape;

	private boolean inTextBody=false;
	private String textBody="";
	private String arrowHeadId="";
	private String arrowEndId="";
	private List<SlideShape> shapesList;
	private List<SlideShape> labelsList;
	private int order;
	private String _shapeName, _shapeId; 
	private int _offx, _offy, _cx, _cy;
	
	public SlideParser(String xmlFile){
		this.xmlFile=xmlFile;
		shapesList=new ArrayList<SlideShape>();
		labelsList=new ArrayList<SlideShape>();
	}
	
	public void parseDocument(){
		// reset variables
		textBody="";
		arrowHeadId="";
		arrowEndId="";
		order=-1;
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
		catch (Exception e) {
			e.printStackTrace();
			}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException{
		// Part 1: Parsing the original screen shoot info
		// if current element is picture element
		if (qName.equalsIgnoreCase("p:pic")) {
			inPictureElement=true;
		}
		/*
		 * if the current child element is the "a:blip", get the qualified name or the relationship id.
		 * This must be done because the slide.xml file doesn't include the image file name in the /media directory.
		 */
		else if(inScreenshot && qName.equalsIgnoreCase("a:blip")){
			// get the relationship id
			originalScreenshot.setRelationshipID(attributes.getValue("r:embed"));
		}
		/*
		 * if the current element is the audio file, get the relationship id number.
		 */
		else if(inSound && qName.equalsIgnoreCase("a:audioFile")){
			mSound.setRelationshipId(attributes.getValue("r:link"));
		}
		/* if the current child element is the non-visual propeties of the shape (p:cNvPr), 
		then get the screenshot name and filename
		*/
		else if(inPictureElement && qName.equalsIgnoreCase("p:cNvPr")){
				String name=attributes.getValue("name");
				if(name.contains("Picture")){
					originalScreenshot=new Screenshot();
					inScreenshot=true;
					originalScreenshot.setName(name);
				}
				else if(name.contains("Sound")){
					mSound=new Sound();
					inSound=true;
					mSound.setName(name);
				}
		}
		
		// if the current child element is the shape properties (p:spPr), then get the screenshot dimensions
		else if(inScreenshot && qName.equals("p:spPr")){
			inShapeProperties=true;
		}
		
		// if the current child element is bounding box, get the offset in x and y 
		else if(inScreenshot && inShapeProperties && qName.equalsIgnoreCase("a:off")){
			//TODO: not sure if we will need this. See: http://openxmldeveloper.org/discussions/formats/f/13/p/867/2206.aspx
			originalScreenshot.setOffX(Integer.parseInt(attributes.getValue("x")));
			originalScreenshot.setOffY(Integer.parseInt(attributes.getValue("y")));
		}
		
		// if the current child element is the extents in x and y, get the values
		else if(inScreenshot && inShapeProperties && qName.equalsIgnoreCase("a:ext")){
			// Bug#39: check if the cx and cy attributes exist in case of copying and pasting the slide
			String cx_val=attributes.getValue("cx");
			String cy_val=attributes.getValue("cy");
			if(cx_val!=null&&cy_val!=null){
				originalScreenshot.setCx(Integer.parseInt(attributes.getValue("cx")));
				originalScreenshot.setCy(Integer.parseInt(attributes.getValue("cy")));
			}
		}
		
		// Part2: Parsing the shape information.
		// if the current element is a shape
		else if(qName.equalsIgnoreCase("p:sp")){
			inShape=true;
			order++;
			// shape info variables
			_shapeName="";
			_shapeId=""; 
			_offx=0; _offy=0; _cx=0; _cy=0;
		}
		// if the current element is the shape type, create the corresponding shape object
		//TODO check if a:prstGeom is more accurate than p:cNvPr
		else if(inShape&&qName.equalsIgnoreCase("p:cNvPr")){
			// get the shape name
			_shapeName=attributes.getValue("name");
			// get the shape id
			_shapeId=attributes.getValue("id");
		}
		// if the current child element is bounding box, get the offset in x and y 
		else if(inShape && qName.equalsIgnoreCase("a:off")){
			_offx=Integer.parseInt(attributes.getValue("x"));
			_offy=Integer.parseInt(attributes.getValue("y"));
		}
		// if the current child element is the extents in x and y, get the values
		else if(inShape && qName.equalsIgnoreCase("a:ext")){
			_cx=Integer.parseInt(attributes.getValue("cx"));
			_cy=Integer.parseInt(attributes.getValue("cy"));
		}
		
		// if the current child element is the shape persistent geometry, create the shape based on its type
		else if(inShape && qName.equalsIgnoreCase("a:prstGeom")){
			String shapeType=attributes.getValue("prst");
			slideShape=new SlideShape(_shapeId,_shapeName,order,shapeType,_offx,_offy,_cx,_cy,"");
		}
		// if the current element is the solid background color
		else if(inShape && qName.equalsIgnoreCase("a:solidFill")){
			inShapeBackgroundColor=true;
		}
		else if(inShape && inShapeBackgroundColor && qName.equalsIgnoreCase("a:srgbClr")){
			if(slideShape!=null){
				slideShape.setBackgroundColor(attributes.getValue("val"));
			}
		}
		// if the current element is the shape text body
		else if(inShape && qName.equalsIgnoreCase("p:txBody")){
			inTextBody=true;
		}
		// get font size
		else if(inTextBody && qName.equals("a:rPr")){
			String size= attributes.getValue("sz");
			if(size!=null&&slideShape!=null){
				slideShape.setTextSize(Integer.parseInt(size));
			}
		}
		
		// Parsing connected shapes like arrows
		else if(qName.equalsIgnoreCase("p:cxnSp")){
			inArrowShape=true;
		}
		// get the start connected shape id
		else if(inArrowShape&&qName.equalsIgnoreCase("a:stCxn")){
			arrowHeadId=attributes.getValue("id");
		}
		// get the end connected shape id
		else if(inArrowShape&&qName.equalsIgnoreCase("a:endCxn")){
			arrowEndId=attributes.getValue("id");
		}
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(inScreenshot && qName.equalsIgnoreCase("p:pic")){
			if(inScreenshot){
				inScreenshot=false;
			}
			else if(inSound){
				inSound=false;
			}
			inPictureElement=false;
		}
		else if(inScreenshot && inShapeProperties && qName.equalsIgnoreCase("p:spPr")){
			inShapeProperties=false;
		}
		else if(inShape && qName.equalsIgnoreCase("p:sp")){
			inShape=false;
			// if the shape is a label, add it to the label list
			if(slideShape!=null&&slideShape.getBackgroundColor().equals("FFFF00")){
				labelsList.add(slideShape);
			}
			else{
				addShapeToList();
			}
		}
		else if(inArrowShape && qName.equalsIgnoreCase("p:cxnSp")){
			inArrowShape=false;
			setRoundedRectangleDragAndDropOrder();
		}
		else if(inShape && qName.equalsIgnoreCase("a:solidFill")){
			inShapeBackgroundColor=false;
		}
		else if(inTextBody && qName.equalsIgnoreCase("p:txBody")){
			inTextBody=false;
			if(slideShape!=null){
				slideShape.setText(textBody);
			}
			textBody="";
		}
	}
	
	private void setRoundedRectangleDragAndDropOrder() {
		if(shapesList!=null){
			for(SlideShape mShape:shapesList){
				if(mShape.getId().equals(arrowHeadId)){
					mShape.setOrder(0);
					System.out.println(mShape.getName()+" is the drag shape");
				}
				else if(mShape.getId().equals(arrowEndId)){
					mShape.setOrder(1);
					System.out.println(mShape.getName()+" is the drop shape");
				}
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length){
		if(inTextBody){
			textBody+=new String(ch, start, length);
		}
	}
	
	// return the original screenshot
	public Screenshot getScreenshot(){
		return originalScreenshot;
	}
	
	// return the sound
	public Sound getSound(){
		return mSound;
	}
	
	// add the shape to the list
	private void addShapeToList(){
		if(slideShape!=null)
			shapesList.add(slideShape);
	}
	
	// return list of shapes
	public List<SlideShape> getShapes(){
		return  shapesList;
	}
	
	// return a list of labels
	public List<SlideShape> getLabels(){
		return labelsList;
	}
	
}
