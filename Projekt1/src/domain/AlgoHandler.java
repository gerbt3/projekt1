package domain;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Timer;

import examples.Decorable;
import examples.Edge;
import examples.Graph;
import examples.Vertex;

public class AlgoHandler<V,E> implements Handler<V,E> {
	
	private GraphTool<V,E> graphTool;
	private SelectState<V,E> selectState;
	private Method currentAlgoMethod;
	private ArrayList<Graph<V,E>> algoGraphs;
	private Vertex<V> startVertex;
	private Vertex<V> endVertex;
	private Timer t;
	
	public AlgoHandler(GraphTool<V, E> gt) {
		selectState = new SelectState<V,E>(gt);
		this.graphTool=gt;
		algoGraphs = new ArrayList<>();
		t = new Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				int i = 0;
				for (Graph<V,E> g : algoGraphs) {
					System.out.println("@AlgoHandler, startAlgo: graph " + i++ + " : " );
					System.out.println(g);
					graphTool.setCurrentGraph(g);
				}	
			}
		});
	}

	@Override
	public void mouseDown(Decorable d, Point p) {
		//Only vertices can be selected
		if (d instanceof Vertex) selectState.mouseDown(d, p);
		else selectState.mouseDown(null, null);
	}

	@Override
	public void mouseDrag(Decorable d, Point p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseUp(Decorable d, Point p) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Clears the current selection of a vertex or edge,
	 * if user changes tab
	 */
	public void clearSelected() {
		selectState.mouseDown(null, null);
	}
	
	//------------------------------------------------------------------------------------//
	// Methods for controlling an algorithm
	//------------------------------------------------------------------------------------//
	
	/*
	 * Executes the algorithm method
	 */
	public void startAlgo(Method currentAlgoMethod) {
		this.currentAlgoMethod = currentAlgoMethod;
		graphTool.executeMethod(currentAlgoMethod, startVertex, endVertex);
		t.start();
	}
	
	public void pauseAlgo() {
		
	}
	
	public void previousAlgo() {
		
	}
	
	public void nextAlgo() {
		
	}
	
	/*
	 * Stops the animation of an algorithm
	 */
	public void stopAlgo() {
		if (t.isRunning()) t.stop();
	}
	
	public void setTimerTime(int time) {
		t.setDelay(time);
	}
	
	/*
	 * Gets the current selection from the AnnotationParser
	 * and saves it as the startVertex
	 */
	public void setStartVertex() {
		Decorable d = selectState.getSelected();
		if (d instanceof Vertex) startVertex = (Vertex<V>) d;
		clearSelected();
	}
	
	/*
	 * Gets the current selection from the AnnotationParser
	 * and saves it as the endVertex
	 */
	public void setEndVertex() {
		Decorable d = selectState.getSelected();
		if (d instanceof Vertex) endVertex = (Vertex<V>) d;
		clearSelected();
	}
	
	/*
	 * Deletes the saved start and end vertex
	 */
	public void clearStartEndVertex() {
		startVertex = null;
		endVertex = null;
	}
	
	/*
	 * Gets all annotated methods from the AnnotationParser
	 */
	public Vector<Method> getAnnotatedMethods() {
		return graphTool.getAnnotatedMethods();
	}
	
	/*
	 * Gives the AnnotationParser a method to execute
	 */
	public void executeMethod(Method method) {
		//graphTool.executeMethod(method);
	}
	
}
