package domain;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Method;

import examples.Decorable;
import examples.Edge;
import examples.Graph;
import examples.GraphExamples;
import examples.IncidenceListGraph;
import examples.Vertex;


public class GraphTool<V,E> {
	
	private int nameIndex=1;
	private Graph<V, E> currentGraph;
	public static Color STANDARD = Color.BLACK;
	public static Color SELECTED = Color.BLUE;
	private GraphSerializer<V, E> graphSerializer;
	private AnnotationParser<V,E> parser;
	private ViewHandler<V,E> viewHandler;
	private boolean graphSaved = true;
	
	public GraphTool(GraphExamples<V,E> ge){
		this(new IncidenceListGraph<V,E>(), ge);
		if(!viewHandler.chooseGraphOption())
			this.createGraph(false);
	}
	
	public GraphTool(Graph<V,E> g, GraphExamples<V,E> ge){
	
		currentGraph=g;
		this.calculatePositions(currentGraph);
		parser = new AnnotationParser<V,E>(ge, this);
		graphSerializer = new GraphSerializer<V, E>();
		viewHandler=new ViewHandler<V,E>(this);
		viewHandler.setGraph(currentGraph);
	}
	
	public Graph<V,E> getCurrentGraph() {
		return currentGraph;
	}

	//------------------------------------------------------------------------------------//
	// Methods for drawing a graph
	//------------------------------------------------------------------------------------//
	
	public void createGraph(boolean directed){
		currentGraph=new IncidenceListGraph<V, E>(directed);
		nameIndex=1;
		viewHandler.setGraph(currentGraph);
	}

