package domain;

import java.awt.Point;

import examples.Decorable;

public interface Handler<V,E> {
 
	public void mouseDown(Decorable d, Point p);
	public  void mouseDrag(Decorable d, Point p);
	public  void mouseUp(Decorable d, Point p);
}
