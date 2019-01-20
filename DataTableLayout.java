import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DataTableLayout {

	private JFrame frame;
	private JPanel topPanel;
	private JButton exitButton,defineFilterButton,applyFilterButton;
	private JTextField filterTextField;
	private JMenuItem fileMenuItem;
	private JTextArea dataDisplayTextArea;
	private JScrollPane scrollPane;

	private File fileReference;
	private DataFilter dataFilter;
	private List<DataBundle> currentDataList;
	private String selected = "Cash Only";
	
	
	public DataTableLayout() {
		setUpFrameSpecs();
		setUpTopPanel();
		setUpMiddlePanel();
		
		this.frame.setVisible(true);
	}
	
	/**
	 * Sets up all frame specifications
	 */
	private void setUpFrameSpecs() {
		this.frame = new JFrame("Data Table Layout");
		this.frame.setSize(new Dimension(800,700));
		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setLayout(new BorderLayout());
		
	}
	
	/**
	 * Sets up the panel displayed in the top of the screen 
	 * and all elements contained within it.
	 * When the setup is done, it is added to the frame.
	 */
	private void setUpTopPanel() {
		//Initialize the panel itself
		this.topPanel = new JPanel();
		this.topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//Set up the exit button
		this.exitButton = new JButton("Exit");
		this.exitButton.setFocusable(false);
		this.exitButton.addActionListener(new ButtonListener());
		this.topPanel.add(exitButton);
		
		//Set up the text field for displaying current filters
		this.filterTextField = new JTextField(" No current filters applied ");
		this.filterTextField.setEditable(false);
		this.topPanel.add(filterTextField);
		
		
		//Set up the menu item for loading up the file
		this.fileMenuItem = new JMenuItem("Load File");
		this.fileMenuItem.addActionListener(new LoadFileListener());
		this.topPanel.add(fileMenuItem);
		
		//Set up the button assigned for defining filters
		this.defineFilterButton = new JButton("Define filter");
		this.defineFilterButton.addActionListener(new ButtonListener());
		this.defineFilterButton.setFocusable(false);
		this.topPanel.add(defineFilterButton);
		
		//Set up the button assigned for applying filters to data
		this.applyFilterButton = new JButton("Apply Filter");
		this.applyFilterButton.addActionListener(new ButtonListener());
		this.topPanel.add(applyFilterButton);
		
		this.frame.add(topPanel,BorderLayout.NORTH);
		
	}
	
	/**
	 * Sets up the middle panel and all components within it
	 * After the setup is done, adds it to the frame
	 */
	private void setUpMiddlePanel() {		
		
		this.dataDisplayTextArea = new JTextArea();
		this.dataDisplayTextArea.setEditable(false);
		
		this.scrollPane = new JScrollPane(dataDisplayTextArea);
		

		this.frame.add(scrollPane);
	}
	
	class LoadFileListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fileChooser = new JFileChooser();
			
			if(fileChooser.showOpenDialog(frame) == fileChooser.APPROVE_OPTION) {
				fileReference = fileChooser.getSelectedFile();
				
				dataFilter = new DataFilter(fileReference);
				
				Thread dataThread = new Thread(dataFilter);
				dataThread.start();
				try {
					dataThread.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} 
				
				
				currentDataList = dataFilter.getList();
				displayCurrentList();
			}
			
		}
		
	}
	
	/**
	 * Displays all of the elements contained in the current data list
	 */
	private void displayCurrentList() {
		StringBuilder listElements = new StringBuilder();
		
		listElements.append("Driver ID\t\t\t"
							+"Distance\t"
							+"Payment\t"
							+"Amount\n");
		for(DataBundle eachBundle : this.currentDataList) {
			listElements.append(eachBundle.toString() + "\n");
		}
		
		this.dataDisplayTextArea.setText(listElements.toString());
		this.dataDisplayTextArea.setCaretPosition(0);
	}
	class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton testButton = (JButton)e.getSource();
			
			if(testButton.equals(exitButton)) {
				System.out.println("A user requested exit performed.");
				System.exit(0);
				
			}else if(testButton.equals(defineFilterButton)) {
				Object[] options = {"Cash Only","Card Only","Distance > 0.6"};
				
				selected = (String)JOptionPane.showInputDialog(
						frame,
						"Select a condition for filtering",
						"Filter Options",
						JOptionPane.PLAIN_MESSAGE,
						null,
						options,
						"Cash Only");
				
				
			}else {
				if(currentDataList != null) {
					
					switch(selected) {
						case "Cash Only":
							applyCashOnlyFilter();
							break;
							
						case "Card Only":
							applyCardOnlyFilter();
							break;
						default:
							applyDistanceFilter();
					}
					displayCurrentList();
					
				}
			}
			
			testButton.setFocusable(false);
			
		}
		
	}
	/**
	 * Applies a filter and displays the records containing only
	 * "Cash" type of payment.
	 */
	public void applyCashOnlyFilter(){
		this.currentDataList = this.currentDataList
									.stream()
									.filter(record -> record.hasCSHpayment())
									.collect(Collectors.toList());
		
	}
	
	/**
	 * Applies a filter and displays the records containing only
	 * "Card" type of payment.
	 */
	public void applyCardOnlyFilter(){
		this.currentDataList = this.currentDataList
									.stream()
									.filter(record -> !record.hasCSHpayment())
									.collect(Collectors.toList());
	}
	
	/**
	 * Applies a filter and displays the records containing only 
	 * the distance greater than 0.6 miles.
	 */
	public void applyDistanceFilter() {
		this.currentDataList = this.currentDataList
								   .stream()
								   .filter(record -> record.hasDistanceGreaterThan())
								   .collect(Collectors.toList());
	}
	public static void main(String[] args) {
		new DataTableLayout();
	}
}
