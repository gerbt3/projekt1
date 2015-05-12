package domain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class AlgoView<V,E> extends JPanel {

	private static final long serialVersionUID = 1L;
	private AlgoHandler<V,E> algoHandler;
	private Method currentAlgoMethod;
	private boolean startVertexSelected = false;
	private boolean endVertexSelected = false;

	public AlgoView(AlgoHandler<V,E> algoHandler) {	
		
		this.setBackground(new Color(100,100,100));
		
		this.algoHandler= algoHandler;
		setLayout(new BorderLayout());
		constructPanelComponents();
	}
	
	private void constructPanelComponents() {
			
		JButton startButton = new JButton("Start");
		JButton backButton = new JButton("Back");
		JButton forwardButton = new JButton("Forward");
		JButton stopButton = new JButton("Stop");
		JButton commitButton = new JButton("Commit");
		//If the algorithm needs an start or a end vertex
		JButton startVertexButton = new JButton("Startvertex");
		JButton endVertexButton = new JButton("Endvertex");
		
		
		/*
		 * Makes a list to choose from with all available algorithms
		 */
		Vector<Method> algoMethods = algoHandler.getAnnotatedMethods();
		String[] algoMethodNames = new String[algoMethods.size()];
		for (int i = 0; i < algoMethods.size(); i++) {
			algoMethodNames[i] = algoMethods.get(i).getName();
		}
		JComboBox algoList = new JComboBox(algoMethodNames);
		currentAlgoMethod = algoMethods.get(algoList.getSelectedIndex());
		
		/*
		 * 
		 */
		startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            	//If algorithm needs a startVertex or an endVertex and they're not set,
            	//then prompt for the vertices to be set
            	if (currentAlgoMethod.getAnnotation(Algorithm.class).vertex() && !startVertexSelected) {
            		
            		JOptionPane.showMessageDialog(null, "A start vertex needs to be selected");
            	
            	} else if (currentAlgoMethod.getAnnotation(Algorithm.class).vertex2() && !endVertexSelected) {
            		
            		JOptionPane.showMessageDialog(null, "A end vertex needs to be selected");
            	
            	} else {
            	 	
            		algoHandler.startAlgo(currentAlgoMethod);
            	}
            	
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
            	algoHandler.stopAlgo();
            }
         });
		
		commitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            }
         });
		
		algoList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            	String algoName = (String) algoList.getSelectedItem();
            	int index = 0;
            	for (int i = 0; i < algoMethods.size(); i++) {
        			if (algoName.equals(algoMethods.get(i).getName())) index = i;
        		}
            	currentAlgoMethod = algoMethods.get(index);
            	
            	//Reset all selections and stop the timer
            	algoHandler.stopAlgo();
            	algoHandler.clearStartEndVertex();
            	startVertexSelected = false;
            	endVertexSelected = false;
            	
            	if (currentAlgoMethod.getAnnotation(Algorithm.class).vertex()) {
            		startVertexButton.setVisible(true);
            	} else {
            		startVertexButton.setVisible(false);
            	}
            	
            	if (currentAlgoMethod.getAnnotation(Algorithm.class).vertex2()) {
            		endVertexButton.setVisible(true);
            	} else {
            		endVertexButton.setVisible(false);
            	}
            }
		});
		
		startVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.setStartVertex();
            	startVertexSelected = true;
            }
         });
		
		endVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.setEndVertex();
            	endVertexSelected = true;
            }
         });

		JPanel toolPanel = new JPanel(new FlowLayout());
		
		toolPanel.add(startButton);
		toolPanel.add(backButton);
		toolPanel.add(forwardButton);
		toolPanel.add(stopButton);
		toolPanel.add(commitButton);
		toolPanel.add(algoList);
		//Buttons are only shown if algorithm needs them 
		toolPanel.add(startVertexButton);
		startVertexButton.setVisible(false);
		toolPanel.add(endVertexButton);
		endVertexButton.setVisible(false);
		
		add(toolPanel, BorderLayout.NORTH);
	}
}
