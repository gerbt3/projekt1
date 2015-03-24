package domain;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Iterator;

import domain.ViewController.DrawState;
import examples.Edge;
import examples.Graph;
import examples.Vertex;


public class GraphTool<V,E> {

	DrawController drawcontroller;
	ViewController viewcontroller;
	Graph currentGraph;
	GraphView graphview;

	public GraphTool(Graph g){

		currentGraph=g;
		viewcontroller=new ViewController();
		this.calculatePositions(currentGraph);
		this.drawGraph(currentGraph);

	}

	private void drawGraph(Graph g) {
		// TODO Auto-generated method stub

	}

	private void calculatePositions(Graph<V, E> g) {

		double number=g.numberOfVertices();
		double radius=number*10;
		Iterator<Vertex<V>> it =g.vertices();
		int i=0;
		Vertex v;
		Edge e;
		Iterator<Edge<E>> itE=g.edges();
		Vertex[] ver;
		while(it.hasNext()){
			v=it.next();
			int x=(int) (radius*Math.cos(i/number*2.0*Math.PI)+70.0);
			int y=(int) (radius*Math.sin(i/number*2.0*Math.PI)+70.0);
			v.set("x_pos", x);
			v.set("y_pos", y);
			i++;
		}
		while(itE.hasNext()){
			e=itE.next();
			ver=g.endVertices(e);
			e.set("x_start", ver[0].get("x_pos"));
			e.set("y_start", ver[0].get("y_pos"));
			e.set("x_stop", ver[1].get("x_pos"));
			e.set("y_stop", ver[1].get("y_pos"));
		}
	}
}