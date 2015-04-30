package domain;

import java.awt.Point;
import examples.Decorable;
import examples.Vertex;

public class EdgeState<V, E> extends EditorState {

	private GraphTool<V,E> graphTool;
	private Vertex<V> startVertex;

	public EdgeState(GraphTool<V,E> g){
		this.graphTool=g;
	}

	@Override
	public void mouseDown(Decorable d, Point p) {

		startVertex=null;
		if(d!=null){
			if(d instanceof Vertex){
				startVertex=(Vertex<V>) d;
			}
		}
	}

	@Override
	public void mouseDrag(Decorable d, Point p) {
		if(startVertex!=null)
			graphTool.insertEdge(startVertex, p);
	}

	@Override
	public void mouseUp(Decorable d, Point p) {

		if(d!=null&&d!=startVertex&&startVertex!=null){
			if(d instanceof Vertex){
				if(!d.equals(startVertex)){
					graphTool.insertEdge(startVertex, (Vertex<V>)d);
				}

			}
		}
		else
			graphTool.deleteEdge();
	}
}
