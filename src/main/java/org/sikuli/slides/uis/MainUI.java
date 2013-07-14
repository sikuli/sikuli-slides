/**
Khalid
*/
package org.sikuli.slides.uis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import org.sikuli.slides.Main;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.MyFileFilter;
import org.sikuli.slides.utils.Utils;
import org.sikuli.slides.utils.logging.TextAreaAppender;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;



public class MainUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = -839784598366545263L;
    private JButton btn_open;
    private JComboBox runningModeList;
    private JLabel statusLabel;
    public JTextArea logArea;
    private JPanel bottomPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, helpMenu;
    private JMenuItem openFileMenuItem, quitMenuItem, editPrefsMenuItem, helpMenuItem;
    

    public MainUI(){
        super("sikuli-slides");
    }
    
    private void createAndShowUI() {
    	// create the menu
    	menuBar = new JMenuBar();
    	
    	fileMenu = new JMenu("File");
    	fileMenu.setMnemonic(KeyEvent.VK_F); // Use keyboard shortcut: Alt + F
    	fileMenu.getAccessibleContext().setAccessibleDescription("File menu that has the main menu items.");
    	menuBar.add(fileMenu);
    	
    	openFileMenuItem = new JMenuItem("Open File...");
    	openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    	openFileMenuItem.getAccessibleContext().setAccessibleDescription("Open File");
    	openFileMenuItem.addActionListener(this);
    	fileMenu.add(openFileMenuItem);
    	
    	quitMenuItem = new JMenuItem("Quit Sikuli-Slides");
    	quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    	quitMenuItem.getAccessibleContext().setAccessibleDescription("Quit Sikuli-Slides");
    	quitMenuItem.addActionListener(this);
    	fileMenu.add(quitMenuItem);
    	
    	editMenu = new JMenu("Edit");
    	editMenu.setMnemonic(KeyEvent.VK_E); // Use keyboard shortcut: Alt + E
    	editMenu.getAccessibleContext().setAccessibleDescription("Edit menu that has the user's preferences and settings");
    	menuBar.add(editMenu);
    	
    	editPrefsMenuItem = new JMenuItem("Edit preferences...");
    	editPrefsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_E, ActionEvent.CTRL_MASK));
    	editPrefsMenuItem.getAccessibleContext().setAccessibleDescription("Edit preferences");
    	editPrefsMenuItem.addActionListener(this);
    	editMenu.add(editPrefsMenuItem);
    	
    	helpMenu = new JMenu("Help");
    	helpMenu.setMnemonic(KeyEvent.VK_H); // Use keyboard shortcut: Alt + H
    	helpMenu.getAccessibleContext().setAccessibleDescription("Help menu");
    	menuBar.add(helpMenu);
    	
    	helpMenuItem = new JMenuItem("Sikuli-Slides Help");
    	helpMenuItem.getAccessibleContext().setAccessibleDescription("SikuliSlides Help");
    	helpMenuItem.addActionListener(this);
    	helpMenu.add(helpMenuItem);
    	
    	setJMenuBar(menuBar);
    	
    	JToolBar horizontal_toolbar=new JToolBar();
        horizontal_toolbar.setFloatable(false); // to make a tool bar immovable
        horizontal_toolbar.setMargin(new Insets(2, 5, 5, 5));
        horizontal_toolbar.setBackground(Color.decode("#e5e5e5"));
        horizontal_toolbar.setFocusable(false);
        add(horizontal_toolbar, BorderLayout.NORTH);
        
        ImageIcon open_file_icon=new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"powerpoint-icon.png"));
        btn_open= new JButton("Open",open_file_icon);
        btn_open.setToolTipText("Open PowerPoint file (*.pptx)");
        btn_open.addActionListener(this);
        btn_open.setFocusable(false);
        btn_open.setMaximumSize(btn_open.getPreferredSize());
        horizontal_toolbar.add(btn_open);
        
        // running modes
        JLabel runningModeLabel=new JLabel("  Running Mode:");
        horizontal_toolbar.add(runningModeLabel);
        
        String[]validModes={"Action","Help","Tutorial"};
        runningModeList=new JComboBox(validModes);
        
        runningModeList.addActionListener(this);
        runningModeList.setMaximumSize(runningModeList.getPreferredSize());
        runningModeList.addActionListener(this);
        horizontal_toolbar.add(runningModeList);
        
        // output area
        bottomPanel=new JPanel(new BorderLayout());
        
        logArea=new JTextArea(5,30);
        DefaultCaret caret = (DefaultCaret)logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        bottomPanel.add(scrollPane,BorderLayout.NORTH);
        
        // status label
        statusLabel = new JLabel("");
        statusLabel.setPreferredSize(new Dimension(-1, 22));
        statusLabel.setBorder(LineBorder.createGrayLineBorder());
        bottomPanel.add(statusLabel,BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);

        setFocusable(true);
        
        setTitle("Sikuli-Slides");
        setSize(600, 200);
        //pack();
        setResizable(false);
        setLocationRelativeTo(null); //center the window on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		  // open .pptx file
        if (e.getSource() == btn_open || e.getSource() == openFileMenuItem) {
                // open the .pptx file
                File file=openFile();
                runSikuli(file);
        }
        else if(e.getSource() == runningModeList){
        	if(runningModeList.getSelectedIndex()==0){
        		Constants.ACTION_MODE=true;
        		Constants.HELP_MODE=false;
        		Constants.TUTORIAL_MODE=false;
        	}
        	else if(runningModeList.getSelectedIndex()==1){
        		Constants.ACTION_MODE=false;
        		Constants.HELP_MODE=true;
        		Constants.TUTORIAL_MODE=false;
        	}
        	else if(runningModeList.getSelectedIndex()==2){
        		Constants.ACTION_MODE=false;
        		Constants.HELP_MODE=false;
        		Constants.TUTORIAL_MODE=true;
        	}
        }
        else if(e.getSource() == editPrefsMenuItem){
        	// open preferences editor UI
        	PreferencesEditorUI.showPreferencesEditorUI();
        }
        else if(e.getSource() == helpMenuItem){
        	// open sikuli-slides help
        	Utils.openURLInBrowser("http://sikuli.org");
        }
        else if(e.getSource() == quitMenuItem){
        	System.exit(0);
        }
	}
	
	private File openFile(){
		// clear log area content
		if(logArea != null){
			logArea.setText("");
		}
        // create file chooser object
        JFileChooser fc = new JFileChooser();
        MyFileFilter myFileFilter=new MyFileFilter();
        fc.addChoosableFileFilter(myFileFilter);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(myFileFilter);
        
        // show the open file dialog
        int returnVal = fc.showOpenDialog(MainUI.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
                return fc.getSelectedFile();
        }
        else return null;
	}

	
	private void runSikuli(File file){		
		Main main=new Main();
		if(file!=null){
			statusLabel.setText(file.getAbsolutePath());
			// Minimize the running JFrame window
			setState(JFrame.ICONIFIED);
			main.doSikuliPowerPoint(file);
		}
	}
	
	private void setupLogger(){
		if(logArea != null){
    		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    		// Create a layout to format log messages
    		PatternLayout patternLayout = new PatternLayout();
    		patternLayout.setPattern("%msg%n");
    		patternLayout.setContext(loggerContext);
    		patternLayout.start();
    		// Create custom appender to log into the GUI
    		TextAreaAppender appender = new TextAreaAppender();
    		appender.setjTextArea(logArea);
    		appender.setPatternLayout(patternLayout);
    		appender.setContext(loggerContext);
    		appender.start();
    		// Attach the appender to the logger
    		Logger logger = (Logger) loggerContext.getLogger("org.sikuli.slides");
    		logger.addAppender(appender);
    		logger.setAdditive(false);
    	}
	}
	
	public static void runGuiTool(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                    MainUI mainUI=new MainUI();
                    mainUI.createAndShowUI();
                    mainUI.setupLogger();
            }
    });
	}

}
