package domain;

import java.awt.Point;

import examples.Decorable;
import examples.Vertex;

public class VertexState<V,E> extends EditorState{

	private GraphTool<V,E> graphtool;
	private Vertex<V> selectedVertex;
	
	public VertexState(GraphTool<V,E> gt){
		
		this.graphtool=gt;
	}
	
	@Override
	public void mouseDown(Decorable d, Point p) {
		
		selectedVertex=graphtool.insertVertex(p);
	}

	@Override
	public void mouseDrag(Decorable d, Point p) {
		
		graphtool.moveVertex(selectedVertex, p);
	}

	@Override
	public void mouseUp(Decorable d, Point p) {
		graphtool.moveVertex(selectedVertex, p);
	}

}
