package domain;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class GraphFrame extends JFrame {
	
	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 400;
	
	public GraphFrame() {
		
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		constructMenuComponents();
		constructTabComponents();
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
	
	private void constructTabComponents() {
		
	    JPanel graphPanel = new EditorView();
        JPanel algoPanel = new AlgoView();
    
        JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
 
        tabpane.addTab("graph", graphPanel);
        tabpane.addTab("algo", algoPanel);
     
        add(tabpane, BorderLayout.CENTER);
	}
}
