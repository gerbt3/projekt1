package domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import examples.Graph;
import examples.GraphExamples;
import examples.Vertex;


public class AnnotationParser<V, E> {

	private GraphExamples<V,E> graphExamples;
	private GraphTool<V,E> graphTool;
	private ArrayList<Method> annotatedMethods;
	
	public AnnotationParser(GraphExamples<V,E> ge, GraphTool<V,E> gt){
		this.graphExamples=ge;
		this.graphTool=gt;
	}

	/*
	 * returns annotated methods
	 */
	public Vector<Method> getAnnotatedMethods(){
	
		annotatedMethods=new ArrayList<>();
		Method[] methods = GraphExamples.class.getMethods();
		for(int i=0; i<methods.length; i++){
			if(methods[i].isAnnotationPresent(Algorithm.class)){
				annotatedMethods.add(methods[i]);
			}
		}
		return new Vector<Method>(annotatedMethods);
	}

	/*
	 * returns if the method needs a startvertex
	 */
	public boolean isStartvertexNeeded(Method method){
		return method.getAnnotation(Algorithm.class).vertex();
	}
	
	/*
	 * returns if the method needs a endvertex
	 */
	public boolean isEndvertexNeeded(Method method){
		return method.getAnnotation(Algorithm.class).vertex2();
	}
	
	/*
	 * Executes an algorithm
	 * The values of startVertex and endVertex are null,
	 * if they aren't needed
	 */
	public void executeMethod(Method method, Vertex<V> startVertex, Vertex<V> endVertex){
		Graph<V,E> graph = graphTool.getCurrentGraph();
		Vertex<V> v1=startVertex, v2=endVertex;
		if(method!=null){
	
			if(!method.getAnnotation(Algorithm.class).vertex()){
				try {
					method.invoke(graphExamples, graph, graphTool);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			else {
				if(v1==null) throw new NullPointerException("startvertex");
				if(!method.getAnnotation(Algorithm.class).vertex2()){
					try {
						method.invoke(graphExamples, graph, v1, graphTool);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				}else{
					if(v2==null)throw new NullPointerException("stopvertex");

					try {
						method.invoke(graphExamples, graph, v1, v2, graphTool);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
