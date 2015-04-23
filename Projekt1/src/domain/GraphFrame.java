package domain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import domain.EditorHandler.State;
import domain.GraphTool.Attribut;

/*
 * Constructs the frame with the different menus
 * for drawing a graph or animating an algorithm
 */
public class GraphFrame<V, E> extends JFrame {

	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 400;
	private EditorHandler<V, E> handler;
	private MenuHandler menuHandler;
	private String currentGraphName;

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

	//------------------------------------------------------------------------------------//
	// Helper methods for constructing the frame
	//------------------------------------------------------------------------------------//

	/*
	 * Constructs the main menu with options for creating a new graph
	 * or saving and opening graphs
	 */
	private void constructMenuComponents() {

		JPanel menuPanel = new JPanel();

		JButton newButton = new JButton("New");
		JButton saveButton = new JButton("Save");
		JButton saveAsButton = new JButton("Save as");
		JButton openButton = new JButton("Open");
		JButton deleteButton = new JButton("Delete");

		/*
		 * Creates a new undirected or directed graph
		 */
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				chooseGraphOption();
			}
		});

		/*
		 * Saves the current graph
		 * Asks for a name if the graph doesn't have one
		 */
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				if (currentGraphName == null) currentGraphName = askForGraphName();
				saveGraph(currentGraphName);
			}
		});

		/*
		 * Saves the current graph under a new name
		 * Warns before overwriting an existing graph
		 */
		saveAsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				currentGraphName = askForGraphName();
				saveGraph(currentGraphName);	
			}
		});

		/*
		 * Opens a saved graph for the directory GraphFiles
		 * Calls the openGraph method of the menuHandler class
		 */
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

				if (name != null && !name.isEmpty()) {

					try {
						menuHandler.openGraph(name);
						currentGraphName = name;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		/*
		 * Deletes a saved graph
		 * Warns before deleting a graph
		 */
		deleteButton.addActionListener(new ActionListener() {
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

				if (name != null && !name.isEmpty()) {
					
					File file = new File("GraphFiles/" + name + ".ser");
					Path path = file.toPath();
					
					int deleteOption = 0;
					for (int i = 0; i < options.length; i++) {
						if (options[i].equals(name)) {
							deleteOption = JOptionPane.showConfirmDialog(null,
									"Do you want to delete this graph?", 
									"Delete a graph", JOptionPane.YES_NO_OPTION);
						}
					}
					
					if (deleteOption == JOptionPane.YES_OPTION) {
						
						try {
							Files.delete(path);
						} catch (NoSuchFileException x) {
							System.err.format("%s: no such" + " file or directory%n", path);
						} catch (DirectoryNotEmptyException x) {
							System.err.format("%s not empty%n", path);
						} catch (IOException x) {
							// File permission problems are caught here.
							System.err.println(x);
						}
					}
					
				}
			}
		});

		menuPanel.add(newButton);
		menuPanel.add(saveButton);
		menuPanel.add(saveAsButton);
		menuPanel.add(openButton);
		menuPanel.add(deleteButton);

		add(menuPanel, BorderLayout.NORTH);
	}

	/*
	 * Constructs the menu for handling the attributes of vertices and edges
	 * Calls the itemChanged method of the menuHandler class
	 */
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

	/*
	 * Constructs the tabs for either drawing graphs or animating algorithms
	 */
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

	//------------------------------------------------------------------------------------//
	// Helper method for creating a new graph
	//------------------------------------------------------------------------------------//

	/*
	 * Creates a new graph
	 * Gives the choice to create a directed or an undirected graph
	 * Calls the createGraph method of the menuHandler class
	 */
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
				currentGraphName = null;
				return true;
			} else {
				menuHandler.createGraph(true);
				currentGraphName = null;
				return true;
			}
		}
	}

	//------------------------------------------------------------------------------------//
	// Helper methods for saving a graph
	//------------------------------------------------------------------------------------//

	/*
	 * Prompts for a name to save the graph under
	 * If the name already exists, prompts for overwriting name or not
	 */
	private String askForGraphName() {

		String[] options = getFileNames();

		String name = null;
		boolean goodName = false;
		int overwriteOption = 0;

		do {

			overwriteOption = 0;

			name = (String) JOptionPane.showInputDialog(null, "Enter a file name", 
					"Save a graph", JOptionPane.PLAIN_MESSAGE);

			//Leaves the loop if the saving is canceled or the window is closed
			if (name == null) break;

			//Leaves the loop if the name is not empty or does not already exist
			if (name != null && !name.isEmpty()) {

				//Checks if the name already exists
				for (int i = 0; i < options.length; i++) {
					if (options[i].equals(name)) {
						overwriteOption = JOptionPane.showConfirmDialog(null,
								"The name already exists. Overwrite?", 
								"File already exists", JOptionPane.YES_NO_OPTION);
					}
				}
				goodName = true;
				if (overwriteOption == JOptionPane.NO_OPTION) goodName = false;
			}

		} while (!goodName);

		return name;
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

	/*
	 * Saves the graph under the given name
	 * Calls the saveGraph method of the menuHandler class
	 */
	private void saveGraph(String name) {

		if (name != null) {

			try {
				menuHandler.saveGraph(name);
				currentGraphName = name;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
