/**
Khalid
*/
package org.sikuli.slides.shapes;

import static org.sikuli.api.API.browse;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.sikuli.slides.screenshots.SlideTargetRegion;
/**
 * Cloud shape to open the browser
 * @author Khalid
 *
 */
public class Cloud extends Shape {
	private int width;
	private int height;
	
	public Cloud(String name,String id, int offx, int offy, int cx,  int cy, int width, int height, String text, int order){
		super(id,name,offx,offy,cx,cy,text,order);
		this.width=width;
		this.height=height;
	}
	public Cloud(String id, String name, int order) {
		super(id,name,0,0,0,0,"",order);
		this.width=0;
		this.height=0;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public String toString(){
		return "Cloud info:\n" +super.toString()+
				"\n width:"+width+"\n height:"+height+
				"\n ******************************************";
	}
	@Override
	public void doSikuliAction(File targetFile, SlideTargetRegion contextRegion) {
		try {
			String userURL=getText();
			URL url=null;
			if(userURL.startsWith("http://")){
				url=new URL(userURL);
			}
			else{
				url=new URL("http://"+userURL);
			}
			browse(url);
		} catch (MalformedURLException e) {
			System.err.println("The text body of the Cloud shape doesn't contain a valid URL.");
		}
	}

}
