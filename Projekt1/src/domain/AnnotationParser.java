package domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import examples.GraphExamples;
import examples.Vertex;

public class AnnotationParser<V, E> {

	private GraphExamples<V,E> graphexamples;
	private GraphTool<V,E> graphtool;
	private ArrayList<Method> annotatedMethods;
	public AnnotationParser(GraphExamples<V,E> ge, GraphTool<V,E> gt){
		this.graphexamples=ge;
		this.graphtool=gt;
	}

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

	public void executeMethod(Method method){
		Vertex<V> v1=null, v2=null;
		if(method!=null){
			if(method.getAnnotation(Algorithm.class).vertex()){
				v1=graphtool.getStartVertex();
			}
			if(method.getAnnotation(Algorithm.class).vertex2()){
				v2=graphtool.getStopVertex();
			}
			if(!method.getAnnotation(Algorithm.class).vertex()){
				try {
					method.invoke(graphexamples);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			else {
				if(v1==null) throw new NullPointerException("startvertex");
				if(!method.getAnnotation(Algorithm.class).vertex2()){
					try {
						method.invoke(graphexamples, v1);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				}else{
					if(v2==null)throw new NullPointerException("stopvertex");

					try {
						method.invoke(graphexamples,v1,v2 );
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
