package domain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;




public class AlgoView extends JPanel {

	
	
	public AlgoView() {
		
		
		
		setLayout(new BorderLayout());
		constructPanelComponents();
	}
	
	private void constructPanelComponents() {
		
		JButton selectionButton = new JButton("Select");
		JButton startButton = new JButton("Start");
		JButton backButton = new JButton("Back");
		JButton forwardButton = new JButton("Forward");
		JButton stopButton = new JButton("Stop");
		JButton commitButton = new JButton("Commit");
		
		selectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            }
         });
		
		startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               
            }
         });
		
		backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            }
         });
		
		forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               
            }
         });
		
		stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            }
         });
		
		commitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            }
         });
		
		JPanel toolPanel = new JPanel(new FlowLayout());
		
		toolPanel.add(selectionButton);
		toolPanel.add(startButton);
		toolPanel.add(backButton);
		toolPanel.add(forwardButton);
		toolPanel.add(stopButton);
		toolPanel.add(commitButton);
		
		add(toolPanel, BorderLayout.NORTH);
	}
}
