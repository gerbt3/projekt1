package domain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	public GraphFrame(EditorHandler<V,E> handler, GraphView<V,E> gv) {

		this.handler=handler;
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		constructMenuComponents();
		constructTabComponents();
		this.add(gv, BorderLayout.CENTER);
	}

	public void chooseGraphOption(){
		Object[] options = {"Undirected graph", "Directed graph"};
		int choice = JOptionPane.showOptionDialog(null,
				"Choose a graph option:",
				"Graph options",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);

		if (choice == 0) {
			//Undirected graph
		} else {
			//Directed graph
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

		menuPanel.add(newButton);
		menuPanel.add(saveButton);
		menuPanel.add(openButton);

		add(menuPanel, BorderLayout.NORTH);
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
