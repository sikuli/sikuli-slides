package org.sikuli.slides.api.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.net.URL;

import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.KeywordElement;
import org.sikuli.slides.api.models.SlideElement;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;

class PSlideElement extends PNode {
	
	public PSlideElement(SlideElement element){
		
		int x = element.getOffx();
		int y = element.getOffy();
		int w = element.getCx();
		int h = element.getCy();
		
		if (element instanceof ImageElement){
			URL url = ((ImageElement) element).getSource();
			PImage image = new PImage(url);
			
			image.setBounds(x,y,w,h);
			addChild(image);
		} 
		
		else if (element.getText() != null && element.getText().length() > 0 
				|| (element instanceof KeywordElement)){
			String text = element.getText();
			if (element instanceof KeywordElement){
				text = ((KeywordElement)element).getKeyword().getName() + " " + text;				
			}					
			PNode node = new PNode();
			PText txt = new PText(text);
			node.addChild(txt);
			txt.setTextPaint(Color.black);
//			txt.setFont(txt.getFont().deriveFont(1f*element.getTextSize()));
			PBounds b = txt.getBounds();
			double xscale = 1f * w / b.width;
			double yscale = 1f * h / b.height;
			
			txt.setTransform(AffineTransform.getScaleInstance(xscale,yscale));
			txt.setOffset(x,y);
			node.setBounds(x,y,w,h);
			addChild(node);
		
			if (element.getBackgroundColor() != null){

				Color bgColor = Color.decode("#" + element.getBackgroundColor());
				if (bgColor != null){
					txt.setPaint(bgColor);
				}else{
					txt.setPaint(null);	
				}				
			}
				
						
		}else{
				
			PPath p = PPath.createRectangle(0,0,w,h);
			
			try{
				Color color = Color.decode("#" + element.getLineColor());
				p.setStrokePaint(color);
			}catch (NumberFormatException e){
				p.setStrokePaint(Color.red);
			}
						
			p.setStroke(new BasicStroke(36000));
			p.setPaint(null);
			p.setOffset(x, y);
			addChild(p);
		}

	}	
}