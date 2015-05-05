package domain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import examples.Decorable;
import examples.Graph;


public class GraphView<V,E> extends JPanel {

	private GraphComponent<V,E> comp;
	private Handler<V,E> handler;
	private MenuHandler<V,E> menuHandler;
	
	public GraphView(MenuHandler<V,E> h){
		comp = new GraphComponent<V, E>(this);
		this.menuHandler=h;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.constructComponents();
		this.add(comp, BorderLayout.CENTER);
	}
	
	public void setHandler(Handler<V,E> handler) {
		this.handler = handler;
	}
	
	public void paintGraph(Graph<V,E> currentGraph) {

		comp.setGraph(currentGraph);
	}
	
	public void insertEdge(Point p1, Point p2) {

		comp.insertEdge(p1, p2);
	}
	
	public void mouseDown(Decorable d, Point p){
		handler.mouseDown(d, p);
	}
	public void mouseUp(Decorable d, Point p){
		handler.mouseUp(d, p);
	}
	public void mouseDrag(Decorable d, Point p){
		handler.mouseDrag(d, p);
	}

	public void deleteEdge() {
		
		comp.deleteEdge();
	}

	public void setFlag(Attribut attr, boolean selected) {
		comp.setFlag(attr, selected);
		
	}
	
	/*
	 * Constructs the menu for handling the attributes of vertices and edges
	 * Calls the itemChanged method of the menuHandler class
	 */
	private void constructComponents() {

		JSlider slider = new JSlider(1,20);
		JPanel attributeMenuPanel = new JPanel(new GridLayout(3,1));
		JCheckBox name=new JCheckBox("name");
		JCheckBox weight=new JCheckBox("weight");
		JCheckBox string=new JCheckBox("string");
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				comp.setZoomSize(slider.getValue());
			}
		});
		name.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				menuHandler.itemChanged(Attribut.name, name.isSelected());

			}
		});
		weight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				menuHandler.itemChanged(Attribut.weight, weight.isSelected());

			}
		});
		string.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				menuHandler.itemChanged(Attribut.string, string.isSelected());

			}
		});
		attributeMenuPanel.add(name);
		attributeMenuPanel.add(weight);
		attributeMenuPanel.add(string);

		add(attributeMenuPanel, BorderLayout.EAST);
		add(slider, BorderLayout.SOUTH);
	}
	
	
	
}
