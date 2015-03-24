package domain;

import java.awt.Point;
import java.awt.event.MouseEvent;

public class EditorHandler {
	
	public enum State{
		VERTEX,
		EDGE,
		SELECT
	}
	
	private EditorState currentState;
	
	public EditorHandler(){
		currentState=new SelectState();
	}
	
	public void setState(State state){
		switch(state){
		case VERTEX:
				currentState=new VertexState();
			break;
		case EDGE:
				currentState=new EdgeState();
			break;
		case SELECT:
				currentState=new SelectState();
			break;
		}	
	}
	
	public void mouseDown(MouseEvent e){
		currentState.mouseDown(e);
	}
	public  void mouseDrag(MouseEvent e){
		currentState.mouseDrag(e);
	}
	public  void mouseUp(MouseEvent e){
		currentState.mouseUp(e);
	}
	public void deleteVertex(Point p){
		currentState.deleteVertex(p);
	}
	public void deleteEdge(Point p1, Point p2){
		currentState.deleteEdge(p1, p2);
	}
	public void undo(){
		currentState.undo();
	}
	public void redo(){
		currentState.redo();
	}

}
