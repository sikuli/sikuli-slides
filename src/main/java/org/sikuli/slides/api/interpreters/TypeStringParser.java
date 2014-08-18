package org.sikuli.slides.api.interpreters;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sikuli.slides.api.interpreters.TypeStringParser.TypeStringPart.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.collect.Lists;

public class TypeStringParser {
	
	static Logger log = LoggerFactory.getLogger(TypeStringParser.class);
	
	static public class TypeStringPart {
		
		public TypeStringPart(Type type, String text) {
			this.type = type;
			this.text = text;
		}
		public enum Type {
			Key,
			Text
		};
		
		private String text;
		private Type type;

		public String getText() {
			return text;
		}
		
		public Type getType() {
			return type;
		}		
		
		public String toString(){
			return text;
		}
	}
	
	public List<TypeStringPart> parse(String input){

		List<TypeStringPart> results = Lists.newArrayList();

		Pattern pattern = Pattern.compile("(.*?)\\[([A-Z]+)\\]");		
		Matcher matcher = pattern.matcher(input);
		
		log.trace("input: " + input);
		
		String rest = input;
		while (matcher.find()) {			
			log.trace("groups: 1: [{}], 2: [{}]", matcher.group(1), matcher.group(2));
			String substring = matcher.group(1);				
			String key = matcher.group(2);
			if (!substring.isEmpty()){
				results.add(new TypeStringPart(TypeStringPart.Type.Text, substring));
			}
			if (!key.isEmpty()){
				results.add(new TypeStringPart(TypeStringPart.Type.Key, key));
			}
			
			rest = input.substring(matcher.end());
		}
		
		if (!rest.isEmpty()){
			results.add(new TypeStringPart(Type.Text, rest));
		}

		log.debug("results: {}", results);
		return results;
	}
}