package domain;

import java.awt.Point;
import java.awt.event.MouseEvent;

import examples.Decorable;

public abstract class EditorState {

	public abstract void mouseDown(Decorable d, Point p);
	public abstract void mouseDrag(Decorable d, Point p);
	public abstract void mouseUp(Decorable d, Point p);
	public void deleteDecorable(){
		// nothing to implement
	}
	public void undo(){
		// TODO
	}
	public void redo(){
		// TODO
	}
}
