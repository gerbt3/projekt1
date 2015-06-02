package domain;

import java.awt.Point;
import examples.Decorable;

public abstract class EditorState {

	public abstract void mouseDown(Decorable d, Point p);
	public abstract void mouseDrag(Decorable d, Point p);
	public abstract void mouseUp(Decorable d, Point p);
	
	/*
	 * only SelectState uses this function
	 */
	public void deleteDecorable(){
		// nothing to implement
	}
	
	/*
	 * only SelectState uses this function
	 */
	public void changeAttribut(String text){
		// nothing to implement
	}
}
