package org.sikuli.slides.api.interpreters;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.interpreters.TypeStringParser.TypeStringPart;
import org.sikuli.slides.api.interpreters.TypeStringParser.TypeStringPart.Type;


public class TypeStringParserTest {

	//	ParseKey

	private TypeStringParser p;
	
	@Before
	public void setUp(){
		p = new TypeStringParser();
	}
	@Test
	public void sikuli_ENTER_pig_ESC(){
		String text = "sikuli[ENTER]pig[ESC]";
				
		List<TypeStringPart> parts = p.parse(text);

		assertThat(parts.size(), equalTo(4));
		assertThat(parts.get(0).getText(), equalTo("sikuli"));
		assertThat(parts.get(1).getText(), equalTo("ENTER"));
		assertThat(parts.get(2).getText(), equalTo("pig"));
		assertThat(parts.get(3).getText(), equalTo("ESC"));
	}

	@Test
	public void ENTER(){
		String text = "[ENTER]";
			
		List<TypeStringPart> parts = p.parse(text);

		assertThat(parts.size(), equalTo(1));
		assertThat(parts.get(0).getText(), equalTo("ENTER"));
		assertThat(parts.get(0).getType(), equalTo(Type.Key));
	}

	@Test
	public void ENTER_ESC_pp(){
		String text = "[ENTER][ESC]pp";
;		
		List<TypeStringPart> parts = p.parse(text);

		assertThat(parts.size(), equalTo(3));
		assertThat(parts.get(0).getText(), equalTo("ENTER"));
		assertThat(parts.get(0).getType(), equalTo(Type.Key));

		assertThat(parts.get(1).getText(), equalTo("ESC"));
		assertThat(parts.get(1).getType(), equalTo(Type.Key));

		assertThat(parts.get(2).getText(), equalTo("pp"));
		assertThat(parts.get(2).getType(), equalTo(Type.Text));

	}	
	
	@Test
	public void ENTER_how_are_you(){
		String text = "[ENTER]how are you";
;		
		List<TypeStringPart> parts = p.parse(text);

		assertThat(parts.size(), equalTo(2));
		assertThat(parts.get(0).getText(), equalTo("ENTER"));
		assertThat(parts.get(0).getType(), equalTo(Type.Key));

		assertThat(parts.get(1).getText(), equalTo("how are you"));
		assertThat(parts.get(1).getType(), equalTo(Type.Text));

	}		

	@Test
	public void sikuli_ENTER_ESC(){
		String text = "sikuli[ENTER][ESC]";
	
		List<TypeStringPart> parts = p.parse(text);

		assertThat(parts.size(), equalTo(3));
		assertThat(parts.get(0).getText(), equalTo("sikuli"));
		assertThat(parts.get(1).getText(), equalTo("ENTER"));
		assertThat(parts.get(2).getText(), equalTo("ESC"));
	}


}
