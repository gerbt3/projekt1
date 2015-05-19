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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


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
	
	/*
	 * Constructs a panel with buttons for choosing different algorithm
	 * and animating automatically or manually
	 */
	private void constructPanelComponents() {
		
		ImageIcon playIcon = new ImageIcon("Images/play.png");
		ImageIcon leftIcon = new ImageIcon("Images/left.png");
		ImageIcon rightIcon = new ImageIcon("Images/right.png");
		ImageIcon pauseIcon = new ImageIcon("Images/pause.png");
		ImageIcon stopIcon = new ImageIcon("Images/stop1.png");
		ImageIcon startvertexIcon = new ImageIcon("Images/startvertex.png");
		ImageIcon endvertexIcon = new ImageIcon("Images/endvertex.png");
		
		JButton startButton = new JButton(playIcon);
		JButton pauseButton = new JButton(pauseIcon);
		JButton backButton = new JButton(leftIcon);
		JButton forwardButton = new JButton(rightIcon);
		JButton stopButton = new JButton(stopIcon);
		JButton commitButton = new JButton("Commit");
		//If the algorithm needs an start or a end vertex
		JButton startVertexButton = new JButton(startvertexIcon);
		JButton endVertexButton = new JButton(endvertexIcon);
		
		//Slider for controlling the tempo of the animation of an algorithm
		JSlider slider = new JSlider(100, 10000);
		slider.setValue(1000);
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				algoHandler.setTimerTime(slider.getValue());
			}
		});
		
		FlowLayout fl = new FlowLayout();
		JPanel p = new JPanel(fl);
		
		ImageIcon timeIcon = new ImageIcon("Images/time.png");
		
		p.add(new JLabel(timeIcon));
		p.add(slider);
	
		add(p, BorderLayout.SOUTH);
		
		//Makes a list to choose from with all available algorithms
		Vector<Method> algoMethods = algoHandler.getAnnotatedMethods();
		String[] algoMethodNames = new String[algoMethods.size()];
		for (int i = 0; i < algoMethods.size(); i++) {
			algoMethodNames[i] = algoMethods.get(i).getName();
		}
		JComboBox algoList = new JComboBox(algoMethodNames);
		currentAlgoMethod = algoMethods.get(algoList.getSelectedIndex());
		
		//Starts the automatic animation of an algorithm
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
		
		//Pauses the automatic animation of an algorithm
		pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.pauseAlgo();
            }
         });
		
		//Goes a step back in the animation of an algorithm
		backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.previousAlgo();
            }
         });
		
		//Goes a step forward in the animation of an algorithm
		forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.nextAlgo();
            }
         });
		
		//Stops the animation of an algorithm
		stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.stopAlgo();
            }
         });
		
		//Currently has no implementation
		commitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	//TODO Functions to implement?
            }
         });
		
		//A list to choose different algorithms from
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
		
		//To set a selected vertex as startvertex if needed
		startVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.setStartVertex();
            	startVertexSelected = true;
            }
         });
		
		//To set a selected vertex as endvertex if needed
		endVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.setEndVertex();
            	endVertexSelected = true;
            }
         });

		JPanel toolPanel = new JPanel(new FlowLayout());
		
		toolPanel.add(startButton);
		toolPanel.add(pauseButton);
		toolPanel.add(backButton);
		toolPanel.add(forwardButton);
		toolPanel.add(stopButton);
		//toolPanel.add(commitButton);
		toolPanel.add(algoList);
		//Buttons for startvertex and endvertex
		//are only shown if an algorithm needs them 
		toolPanel.add(startVertexButton);
		startVertexButton.setVisible(false);
		toolPanel.add(endVertexButton);
		endVertexButton.setVisible(false);
		
		add(toolPanel, BorderLayout.NORTH);
	}
}
