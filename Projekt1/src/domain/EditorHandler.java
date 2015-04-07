package domain;

import java.awt.Point;
import java.awt.event.MouseEvent;

import examples.Decorable;

public class EditorHandler {

	public enum State{
		VERTEX,
		EDGE,
		SELECT,
		INACTIVE
	}

	private EditorState currentState;
	private VertexState vertexState;
	private EdgeState edgeState;
	private SelectState selectState;

	public EditorHandler(SelectState selectState, VertexState vertexState,EdgeState edgeState){
		currentState=selectState;
		this.selectState=selectState;
		this.vertexState=vertexState;
		this.edgeState=edgeState;
	}

	public void setState(State state){
		switch(state){
		case VERTEX:
			currentState=vertexState;
			break;
		case EDGE:
			currentState=edgeState;
			break;
		case SELECT:
			currentState=selectState;
			break;
		case INACTIVE:
			if(currentState instanceof SelectState)
				currentState.mouseDown(null, null);
			currentState=null;
			break;
		}	
	}

	public void mouseDown(Decorable d, Point p){
		if(currentState!=null)
			currentState.mouseDown(d, p);
	}
	public  void mouseDrag(Decorable d, Point p){
		if(currentState!=null)
			currentState.mouseDrag(d, p);
	}
	public  void mouseUp(Decorable d, Point p){
		if(currentState!=null)
			currentState.mouseUp(d, p);
	}
	public void deleteDecorable(){
		if(currentState!=null)
			currentState.deleteDecorable();
	}
	
	public void undo(){
		if(currentState!=null)
			currentState.undo();
	}
	public void redo(){
		if(currentState!=null)
			currentState.redo();
	}

}
