package domain;

import java.awt.Point;

import examples.Decorable;

public class EditorHandler<V,E> {

	public enum State{
		VERTEX,
		EDGE,
		SELECT,
		INACTIVE
	}

	private EditorState currentState;
	private VertexState<V,E> vertexState;
	private EdgeState<V,E> edgeState;
	private SelectState<V,E> selectState;

	public EditorHandler(SelectState<V,E> selectState, VertexState<V,E> vertexState,EdgeState<V,E> edgeState){
		currentState=selectState;
		this.selectState=selectState;
		this.vertexState=vertexState;
		this.edgeState=edgeState;
	}

	public void setState(State state){
		if(currentState instanceof SelectState && !(state==State.SELECT)){
			currentState.mouseDown(null, null);
		}
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

	public void changeAttribut(String text) {
		
	}

}
