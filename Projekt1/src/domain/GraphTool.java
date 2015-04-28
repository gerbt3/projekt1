package domain;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;

import javax.swing.JFrame;

import domain.GraphTool.Attribut;

import java.awt.Color;
import java.io.IOException;

import examples.Decorable;
import examples.Edge;
import examples.Graph;
import examples.GraphExamples;
import examples.IncidenceListGraph;
import examples.Vertex;


public class GraphTool<V,E> {

	public enum Attribut{
		pos_x,
		pos_y,
		color,
		name,
		weight,
		string
	}
	
	private int nameIndex=1;
	private Graph<V, E> currentGraph;
	private GraphView<V,E> graphview;
	public static Color STANDARD = Color.BLACK;
	public static Color SELECTED = Color.BLUE;
	private GraphFrame<V, E> frame;
	private GraphSerializer<V, E> graphSerializer;
	private AlgoHandler algoHandler;
	private AnnotationParser parser;
	
	public GraphTool(GraphExamples<V,E> ge){
		
		this(new IncidenceListGraph<V,E>(), ge);
		if(!frame.chooseGraphOption())
			this.createGraph(false);
	}
	
	public GraphTool(Graph<V,E> g, GraphExamples<V,E> ge){

		AnnotationParser parser = new AnnotationParser<V,E>(ge, this);
		currentGraph=g;
		this.calculatePositions(currentGraph);
		new VertexState<V,E>(this);
		EditorHandler<V, E> handler = new EditorHandler<V, E>(new SelectState<V,E>(this), new VertexState<V,E>(this), new EdgeState<V, E>(this));
		algoHandler = new AlgoHandler(this, parser);
		graphview=new GraphView<V,E>(g, handler);
		frame= new GraphFrame<V, E>(handler, algoHandler, new MenuHandler(this), graphview );
		frame.setSize(1000, 700);
		frame.setTitle("GraphTool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		graphSerializer = new GraphSerializer<V, E>(); //types???
		
	}

	public void createGraph(boolean directed){
		currentGraph=new IncidenceListGraph<V, E>(directed);
		nameIndex=1;
		graphview.paintGraph(currentGraph);
	}

	private void calculatePositions(Graph<V, E> g) {

		double number=g.numberOfVertices();
		double radius=number*10;
		Iterator<Vertex<V>> it =g.vertices();
		int i=0;
		Vertex<V> v;
		
		
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
		Edge<E> e;
		while(itE.hasNext()){
			e=itE.next();
			e.set(Attribut.color, STANDARD);
		}
	}

	public Vertex<V> insertVertex(Point p){
		Vertex<V> v=currentGraph.insertVertex((V) "");
		double radius = GraphComponent.width/2.0;
		v.set(Attribut.pos_x, p.getX()-radius);
		v.set(Attribut.pos_y, p.getY()-radius);
		v.set(Attribut.color, STANDARD);
		v.set(Attribut.name, Integer.toString(nameIndex));
		nameIndex++;
		// graph speichern
		graphview.paintGraph(currentGraph);
		return v;
	}

	public void moveVertex(Vertex<V> v, Point p){
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

	public void insertEdge(Vertex<V> startVertex, Point p2) {
		double radius=GraphComponent.width/2.0;
		Point p1=new Point();
		p1.setLocation((double)startVertex.get(Attribut.pos_x)+radius,(double)startVertex.get(Attribut.pos_y)+radius);
		graphview.insertEdge(p1, p2);
	}

	public void insertEdge(Vertex<V> from, Vertex<V> to) {
		
		Edge<E> e_from;
		if(currentGraph.isDirected()){
			for(Iterator<Edge<E>>it1=currentGraph.incidentInEdges(to);it1.hasNext();){
				e_from=it1.next();
				for(Iterator<Edge<E>>it2=currentGraph.incidentOutEdges(from);it2.hasNext();){
					if(e_from.equals(it2.next())){
						graphview.deleteEdge();
						graphview.paintGraph(currentGraph);
						return;
					}
				}
			}
		}
		else{
			for(Iterator<Edge<E>>it1=currentGraph.incidentEdges(from);it1.hasNext();){
				e_from=it1.next();
				for(Iterator<Edge<E>>it2=currentGraph.incidentEdges(to);it2.hasNext();){
					if(e_from.equals(it2.next())){
						graphview.deleteEdge();
						graphview.paintGraph(currentGraph);
						return;
					}
				}
			}
		}
		Edge<E> e=currentGraph.insertEdge(from, to, (E) "");
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

	public void deleteVertex(Vertex<V> selected) {
		currentGraph.removeVertex(selected);
		graphview.paintGraph(currentGraph);
	}

	public void deleteEdge(Edge<E> selected) {
		currentGraph.removeEdge(selected);
		graphview.paintGraph(currentGraph);
	}
	
	public void newGraph(boolean directed) {
		//TODO somewhere: do you want to save the old graph?
		currentGraph = new IncidenceListGraph<V,E>(directed);
	}
	
	public void saveGraph(String name) throws IOException {
		graphSerializer.saveGraph(name, currentGraph);
	}
	
	public void openGraph(String name) throws IOException {
		Graph graph = graphSerializer.openGraph(name);
		currentGraph = graph;
		graphview.paintGraph(currentGraph);
	}
	
	public Vertex<V> getStartVertex() {
		// TODO angewählter Vertex holen
		return null;
	}

	public Vertex<V> getStopVertex() {
		// TODO angewählter Vertex holen
		return null;
	}

	public void changeAttribut(Decorable d, Attribut attr, String text){
		d.set(attr, text);
		graphview.paintGraph(currentGraph);
	}

	public void itemChanged(Attribut attr, boolean selected) {
		
		graphview.setFlag(attr, selected);
		graphview.paintGraph(currentGraph);
	}
	
}
