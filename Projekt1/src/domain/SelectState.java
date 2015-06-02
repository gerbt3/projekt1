package domain;

import java.awt.Color;
import java.awt.Point;

import examples.Decorable;
import examples.Edge;
import examples.Vertex;


public class SelectState<V,E> extends EditorState {

	private GraphTool<V, E> graphTool;
	private Decorable selected;
	private Point selectedPoint;
	private Color oldColor;
	
	public SelectState(GraphTool<V,E> g){
		this.graphTool=g;
	}

	/*
	 * if a decorable is clicked, it will change the color
	 */
	@Override
	public void mouseDown(Decorable d, Point p) {

		if(selected!=null){
			graphTool.setColor(selected, oldColor);
			selected=null;
		}

		if(d!=null){
			selected=d;
			oldColor=(Color) d.get(Attribut.color);
			graphTool.setColor(d, GraphTool.SELECTED);
			selectedPoint=p;
		}

	}
	
	/*
	 * moves the selected Vertex to his new position
	 */
	@Override
	public void mouseDrag(Decorable d, Point p) {

		if(selected instanceof Vertex){
			if(!(Math.abs(p.getX()-selectedPoint.getX())<=0.5&&Math.abs(p.getY()-selectedPoint.getY())<=0.5))
				graphTool.moveVertex((Vertex<V>)selected, p);
		}
	}

	/*
	 * moves the selected Vertex to his new position
	 */
	@Override
	public void mouseUp(Decorable d, Point p) {
		this.mouseDrag(d, p);
		graphTool.serializeEditorGraph();
	}

	/*
	 * deletes an edge or a vertex
	 */
	@Override
	public void deleteDecorable(){
		if(selected!=null){
			if(selected instanceof Vertex){
				graphTool.deleteVertex((Vertex<V>)selected);
			}
			else{
				graphTool.deleteEdge((Edge<E>)selected);
			}
			selected=null;
		}
	}
	
	/*
	 * changes the name of a vertex or the weight of an edge
	 */
	@Override
	public void changeAttribut(String text){
		if(selected instanceof Vertex){
			graphTool.changeAttribut(selected,Attribut.name, text);
		}
		if(selected instanceof Edge){
			graphTool.changeAttribut(selected,Attribut.weight, text);
		}
	}

	/*
	 * returns the selected decorable
	 */
	public Decorable getSelected() {
		
		return selected;
	}
}
