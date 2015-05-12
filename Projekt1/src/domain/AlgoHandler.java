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
	private Vertex<V> startVertex;
	private Vertex<V> endVertex;
	private Thread algoThread;
	
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
	
	/*
	 * Executes the algorithm method
	 * and starts a new thread to animate 
	 * the different states of the graph
	 */
	public void startAlgo(Method currentAlgoMethod) {
		this.currentAlgoMethod = currentAlgoMethod;
		algoGraphs = graphTool.executeMethod(currentAlgoMethod, startVertex, endVertex);
		startVertex = null;
		endVertex = null;
		
		algoThread = new Thread(new RunAlgo());
		algoThread.start();
	}
	
	/*
	 * Stops the animation of an algorithm
	 * and interrupts the algoThread
	 */
	public void stopAlgo() {
		algoThread.interrupt();
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
	
	/*
	 * Thread for animating an algorithm
	 */
	private class RunAlgo implements Runnable {
		public void run() {
			int i = 0;
			for (Graph<V,E> g : algoGraphs) {
				System.out.println("@AlgoHandler, startAlgo: graph " + i++ + " : " );
				System.out.println(g);
				try {
					
					Thread.sleep(2000);
					
				} catch (InterruptedException e) {
					System.out.println("@AlgoHandler: RunAlgo: run: Thread interrupted");
				}
				graphTool.setCurrentGraph(g);
			}
		}
	}
}
