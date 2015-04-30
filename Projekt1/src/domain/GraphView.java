package domain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import javax.swing.JPanel;
import examples.Decorable;
import examples.Graph;


public class GraphView<V,E> extends JPanel {

	private GraphComponent<V,E> comp;
	private Handler<V,E> handler; 
	
	public GraphView(){
		comp = new GraphComponent<V, E>(this);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
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
	
	
	
	
}
