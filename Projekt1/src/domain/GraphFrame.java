package domain;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import domain.EditorHandler.State;
import examples.Graph;

public class GraphFrame extends JFrame {
	
	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 400;
	private EditorHandler handler;
	
	public GraphFrame(EditorHandler handler, GraphView gv) {
		
		this.handler=handler;
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		constructMenuComponents();
		constructTabComponents(handler);
		this.add(gv, BorderLayout.CENTER);
	}
	

	private void constructMenuComponents() {
		
		JPanel menuPanel = new JPanel();
		
		JButton newButton = new JButton("New");
		JButton saveButton = new JButton("Save");
		JButton openButton = new JButton("Open");
		
		menuPanel.add(newButton);
		menuPanel.add(saveButton);
		menuPanel.add(openButton);
		
		add(menuPanel, BorderLayout.NORTH);
	}
	
	private void constructTabComponents(EditorHandler handler) {
		
	    JPanel graphPanel = new EditorView(handler);
        JPanel algoPanel = new AlgoView();
    
        JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
 
        tabpane.addTab("graph", graphPanel);
        tabpane.addTab("algo", algoPanel);
        tabpane.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				
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
