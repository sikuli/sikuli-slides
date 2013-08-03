/**
Khalid Alharbi
Tom Yeh
*/
package org.sikuli.slides.parsers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sikuli.slides.models.ScreenshotElement;
import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class SlideParser {
	
	static Map<String, String> parseRelationships(Document doc, URL xmlUrl){
		Map<String, String> ret = Maps.newHashMap();
		NodeList nodeList = doc.getElementsByTagName("Relationship");
		for (int i = 0; i < nodeList.getLength(); ++i){
			Element el = (Element) nodeList.item(i);
			String id = el.getAttribute("Id");
			String target = el.getAttribute("Target");			
			String absoluteTarget = new File(xmlUrl.getFile()).getParent() + File.separator + target;
			ret.put(id,  absoluteTarget);
		}
		return ret;
	}
	
	void parseSlideElement(Node node, SlideElement e){
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			
			Element spPr = (Element) element.getElementsByTagName("p:spPr").item(0);
			
			Element off = (Element) spPr.getElementsByTagName("a:off").item(0);				
			e.setOffx(Integer.parseInt(off.getAttribute("x")));
			e.setOffy(Integer.parseInt(off.getAttribute("y")));
			
			Element ext = (Element) spPr.getElementsByTagName("a:ext").item(0);			
			e.setCx(Integer.parseInt(ext.getAttribute("cx")));
			e.setCy(Integer.parseInt(ext.getAttribute("cy")));
			
			Element cNvPr = (Element) element.getElementsByTagName("p:cNvPr").item(0);				
			e.setName(cNvPr.getAttribute("name"));
			e.setId(cNvPr.getAttribute("id"));
			
			NodeList txBodyList = element.getElementsByTagName("p:txBody");
			if (txBodyList.getLength() > 0){
				Element txBodyElement = (Element) txBodyList.item(0);					
				NodeList l = txBodyElement.getElementsByTagName("a:t");
				if (l.getLength() > 0){
					e.setText(l.item(0).getTextContent());					
				}
								
				l = txBodyElement.getElementsByTagName("a:rPr");
				if (l.getLength() > 0){
					Element rPr = (Element) l.item(0);
					if (rPr.hasAttribute("sz")){
						e.setTextSize(Integer.parseInt(rPr.getAttribute("sz")));
					}											
				}
			}
				
			NodeList list = element.getElementsByTagName("a:solidFill");
			if (list.getLength() > 0){
				Element solidFill = (Element) list.item(0);
				Element srgbClr = (Element) solidFill.getElementsByTagName("a:srgbClr").item(0);
				e.setBackgroundColor(srgbClr.getAttribute("val"));
			}
			
			list = element.getElementsByTagName("a:prstGeom");
			if (list.getLength() > 0){
				Element prstGeom = (Element) list.item(0);
				e.setGeom(prstGeom.getAttribute("prst"));
			}			
			
		}		
	}
	
	void parseScreenshotElement(Node node, ScreenshotElement e, Map<String,String> map){
		parseSlideElement(node, e);			
		Element blip = (Element) ((Element) node).getElementsByTagName("a:blip").item(0);			
		String relationshipID = blip.getAttribute("r:embed");			
		String target = map.get(relationshipID);
		e.setFileName(target);
	}
	
	
	public Slide parse(URL xmlUrl, URL relUrl){
		Slide slide = new Slide();
		
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		Document relDoc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlUrl.getPath());
			relDoc = dBuilder.parse(relUrl.getPath());
		} catch (SAXException e) {
		} catch (IOException e) {
		} catch (ParserConfigurationException e) {
		}
		
		if (doc == null)
			return null;

		Map<String, String> map  = parseRelationships(relDoc, xmlUrl);
		
		
		NodeList shapeNodeList = doc.getElementsByTagName("p:sp");			
		for (int i = 0 ; i < shapeNodeList.getLength(); ++ i){
			Node nNode = shapeNodeList.item(i);							
			if (((Element) nNode).getElementsByTagName("a:blip").getLength()>0){			
				ScreenshotElement e = new ScreenshotElement();
				parseScreenshotElement(nNode, e, map);	
				slide.add(e);				
			}else{		
				SlideElement e = new SlideElement();
				parseSlideElement(nNode, e);		
				slide.add(e);
			}
		}
		
		NodeList picList = doc.getElementsByTagName("p:pic");			
		for (int i = 0 ; i < picList.getLength(); ++ i){
			Node nNode = picList.item(i);
			ScreenshotElement e = new ScreenshotElement();
			parseScreenshotElement(nNode, e, map);	
			slide.add(e);
		}		
		return slide;
	}
	
}
