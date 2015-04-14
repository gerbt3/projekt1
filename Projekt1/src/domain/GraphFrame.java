package domain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import domain.EditorHandler.State;

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

				String s = (String) JOptionPane.showInputDialog(null, "Enter a file name");
				try {
					menuHandler.saveGraph(s);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				File folder = new File("GraphFiles/");
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						System.out.println("File " + listOfFiles[i].getName());
					} else if (listOfFiles[i].isDirectory()) {
						System.out.println("Directory " + listOfFiles[i].getName());
					}
				}
			}
		});

		menuPanel.add(newButton);
		menuPanel.add(saveButton);
		menuPanel.add(openButton);

		add(menuPanel, BorderLayout.NORTH);
	}

	private void constructAttributMenuComponents() {

		JPanel attributeMenuPanel = new JPanel();

		add(attributeMenuPanel, BorderLayout.SOUTH);
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
