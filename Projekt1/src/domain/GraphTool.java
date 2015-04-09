package domain;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import examples.Decorable;
import examples.Edge;
import examples.Graph;
import examples.IncidenceListGraph;
import examples.Vertex;


public class GraphTool<V,E> {

	public enum Attribut{
		pos_x,
		pos_y,
		color,
		name,
		weight,
		distance,
		visited
	}
	private ArrayList<Graph> graphHistory=new ArrayList<>();
	private int historyIndex=-1;
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

	public void saveGraph(){
		
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
		double delta=5;
		Dimension d=graphview.getSize();
		double radius = GraphComponent.width/2.0;
		double x=p.getX();
		double y=p.getY();
		
		if(x>(d.getWidth()-radius)){
			v.set(Attribut.pos_x, d.getWidth()-2*radius);
		}
		else if(x<(0+radius)){
			v.set(Attribut.pos_x, 0.0);
		}
		else
			v.set(Attribut.pos_x, x-radius);
		
		if(y>(d.getHeight()-radius)){
			v.set(Attribut.pos_y, d.getHeight()-2*radius);
		}
		else if(y<(0+radius)){
			v.set(Attribut.pos_y, 0.0);
		}
		else
			v.set(Attribut.pos_y, y-radius);
		// Graph speichern
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

	public void deleteVertex(Vertex selected) {
		currentGraph.removeVertex(selected);
		graphview.paintGraph(currentGraph);
	}

	public void deleteEdge(Edge selected) {
		currentGraph.removeEdge(selected);
		graphview.paintGraph(currentGraph);
	}
	
	public void newGraph(boolean directed) {
		//TODO somewhere: do you want to save the old graph?
		currentGraph = new IncidenceListGraph(directed);
	}
	
	public void saveGraph(String name) throws IOException {
		//TODO test this
		String filename = name + ".ser";
		ObjectOutputStream oos = null;
		try {			
			oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));	
		    oos.writeObject(currentGraph); 
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			oos.close();
		}
	}
	
	public void openGraph(String name) throws IOException {
		//TODO test this
		String filename = name + ".ser";
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(filename));
			currentGraph = (Graph) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			ois.close();
		}
	}
}
