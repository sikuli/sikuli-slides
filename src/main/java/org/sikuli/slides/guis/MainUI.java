/**
Khalid
*/
package org.sikuli.slides.guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.MyFileFilter;

public class MainUI extends JFrame implements ActionListener, ChangeListener, KeyListener {
	private static final long serialVersionUID = -839784598366545263L;
    private JButton btn_open;
    private JComboBox runningModeList;
    private JSlider perciseControlSlider;
    private JLabel statusLabel;
    
    
    public MainUI(){
        super("sikuli-slides");
        initUI();
    }
    
    private void initUI() {
    	JToolBar horizontal_toolbar=new JToolBar();
        horizontal_toolbar.setFloatable(false); // to make a tool bar immovable
        horizontal_toolbar.setMargin(new Insets(2, 5, 5, 5));
        horizontal_toolbar.setBackground(Color.decode("#e5e5e5"));
        horizontal_toolbar.setFocusable(false);
        add(horizontal_toolbar, BorderLayout.NORTH);
        
        ImageIcon open_file_icon=new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"powerpoint-icon.png"));
        btn_open= new JButton("Open",open_file_icon);
        btn_open.setToolTipText("Open PowerPoint file (*.pptx");
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
        
        // precise control
        JLabel perciseControlLabel=new JLabel("  Precision Value:");
        horizontal_toolbar.add(perciseControlLabel);
        
        int initPreciseVal=(int)(Constants.MinScore*10);
        perciseControlSlider=new JSlider(JSlider.HORIZONTAL,1,10,initPreciseVal);
        perciseControlSlider.setPaintLabels(true);
        perciseControlSlider.setPaintTicks(true);
        perciseControlSlider.setMajorTickSpacing(1);
        perciseControlSlider.setMinorTickSpacing(1);
        perciseControlSlider.setMaximumSize(perciseControlSlider.getPreferredSize());
        perciseControlSlider.addChangeListener(this);
        //perciseControlSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Precision"));
        horizontal_toolbar.add(perciseControlSlider);
        
        // status label
        statusLabel = new JLabel("");
        statusLabel.setPreferredSize(new Dimension(-1, 22));
        statusLabel.setBorder(LineBorder.createGrayLineBorder());
        add(statusLabel, BorderLayout.SOUTH);

        addKeyListener(this);
        setFocusable(true);
        
        setTitle("Sikuli-Slides");
        pack();
        setResizable(false);
        setLocationRelativeTo(null); //center the window on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_O) && 
				((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            // open the .pptx file
            File file=openFile();
            runSikuli(file);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		  // open .pptx file
        if (e.getSource() == btn_open) {
                // open the .pptx file
                File file=openFile();
                runSikuli(file);
        }
        else if(e.getSource()==runningModeList){
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
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==perciseControlSlider){
			int precision=perciseControlSlider.getValue();
			Constants.MinScore=(double)precision/10;
		}
	}
	
	private File openFile(){
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

	private static void runUI(){
		// take the menu bar off the jframe
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // set the name of the application menu item
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Sikuli-Slides");
        // set the look and feel
        try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        } catch (InstantiationException e) {
                e.printStackTrace();
        } catch (IllegalAccessException e) {
                e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
        }
        
        SwingUtilities.invokeLater(new Runnable() {
                public void run(){
                        MainUI mainUI=new MainUI();
                        mainUI.setVisible(true);
                }
        });
	}
	
	private void runSikuli(File file){
		// Minimize the running JFrame window
		setState(JFrame.ICONIFIED);
		
		Main main=new Main();
		if(file!=null){
			main.doSikuliPowerPoint(file);
		}
	}
	
	public static void runGuiTool(){
		runUI();
	}

}
