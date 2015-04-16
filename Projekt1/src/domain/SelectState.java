package domain;

import java.awt.Point;

import examples.Decorable;
import examples.Edge;
import examples.Vertex;

public class SelectState<V,E> extends EditorState {

	private GraphTool<V, E> graphtool;
	private Decorable selected;
	private Point selectedPoint;
	public SelectState(GraphTool<V,E> g){
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
			selectedPoint=p;
		}

	}

	@Override
	public void mouseDrag(Decorable d, Point p) {

		if(selected instanceof Vertex){
			if(!(Math.abs(p.getX()-selectedPoint.getX())<=0.5&&Math.abs(p.getY()-selectedPoint.getY())<=0.5))
				graphtool.moveVertex((Vertex<V>)selected, p);
		}
	}

	@Override
	public void mouseUp(Decorable d, Point p) {
		this.mouseDrag(d, p);
	}

	@Override
	public void deleteDecorable(){
		if(selected!=null){
			if(selected instanceof Vertex){
				graphtool.deleteVertex((Vertex<V>)selected);
			}
			else{
				graphtool.deleteEdge((Edge<E>)selected);
			}
			selected=null;
		}
	}
	
	@Override
	public void changeAttribut(String text){
		if(selected instanceof Vertex){
			graphtool.changeName((Vertex)selected, text);
		}
		if(selected instanceof Edge){
			graphtool.changeWeight((Edge)selected, text);
		}
	}
}
