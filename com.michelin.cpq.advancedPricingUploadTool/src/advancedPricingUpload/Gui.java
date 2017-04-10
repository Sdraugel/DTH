package advancedPricingUpload;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class builds the ui window.
 * 
 * @author n556488 - Steven Draugel
 *
 */
@SuppressWarnings("serial")
public class Gui extends JPanel implements ActionListener {
	
	private JButton openButton; //the browse/open file button
	private JButton uploadButton; //the upload button
	private JProgressBar progressBar; //the progress bar
	private JTextArea log; //the logging area
	private JFileChooser fc; //provides a simple mechanism for the user to choose a file
	private File file; //the selected file
	private JTextArea selectedFile = new JTextArea(); //String label for the currently selected file
	private Border fileTitle = null; //the title for the selected file area
	private static JFrame frame = new JFrame("Advanced Pricing Data Upload Tool, Version 1.0, 8-4-2016"); //the window that contains the program

	/**
	 * Builds the gui window and attaches all of the displayed objects to it
	 */
	public Gui() {
		super(new BorderLayout());
		
		java.net.URL openURL = Gui.class.getResource("open.png"); //retrieves the picture for the open button so that the path can stay dynamic
		java.net.URL uploadURL = Gui.class.getResource("upload.png"); //retrieves the picture for the upload button so that the path can stay dynamic
		
		ImageIcon openIcon = new ImageIcon(openURL);
		ImageIcon uploadIcon = new ImageIcon(uploadURL);
		
		//log for the action listeners
		log = new JTextArea(50,50);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		log.setLineWrap(true);
		JScrollPane logScrollPane = new JScrollPane(log);
		
		//file chooser object
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Office 2007", "xlsx");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
		//creates the panel window for the buttons
		JPanel panel1 = new JPanel();	
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));
		JTextArea instructions = new JTextArea();
		JTextArea selectDialog = new JTextArea();
		JTextArea uploadDialog = new JTextArea();
		
		Color c = new Color(0,0,0,0);
		
        //open button
		openButton = new JButton("Browse", openIcon);
		openButton.addActionListener(this);
		
		//upload button
		uploadButton = new JButton("Upload", uploadIcon);
		uploadButton.addActionListener(this);
		
		//progress bar
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setStringPainted(true);
		
		//the instructions panel
		instructions.setText(" This system is used to upload files to the ADH_APS database. When a file is uploaded all data, in the associated table, will be deleted." + 
							"\n" + "Files must be in the .xlsx format" + "\n");
		instructions.setEditable(false);
		instructions.setBackground(c);
		instructions.setLineWrap(true);
		Border instructionsTitle = null;
		instructionsTitle = BorderFactory.createTitledBorder(instructionsTitle, "Instructions: ", TitledBorder.LEFT, TitledBorder.TOP);
		instructions.setBorder(instructionsTitle);
		
		//the step one panel
		Border stepOneTitle = null;
		stepOneTitle = BorderFactory.createTitledBorder(stepOneTitle, "Step 1:", TitledBorder.LEFT, TitledBorder.TOP);
		selectDialog.setText(" Select a file to upload ");
		selectDialog.setEditable(false);	
		selectDialog.setBackground(c);
		selectDialog.setBorder(stepOneTitle);

		//the selected file panel
		fileTitle = BorderFactory.createTitledBorder(fileTitle, "Selected File:", TitledBorder.LEFT, TitledBorder.TOP);
		selectedFile.setText("No file selected");
		selectedFile.setBorder(fileTitle);
		selectedFile.setEditable(false);
		selectedFile.setBackground(c);
		selectedFile.setEditable(false);
		selectedFile.setLineWrap(true);
		
		//the step two panel
		Border stepTwoTitle = null;
		stepTwoTitle = BorderFactory.createTitledBorder(stepTwoTitle, "Step 2:", TitledBorder.LEFT, TitledBorder.TOP);
		uploadDialog.setText(" Upload the file ");
		uploadDialog.setEditable(false);
		uploadDialog.setBackground(c);
		uploadDialog.setBorder(stepTwoTitle);
		
		//attaching all the ui objects to the main panel that is attached to the frame
		panel1.add(instructions);
		panel1.add(selectDialog);
		panel1.add(openButton);
		panel1.add(selectedFile);
		panel1.add(uploadDialog);
		panel1.add(uploadButton);
		panel1.add(progressBar);		
		
		//attach the main ui panel and log panel to the main frame
		add(panel1, BorderLayout.BEFORE_FIRST_LINE);
		add(logScrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Handles what happens based on event listeners. The user can either select a file
	 * or upload one. Also handles what happens if no file is selected and the user attempts
	 * to start an upload.
	 * 
	 * @param e - the action event
	 */
	public void actionPerformed(ActionEvent e) {
		
		//handles the open button action
		if(e.getSource() == openButton){
			int returnVal =  fc.showOpenDialog(Gui.this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				this.file =  fc.getSelectedFile();
				selectedFile.setText(this.file.toString());
				selectedFile.setBorder(fileTitle);
				frame.setSize(450, 701);
				selectedFile.update(getGraphics());
				frame.setSize(450, 700);
				selectedFile.update(getGraphics());
				return ;

			}  
			else {
                log.append("Open command cancelled by user." + "\n");
            }
            log.setCaretPosition(log.getDocument().getLength());
        } 
		
		else if (e.getSource() == uploadButton) {
			if (file == null) {
				log.append("No file selected. Please select a file to upload." + "\n");
				return;
			}
			else {
				Object[] options = {"Yes", "No"};
				int n = JOptionPane.showOptionDialog(frame,  
						"Are you sure you want to upload new data? All previous data will be deleted", 
						"WARNING", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE,  
						null, options, 
						options[1]);

				if (n == 0) {
					Upload upload = new Upload(this.file, log, progressBar);
				}
				
				
			}
        }
		return;
	}
	
	public static void showDialog(Component c) {
		JOptionPane.showMessageDialog(c, "Closing window!");
	}
	
	/**
	 * Instantiates the frame object that will hold all of the UI objects.
	 */
	private static void createAndShowGUI() {
        //Create and set up the window.
        
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        CloseListener cl = new CloseListener ("Are you sure you want to exit the application",
        	    "Exit Application");
        
        frame.addWindowListener(cl);
        
        //Add content to the window.
        frame.add(new Gui());
 
        //Display the window.
        frame.pack();
        frame.setSize(450, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
		return;
    }

	/**
	 * Main method
	 * 
	 * @param args - passed in parameters (usually none)
	 */
	public static void main(String[] args) {
		System.gc();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}
