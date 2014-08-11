/**
Khalid Alharbi
Tom Yeh
*/
package org.sikuli.slides.api.parsers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sikuli.slides.api.interpreters.Keyword;
import org.sikuli.slides.api.interpreters.KeywordDictionary;
import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.KeywordElement;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SlideParser {
	
	static Map<String, String> parseRelationships(Document doc, File xml){
		Map<String, String> ret = Maps.newHashMap();
		NodeList nodeList = doc.getElementsByTagName("Relationship");
		for (int i = 0; i < nodeList.getLength(); ++i){
			Element el = (Element) nodeList.item(i);
			String id = el.getAttribute("Id");
			String target = el.getAttribute("Target");			
			String absoluteTarget = xml.getParent() + File.separator + target;
			ret.put(id,  absoluteTarget);
		}
		return ret;
	}
	
	void parseSlideElement(Node node, SlideElement e){
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			
			Element spPr = (Element) element.getElementsByTagName("p:spPr").item(0);
			
			Element off = (Element) spPr.getElementsByTagName("a:off").item(0);		
			if (off != null){
				e.setOffx(Integer.parseInt(off.getAttribute("x")));
				e.setOffy(Integer.parseInt(off.getAttribute("y")));
			}
			
			Element ln = (Element) spPr.getElementsByTagName("a:ln").item(0);
			if (ln != null){
				Node schemeClr = ln.getElementsByTagName("a:schemeClr").item(0);
				if (schemeClr != null){
					e.setLineColor(((Element) schemeClr).getAttribute("val"));
				}				
			}
			
			Element ext = (Element) spPr.getElementsByTagName("a:ext").item(0);			
			if (ext != null){
				e.setCx(Integer.parseInt(ext.getAttribute("cx")));
				e.setCy(Integer.parseInt(ext.getAttribute("cy")));
			}
			
			Element cNvPr = (Element) element.getElementsByTagName("p:cNvPr").item(0);		
			if (cNvPr != null){
				e.setName(cNvPr.getAttribute("name"));
				e.setId(cNvPr.getAttribute("id"));
			}
			
			String text = parseText(node);
			e.setText(text);
			
			NodeList txBodyList = element.getElementsByTagName("p:txBody");
			if (txBodyList.getLength() > 0){
				Element txBodyElement = (Element) txBodyList.item(0);					
								
				NodeList l = txBodyElement.getElementsByTagName("a:rPr");
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
				if (srgbClr != null)
					e.setBackgroundColor(srgbClr.getAttribute("val"));
			}
			
			list = element.getElementsByTagName("a:prstGeom");
			if (list.getLength() > 0){
				Element prstGeom = (Element) list.item(0);				
				e.setGeom(prstGeom.getAttribute("prst"));
			}			
			
		}		
	}
	
	ImageElement parseImageElement(Node node, Map<String,String> map){
		ImageElement e = new ImageElement();
		parseSlideElement(node, e);			
		Element blip = (Element) ((Element) node).getElementsByTagName("a:blip").item(0);			
		String relationshipID = blip.getAttribute("r:embed");			
		String target = map.get(relationshipID);
		e.setFileName(target);
		return e;
	}
	
	String parseParagraph(Node paragraphNode){	
		NodeList l = ((Element) paragraphNode).getElementsByTagName("a:t");
		String combinedText = "";
		for (int i = 0; i < l.getLength(); ++i){
			combinedText = combinedText + l.item(i).getTextContent();
		}
		return combinedText;
	}
	
	String parseText(Node node){
		Element element = (Element) node;
		NodeList txBodyList = element.getElementsByTagName("p:txBody");
		if (txBodyList.getLength() > 0){
			Element txBodyElement = (Element) txBodyList.item(0);
			NodeList paragraphNodes = txBodyElement.getElementsByTagName("a:p");
			List<String> paras = Lists.newArrayList();
			for (int i = 0; i < paragraphNodes.getLength(); ++i){
				paras.add(parseParagraph(paragraphNodes.item(i)));
			}
			return Joiner.on("\n").join(paras);
		}
		return null;
	}
	
	public KeywordElement parseKeywordElement(Node node){
		String text = parseText(node);
		if (text == null)
			return null;
		
		Keyword keyword = KeywordDictionary.lookup(text);
		if (keyword == null)
			return null;
		
		KeywordElement keywordElement = new KeywordElement();
		keywordElement.setKeyword(keyword);
		
		parseSlideElement(node, keywordElement);
		
		String matchedPrefix = keyword.match(text);
				
		// remove the keyword from the text
		String rest = text.substring(matchedPrefix.length(), text.length());
		rest = rest.trim();		
		keywordElement.setText(rest);
		
		return keywordElement;
	}
	
	public Slide parse(File xml, File rel){
		Slide slide = new Slide();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		Document relDoc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xml.getPath());
			relDoc = dBuilder.parse(rel.getPath());
		} catch (SAXException e) {
		} catch (IOException e) {
		} catch (ParserConfigurationException e) {
		}
		
		if (doc == null)
			return slide;

		Map<String, String> map  = parseRelationships(relDoc, xml);
		
		NodeList picList = doc.getElementsByTagName("p:pic");			
		for (int i = 0 ; i < picList.getLength(); ++ i){
			Node nNode = picList.item(i);
			SlideElement e = parseImageElement(nNode, map);	
			slide.add(e);
		}	
		
		NodeList shapeNodeList = doc.getElementsByTagName("p:sp");			
		for (int i = 0 ; i < shapeNodeList.getLength(); ++ i){
			
			Node nNode = shapeNodeList.item(i);	
			SlideElement e;
//			if ((e = parseKeywordElement(nNode)) != null){
//							
//				slide.add(e);
//				
//			}else{
									
				if (((Element) nNode).getElementsByTagName("a:blip").getLength()>0){								
					e = parseImageElement(nNode, map);	
					slide.add(e);				
				}else{		
					e = new SlideElement();
					parseSlideElement(nNode, e);		
					slide.add(e);
				}
//			}
		}
		
			
		return slide;
	}
	
}
