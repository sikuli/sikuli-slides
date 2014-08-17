package org.sikuli.slides.driver;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.slides.driver.WidgetContainerFactory;
import org.sikuli.slides.driver.Widget;
import org.sikuli.slides.driver.annotations.Label;
import org.sikuli.slides.driver.annotations.Source;

@Source("header.pptx")
class Header {
    @Label("help")
    public Widget help;        
    @Label(value = "doc")
    public Widget doc;    
    public Widget download;    
    public Widget logo;
    public Widget overview;
}

public class SlideUIFactoryTest {
	
	@Test
	public void initElements() throws IOException{
		
		Header header = WidgetContainerFactory.create(Header.class);		

		header.help.click();		
		header.download.click();
		header.doc.click();
		header.overview.click();
		header.logo.click();
		
	}
}