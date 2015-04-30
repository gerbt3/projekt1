package domain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import domain.EditorHandler.State;


public class EditorView<V, E> extends JPanel {
	
	private EditorHandler<V,E> editorHandler;
	
	public EditorView(EditorHandler<V,E> handler) {
		this.editorHandler=handler;
		setLayout(new BorderLayout());
		constructPanelComponents();
	}

	private void constructPanelComponents() {
		
		JButton selectionButton = new JButton("Select");
		JButton deleteButton = new JButton("Delete");
		JButton vertexButton = new JButton("Vertex");
		JButton edgeButton = new JButton("Edge");
		JTextField attributText = new JTextField(15);
		JButton editAttributButton=new JButton("Edit");
		
		editAttributButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editorHandler.changeAttribut(attributText.getText());
			}
		});
		
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
		
		toolPanel.add(attributText);
		toolPanel.add(editAttributButton);
		toolPanel.add(selectionButton);
		toolPanel.add(deleteButton);
		toolPanel.add(vertexButton);
		toolPanel.add(edgeButton);
		
		
		add(toolPanel, BorderLayout.NORTH);
	}
}