	private void calculatePositions(Graph<V, E> g) {

		//Dimension d=viewHandler.getSize();
		double number=g.numberOfVertices();
		double radius=number*10;
		Iterator<Vertex<V>> it =g.vertices();
		int i=0;
		Vertex<V> v;
		
		
		while(it.hasNext()){
			v=it.next();
			double x=radius*Math.cos(i/number*2.0*Math.PI)+radius;
			double y=radius*Math.sin(i/number*2.0*Math.PI)+radius;
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
		viewHandler.setGraph(currentGraph);
		return v;
	}

	public void moveVertex(Vertex<V> v, Point p){
		Dimension d=viewHandler.getSize();
		double radius = GraphComponent.width/2.0;
		double x=p.getX();
		double y=p.getY();
		
		/*if(x>(d.getWidth()-radius)){
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
			v.set(Attribut.pos_y, y-radius);*/
		v.set(Attribut.pos_x, x-radius);
		v.set(Attribut.pos_y, y-radius);
		
		// Graph speichern
		viewHandler.setGraph(currentGraph);
	}

	public void insertEdge(Vertex<V> startVertex, Point p2) {
		double radius=GraphComponent.width/2.0;
		Point p1=new Point();
		p1.setLocation((double)startVertex.get(Attribut.pos_x),(double)startVertex.get(Attribut.pos_y));
		viewHandler.insertEdge(p1, p2);
	}

	public void insertEdge(Vertex<V> from, Vertex<V> to) {
		
		Edge<E> e_from;
		if(currentGraph.isDirected()){
			for(Iterator<Edge<E>>it1=currentGraph.incidentInEdges(to);it1.hasNext();){
				e_from=it1.next();
				for(Iterator<Edge<E>>it2=currentGraph.incidentOutEdges(from);it2.hasNext();){
					if(e_from.equals(it2.next())){
						viewHandler.deleteEdge();
						viewHandler.setGraph(currentGraph);
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
						viewHandler.deleteEdge();
						viewHandler.setGraph(currentGraph);
						return;
					}
				}
			}
		}
		Edge<E> e=currentGraph.insertEdge(from, to, (E) "");
		e.set(Attribut.color, STANDARD);
		e.set(Attribut.weight, "1");
		viewHandler.deleteEdge();
		viewHandler.setGraph(currentGraph);

	}

	public void deleteEdge(){
		viewHandler.deleteEdge();
	}
	
	public void setColor(Decorable d, Color c){
		d.set(Attribut.color, c);
		viewHandler.setGraph(currentGraph);
	}
	
	/*
	 * Changes the color of each vertex and edge
	 * of the current graph to black
	 */
	public void resetColor() {
		Iterator<Vertex<V>> vIt = currentGraph.vertices();
		while (vIt.hasNext()) {
			vIt.next().set(Attribut.color, Color.black);
		}
		Iterator<Edge<E>> eIt = currentGraph.edges();
		while (eIt.hasNext()) {
			eIt.next().set(Attribut.color, Color.black);
		}
		viewHandler.setGraph(currentGraph);
	}

	public void deleteVertex(Vertex<V> selected) {
		currentGraph.removeVertex(selected);
		viewHandler.setGraph(currentGraph);
	}

	public void deleteEdge(Edge<E> selected) {
		currentGraph.removeEdge(selected);
		viewHandler.setGraph(currentGraph);
	}
	
	//------------------------------------------------------------------------------------//
	// Methods for creating a new graph, saving and loading a graph
	//------------------------------------------------------------------------------------//
	
	public void newGraph(boolean directed) {
		currentGraph = new IncidenceListGraph<V,E>(directed);
	}
	
	public void saveGraph(String name) throws IOException {
		graphSerializer.saveGraph(name, currentGraph);
		viewHandler.setGraph(currentGraph);
	}
	
	public Graph<V,E> openGraph(String name) throws IOException {
		Graph graph = graphSerializer.openGraph(name);
		currentGraph = graph;
		viewHandler.setGraph(currentGraph);
		return currentGraph;
	}
	
	//------------------------------------------------------------------------------------//
	// Helper-methods for executing an algorithm
	//------------------------------------------------------------------------------------//

	public void changeAttribut(Decorable d, Attribut attr, String text){
		d.set(attr, text);
		viewHandler.setGraph(currentGraph);
	}

	public Vector<Method> getAnnotatedMethods() {
		return parser.getAnnotatedMethods();
	}

	/*
	 * Executes an algorithm
	 * an returns all by the algorithm generated graphs
	 * Return value will be null, if the deserializing of the graphs fails
	 * and has to be tested for it
	 */
	public void executeMethod(Method method, Vertex<V> startVertex, Vertex<V> endVertex) {
		parser.executeMethod(method, startVertex, endVertex);
		try {
			graphSerializer.deserializeAlgoGraphs();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("@GraphTool: executeMethod: Failed to deserialize graphs");
			e.printStackTrace();
		}
	}
	
	/*
	 * Get the previous graph in the algorithm animation
	 * based on the current position in the arraylist of graphs
	 */
	public void previousGraph() {
		if (graphSerializer.hasPreviousGraph()) {
			currentGraph = graphSerializer.getPreviousGraph();
			viewHandler.setGraph(currentGraph);
		}
	}
	
	/*
	 * Get the previous graph in the algorithm animation
	 * based on the current position in the arraylist of graphs
	 */
	public void nextGraph() {
		if (graphSerializer.hasNextGraph()) {
			currentGraph = graphSerializer.getNextGraph();
			viewHandler.setGraph(currentGraph);
		}
	}
	
	/*
	 * Resets the current index in the arraylist of graphs,
	 * when the algorithm animation gets stopped 
	 */
	public void stop() {
		graphSerializer.resetAlgoIndex();
	}
	
	/*
	 * Serializes the graph after each change 
	 * an algorithm in the GraphExamples class made
	 */
	public void serializeGraph(Graph<V,E> g) {
		try {
			graphSerializer.serializeGraph(g);
		} catch (IOException e) {
			System.out.println("@GraphTool: GraphSerializer failed to serialize a graph");
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------------------------------------------//
	// Helper-methods for checking if graph is saved before changing tabs
	//------------------------------------------------------------------------------------//
	
	/*
	 * Sets a flag if the current graph was saved
	 * after making changes to it or not
	 */
	public void setGraphSaved(boolean saved) {
		graphSaved = saved;
	}
	
	/*
	 * Returns whether the current graph was saved
	 * after making changes to it or not
	 */
	public boolean getGraphSaved() {
		return graphSaved;
	}
	
}
