package domain;

import java.awt.Point;
import java.lang.reflect.Method;
import java.util.Vector;

import examples.Decorable;

public class AlgoHandler<V,E> implements Handler<V,E> {
	
	private SelectState selectState;
	private AnnotationParser parser;
	private Method currentAlgoMethod;
	
	public AlgoHandler(GraphTool tool, AnnotationParser parser) {
		selectState = new SelectState<V,E>(tool);
		this.parser = parser;
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

	
	public void startAlgo(Method currentAlgoMethod) {
		this.currentAlgoMethod = currentAlgoMethod;
	}
	
	/*
	 * Gets all annotated methods from the AnnotationParser
	 */
	public Vector<Method> getAnnotatedMethods() {
		return parser.getAnnotatedMethods();
	}
	
	/*
	 * Gives the AnnotationParser a method to execute
	 */
	public void executeMethod(Method method) {
		parser.executeMethod(method);
	}
	
}
