package domain;

import java.awt.Point;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import examples.Decorable;
import examples.Edge;
import examples.Graph;
import examples.Vertex;

public class AlgoHandler<V,E> implements Handler<V,E> {
	
	private GraphTool<V,E> graphTool;
	private SelectState<V,E> selectState;
	private boolean selectedState = false;
	private Method currentAlgoMethod;
	private ArrayList<Graph<V,E>> algoGraphs;
	
	public AlgoHandler(GraphTool<V, E> gt) {
		selectState = new SelectState<V,E>(gt);
		this.graphTool=gt;
		algoGraphs = new ArrayList<>();
	}

	@Override
	public void mouseDown(Decorable d, Point p) {
		//Only vertices can be selected
		if (selectedState && d instanceof Vertex) selectState.mouseDown(d, p);
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
	
	public void setSelectedState(boolean selectedState) {
		this.selectedState = selectedState;
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
	
	public void startAlgo(Method currentAlgoMethod) {
		this.currentAlgoMethod = currentAlgoMethod;
		//algoGraphs = graphTool.executeMethod(currentAlgoMethod);
		for (Graph<V,E> g : algoGraphs) System.out.println(g);
		
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
