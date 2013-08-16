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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import org.sikuli.slides.Main;
import org.sikuli.slides.utils.logging.TextAreaAppender;
import org.sikuli.slides.v1.core.RunOptions;
import org.sikuli.slides.v1.utils.Constants;
import org.sikuli.slides.v1.utils.MyFileFilter;
import org.sikuli.slides.v1.utils.Utils;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;



public class MainUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = -839784598366545263L;
    private JButton btn_open_file, btn_open_url, btn_run;
    private JComboBox runningModeList;
    private JLabel statusLabel;
    public JTextArea logArea;
    private JMenuItem openFileMenuItem, openURLMenuItem,quitMenuItem, 
    	editPrefsMenuItem, runInAutomationModeMenuItem, runInHelpModeMenuItem, 
    	runInTutorialModeMenuItem, helpMenuItem, aboutMenuItem;
    private ImageIcon open_url_icon;
    private File pptxFile = null;
    private static final String NEW_LINE = System.getProperty("line.separator");
    
    public MainUI(){
        super("sikuli-slides");
    }
    
    private void createAndShowUI() {
    	// create the menu
    	JMenuBar menuBar = new JMenuBar();
    	
    	JMenu fileMenu = new JMenu("File");
    	fileMenu.setMnemonic(KeyEvent.VK_F); // Use keyboard shortcut: Alt + F
    	fileMenu.getAccessibleContext().setAccessibleDescription("File menu that has the main menu items.");
    	menuBar.add(fileMenu);
    	
    	openFileMenuItem = new JMenuItem("Open File...");
    	openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    	openFileMenuItem.getAccessibleContext().setAccessibleDescription("Open File");
    	openFileMenuItem.addActionListener(this);
    	fileMenu.add(openFileMenuItem);
    	
    	openURLMenuItem = new JMenuItem("Open Remote Presentation...");
    	openURLMenuItem.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_U, ActionEvent.CTRL_MASK));
    	openURLMenuItem.getAccessibleContext().setAccessibleDescription("Open URL");
    	openURLMenuItem.addActionListener(this);
    	fileMenu.add(openURLMenuItem);
    	
    	quitMenuItem = new JMenuItem("Quit Sikuli-Slides");
    	quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    	quitMenuItem.getAccessibleContext().setAccessibleDescription("Quit Sikuli-Slides");
    	quitMenuItem.addActionListener(this);
    	fileMenu.add(quitMenuItem);
    	
    	JMenu editMenu = new JMenu("Edit");
    	editMenu.setMnemonic(KeyEvent.VK_E); // Use keyboard shortcut: Alt + E
    	editMenu.getAccessibleContext().setAccessibleDescription("Edit menu that has the user's preferences and settings");
    	menuBar.add(editMenu);
    	
    	editPrefsMenuItem = new JMenuItem("Edit preferences...");
    	editPrefsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_E, ActionEvent.CTRL_MASK));
    	editPrefsMenuItem.getAccessibleContext().setAccessibleDescription("Edit preferences");
    	editPrefsMenuItem.addActionListener(this);
    	editMenu.add(editPrefsMenuItem);
    	
    	JMenu runMenuItem = new JMenu("Run");
    	runMenuItem.setMnemonic(KeyEvent.VK_R); // Use keyboard shortcut: Alt + R
    	runMenuItem.getAccessibleContext().setAccessibleDescription("Run menu");
    	menuBar.add(runMenuItem);
    	
    	runInAutomationModeMenuItem = new JMenuItem("Run in automation mode");
    	runInAutomationModeMenuItem.getAccessibleContext().setAccessibleDescription("Run in automation mode");
    	runInAutomationModeMenuItem.addActionListener(this);
    	runInAutomationModeMenuItem.setEnabled(false);
    	runMenuItem.add(runInAutomationModeMenuItem);
    	
    	runInHelpModeMenuItem = new JMenuItem("Run in help mode");
    	runInHelpModeMenuItem.getAccessibleContext().setAccessibleDescription("Run in help mode");
    	runInHelpModeMenuItem.addActionListener(this);
    	runInHelpModeMenuItem.setEnabled(false);
    	runMenuItem.add(runInHelpModeMenuItem);
    	
    	runInTutorialModeMenuItem = new JMenuItem("Run in tutorial mode");
    	runInTutorialModeMenuItem.getAccessibleContext().setAccessibleDescription("Run in tutorial mode");
    	runInTutorialModeMenuItem.addActionListener(this);
    	runInTutorialModeMenuItem.setEnabled(false);
    	runMenuItem.add(runInTutorialModeMenuItem);
    	
    	JMenu helpMenu = new JMenu("Help");
    	helpMenu.setMnemonic(KeyEvent.VK_H); // Use keyboard shortcut: Alt + H
    	helpMenu.getAccessibleContext().setAccessibleDescription("Help menu");
    	menuBar.add(helpMenu);
    	
    	helpMenuItem = new JMenuItem("Sikuli-Slides Help");
    	helpMenuItem.getAccessibleContext().setAccessibleDescription("SikuliSlides Help");
    	helpMenuItem.addActionListener(this);
    	helpMenu.add(helpMenuItem);
    	
    	aboutMenuItem = new JMenuItem("About Sikuli-Slides");
    	aboutMenuItem.getAccessibleContext().setAccessibleDescription("About Sikuli Slides");
    	aboutMenuItem.addActionListener(this);
    	helpMenu.add(aboutMenuItem);
    	
    	setJMenuBar(menuBar);
    	
    	JToolBar horizontal_toolbar=new JToolBar();
        horizontal_toolbar.setFloatable(false); // to make a tool bar immovable
        horizontal_toolbar.setMargin(new Insets(2, 5, 5, 5));
        horizontal_toolbar.setBackground(Color.decode("#e5e5e5"));
        horizontal_toolbar.setFocusable(false);
        add(horizontal_toolbar, BorderLayout.NORTH);
        
        ImageIcon open_file_icon = new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"powerpoint-icon.png"));
        btn_open_file = new JButton("Open File",open_file_icon);
        btn_open_file.setToolTipText("Open PowerPoint file (*.pptx) stored locally");
        btn_open_file.addActionListener(this);
        btn_open_file.setFocusable(false);
        btn_open_file.setMaximumSize(btn_open_file.getPreferredSize());
        horizontal_toolbar.add(btn_open_file);
        
        open_url_icon = new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"file-cloud-icon.png"));
        btn_open_url = new JButton("Open URL",open_url_icon);
        btn_open_url.setToolTipText("Open URL to a presentation file (*.pptx) stored remotely");
        btn_open_url.addActionListener(this);
        btn_open_url.setFocusable(false);
        btn_open_url.setMaximumSize(btn_open_url.getPreferredSize());
        horizontal_toolbar.addSeparator();
        horizontal_toolbar.add(btn_open_url);
        
        // running modes
        JLabel runningModeLabel=new JLabel("  Running Mode:");
        
        horizontal_toolbar.addSeparator();
        horizontal_toolbar.add(runningModeLabel);
        
        String[]validModes={"Automation","Help","Tutorial"};
        runningModeList=new JComboBox(validModes);
        
        runningModeList.addActionListener(this);
        runningModeList.setMaximumSize(runningModeList.getPreferredSize());
        runningModeList.addActionListener(this);
        horizontal_toolbar.add(runningModeList);
        
        ImageIcon btn_run_icon = new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"icon_32.png"));
        btn_run = new JButton("Run",btn_run_icon);
        btn_run.setToolTipText("Run Sikuli-Slides");
        btn_run.addActionListener(this);
        btn_run.setFocusable(false);
        btn_run.setMaximumSize(btn_open_url.getPreferredSize());
        btn_run.setEnabled(false);
        horizontal_toolbar.addSeparator();
        horizontal_toolbar.add(btn_run);
        
        // output area
        JPanel bottomPanel=new JPanel(new BorderLayout());
        
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
        if (e.getSource() == btn_open_file || e.getSource() == openFileMenuItem) {
                // open the .pptx file
                this.pptxFile = openFile();
    			if(this.pptxFile != null){
    				enableRunComponents();
    				statusLabel.setText(this.pptxFile.getName());
    			}
    			else{
    				disableRunComponents();
    			}
        }
        else if (e.getSource() == btn_open_url || e.getSource() == openURLMenuItem) {
        	showURLDialog();
        }
        else if(e.getSource() == btn_run) {
        	if(this.pptxFile != null){
        		logArea.setText("");
        		runSikuli(this.pptxFile);
        	}
        }
        else if(e.getSource() == runInAutomationModeMenuItem){
        	if(this.pptxFile != null){
        		setRunningMode('a');
        	}
        }
        else if(e.getSource() == runInHelpModeMenuItem){
        	if(this.pptxFile != null){
        		setRunningMode('h');
        	}
        }
        else if(e.getSource() == runInTutorialModeMenuItem){
        	if(this.pptxFile != null){
        		setRunningMode('t');
        		runSikuli(this.pptxFile);
        	}
        }
        else if(e.getSource() == runningModeList){
        	if(runningModeList.getSelectedIndex()==0){
        		setRunningMode('a');
        	}
        	else if(runningModeList.getSelectedIndex()==1){
        		setRunningMode('h');
        	}
        	else if(runningModeList.getSelectedIndex()==2){
        		setRunningMode('t');
        	}
        }
        else if(e.getSource() == editPrefsMenuItem){
        	// open preferences editor UI
        	PreferencesEditorUI.showPreferencesEditorUI();
        }
        else if(e.getSource() == helpMenuItem){
        	// open sikuli-slides help
        	Utils.openURLInBrowser("http://slides.sikuli.org");
        }
        else if(e.getSource() == aboutMenuItem){
        	showAboutDialog();
        }
        else if(e.getSource() == quitMenuItem){
        	System.exit(0);
        }
	}

	private void showURLDialog() {
		String downloadFileURL= (String) JOptionPane.showInputDialog(this,
				"Remote presentation file download link:", "Open Remote Presentation", 
				JOptionPane.YES_NO_OPTION, open_url_icon, null, null);
		if(downloadFileURL != null){
			this.pptxFile = Utils.downloadFile(downloadFileURL);
			if(this.pptxFile != null){
				enableRunComponents();
				this.statusLabel.setText(downloadFileURL);
			}
			else{
				disableRunComponents();
			}
		}
	}
	
	public void showAboutDialog(){
		String message = "Sikuli -slides" + NEW_LINE +
				"Version: " + MainUI.class.getPackage().getImplementationVersion() + NEW_LINE +
				"\u00A9 " + "Sikuli Lab 2013 | Department of Computer Science | University of Colorado Boulder";
		ImageIcon about_icon = new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"icon_128.png"));
		JOptionPane.showMessageDialog(this, message, "About Sikuli-Slides",
				JOptionPane.OK_OPTION, about_icon);
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
	private void enableRunComponents() {
		this.btn_run.setEnabled(true);
		this.runInAutomationModeMenuItem.setEnabled(true);
		this.runInHelpModeMenuItem.setEnabled(true);
		this.runInTutorialModeMenuItem.setEnabled(true);
	}
	
	private void disableRunComponents() {
		this.btn_run.setEnabled(false);
		this.runInAutomationModeMenuItem.setEnabled(false);
		this.runInHelpModeMenuItem.setEnabled(false);
		this.runInTutorialModeMenuItem.setEnabled(false);
		this.statusLabel.setText("");
	}
	/**
	 * Sets the running mode
	 * @param mode the first character of the mode to be run. 'a' = automation mode, 
	 * 'h' = help mode, and 't' = tutorial mode.
	 */
	private void setRunningMode(char mode){
		if(mode == 'a'){
    		Constants.AUTOMATION_MODE = true;
    		Constants.HELP_MODE = false;
    		Constants.TUTORIAL_MODE = false;
		}
		else if(mode == 'h'){
			Constants.AUTOMATION_MODE = false;
			Constants.HELP_MODE = true;
			Constants.TUTORIAL_MODE = false;
		}
		else if(mode == 't'){
			Constants.AUTOMATION_MODE = false;
			Constants.HELP_MODE = false;
			Constants.TUTORIAL_MODE = true;
		}
	}
	
	private void runSikuli(File file){		
		Main main=new Main();
		if(file!=null){
			// Minimize the running JFrame window
			setState(JFrame.ICONIFIED);
			RunOptions options = new RunOptions();
			options.setSourceName(file.getAbsolutePath());
			main.doSikuliPowerPoint(options);
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
