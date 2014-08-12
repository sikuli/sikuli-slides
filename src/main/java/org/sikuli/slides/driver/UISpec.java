package org.sikuli.slides.driver;

import java.util.List;

import org.sikuli.api.Target;

import com.google.common.collect.Lists;

/**
 * UI Specification, consisting of a list of elements and how each element can be 
 * found using a {@link Target}.  The primary use of this is to hold the result of interpreting
 * a slide's content by a {@link UISpecInterpreter} object and then to be read by another
 * class to use the information to actually find the element on the screen.
 * 
 * @author tomyeh
 *
 */
class UISpec {

	List<UIElement> specElements = Lists.newArrayList();

	void add(UIElement element){
		specElements.add(element);
	}

	public int getElementCount(){
		return specElements.size();
	}

	public UIElement getElement(int index) {
		return specElements.get(index);
	}

	public UIElement findElementByLabel(String label) {
		for (UIElement e : specElements){			
			if (e.getLabel().equals(label)){
				return e;
			}
		}
		return null;
	}

}