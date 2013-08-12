package org.sikuli.slides.api;

import java.util.Map;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.models.Slide;
import org.stringtemplate.v4.ST;

import com.google.common.collect.Maps;

public class Context {
	private ScreenRegion screenRegion;
	
	// the default slide selector is to accept all
	private SlideSelector slideSelector = new SlideSelector(){
		@Override
		public boolean accept(Slide slide) {			
			return true;
		}		
	};
	private Map<String, Object> parameters = Maps.newHashMap();
	
	public Context(ScreenRegion screenRegion) {
		this.screenRegion = screenRegion;
	}
	
	public Context(){
		screenRegion = new DesktopScreenRegion();
	}

	public ScreenRegion getScreenRegion() {
		return screenRegion;
	}

	public void setScreenRegion(ScreenRegion screenRegion) {
		this.screenRegion = screenRegion;
	}

	public void addParameter(String key, Object value){
		parameters.put(key, value);
	}

	public Map<String, Object> getParameters() {
		return Maps.newHashMap(parameters);
	}
	
	public String render(String tempalteText) {
		ST st = new ST(tempalteText);
		for (Map.Entry<String, Object> e : parameters.entrySet()){
			st.add(e.getKey(), e.getValue());	
		}
		return st.render();
	}

	public SlideSelector getSlideSelector() {
		return slideSelector;
	}

	public void setSlideSelector(SlideSelector slideSelector) {
		this.slideSelector = slideSelector;
	}
}