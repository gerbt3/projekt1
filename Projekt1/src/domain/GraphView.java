package domain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import examples.Decorable;
import examples.Graph;
import examples.Vertex;


public class GraphView extends JPanel {

	private GraphComponent comp;
	private EditorHandler handler;
	
	public GraphView(Graph g, EditorHandler handler){
		this.handler=handler;
		comp = new GraphComponent(g, this);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.add(comp, BorderLayout.CENTER);
	}
	
	public void paintGraph(Graph currentGraph) {

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
}
