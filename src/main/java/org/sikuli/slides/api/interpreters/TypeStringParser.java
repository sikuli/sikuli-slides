package org.sikuli.slides.api.interpreters;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sikuli.slides.api.interpreters.TypeStringParser.TypeStringPart.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.collect.Lists;

public class TypeStringParser {
	
	Logger log = LoggerFactory.getLogger(TypeStringParser.class);
	
	static class TypeStringPart {
		
		public TypeStringPart(Type type, String text) {
			this.type = type;
			this.text = text;
		}
		enum Type {
			Key,
			Text
		};
		
		String text;
		Type type;		
	}
	
	List<TypeStringPart> parse(String input){

		List<TypeStringPart> results = Lists.newArrayList();

		Pattern pattern = Pattern.compile("(.*?)\\[([A-Z]+)\\]");		
		Matcher matcher = pattern.matcher(input);
		
		log.trace("input: " + input);
		
		String rest = "";
		while (matcher.find()) {
			
			System.out.println("group 1: " + matcher.group(1));
			log.trace("group 1: " + matcher.group(1) + ", group 2: " + matcher.group(2));
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

		return results;
	}
}