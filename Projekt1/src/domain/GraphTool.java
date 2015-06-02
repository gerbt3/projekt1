package domain;

import java.awt.Point;
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
	
	/*
	 * constructor without graph
	 */
	public GraphTool(GraphExamples<V,E> ge){
		this(new IncidenceListGraph<V,E>(), ge);
		if(!viewHandler.chooseGraphOption())
			this.createGraph(false);
		else
			this.createGraph(true);
	}
	
	/*
	 * constructor with graph
	 */
	public GraphTool(Graph<V,E> g, GraphExamples<V,E> ge){
		
		currentGraph=g;
		this.calculatePositions(currentGraph);
		parser = new AnnotationParser<V,E>(ge, this);
		graphSerializer = new GraphSerializer<V, E>();
		viewHandler=new ViewHandler<V,E>(this);
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}
	
	/*
	 * returns currentGraph
	 */
	public Graph<V,E> getCurrentGraph() {
		return currentGraph;
	}

	//------------------------------------------------------------------------------------//
	// Methods for drawing a graph
	//------------------------------------------------------------------------------------//
	
	/*
	 * creates a new Graph
	 * if the param directed is true, the graph is directed
	 */
	public void createGraph(boolean directed){
		currentGraph=new IncidenceListGraph<V, E>(directed);
		graphSerializer.clearEditorGraphs();
		nameIndex=1;
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/*
	 * calculate the position of for vertices so that the vertices are arranged in a circle
	 */
	private void calculatePositions(Graph<V, E> g) {

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

	/*
	 * inserts a new vertex into the graph
	 */
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
		serializeEditorGraph();
		return v;
	}
	
	/*
	 * moves a vertex to another position
	 */
	public void moveVertex(Vertex<V> v, Point p){
		double radius = GraphComponent.width/2.0;
		double x=p.getX();
		double y=p.getY();
		v.set(Attribut.pos_x, x-radius);
		v.set(Attribut.pos_y, y-radius);
		
		// Graph speichern
		viewHandler.setGraph(currentGraph);
	}

	/*
	 * inserts an unfinished line 
	 */
	public void insertEdge(Vertex<V> startVertex, Point p2) {

		Point p1=new Point();
		p1.setLocation((double)startVertex.get(Attribut.pos_x),(double)startVertex.get(Attribut.pos_y));
		viewHandler.insertEdge(p1, p2);
	}

	/*
	 * inserts a new edge into the graph
	 */
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
		e.set(Attribut.weight, 1.0);
		viewHandler.deleteEdge();
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/*
	 * deletes the unfinished line
	 */
	public void deleteEdge(){
		viewHandler.deleteEdge();
	}
	
	/*
	 * if a vertex is selected, the color changes
	 * with this method a decorable change his color
	 */
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

	/*
	 * removes a Vertex from the graph
	 */
	public void deleteVertex(Vertex<V> selected) {	
		currentGraph.removeVertex(selected);
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/*
	 * removes an Edge from the graph
	 */
	public void deleteEdge(Edge<E> selected) {
		currentGraph.removeEdge(selected);
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}
	
	//------------------------------------------------------------------------------------//
	// Methods for creating a new graph, saving and loading a graph
	//------------------------------------------------------------------------------------//
	
	public void saveGraph(String name) throws IOException {
		graphSerializer.saveGraph(name, currentGraph);
		viewHandler.setGraph(currentGraph);
	}
	
	public Graph<V,E> openGraph(String name) throws IOException {
		currentGraph = graphSerializer.openGraph(name);
		viewHandler.setGraph(currentGraph);
		graphSerializer.clearEditorGraphs();
		graphSerializer.serializeEditorGraph(currentGraph);
		return currentGraph;
	}
	
	//------------------------------------------------------------------------------------//
	// Helper-methods for undoing and redoing an action in the graph editor
	//------------------------------------------------------------------------------------//
	
	/*
	 * Serializes a graph after a change was made to it
	 */
	public void serializeEditorGraph() {
		try {
			resetColor();
			graphSerializer.serializeEditorGraph(currentGraph);
			
		} catch (IOException e) {
			System.out.println("@GraphTool: GraphSerializer failed to serialize a graph");
			e.printStackTrace();
		}
	}
	
	/*
	 * Undoes the last saved action in the graph editor
	 * if the action is possible
	 */
	public void undo() {
		if (graphSerializer.isUndoPossible()) {
			Graph<V,E> g = null;
			
			try {
				g = graphSerializer.undo(currentGraph);
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("@GraphTool: undo(): failed to deserialize a graph");
				e.printStackTrace();
			}
			
			if (g != null) {
				currentGraph = g;
				viewHandler.setGraph(currentGraph); 
			}
		}
	}
	
	/*
	 * Redoes the next saved action in the graph editor
	 * if the action is possible
	 */
	public void redo() {
		if (graphSerializer.isRedoPossible()) {
			
			Graph<V,E> g = null;
			
			try {
				g = graphSerializer.redo();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("@GraphTool: undo(): failed to deserialize a graph");
				e.printStackTrace();
			}
			
			if (g != null) {
				currentGraph = g;
				viewHandler.setGraph(currentGraph); 
			}
		}	
	}
	
	//------------------------------------------------------------------------------------//
	// Helper-methods for executing an animating an algorithm in the algorithm editor
	//------------------------------------------------------------------------------------//

	/*
	 * changes an attribut of a decorable
	 */
	public void changeAttribut(Decorable d, Attribut attr, String text){	
		d.set(attr, text);
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/*
	 * returns annotated methods from GraphExamples
	 */
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
		boolean isStart = graphSerializer.isStart();
		//Executes the algorithm and serializes all generates graphs only
		//when the animation is started, not when it's paused
		//When it's gets started, isStart is true
		if (isStart) {
			resetColor();
			parser.executeMethod(method, startVertex, endVertex);
			try {
				graphSerializer.deserializeAlgoGraphs();
				resetColor();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("@GraphTool: executeMethod: Failed to deserialize graphs");
				e.printStackTrace();
			}
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
	 * Checks if the arraylist of graphs has a next element
	 */
	public boolean hasNextGraph() {
		return graphSerializer.hasNextGraph();
	}
	
	/*
	 * Get the previous graph in the algorithm animation
	 * based on the current position in the arraylist of graphs
	 */
	public void nextGraph() {
		if (graphSerializer.hasNextGraph()) {
			currentGraph = graphSerializer.getNextGraph();
			viewHandler.setGraph(currentGraph);
		} else {
			//Stops the animation when it gets to end manually
			stop();
		}
	}
	
	/*
	 * Resets the current index in the arraylist of graphs,
	 * when the algorithm animation gets stopped 
	 */
	public void stop() {
		graphSerializer.clearAlgoGraphs();
	}
	
	/*
	 * Serializes the graph after each change 
	 * an algorithm in the GraphExamples class made
	 */
	public void serializeAlgoGraph(Graph<V,E> g) {
		try {
			graphSerializer.serializeAlgoGraph(g);
		} catch (IOException e) {
			System.out.println("@GraphTool: serializeAlgoGraph: GraphSerializer failed to serialize a graph");
			e.printStackTrace();
		}
	}
	
	/*
	 * Resets the color of the startbutton
	 * after the animations of algorithms has finished
	 */
	public void resetStartButton() {
		viewHandler.resetStartButton();
	}
	
}
