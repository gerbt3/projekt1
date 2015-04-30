package domain;

import java.awt.Point;
import java.lang.reflect.Method;
import java.util.Vector;

import examples.Decorable;

public class AlgoHandler<V,E> implements Handler<V,E> {
	
	private SelectState<V,E> selectState;
	private Method currentAlgoMethod;
	private GraphTool<V,E> graphTool;
	
	public AlgoHandler(GraphTool<V, E> gt) {
		selectState = new SelectState<V,E>(gt);
		this.graphTool=gt;
	}

	@Override
	public void mouseDown(Decorable d, Point p) {
		selectState.mouseDown(d, p);
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
	 * Clears the current selection of a vertex,
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
		graphTool.executeMethod(method);
	}
	
}
