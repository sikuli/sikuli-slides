package org.sikuli.slides.driver;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.slides.driver.SlideUIFactory;
import org.sikuli.slides.driver.UIElement;
import org.sikuli.slides.driver.annotations.Label;
import org.sikuli.slides.driver.annotations.Source;

@Source("header.pptx")
class Header {
    @Label("help")
    public UIElement help;        
    @Label(value = "doc")
    public UIElement doc;    
    public UIElement download;    
    public UIElement logo;
    public UIElement overview;
}

public class SlideUIFactoryTest {
	
	@Test
	public void initElements() throws IOException{
		
		Header header = SlideUIFactory.create(Header.class);		

		header.help.click();		
		header.download.click();
		header.doc.click();
		header.overview.click();
		header.logo.click();
		
	}
}
