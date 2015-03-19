package domain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import domain.ViewController.DrawState;


public class AlgoView extends JPanel {

	public ViewController viewController;
	
	public AlgoView() {
		
		viewController = new ViewController();
		
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
            	viewController.selectClicked();
            }
         });
		
		startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               viewController.startClicked();
            }
         });
		
		backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	viewController.beforeStepClicked();
            }
         });
		
		forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               viewController.nextStepClicked();
            }
         });
		
		stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	viewController.stopClicked();
            }
         });
		
		commitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	viewController.commitClicked();
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
