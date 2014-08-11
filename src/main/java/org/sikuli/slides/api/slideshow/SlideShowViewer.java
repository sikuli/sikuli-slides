package org.sikuli.slides.api.slideshow;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.views.PSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

@SuppressWarnings("serial")
public class SlideShowViewer extends JFrame implements SlideShowListener {

	static private Logger logger = LoggerFactory.getLogger(SlideShowViewer.class);

	private JButton previousButton;
	private JButton nextButton;
	private PlayButton playButton;
	private JComboBox slideList;
	private Slide currentSlide;
	private PCanvas canvas;
	private SlideShowController slideshow;

	private ControlBar controlBar;
	private String sourcePath;

	private PPath statusRectangle;
	
	private boolean minimized = false;
	
	
	public static void main(String... args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){			
				SlideShowViewer viewer = new SlideShowViewer();
				viewer.setVisible(true);
				viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}			
		});
	}

	class GettingStartedView extends PNode {
		
		GettingStartedView(){
			final PText txt = new PText("Drag a PPTX file here to open");
			txt.setTextPaint(Color.black);
			txt.setScale(2);
			addChild(txt);
		}
	}
	
	public SlideShowViewer() {
		getContentPane().setLayout(new BorderLayout());
		canvas = new PCanvas();
		canvas.setPanEventHandler(null);
		getContentPane().add(canvas, BorderLayout.CENTER);
		canvas.getLayer().addChild(new GettingStartedView());
		controlBar = new ControlBar();
		getContentPane().add(controlBar, BorderLayout.SOUTH);
		setLocation(50,500);
		maximize();
		setTitle("Sikuli Slides");
		setResizable(false);
		setAlwaysOnTop(true);

		new FileDrop(null, getContentPane(), new FileDrop.Listener(){  
			public void filesDropped(File[] files){
				// only open the file file in the list
				if (files.length > 0){
					invokeOpen(files[0]);
				}
			}
		});

		initHotKeys();
	}

	void invokePrevious(){
		slideshow.previous();
	}

	void invokeNext(){
		slideshow.next();
	}

	void invokeJumpTo(int index){
		slideshow.jumpTo(index);
	}
	
	void minimize(){
		setSize(400,80);
		canvas.setVisible(false);		
	}
	
	void maximize(){
		setSize(400,380);
		canvas.setVisible(true);
	}
	
	void toggleSize(){
		minimized = !minimized;
		if (minimized){			
			minimize();		
		}else{
			maximize();				
		}
	}

	void togglePlay(){
		if (slideshow.isPaused()){
			slideshow.play();			
		}else{
			slideshow.pause();
		}
		playButton.refresh();
	}

	void invokeRefresh(){
		logger.debug("[invokeRefresh]");
		if (sourcePath != null){
			int currentSlideNumber = currentSlide.getNumber();
			invokeOpen(new File(sourcePath));
			slideshow.jumpTo(currentSlideNumber - 1);
		}
	}

	public void invokeOpen(URL url){
		PPTXSlidesReader reader = new PPTXSlidesReader();		
		List<Slide> slides;
		try {
			slides = reader.read(url);
			sourcePath = url.getPath();
			setTitle(url.toString());
			onSlidesLoaded(slides);
		} catch (IOException e) {
		}
	}

	public void invokeOpen(File file){
		PPTXSlidesReader reader = new PPTXSlidesReader();		
		List<Slide> slides;
		try {
			slides = reader.read(file);
			sourcePath = file.getPath();
			setTitle(file.toString());
			onSlidesLoaded(slides);
		} catch (IOException e) {
		}
	}
	
	class CustomButton extends JButton{		
		CustomButton(){
			setBorderPainted(false);
			setFocusPainted(false);
			setContentAreaFilled(false);
		}		
	}
	
	class ResizeButton extends CustomButton implements ActionListener {
		
		ImageIcon bigger;
		ImageIcon smaller;
		
		ResizeButton(){			
			try {
				BufferedImage img = ImageIO.read(getClass().getResource("bigger.png"));
				bigger = new ImageIcon(img);
				img = ImageIO.read(getClass().getResource("smaller.png"));
				smaller = new ImageIcon(img);
			} catch (IOException e1) {
			}			
			
			addActionListener(this);
			
			refresh();
		}
		
		void refresh(){
			if (minimized){
				setIcon(bigger);
				setToolTipText("Bigger");
			}else{
				setIcon(smaller);
				setToolTipText("Smaller");
			}
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			toggleSize();
			refresh();
		}
		
	}

	class PlayButton extends CustomButton {
		
		ImageIcon play;
		ImageIcon pause;

		
		PlayButton(){
			super();
			BufferedImage img;
			try {
				img = ImageIO.read(getClass().getResource("play.png"));
				play = new ImageIcon(img);
				img = ImageIO.read(getClass().getResource("pause.png"));
				pause = new ImageIcon(img);
			} catch (IOException e1) {
			}
			
			
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					togglePlay();		
				}				
			});
			refresh();
		}		

		void refresh(){
			if (slideshow == null){
				setEnabled(false);
				setIcon(play);
				setToolTipText("Play");
			}else{
				if (slideshow.isPaused()){
					setIcon(play);
					setToolTipText("Play");
				}else{
					setIcon(pause);
					setToolTipText("Pause");
				}
				setEnabled(true);
			}
		}
	}
	
	class ControlBar extends JPanel {

		private CustomButton refreshButton;
		private ResizeButton resizeButton;

		public ControlBar(){
//			setLayout(new FlowLayout(FlowLayout.LEADING, 3, 3));
//			SpringLayout layout = new SpringLayout();
			BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
			setLayout(layout);
			 
			previousButton = new CustomButton();
			previousButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					invokePrevious();
				}				
			});
			try {
				Image img;
				img = ImageIO.read(getClass().getResource("left.png"));
				previousButton.setIcon(new ImageIcon(img));
			} catch (IOException e1) {
			}	
			previousButton.setToolTipText("Previous Slide");

			nextButton = new CustomButton();
			nextButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					invokeNext();
				}				
			});
			try {
				Image img;
				img = ImageIO.read(getClass().getResource("right.png"));
				nextButton.setIcon(new ImageIcon(img));
			} catch (IOException e1) {
			}	
			nextButton.setToolTipText("Next Slide");


			playButton = new PlayButton();

			slideList = new JComboBox(new String[]{"N/A"});			
			slideList.setSelectedIndex(0);
			slideList.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent event) {
					JComboBox source = (JComboBox) event.getSource();
					// if the popup is visible, it means the user is manually
					// changing the combobox selection
					if(source.isPopupVisible()){
						invokeJumpTo(slideList.getSelectedIndex());
			        }					
				}
				
			});
			
			refreshButton = new CustomButton();
			refreshButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					invokeRefresh();
				}				
			});
			try {
				Image img;
				img = ImageIO.read(getClass().getResource("refresh.png"));
				refreshButton.setIcon(new ImageIcon(img));
			} catch (IOException e1) {
			}
			refreshButton.setToolTipText("Refresh");
			
			resizeButton = new ResizeButton();
			
			add(previousButton);
			add(playButton);
			add(nextButton);
			add(slideList);
			add(refreshButton);
			add(resizeButton);

			setEnabled(false);
		}		

		@Override
		public void setEnabled(boolean enabled){
			previousButton.setEnabled(enabled);
			playButton.setEnabled(enabled);
			nextButton.setEnabled(enabled);
			slideList.setEnabled(enabled);		
			refreshButton.setEnabled(enabled);	
		}

		public void refresh(){
			List<Slide> slides = slideshow.getContent();
			if (slides != null){
				List<String> names = Lists.newArrayList();
				for (Slide slide : slides){
					names.add("Slide " + slide.getNumber());
				}
				DefaultComboBoxModel model = new DefaultComboBoxModel(names.toArray());
				slideList.setModel(model);
				slideList.setEnabled(true);
				playButton.setEnabled(true);
				refreshButton.setEnabled(true);
				resizeButton.setEnabled(true);

			}else{
				setEnabled(false);
			}
		}
	}

	void onSlidesLoaded(List<Slide> slides){
		Context context = new Context();
		if (slideshow != null){
			slideshow.quit();
		}
		slideshow = new DefaultSlideShowController(context, slides);
		slideshow.addListener(this);
		slideshow.pause();
		slideshow.start();

		slideshow.jumpTo(0);
		controlBar.refresh();
	}

	private void initHotKeys() {
		GlobalHotkeyManager hotkeys = new GlobalHotkeyManager(){
			@Override
			public void nativeKeyPressed(NativeKeyEvent e) {
				if (isAltPressed(e) && isCtrlPressed(e)){
					if (e.getKeyCode() == KeyEvent.VK_RIGHT){
						invokeNext();
					}else if (e.getKeyCode() == KeyEvent.VK_LEFT){			
						invokePrevious();
					}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
						//						quit();
					}else if (e.getKeyCode() == KeyEvent.VK_P){					 
						togglePlay();
					}else if (e.getKeyCode() == KeyEvent.VK_R){
						invokeRefresh();
					}
				}				
			}			
		};
		hotkeys.start();
	}

	public void setSlide(Slide slide){
		currentSlide = checkNotNull(slide);		

		canvas.getLayer().removeAllChildren();

		PNode pSlide = new PSlide(slide);
		// scale to fit the display area
		double xScale = 1.0 * canvas.getWidth()/ pSlide.getWidth();
		double yScale = 1.0 * canvas.getHeight() / pSlide.getHeight();
		pSlide.scale(Math.min(xScale,yScale));

		canvas.getLayer().addChild(pSlide);

		if (statusRectangle == null){
			statusRectangle = PPath.createRectangle(0,0,canvas.getWidth(), canvas.getHeight());
		}
		statusRectangle.setStrokePaint(null);
		statusRectangle.setPaint(null);		
		statusRectangle.setStroke(new BasicStroke(10));
		statusRectangle.setTransparency(0.5f);

		canvas.getLayer().addChild(statusRectangle);

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){			
				canvas.repaint();	
				previousButton.setEnabled(slideshow.hasPrevious());
				nextButton.setEnabled(slideshow.hasNext());
				
				int newIndex = currentSlide.getNumber()-1;
				if (newIndex != slideList.getSelectedIndex())
					slideList.setSelectedIndex(newIndex);
//				slideList.setS
			}			
		});
	}

	@Override
	public void slideExecuted(Slide slide) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){			
				playButton.refresh();
				statusRectangle.setStrokePaint(Color.green);
				statusRectangle.repaint();	
			}			
		});
	}

	@Override
	public void slideFinished(Slide slide) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){			
				playButton.refresh();
				statusRectangle.setStrokePaint(null);
				statusRectangle.repaint();			
			}			
		});
	}

	@Override
	public void slideFailed(Slide slide) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){			
				playButton.refresh();
				statusRectangle.setStrokePaint(Color.red);
				statusRectangle.repaint();
			}			
		});
	}

	@Override
	public void slideSelected(Slide slide) {
		setSlide(slide);		
	}

}