/**
Khalid
*/
package org.sikuli.slides.presentation;

public class Presentation {
	private int cX;
	private int cY;
	private int slidesCount;
	
	public int getCX() {
		return cX;
	}
	public void setCX(int cX) {
		this.cX = cX;
	}
	public int getCY() {
		return cY;
	}
	public void setCY(int cY) {
		this.cY = cY;
	}
	/**
	 * returns the number of slides in the presentation document 
	 * @return the number of slides in the presentation document 
	 */
	public int getSlidesCount() {
		return slidesCount;
	}
	/**
	 * sets the number of slides in the presentation document 
	 * @param slidesCount the number of slides in the presentation document 
	 */
	public void setSlidesCount(int slidesCount) {
		this.slidesCount = slidesCount;
	}
	public String toString(){
		return "Presentation info:\n" +
				"cX="+cX+" cY="+cY+"number of slides="+slidesCount+
				"\n ******************************************";
	}
}
