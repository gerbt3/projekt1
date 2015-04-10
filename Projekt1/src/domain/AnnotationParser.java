package domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

import examples.GraphExamples;
import examples.Vertex;

public class AnnotationParser {

	private GraphExamples graphexamples;
	private GraphTool graphtool;
	private HashMap<String, Method> annotatedMethods;
	public AnnotationParser(GraphExamples ge, GraphTool gt){
		this.graphexamples=ge;
		this.graphtool=gt;
	}

	public Iterator<Method> getAnnotatedMethods(){
		annotatedMethods=new HashMap<>();
		Method[] methods = GraphExamples.class.getMethods();
		for(int i=0; i<methods.length; i++){
			if(methods[i].isAnnotationPresent(Algorithm.class)){
				annotatedMethods.put(methods[i].getName(), methods[i]);
			}
		}
		return annotatedMethods.values().iterator();
	}

	public void executeMethod(String methodname){
		Vertex v1=null, v2=null;
		Method method=annotatedMethods.get(methodname);
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
