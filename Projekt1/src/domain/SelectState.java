package domain;

import java.awt.Point;
import java.awt.event.MouseEvent;

import examples.Decorable;
import examples.Vertex;

public class SelectState extends EditorState {

	private GraphTool graphtool;
	private Decorable selected;

	public SelectState(GraphTool g){
		this.graphtool=g;
	}

	@Override
	public void mouseDown(Decorable d, Point p) {

		if(selected!=null){
			graphtool.setColor(selected, GraphTool.STANDARD);
			selected=null;
		}

		if(d!=null){
			selected=d;
			graphtool.setColor(d, GraphTool.SELECTED);
		}

	}

	@Override
	public void mouseDrag(Decorable d, Point p) {

		if(selected instanceof Vertex){
			graphtool.moveVertex((Vertex)selected, p);
		}
	}

	@Override
	public void mouseUp(Decorable d, Point p) {
		this.mouseDrag(d, p);
	}

	@Override
	public void deleteVertex(Point p){

	}

	@Override
	public void deleteEdge(Point p1, Point p2){

	}
}
