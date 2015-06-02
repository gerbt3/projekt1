package domain;

import java.awt.Point;
import examples.Decorable;

public class EditorHandler<V,E> implements Handler<V, E> {

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
	private GraphTool<V,E> graphTool;
	
	public EditorHandler(GraphTool<V, E> gt) {
		graphTool = gt;
		this.selectState=new SelectState<V,E>(gt);
		this.vertexState=new VertexState<V,E>(gt);
		this.edgeState=new EdgeState<V,E>(gt);
		this.currentState=this.selectState;
	}

	/*
	 * sets the current state, depending on which state is set,
	 * another method is called in the other methods of this class
	 */
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

	/*
	 * calls the method mouseDown of the currentState
	 */
	public void mouseDown(Decorable d, Point p){
		if(currentState!=null) 
			currentState.mouseDown(d, p);	
	}
	
	/*
	 * calls the method mouseDrag of the currentState
	 */
	public  void mouseDrag(Decorable d, Point p){
		if(currentState!=null)
			currentState.mouseDrag(d, p);
	}
	
	/*
	 * calls the method mouseUp of the currentState
	 */
	public  void mouseUp(Decorable d, Point p){
		if(currentState!=null)
			currentState.mouseUp(d, p);	
	}
	
	
	public void deleteDecorable(){
		if(currentState!=null) {
			currentState.deleteDecorable();
		}
	}
	
	/*
	 * Undoes the last saved action
	 */
	public void undo(){
		graphTool.undo();
		
	}
	/*
	 * Redoes the next saved action
	 */
	public void redo(){
		graphTool.redo();
	}

	/*
	 * if the currentState is SelectState,
	 * the attribut of the selected Decorable will be changed
	 */
	public void changeAttribut(String text) {
		if(currentState!=null)
			currentState.changeAttribut(text);
	}

	/*
	 * returns the selected Decorable
	 */
	public Decorable getSelected() {
		
		return selectState.getSelected();
	}
}
