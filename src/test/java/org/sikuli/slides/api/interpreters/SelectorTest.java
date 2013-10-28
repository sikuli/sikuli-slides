package org.sikuli.slides.api.interpreters;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.interpreters.KeywordDictionary;
import org.sikuli.slides.api.models.Selector;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;

public class SelectorTest {

	private Slide slide;

	@Before
	public void setUp() throws IOException{
		slide = new Slide();

	}

	@Test
	public void testEmptyTextShouldNotBeSelectedByHasText(){
		// these are not selected
		slide.newElement().text("").add();		
		// this is selected
		slide.newElement().text(" ").add();

		int n = Selector.select(slide).hasText().all().size();
		assertThat(n, equalTo(1));		
	}

	@Test
	public void testNullTextShouldNotBeSelectedByHasText(){
		// these are not selected
		slide.newElement().text(null).add();		
		// this is selected
		slide.newElement().text(" ").add();

		int n = Selector.select(slide).hasText().all().size();
		assertThat(n, equalTo(1));		
	}

	@Test
	public void testIsKeywordAndHasTextCanBeChained(){
		SlideElement yes = slide.newKeywordElement().keyword(KeywordDictionary.TYPE).text("something").add();
		SlideElement no  = slide.newKeywordElement().keyword(KeywordDictionary.TYPE).add();

		List<SlideElement> ret = Selector.select(slide).hasText().isKeyword().all();
		assertThat(ret.size(), equalTo(1));
		assertThat(ret.get(0), equalTo(yes));
	}
	
	@Test
	public void testIsNotKeywordShouldSelectNoKeyword(){
		SlideElement yes = slide.newElement().text("something").add();
		SlideElement no  = slide.newKeywordElement().keyword(KeywordDictionary.TYPE).add();

		List<SlideElement> ret = Selector.select(slide).isNotKeyword().all();
		assertThat(ret.size(), equalTo(1));
		assertThat(ret.get(0), equalTo(yes));
	}

	@Test
	public void testSelectTwoTextElements(){
		slide.newElement().text("some text").add();
		slide.newElement().text("some text").add();
		
		int n = Selector.select(slide).hasText().all().size();
		assertThat(n, equalTo(2));
	}
	
	@Test
	public void testIsKeywordCanSelectMultipleKeywords(){
		slide = new Slide();
		slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();
		slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();
		slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();

		int n = Selector.select(slide).isKeyword().all().size();
		assertThat(n, equalTo(3));
	}

	@Test
	public void testIntersects(){
		SlideElement e1 = slide.newElement().bounds(100,100,100,100).add();
		SlideElement e2 = slide.newElement().bounds(50,50,100,100).add();

		List<SlideElement> ret = Selector.select(slide).intersects(e2).all();
		assertThat(ret.size(), equalTo(1));
		assertThat(ret.get(0), equalTo(e1));
	}

	@Test
	public void testCanIntersectsOnlyTextElements(){
		
		SlideElement e1 = slide.newElement().bounds(100,100,100,100).add();
		SlideElement e2 = slide.newElement().bounds(50,50,100,100).add();
		SlideElement e3 = slide.newElement().bounds(50,50,100,100).text("this should be the only one selected").add();
		
		List<SlideElement> ret = Selector.select(slide).hasText().intersects(e1).all();
		assertThat(ret.size(), equalTo(1));
		assertThat(ret.get(0), equalTo(e3));
	}

	@Test
	public void testIsKeywordCanSelectBySpecificKeyword(){
		
		slide = new Slide();
		SlideElement e1 = slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();
		SlideElement e2 = slide.newKeywordElement().keyword(KeywordDictionary.DOUBLE_CLICK).add();
		
		List<SlideElement> ret = Selector.select(slide).isKeyword(KeywordDictionary.CLICK).all();
		assertThat(ret.size(), equalTo(1));
		assertThat(ret.get(0), equalTo(e1));
	}


	@Test
	public void testIsTargetCanFindOneTargetOnImage(){

		slide.newImageElement().bounds(20,20,200,200).add();
		SlideElement t1 = slide.newElement().bounds(50,50,100,100).add();

		List<SlideElement> ret = Selector.select(slide).isTarget().all();
		assertThat(ret.size(), equalTo(1));
		assertThat(ret.get(0), equalTo(t1));
	}
	
	@Test
	public void testIsTargetCanFindMultipleTargetsOnSameImage(){

		slide.newImageElement().bounds(20,20,200,200).add();
		SlideElement t1 = slide.newElement().bounds(50,50,100,100).add();
		slide.newElement().bounds(90,90,20,20).add();
		
			
		List<SlideElement> ret = Selector.select(slide).isTarget().all();
		assertThat(ret.size(), equalTo(2));
		assertThat(ret.get(0), equalTo(t1));
	}
	
	@Test
	public void testIsImageCanFindImageElements(){

		slide.newImageElement().add();
		slide.newImageElement().add();
		slide.newElement().add();

		List<SlideElement> ret = Selector.select(slide).isImage().all();
		assertThat(ret.size(), equalTo(2));
	}
	
	@Test
	public void testIsKeywordCanFindKeywordElements(){

		slide.newKeywordElement().keyword(null).add();
		slide.newKeywordElement().keyword(null).add();		
		
		List<SlideElement> ret = Selector.select(slide).isKeyword().all();
		assertThat(ret.size(), equalTo(2));
	}


}
