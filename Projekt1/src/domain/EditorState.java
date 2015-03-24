package domain;

import java.awt.Point;
import java.awt.event.MouseEvent;

public abstract class EditorState {

	public abstract void mouseDown(MouseEvent e);
	public abstract void mouseDrag(MouseEvent e);
	public abstract void mouseUp(MouseEvent e);
	public void deleteVertex(Point p){
		// nothing to implement
	}
	public void deleteEdge(Point p1, Point p2){
		// nothing to implement
	}
	public void undo(){
		// TODO
	}
	public void redo(){
		// TODO
	}
}
