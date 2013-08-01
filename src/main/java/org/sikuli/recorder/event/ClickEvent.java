package org.sikuli.recorder.event;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Objects;

public class ClickEvent extends Event {
	private int x;
	private int y;
	private int button;
	private int clickCount;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}	
	public int getButton() {
		return button;
	}
	public void setButton(int button) {
		this.button = button;
	}	
	public int getCount() {
		return clickCount;
	}
	public void setClickCount(int count) {
		this.clickCount = count;
	}
	
	public JSONObject toJSON(){
		JSONObject obj=new JSONObject();
		obj.put("x",x);
		obj.put("y",y);
		obj.put("button",button);
		obj.put("clickCount", clickCount);
		return obj;
	}
	
	static public ClickEvent createFromJSON(String jsonText){		
		ClickEvent e = new ClickEvent();
		JSONParser parser = new JSONParser();
		try {
			Map json;
			json = (Map) parser.parse(jsonText);//, containerFactory);
			long x = (Long) json.get("x");
			long y=  (Long) json.get("y");
			long button = (Long) json.get("button");
			long count = (Long) json.get("clickCount");
			e.setX((int) x);
			e.setY((int) y);
			e.setButton((int) button);
			e.setClickCount((int) count);
			return e;
		} catch (ParseException ep) {
			return null;
		}
	}
	
	
	@Override
	public String toString() {
	    return Objects.toStringHelper(this.getClass()).add("x", x)
	            .add("y", y)
	            .add("button", button)
	            .add("clickCount", clickCount)
	            .toString();
	}

}