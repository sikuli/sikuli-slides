package org.sikuli.slides.driver;

import java.util.List;

import org.sikuli.api.Target;

import com.google.common.collect.Lists;

/**
 * Specification of a user interface using a slide. A specification consists of
 * consists of a list of elements and how each element can be found using a
 * {@link Target}. The primary use of this is to hold the result of interpreting
 * a slide's content by a {@link SlideSpecInterpreter} object and then to be read
 * by another class to use the information to actually find the element on the
 * screen.
 * 
 * @author tomyeh
 * 
 */
class SlideSpec {

	List<Widget> specElements = Lists.newArrayList();

	void add(Widget element){
		specElements.add(element);
	}

	public int getElementCount(){
		return specElements.size();
	}

	public Widget getElement(int index) {
		return specElements.get(index);
	}

	public Widget findElementByLabel(String label) {
		for (Widget e : specElements){			
			if (e.getLabel().equals(label)){
				return e;
			}
		}
		return null;
	}

}