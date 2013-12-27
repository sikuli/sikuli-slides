package org.sikuli.recorder.pptx;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sikuli.recorder.Utils;
import org.sikuli.recorder.Zip;
import org.sikuli.recorder.event.ClickEvent;
import org.sikuli.recorder.event.ClickEventGroup;
import org.sikuli.recorder.event.Event;
import org.sikuli.recorder.event.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class PPTXWriter {

	static STGroup group = new STGroupDir("org/sikuli/recorder/pptx","utf-8", '$', '$');
	static Logger logger = LoggerFactory.getLogger(PPTXWriter.class);

	static class BoxSTModel {
		private int x;
		private int y;
		private int cx;
		private int cy;
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
		public int getCx() {
			return cx;
		}
		public void setCx(int cx) {
			this.cx = cx;
		}
		public int getCy() {
			return cy;
		}
		public void setCy(int cy) {
			this.cy = cy;
		}
	}

	static class SlideSTModel {
		private int id;
		private int rid;
		private String name;
		private String imageName;
		private String command;
		private File imageSrc;
		private int imageCx;
		private int imageCy;
		private BoxSTModel box;
		private String content;

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getRid() {
			return rid;
		}
		public void setRid(int rid) {
			this.rid = rid;
		}
		public String getImageName() {
			return imageName;
		}
		public void setImageName(String imageName) {
			this.imageName = imageName;
		}
		public File getImageSrc() {
			return imageSrc;
		}
		public void setImageSrc(File imageSrc) {
			this.imageSrc = imageSrc;
		}
		public BoxSTModel getBox() {
			return box;
		}
		public void setBox(BoxSTModel box) {
			this.box = box;
		}
		public int getImageCx() {
			return imageCx;
		}
		public void setImageCx(int imageCx) {
			this.imageCx = imageCx;
		}
		public int getImageCy() {
			return imageCy;
		}
		public void setImageCy(int imageCy) {
			this.imageCy = imageCy;
		}
		public String getCommand() {
			return command;
		}
		public void setCommand(String command) {
			this.command = command;
		}
		public String toString(){
			return Objects.toStringHelper(this)
					.add("name", name)
					.add("command", command)
					.add("imageName", imageName)
					.add("imageSrc", imageSrc.getAbsolutePath())					
					.toString();
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}

	}



	public static void main(String[] args) throws IOException, ParseException{
		
		JSONParser parser=new JSONParser();

		  System.out.println("=======decode=======");
		                
		  FileReader s = new FileReader(new File("import.json"));
		  
//		  String s="[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
		  
		  
		  JSONObject obj = (JSONObject) parser.parse(s);
		  
//		  JSONArray feed;
//		JSONArray rows = (JSONArray) 
				  
//		  JSONObject 
		  JSONObject  feed = (JSONObject) obj.get("feed");
		  JSONArray  entry = (JSONArray) feed.get("entry");
//		  JSONArray array=(JSONArray)obj;
//		  System.out.println("======the 2nd element of array======");
//		  System.out.println(array.get(1));
//		  System.out.println();
		  
		  
		  
		  for (int i = 0; i < entry.size(); ++i){//entry.size(); ++i){
			  List<Item> items = Lists.newArrayList();			  
			  
			  JSONObject row = (JSONObject) entry.get(i);
			  
			  JSONObject field;
			  String value;
			  
			  field  = (JSONObject)  row.get("gsx$yourname");
			  value = (String) field.get("$t");
			  
			  String name = value;
			  
			  items.add(new Item("Name", value));
			  
			  field  = (JSONObject)  row.get("gsx$e-mail");
			  value = (String) field.get("$t");
			  
			  items.add(new Item("Email", value));

			  field  = (JSONObject)  row.get("gsx$www");
			  value = (String) field.get("$t");			  
			  items.add(new Item("URL to homepage", value));
			  			  
			  items.add(new Item("Photo", "{Please insert a photo of yourself here to help us connect you to a face}"));						

			  field  = (JSONObject)  row.get("gsx$start");
			  value = (String) field.get("$t");
			  items.add(new Item("Starting Semester/Year", value));
			  
			  field  = (JSONObject)  row.get("gsx$advisor");
			  value = (String) field.get("$t");			  
			  value = value.replaceAll("&"," and ");
			  items.add(new Item("Advisor", value));
			  
			  items.add(new Item("Committee Members", "{Please enter the names of your committee members, if applicable}"));
			  
			  field  = (JSONObject)  row.get("gsx$currentfunding");
			  value = (String) field.get("$t");			  
			  items.add(new Item("Current Funding", value));
			  
			  field  = (JSONObject)  row.get("gsx$milestone");
			  value = (String) field.get("$t");			  
			  items.add(new Item("Next Milestone", value));

			  field  = (JSONObject)  row.get("gsx$timeline");
			  value = (String) field.get("$t");			  
			  items.add(new Item("Next Milestone Target Time", value));

			  field  = (JSONObject)  row.get("gsx$award");
			  value = (String) field.get("$t");			  
			  if (value.length() > 2){
				  items.add(new Item("Award", value));
			  }
			  items.add(new Item("Award","{Please add one slide for each new award}"));
			  
			  field  = (JSONObject)  row.get("gsx$publications");
			  value = (String) field.get("$t");
			  
//			  /System.out.println(value);
			  String[] toks = value.split("\n");
			  for (String tok : toks){
				  int n = tok.split(" ").length;
				  tok = tok.replaceAll("&"," and ");
				  tok = tok.trim();
				  if (n > 3){
					  System.out.println(name + "  >" + tok);
					  items.add(new Item("Publication", tok));
				  }				  
			  }
			  items.add(new Item("Publication","{Please add one slide for each new publication}"));
			  //System.out.println(toks.length);
			  
			  items.add(new Item("Highlight","{Please insert an image to highlight your research/teaching/service, one image per slide}"));
			  items.add(new Item("Highlight","{Please insert an image to highlight your research/teaching/service, one image per slide}"));
			  
			  String dest = "reviews/" + name + ".pptx";
			  File outputFile = new File(dest);
			  generate(outputFile, items);
			  			  
//			  System.out.println(text);
		  }
//			
//		Object obj=JSONValue.parse(new FileReader(new File("import.json")));
		
		//JSONArray array=(JSONArray)obj;
//		System.out.println(entry.size());
		
//		File outputFile = new File("e.pptx");
//		generate(outputFile);
	}

	public static void generate(File outputFile, List<Item> items){
		File pptxSkeletonDir;
		try {
			pptxSkeletonDir = createSkeletonDir();
			generateFiles(pptxSkeletonDir, items);
			Zip.zipDir(pptxSkeletonDir, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static File createSkeletonDir() throws IOException{
		InputStream inputStream = PPTXWriter.class.getResourceAsStream("skeleton.pptx");
		File temp = File.createTempFile("temp","pptx");
		Utils.stream2file(inputStream, temp);
		File pptxSkeletonDir = Utils.createTempDirectory();
		Zip.unzip(temp, pptxSkeletonDir);
		return pptxSkeletonDir;
	}


	static class SlideDeck {
		
		private List<SlideSTModel> slides = Lists.newArrayList();
		
		int rid = 8;
		int id = 256; 
		int no = 1;
		public void addSlide(String title, String value){
			SlideSTModel slide = new SlideSTModel();
			slide.setName("slide" + (no) + ".xml");
			slide.setCommand(title);
			slide.setContent(value);
			slide.setId(id);
			slide.setRid(rid);
			getSlides().add(slide);
			id++;
			rid++;
			no++;
		}
		public List<SlideSTModel> getSlides() {
			return slides;
		}
	}

	static class Item {
		public Item(String title, String value) {
			this.title = title;
			this.value = value;
			this.value = value.replaceAll("&"," and ");
		}
		String title;
		String value;
	};
	
	public static void generateFiles(File pptxSkeletonDir, List<Item> items) throws IOException {

		File outputDir = pptxSkeletonDir;

		String L = File.separator;

		ST presentation_xml_ST = group.getInstanceOf("presentation_xml");	
		ST presentation_xml_rels_ST = group.getInstanceOf("presentation_xml_rels");
		ST content_types_xml_ST = group.getInstanceOf("content_types_xml");
		ST app_xml_ST = group.getInstanceOf("app_xml");

		//List<SlideSTModel> slides = createSlideSTModels(clickEventGroups);
		

		SlideDeck deck = new SlideDeck();
		for (Item item : items){
			deck.addSlide(item.title, item.value);		
		}
//		deck.addSlide("Name","Tom Yeh");
//		deck.addSlide("Email","tom.yeh@colorado.edu");
//		deck.
		

		List<SlideSTModel> slides = deck.getSlides();

		//copyImagesToPPTX(slides);

		for (int i = 0; i < slides.size(); ++i){
			logger.debug("Writing slide " + (i+1) + " of " + slides.size());
			SlideSTModel slide = slides.get(i);

			ST slide_xml_ST = group.getInstanceOf("simple_slide_xml");
			ST slide_xml_rels_ST = group.getInstanceOf("simple_slide_xml_rels");

			//			// copy image
			//			File dest = new File(outputDir, "ppt" + L + "media" + L + slide.getImageName());
			//			Files.copy(slide.getImageSrc(), dest);
			//
			slide_xml_ST.add("slide", slide);
			slide_xml_rels_ST.add("slide", slide);

			File slideFile = new File(outputDir, "ppt" + L + "slides" + L + slide.getName());				
			File slide_relsFile = new File(outputDir, "ppt" + L + "slides" + L + "_rels" + L + slide.getName() + ".rels");

			Files.write(slide_xml_ST.render(), slideFile, Charsets.UTF_8);
			Files.write(slide_xml_rels_ST.render(), slide_relsFile, Charsets.UTF_8);		

			presentation_xml_ST.add("slide",slide);
			presentation_xml_rels_ST.add("slide",slide);
			content_types_xml_ST.add("slide", slide);				
		}

		app_xml_ST.add("count", slides.size());

		File presentationFile = new File(outputDir, "ppt" + L + "presentation.xml");
		File presentation_relsFile = new File(outputDir, "ppt" + L + "_rels" + L + "presentation.xml.rels");
		File content_types_file = new File(outputDir, "[Content_Types].xml");
		File app_file = new File(outputDir, "docProps" + L + "app.xml");

		Files.write(presentation_xml_ST.render(), presentationFile, Charsets.UTF_8);
		Files.write(presentation_xml_rels_ST.render(), presentation_relsFile, Charsets.UTF_8);
		Files.write(content_types_xml_ST.render(), content_types_file, Charsets.UTF_8);
		Files.write(app_xml_ST.render(), app_file, Charsets.UTF_8);
	}


	//	private static List<SlideSTModel> createSlideSTModels(List<ClickEventGroup> clickEventGroups) {
	//		int rid = 8;
	//		int id = 256; 
	//		int no = 1; // slide1.xml, slide2.xml ...
	//		List<SlideSTModel> slides = Lists.newArrayList();
	//
	//		int imageHeight = 0;
	//		int imageWidth = 0; 				
	//
	//		for (ClickEventGroup g : clickEventGroups ){
	//
	//			File imageSrc = g.getScreenShotEventBefore().getFile();
	//			ClickEvent clickEvent = g.getClickEvent();
	//			int x = clickEvent.getX();
	//			int y = clickEvent.getY();
	//
	//			// assume all images are of the same size
	//			// so we read the image dimensions only once			
	//			if (imageWidth == 0 && imageHeight == 0){
	//				BufferedImage image = null;
	//				try {
	//					image = ImageIO.read(imageSrc);
	//					imageHeight = image.getHeight();
	//					imageWidth = image.getWidth();
	//				} catch (IOException e) {
	//					e.printStackTrace();							
	//				}
	//			}
	//
	//			int maxCx = 9144000;
	//			int maxCy = 6858000;
	//			
	//			int imageCx = maxCx;
	//			int imageCy = maxCy;
	//			
	//			int maxWidth = 1440;
	//			int maxHeight = 1080;
	//			
	//			int boxX = 0;
	//			int boxY = 0;
	//			
	//			int r = 6350;
	//			
	//			
	//			double scaleX = 1.0 * imageWidth / maxWidth;
	//			double scaleY = 1.0 * imageHeight / maxHeight;
	//			double scale;
	//			if (scaleX > scaleY) {
	//				// x is relatively larger
	//				imageCx = maxCx;
	//				imageCy = (int) (imageHeight / scaleX * r);
	//				scale = scaleX;
	//
	//			} else {
	//				imageCx = (int) (imageWidth / scaleY * r);				
	//				imageCy = maxCy;
	//				scale = scaleY;
	//			}
	//					
	//			boxX = (int) (x * r / scale);
	//			boxY = (int) (y * r / scale);
	//
	//			// fixed dimensions			
	//			int boxCx = 50 * r;
	//			int boxCy = 50 * r;					
	//
	//			// center the box
	//			boxX -= (boxCx/2);
	//			boxY -= (boxCy/2);
	//
	//			String name = "slide" + no + ".xml"; // slide1.xml, slide2.xml ... etc
	//			String imageName = "image" + no + ".png";
	//
	//
	//			SlideSTModel slide = new SlideSTModel();
	//			slide.setName(name);
	//			slide.setId(id);
	//			slide.setRid(rid);
	//			slide.setImageName(imageName);
	//			slide.setImageSrc(imageSrc);
	//			slide.setImageCx(imageCx);
	//			slide.setImageCy(imageCy);
	//
	//			BoxSTModel box = new BoxSTModel();
	//			box.setX(boxX);
	//			box.setY(boxY);
	//			box.setCx(boxCx);
	//			box.setCy(boxCy);					
	//			slide.setBox(box);
	//			
	//			String command = "";			
	//			if (clickEvent.getButton() == MouseEvent.BUTTON1){
	//				if (clickEvent.getCount() == 1){
	//					command = "Click";
	//				}else if (clickEvent.getCount() == 2){
	//					command = "Double Click";
	//				}
	//			} else if (clickEvent.getButton() == MouseEvent.BUTTON2 || clickEvent.getButton() == MouseEvent.BUTTON3){
	//				command = "Right Click";
	//			}			
	//			slide.setCommand(command);			
	//			logger.trace("createSlideSTModels() --> " + slide);
	//
	//			rid++;
	//			id++;
	//			no++;
	//
	//			slides.add(slide);
	//		}
	//		return slides;
	//	}	

}
