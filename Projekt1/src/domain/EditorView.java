package domain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import domain.EditorHandler.State;




public class EditorView<V, E> extends JPanel {
	
	private EditorHandler<V,E> handler;
	public EditorView(EditorHandler<V,E> handler) {
		this.handler=handler;
		setLayout(new BorderLayout());
		constructPanelComponents();
	}

	private void constructPanelComponents() {
		
		JButton selectionButton = new JButton("Select");
		JButton deleteButton = new JButton("Delete");
		JButton vertexButton = new JButton("Vertex");
		JButton edgeButton = new JButton("Edge");
		
		selectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               handler.setState(State.SELECT);
            }
         });
		
		deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               handler.deleteDecorable();
            }
         });
		
		vertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               handler.setState(State.VERTEX);
            }
         });
		
		edgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               handler.setState(State.EDGE);
            }
         });
		
		JRadioButton undirectedButton = new JRadioButton("undirected");
		JRadioButton directedButton = new JRadioButton("directed");
		
		ButtonGroup group = new ButtonGroup();
		group.add(undirectedButton);
		group.add(directedButton);
		
		JPanel toolPanel = new JPanel(new FlowLayout());
		
		toolPanel.add(selectionButton);
		toolPanel.add(deleteButton);
		toolPanel.add(vertexButton);
		toolPanel.add(edgeButton);
		
		toolPanel.add(undirectedButton);
		toolPanel.add(directedButton);
		
		add(toolPanel, BorderLayout.NORTH);
	}
}
