package domain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.JComponent;
import examples.Decorable;
import examples.Edge;
import examples.Graph;
import examples.Vertex;

public class GraphComponent<V,E> extends JComponent{

	private Graph<V,E> graph;
	public static double width=30.0;
	private HashMap<Vertex<V>, Ellipse2D.Double> vertices = new HashMap<>();
	private HashMap<Edge<E>, Line2D.Double> edges=new HashMap<>();
	private GraphView<V,E> graphview;
	private Line2D.Double unfinishedLine=null;
	private boolean name=false, weight=false, string=false;

	public GraphComponent(Graph<V,E> g, GraphView<V,E> graphView2){
		this.setGraph(g);
		this.graphview=graphView2;
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				if(e.getButton()==MouseEvent.BUTTON1){
					Decorable d=findDecorable(e);
					graphview.mouseDown(d, e.getPoint());
				}
			}
			@Override
			public void mouseReleased(MouseEvent e){
				if(e.getButton()==MouseEvent.BUTTON1){
					Decorable d=findDecorable(e);
					graphview.mouseUp(d, e.getPoint());
				}
				if(e.getButton()==MouseEvent.BUTTON3){
					// Popupmenu
				}
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {

				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
					Decorable d=findDecorable(e);
					graphview.mouseDrag(d, e.getPoint());
				}
			}
		});
	}

	public void paintComponent(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		Vertex<V> v;
		Edge<E> e;
		Ellipse2D.Double ellipse;
		Line2D.Double line;
		for( Iterator<Ellipse2D.Double> itv = vertices.values().iterator(); itv.hasNext(); ){
			ellipse=itv.next();
			v=this.findKey(vertices, ellipse);
			g2.setColor((Color)v.get(Attribut.color));
			g2.draw(ellipse);
			if(v.has(Attribut.name)&&name){
				g2.drawString((String)v.get(Attribut.name), (int)(7+(double)v.get(Attribut.pos_x)), (int)(width/2+(double)v.get(Attribut.pos_y)));
			}

		}
		for( Iterator<Line2D.Double> ite = edges.values().iterator(); ite.hasNext(); ){
			line=ite.next();
			e=this.findKey(edges, line);
			g2.setColor((Color)e.get(Attribut.color));
			g2.draw(line);
			if(e.has(Attribut.weight)&&weight){
				g2.drawString((String)e.get(Attribut.weight), (int) (line.getX1()+line.getX2())/2,(int) ((line.getY1()+line.getY2())/2));
			}
			if(graph.isDirected())
				drawArrowHead(g2, new Point((int) line.x2, (int) line.y2),new Point((int) line.x1, (int) line.y1));

		}
		
		if(unfinishedLine!=null){
			g2.draw(unfinishedLine);
			if(graph.isDirected())
				drawArrowHead(g2, new Point((int) unfinishedLine.x2, (int) unfinishedLine.y2),new Point((int) unfinishedLine.x1, (int) unfinishedLine.y1));
		}



	}

	//From http://www.coderanch.com/t/340443/GUI/java/Draw-arrow-head-line
	private void drawArrowHead(Graphics2D g2, Point tip, Point tail) {
		double phi = Math.toRadians(22);
		int barb = 12;
		double dy = tip.y - tail.y;
		double dx = tip.x - tail.x;
		double theta = Math.atan2(dy, dx);

		double x, y, rho = theta + phi;
		for(int j = 0; j < 2; j++) {
			x = tip.x - barb * Math.cos(rho);
			y = tip.y - barb * Math.sin(rho);
			g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
			rho = theta - phi;
		}
	}


	private void setLine(Vertex<V> from, Vertex<V> to, Edge<E> e){

		double radius=GraphComponent.width/2.0;
		double x1=(double)from.get(Attribut.pos_x)+radius;
		double x2=(double)to.get(Attribut.pos_x)+radius;
		double y1=(double)from.get(Attribut.pos_y)+radius;
		double y2=(double)to.get(Attribut.pos_y)+radius;
		double ax=1,ay=1,bx=1,by=1;
		if(x1<x2){
			bx=-1;
		}
		else{
			ax=-1;
		}

		if(y1<y2){
			by=-1;

		}
		else{
			ay=-1;
		}
		double alpha=Math.atan(Math.abs((y2-y1)/(x2-x1)));
		edges.put(e, new Line2D.Double(x1+(Math.cos(alpha)*radius)*ax,y1+(Math.sin(alpha)*radius)*ay,x2+(Math.cos(alpha)*radius)*bx,y2+(Math.sin(alpha)*radius)*by));
	}

	public void setGraph(Graph<V,E> g){
		graph=g;
		vertices.clear();
		edges.clear();
		Iterator<Vertex<V>> itv=graph.vertices();
		Iterator<Edge<E>> ite=graph.edges();
		Vertex<V> v;
		Edge<E> e;
		Vertex<V>[] ver;
		while(itv.hasNext()){
			v=(Vertex<V>) itv.next();
			vertices.put(v,new Ellipse2D.Double((double)v.get(Attribut.pos_x),(double)v.get(Attribut.pos_y),width,width));
		}
		while(ite.hasNext()){
			e=(Edge<E>)ite.next();
			ver=g.endVertices(e);
			this.setLine(ver[0], ver[1], e);
		}

		repaint();
	}

	private Decorable findDecorable(MouseEvent e){
		double distance=5.0;
		Point p=e.getPoint();
		Decorable d=null;
		Ellipse2D.Double ellipse;
		Line2D.Double line;
		for( Iterator<Ellipse2D.Double> itv = vertices.values().iterator(); itv.hasNext(); ){
			ellipse=itv.next();
			if(ellipse.contains(p))
			{
				d=this.findKey(vertices, ellipse);
				break;
			}
		}
		if(d==null){
			for( Iterator<Line2D.Double> ite = edges.values().iterator(); ite.hasNext(); ){
				line=ite.next();
				if(line.ptSegDist(p)<distance){
					d=this.findKey(edges, line);
					break;
				}
			}
		}
		return d;

	}

	public <V1, K> K findKey(HashMap<K,V1> map, V1 value ){
		for (Entry<K, V1> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public void insertEdge(Point p1, Point p2) {

		unfinishedLine=new Line2D.Double(p1,p2);
		repaint();
	}

	public void deleteEdge(){
		unfinishedLine=null;
		repaint();
	}

	public void setFlag(Attribut attr, boolean selected) {
		switch(attr){
		case name:
			this.name=selected;
			break;
		case weight:
			this.weight=selected;
			break;
		case string: this.string=selected;
		}
		
	}
}
