package domain;

import java.awt.Point;
import java.awt.event.MouseEvent;

import examples.Decorable;
import examples.Edge;
import examples.Vertex;

public class EdgeState extends EditorState {

	private GraphTool graphtool;
	private Vertex startVertex;

	public EdgeState(GraphTool g){
		this.graphtool=g;
	}

	@Override
	public void mouseDown(Decorable d, Point p) {

		if(d!=null){
			if(d instanceof Vertex){
				startVertex=(Vertex) d;
			}
		}
	}

	@Override
	public void mouseDrag(Decorable d, Point p) {

		graphtool.insertEdge(startVertex, p);
	}

	@Override
	public void mouseUp(Decorable d, Point p) {

		if(d!=null){
			if(d instanceof Vertex){
				if(!d.equals(startVertex)){
					graphtool.insertEdge(startVertex, (Vertex)d);
				}

			}
		}
		else
			graphtool.deleteEdge();
	}
}
