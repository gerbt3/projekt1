package domain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;




public class AlgoView<V,E> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AlgoHandler<V,E> algoHandler;
	private Method currentAlgoMethod;

	public AlgoView(AlgoHandler<V,E> algoHandler) {	
		
		this.algoHandler= algoHandler;
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
		
		/*
		 * Makes a list to choose from with all available algorithms
		 */
		/*Vector<Method> algoMethods = algoHandler.getAnnotatedMethods();
		String[] algoMethodNames = new String[algoMethods.size()];
		for (int i = 0; i < algoMethods.size(); i++) {
			algoMethodNames[i] = algoMethods.get(i).getName();
		}
		JComboBox algoList = new JComboBox(algoMethodNames);
		currentAlgoMethod = algoMethods.get(algoList.getSelectedIndex());
		*/
		selectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            }
         });
		
		startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	algoHandler.startAlgo(currentAlgoMethod);
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
		
	/*	algoList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            	String algoName = (String) algoList.getSelectedItem();
            	int index = 0;
            	for (int i = 0; i < algoMethods.size(); i++) {
        			if (algoName.equals(algoMethods.get(i).getName())) index = i;
        		}
            	currentAlgoMethod = algoMethods.get(index);
            
         });
		}*/
		JPanel toolPanel = new JPanel(new FlowLayout());
		
		toolPanel.add(selectionButton);
		toolPanel.add(startButton);
		toolPanel.add(backButton);
		toolPanel.add(forwardButton);
		toolPanel.add(stopButton);
		toolPanel.add(commitButton);
		//toolPanel.add(algoList);
		
		add(toolPanel, BorderLayout.NORTH);
	}
}
