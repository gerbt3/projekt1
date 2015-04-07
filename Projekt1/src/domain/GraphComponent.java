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
import java.awt.geom.Line2D.Double;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JComponent;

import domain.GraphTool.Attribut;
import examples.Decorable;
import examples.Edge;
import examples.Graph;
import examples.Vertex;

public class GraphComponent extends JComponent{

	private Graph graph;
	public static double width=30;
	private HashMap<Vertex, Ellipse2D.Double> vertices = new HashMap<>();
	private HashMap<Edge, Line2D.Double> edges=new HashMap<>();
	private GraphView graphview;
	private Line2D.Double unfinishedLine=null;

	public GraphComponent(Graph g, GraphView gv){
		this.setGraph(g);
		this.graphview=gv;
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				Decorable d=findDecorable(e);
				graphview.mouseDown(d, e.getPoint());
			}
			@Override
			public void mouseReleased(MouseEvent e){
				Decorable d=findDecorable(e);
				graphview.mouseUp(d, e.getPoint());
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {
				Decorable d=findDecorable(e);
				graphview.mouseDrag(d, e.getPoint());
			}
		});
	}

	public void paintComponent(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		Vertex v;
		Edge e;
		Ellipse2D.Double ellipse;
		Line2D.Double line;
		for( Iterator<Ellipse2D.Double> itv = vertices.values().iterator(); itv.hasNext(); ){
			ellipse=itv.next();
			v=this.findKey(vertices, ellipse);
			g2.setColor((Color)v.get(Attribut.color));
			g2.draw(ellipse);
		}
		for( Iterator<Line2D.Double> ite = edges.values().iterator(); ite.hasNext(); ){
			line=ite.next();
			e=this.findKey(edges, line);
			g2.setColor((Color)e.get(Attribut.color));
			g2.draw(line);
		}
		if(unfinishedLine!=null){
			g2.draw(unfinishedLine);
		}


	}
	public void setGraph(Graph g){
		graph=g;
		vertices.clear();
		edges.clear();
		Iterator itv=graph.vertices();
		Iterator ite=graph.edges();
		Vertex v;
		Edge e;
		while(itv.hasNext()){
			v=(Vertex) itv.next();
			vertices.put(v,new Ellipse2D.Double((double)v.get(Attribut.pos_x),(double)v.get(Attribut.pos_y),width,width));
		}
		while(ite.hasNext()){
			e=(Edge)ite.next();
			edges.put(e,new Line2D.Double((double)e.get(Attribut.x_from),(double)e.get(Attribut.y_from),
					(double)e.get(Attribut.x_to),(double)e.get(Attribut.y_to)));
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
				if(line.ptLineDist(p)<distance){
					d=this.findKey(edges, line);
					break;
				}
			}
		}
		return d;

	}

	public <V, K> K findKey(HashMap<K,V> map, V value ){
		for (Entry<K, V> entry : map.entrySet()) {
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
}
