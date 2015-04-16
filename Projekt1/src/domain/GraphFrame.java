package domain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import domain.EditorHandler.State;
import domain.GraphTool.Attribut;

public class GraphFrame<V, E> extends JFrame {

	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 400;
	private EditorHandler<V, E> handler;
	private MenuHandler menuHandler;

	public GraphFrame(EditorHandler<V,E> handler, MenuHandler menuHandler, GraphView<V,E> gv) {

		this.handler=handler;
		this.menuHandler = menuHandler;
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		constructMenuComponents();
		constructAttributMenuComponents();
		constructTabComponents();
		this.add(gv, BorderLayout.CENTER);
	}

	public boolean chooseGraphOption(){
		Object[] options = {"Undirected graph", "Directed graph"};
		int choice = JOptionPane.showOptionDialog(null,
				"Choose a graph option:",
				"Graph options",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);
		if(choice==JOptionPane.CLOSED_OPTION){
			return false;
		}
		else{
			if (choice == 0) {
				menuHandler.createGraph(false);
				return true;
			} else {
				menuHandler.createGraph(true);
				return true;
			}
		}
	}

	private void constructMenuComponents() {

		JPanel menuPanel = new JPanel();

		JButton newButton = new JButton("New");
		JButton saveButton = new JButton("Save");
		JButton openButton = new JButton("Open");

		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				chooseGraphOption();
			}
		});

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
					
				String[] options = getFileNames();
			
				String name = null;
				boolean goodName = false;
				int overwrite = 0;
				
				do {
					name = (String) JOptionPane.showInputDialog(null, "Enter a file name", 
							"Save a graph", JOptionPane.PLAIN_MESSAGE);
					
					//Leaves the loop if the saving is canceled or the window is closed
					if (name == null) break;
					
					//Leaves the loop if the name is not empty or does not already exist
					if (name != null && !name.isEmpty()) {
						
						//Checks if the name already exists
						for (int i = 0; i < options.length; i++) {
							if (options[i].equals(name)) {
								overwrite = JOptionPane.showConfirmDialog(null,"The name already exists. Overwrite?", 
										"File already exists", JOptionPane.YES_NO_OPTION);
							}
						}
						if (overwrite == JOptionPane.YES_OPTION) goodName = true;
					}
					
				} while (!goodName);
				
				if (name != null) {
					
					try {
						menuHandler.saveGraph(name);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});

		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				String[] options = getFileNames();

				String name = (String) JOptionPane.showInputDialog(
	                    null,
	                    "Choose a graph",
	                    "Open a graph",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    options,
	                    options[0]);
				
				if (name != null) {
					
					try {
						menuHandler.openGraph(name);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
			}
		});

		menuPanel.add(newButton);
		menuPanel.add(saveButton);
		menuPanel.add(openButton);

		add(menuPanel, BorderLayout.NORTH);
	}
	
	/*
	 * Gets all files from GraphFiles
	 * and puts the filenames without endings in an array
	 */
	private String[] getFileNames() {
		
		File folder = new File("GraphFiles/");
		File[] listOfFiles = folder.listFiles();
		
		String[] options = new String[listOfFiles.length];
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String filename = listOfFiles[i].getName();
				filename = filename.substring(0, filename.length()-4);
				options[i] = filename;
			}
		}
		
		return options;
	}

	private void constructAttributMenuComponents() {

		JPanel attributeMenuPanel = new JPanel();
		JCheckBox name=new JCheckBox("name");
		JCheckBox weight=new JCheckBox("weight");
		JCheckBox visited=new JCheckBox("visited");
		name.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				menuHandler.itemChanged(Attribut.name, name.isSelected());
				
			}
		});
		weight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				menuHandler.itemChanged(Attribut.weight, weight.isSelected());
				
			}
		});
		visited.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				menuHandler.itemChanged(Attribut.visited, visited.isSelected());
				
			}
		});
		
		attributeMenuPanel.add(name);
		attributeMenuPanel.add(weight);
		attributeMenuPanel.add(visited);

		add(attributeMenuPanel, BorderLayout.EAST);
	}

	private void constructTabComponents() {

		JPanel graphPanel = new EditorView<V, E>(handler);
		JPanel algoPanel = new AlgoView();

		JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		tabpane.addTab("graph", graphPanel);
		tabpane.addTab("algo", algoPanel);
		tabpane.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// shows which panel is selected
				int i=tabpane.getSelectedIndex();
				if(i==0){
					handler.setState(State.SELECT);
				}
				else
				{
					handler.setState(State.INACTIVE);
				}
			}

		});
		add(tabpane, BorderLayout.SOUTH);
	}
}
