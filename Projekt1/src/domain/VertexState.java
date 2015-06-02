package domain;

import java.awt.Point;
import examples.Decorable;
import examples.Vertex;


public class VertexState<V,E> extends EditorState{

	private GraphTool<V,E> graphTool;
	private Vertex<V> insertedVertex;
	
	public VertexState(GraphTool<V,E> gt){
		this.graphTool=gt;
	}
	
	/*
	 * inserts a new vertex
	 */
	@Override
	public void mouseDown(Decorable d, Point p) {
		insertedVertex=graphTool.insertVertex(p);
	}

	/*
	 * moves the inserted vertex to a new position
	 */
	@Override
	public void mouseDrag(Decorable d, Point p) {
		graphTool.moveVertex(insertedVertex, p);
	}

	/*
	 * moves the inserted vertex to a new position
	 */
	@Override
	public void mouseUp(Decorable d, Point p) {
		graphTool.moveVertex(insertedVertex, p);
	}

}
