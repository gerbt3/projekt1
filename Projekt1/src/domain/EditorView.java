package domain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import domain.EditorHandler.State;


public class EditorView<V, E> extends JPanel {
	
	private EditorHandler<V,E> editorHandler;
	
	public EditorView(EditorHandler<V,E> handler) {
		
		this.setBackground(new Color(100,100,100));
		
		this.editorHandler=handler;
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
               editorHandler.setState(State.SELECT);
            }
         });
		
		deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               editorHandler.deleteDecorable();
            }
         });
		
		vertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               editorHandler.setState(State.VERTEX);
            }
         });
		
		edgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               editorHandler.setState(State.EDGE);
            }
         });
		
		JPanel toolPanel = new JPanel(new FlowLayout());

		toolPanel.add(selectionButton);
		toolPanel.add(deleteButton);
		toolPanel.add(vertexButton);
		toolPanel.add(edgeButton);
		
		add(toolPanel, BorderLayout.NORTH);
	}
}
