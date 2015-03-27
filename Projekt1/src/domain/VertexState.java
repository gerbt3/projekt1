package domain;

import java.awt.Point;
import java.awt.event.MouseEvent;

import examples.Decorable;
import examples.Vertex;

public class VertexState extends EditorState{

	private GraphTool graphtool;
	private Vertex selectedVertex;
	
	public VertexState(GraphTool gt){
		
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
