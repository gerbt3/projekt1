package domain;

import java.awt.Dimension;
import java.awt.Point;
import examples.Graph;


public class ViewHandler<V,E> {

	private GraphTool<V,E> graphTool;
	private GraphFrame<V,E> frame;
	private GraphView<V,E> graphView;
	
	public ViewHandler(GraphTool<V,E> gt){
		this.graphTool=gt;
		frame=new GraphFrame<V,E>(this.graphTool);
		this.graphView=frame.getGraphView();
	}
	
	public void setGraph(Graph<V,E> g){
		graphView.paintGraph(g);
	}

	public void deleteEdge() {
		graphView.deleteEdge();
		
	}

	public void insertEdge(Point p1, Point p2) {
		graphView.insertEdge(p1, p2);
		
	}

	public Dimension getSize() {
		return graphView.getSize();
	}

	public boolean chooseGraphOption() {
		return frame.chooseGraphOption();
	}
}
