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
import org.sikuli.slides.screenshots.Screenshot;
import org.sikuli.slides.shapes.Cloud;
import org.sikuli.slides.shapes.Frame;
import org.sikuli.slides.shapes.Oval;
import org.sikuli.slides.shapes.Rectangle;
import org.sikuli.slides.shapes.RoundedRectangle;
import org.sikuli.slides.shapes.Shape;
import org.sikuli.slides.shapes.TextBox;

public class SlideParser extends DefaultHandler {
	private Screenshot originalScreenshot;
	private String xmlFile;
	private boolean inScreenshot=false;
	private boolean inShapeProperties=false;
	
	private boolean inShape=false;
	private boolean inArrowShape=false;
	private boolean isMultipleShapes=false;
	private Shape shape;

	private boolean inTextBody=false;
	private String textBody="";
	
	private String arrowHeadId="";
	private String arrowEndId="";
	private List<Shape> shapesList;
	private int order;
	
	private String _shapeName, _shapeId; 
	private int _offx, _offy, _cx, _cy;
	
	public SlideParser(String xmlFile){
		this.xmlFile=xmlFile; 
	}
	
	public void parseDocument(){
		// reset variables
		textBody="";
		arrowHeadId="";
		arrowEndId="";
		shapesList=null;
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
		// if current element is the original screenshot, create a new screenshot
		if (qName.equalsIgnoreCase("p:pic")) {
			originalScreenshot=new Screenshot();
			inScreenshot=true;
		}
		/*
		 * if the current child element is the "a:blip", get the qualified name or the relationship id.
		 * This must be done because the slide.xml file doesn't include the image file name in the /media directory.
		 */
		else if(inScreenshot && qName.equalsIgnoreCase("a:blip")){
			// get the relationship id
			originalScreenshot.setRelationshipID(attributes.getValue("r:embed"));
		}
		/* if the current child element is the non-visual propeties of the shape (p:cNvPr), 
		then get the screenshot name and filename
		*/
		else if(inScreenshot && qName.equalsIgnoreCase("p:cNvPr")){
				originalScreenshot.setName(attributes.getValue("name"));
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
				
				// the shape is a rounded rectangle
				if(shapeType.equals("roundRect") && _shapeName.contains("Rounded Rectangle")){
					shape=new RoundedRectangle(_shapeId,_shapeName,order);
					shape.setOffx(_offx);
					shape.setOffy(_offy);
					shape.setCx(_cx);
					shape.setCy(_cy);
					
					if(shapesList==null){
						shapesList=new ArrayList<Shape>();
					}
					if(shapesList!=null){
						shapesList.add(shape);
					}
				}
				// the shape is a rectangle
				else if(shapeType.equals("rect") && _shapeName.contains("Rectangle")){
					shape=new Rectangle(_shapeId,_shapeName,order);
					shape.setOffx(_offx);
					shape.setOffy(_offy);
					shape.setCx(_cx);
					shape.setCy(_cy);
				}
				// the shape is a rectangle
				else if(shapeType.equals("frame") && _shapeName.contains("Frame")){
					shape=new Frame(_shapeId,_shapeName,order);
					shape.setOffx(_offx);
					shape.setOffy(_offy);
					shape.setCx(_cx);
					shape.setCy(_cy);
				}
				// the shape is an ellipse/oval
				else if(shapeType.equals("ellipse") && _shapeName.contains("Oval")){
					shape=new Oval(_shapeId,_shapeName,order);
					shape.setOffx(_offx);
					shape.setOffy(_offy);
					shape.setCx(_cx);
					shape.setCy(_cy);
				}
				
				// the shape is a cloud
				else if(shapeType.equals("cloud") && _shapeName.contains("Cloud")){
					shape=new Cloud(_shapeId,_shapeName,order);
					shape.setOffx(_offx);
					shape.setOffy(_offy);
					shape.setCx(_cx);
					shape.setCy(_cy);
				}
				// the shape is a TextBox
				else if(shapeType.equals("rect") && _shapeName.contains("TextBox")){
					shape=new TextBox(_shapeId,_shapeName,order);
					shape.setOffx(_offx);
					shape.setOffy(_offy);
					shape.setCx(_cx);
					shape.setCy(_cy);
				}	
		}

		// if the current element is the shape text body
		else if(inShape && qName.equalsIgnoreCase("p:txBody")){
			inTextBody=true;
		}
		
		// Parsing connected shapes like arrows
		else if(qName.equalsIgnoreCase("p:cxnSp")){
			inArrowShape=true;
			isMultipleShapes=true;
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
			inScreenshot=false;
		}
		else if(inScreenshot && inShapeProperties && qName.equalsIgnoreCase("p:spPr")){
			inShapeProperties=false;
		}
		else if(inShape && qName.equalsIgnoreCase("p:sp")){
			inShape=false;
		}
		else if(inArrowShape && qName.equalsIgnoreCase("p:cxnSp")){
			inArrowShape=false;
			setRoundedRectangleDragAndDropOrder();
		}
		else if(inTextBody && qName.equalsIgnoreCase("p:txBody")){
			inTextBody=false;
			if(shape!=null)
				shape.setText(textBody);
		}
	}
	
	private void setRoundedRectangleDragAndDropOrder() {
		if(shapesList!=null){
			for(Shape mShape:shapesList){
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
	
	public boolean isMultipleShapes(){
		if(isMultipleShapes){
			isMultipleShapes=false;
			return true;
		}
		return false;
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
	
	// return the shape
	public Shape getShape(){
		return shape;
	}
	
	// return list of shapes
	public List<Shape> getShapes(){
		return  shapesList;
	}
	
}
