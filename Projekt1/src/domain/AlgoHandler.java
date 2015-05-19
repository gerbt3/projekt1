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
	private Vertex<V> startVertex;
	private Vertex<V> endVertex;
	private Timer t;
	
	public AlgoHandler(GraphTool<V, E> gt) {
		selectState = new SelectState<V,E>(gt);
		this.graphTool=gt;
		t = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (graphTool.hasNextGraph()) graphTool.nextGraph();
				else stopAlgo();
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
	// Methods for controlling the animating of an algorithm
	//------------------------------------------------------------------------------------//
	
	/*
	 * Gets all annotated methods from the AnnotationParser
	 */
	public Vector<Method> getAnnotatedMethods() {
		return graphTool.getAnnotatedMethods();
	}
	
	/*
	 * Executes an algorithm method
	 * and start the timer for animating it
	 */
	public void startAlgo(Method currentAlgoMethod) {
		graphTool.executeMethod(currentAlgoMethod, startVertex, endVertex);
		t.start();
	}
	
	//Pauses the timer for animating an algorithm
	public void pauseAlgo() {
		t.stop();
		graphTool.resetStartButton();
	}
	
	//Gets the previous step in the animation of an algorithm
	public void previousAlgo() {
		graphTool.previousGraph();
	}
	
	//Gets the next step in the animation of an algorithm
	public void nextAlgo() {
		graphTool.nextGraph();
	}
	
	/*
	 * Stops the animation of an algorithm
	 * and stops the timer for animating an algorithm
	 */
	public void stopAlgo() {
		if (t.isRunning()) {
			t.stop();
			graphTool.stop();
			graphTool.resetStartButton();
			graphTool.resetColor();
		}
	}
	
	/*
	 * Returns true,
	 * if the timer for animating an algorithm is running
	 */
	public boolean algoIsRunning(){
		return t.isRunning();
	}
	
	/*
	 * Changes the delay of the timer
	 * for animating an algorithm to a given time
	 */
	public void setTimerTime(int time) {
		t.setDelay(time);
	}
	
	public void resetColor() {
		graphTool.resetColor();
	}
	
	//------------------------------------------------------------------------------------//
	// Methods for controlling the startvertex and endvertex of an algorithm
	//------------------------------------------------------------------------------------//
	
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
}
