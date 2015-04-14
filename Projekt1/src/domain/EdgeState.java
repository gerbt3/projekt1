package domain;

import java.awt.Point;
import examples.Decorable;
import examples.Vertex;

public class EdgeState<V, E> extends EditorState {

	private GraphTool<V,E> graphtool;
	private Vertex<V> startVertex;

	public EdgeState(GraphTool<V,E> g){
		this.graphtool=g;
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
			graphtool.insertEdge(startVertex, p);
	}

	@Override
	public void mouseUp(Decorable d, Point p) {

		if(d!=null&&d!=startVertex&&startVertex!=null){
			if(d instanceof Vertex){
				if(!d.equals(startVertex)){
					graphtool.insertEdge(startVertex, (Vertex<V>)d);
				}

			}
		}
		else
			graphtool.deleteEdge();
	}
}
