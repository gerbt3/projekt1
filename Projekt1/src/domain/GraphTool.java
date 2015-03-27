package domain;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Iterator;

import javax.swing.JFrame;

import java.awt.Color;

import examples.Decorable;
import examples.Edge;
import examples.Graph;
import examples.Vertex;


public class GraphTool<V,E> {

	public enum Attribut{
		pos_x,
		pos_y,
		x_from,
		y_from,
		x_to,
		y_to,
		color
	}
	private Graph currentGraph;
	private GraphView graphview;
	public static Color STANDARD = Color.BLACK;
	public static Color SELECTED = Color.BLUE;
	public GraphTool(Graph g){

		currentGraph=g;
		this.calculatePositions(currentGraph);
		new VertexState(this);
		EditorHandler handler = new EditorHandler(new SelectState(this), new VertexState(this), new EdgeState(this));
		graphview=new GraphView(g, handler);
		GraphFrame frame= new GraphFrame(handler, graphview );
		frame.setSize(1000, 700);
		frame.setTitle("GraphTool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void setEdgeAttributes(Vertex from, Vertex to, Edge e){
		
		double radius=GraphComponent.width/2.0;
		double x1=(double)from.get(Attribut.pos_x)+radius;
		double x2=(double)to.get(Attribut.pos_x)+radius;
		double y1=(double)from.get(Attribut.pos_y)+radius;
		double y2=(double)to.get(Attribut.pos_y)+radius;
		double ax=1,ay=1,bx=1,by=1;
		if(x1<x2){
			bx=-1;
		}
		else{
			ax=-1;
		}
		
		if(y1<y2){
			by=-1;
			
		}
		else{
			ay=-1;
		}
		double alpha=Math.atan(Math.abs((y2-y1)/(x2-x1)));
	 	e.set(Attribut.x_from, x1+(Math.cos(alpha)*radius)*ax);
		e.set(Attribut.x_to, x2+(Math.cos(alpha)*radius)*bx);
		e.set(Attribut.y_from, y1+(Math.sin(alpha)*radius)*ay);
		e.set(Attribut.y_to, y2+(Math.sin(alpha)*radius)*by);
	}

	private void calculatePositions(Graph<V, E> g) {

		double number=g.numberOfVertices();
		double radius=number*10;
		Iterator<Vertex<V>> it =g.vertices();
		int i=0;
		Vertex v;
		
		
		while(it.hasNext()){
			v=it.next();
			double x=radius*Math.cos(i/number*2.0*Math.PI)+70.0;
			double y=radius*Math.sin(i/number*2.0*Math.PI)+70.0;
			v.set(Attribut.pos_x, x);
			v.set(Attribut.pos_y, y);
			v.set(Attribut.color, STANDARD);
			i++;
		}
		Iterator<Edge<E>> itE=g.edges();
		Vertex[] ver;
		Edge e;
		while(itE.hasNext()){
			e=itE.next();
			e.set(Attribut.color, STANDARD);
			ver=g.endVertices(e);
			this.setEdgeAttributes(ver[0], ver[1], e);
		}
	}

	public Vertex insertVertex(Point p){
		Vertex v= currentGraph.insertVertex("");
		double radius = GraphComponent.width/2.0;
		v.set(Attribut.pos_x, p.getX()-radius);
		v.set(Attribut.pos_y, p.getY()-radius);
		v.set(Attribut.color, STANDARD);
		// graph speichern
		graphview.paintGraph(currentGraph);
		return v;
	}

	public void moveVertex(Vertex v, Point p){

		double radius = GraphComponent.width/2.0;
		v.set(Attribut.pos_x, p.getX()-radius);
		v.set(Attribut.pos_y, p.getY()-radius);
		// Graph speichern
		Edge e;
		if(currentGraph.isDirected()){
			for(Iterator<Edge> ite=currentGraph.incidentInEdges(v);ite.hasNext();){
				e=ite.next();
				this.setEdgeAttributes(currentGraph.origin(e), currentGraph.destination(e), e);
			}
			for(Iterator<Edge> ite=currentGraph.incidentOutEdges(v);ite.hasNext();){
				e=ite.next();
				this.setEdgeAttributes(currentGraph.origin(e), currentGraph.destination(e), e);
			}
		}
		else{
			Vertex[] ver;
			Iterator<Edge> it = currentGraph.incidentEdges(v);
			while(it.hasNext()){
				e=it.next();
				ver=currentGraph.endVertices(e);
				this.setEdgeAttributes(ver[0], ver[1], e);
			}
		}
		graphview.paintGraph(currentGraph);
	}

	public void insertEdge(Vertex startVertex, Point p2) {
		double radius=GraphComponent.width/2.0;
		Point p1=new Point();
		p1.setLocation((double)startVertex.get(Attribut.pos_x)+radius,(double)startVertex.get(Attribut.pos_y)+radius);
		graphview.insertEdge(p1, p2);
	}

	public void insertEdge(Vertex from, Vertex to) {
		
		Edge e_from;
		if(currentGraph.isDirected()){
			for(Iterator<Edge>it1=currentGraph.incidentInEdges(from);it1.hasNext();){
				e_from=it1.next();
				for(Iterator<Edge>it2=currentGraph.incidentOutEdges(to);it2.hasNext();){
					if(e_from.equals(it2.next())){
						graphview.deleteEdge();
						graphview.paintGraph(currentGraph);
						return;
					}
				}
			}
		}
		else{
			for(Iterator<Edge>it1=currentGraph.incidentEdges(from);it1.hasNext();){
				e_from=it1.next();
				for(Iterator<Edge>it2=currentGraph.incidentEdges(to);it2.hasNext();){
					if(e_from.equals(it2.next())){
						graphview.deleteEdge();
						graphview.paintGraph(currentGraph);
						return;
					}
				}
			}
		}
		Edge e=currentGraph.insertEdge(from, to, "");
		e.set(Attribut.color, STANDARD);
		this.setEdgeAttributes(from, to, e);
		graphview.deleteEdge();
		graphview.paintGraph(currentGraph);

	}

	public void deleteEdge(){
		graphview.deleteEdge();
	}
	public void setColor(Decorable d, Color c){
		d.set(Attribut.color, c);
		graphview.paintGraph(currentGraph);
	}
	
}