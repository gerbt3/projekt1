package examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import domain.Algorithm;
import domain.GraphTool;




public class GraphExamples<V,E> {


	static final private Object NUMBER = new Serializable(){};
	static final private Object VISITED = new Serializable(){};
	static final private Object DISCOVERY = new Serializable(){};

	// for dijkstra:
	static final private Object WEIGHT = new Serializable(){};
	static final private Object DISTANCE = new Serializable(){};
	static final private Object PQLOCATOR = new Serializable(){};
	
	//for kruskal 
	static final private Object MSF = new Serializable(){};
	static final private Object CLUSTER = new Serializable(){};
	
	
	private Graph<V,E> g;
	private Vertex<V> [] vertexArray;

	public GraphExamples(Graph<V,E> g){
		this.g=g;
	}
	
	public void setNumbers(){
		vertexArray = new  Vertex[g.numberOfVertices()];
		Iterator<Vertex<V>> it = g.vertices();
		int num = 0;
		while(it.hasNext()) {
			vertexArray[num]=it.next();
			vertexArray[num].set(NUMBER, num++);
		}
	}
	
	public int[][] getGatewayMatrix(int [][] ad){
		int n = ad.length;
		int [][] dist = new int[n][n];
		int [][] gw = new int[n][n];
		for (int i=0; i<n;i++)
			for (int k=0;k<n;k++){
				dist[i][k] = ad[i][k];
				if (i!=k && ad[i][k]!=1) dist[i][k]= n; // infinity!
				gw[i][k] = -1;
				if (ad[i][k]==1) gw[i][k] = k;
			}
		for (int k=0;k<n;k++)
			for (int i=0;i<n;i++)
				for (int j=0;j<n;j++){
					int newDist = dist[i][k]+dist[k][j];
					if (newDist < dist[i][j]) {
						dist[i][j] =newDist;
						gw[i][j] = gw[i][k];
					}
				}
		return gw;
	}

	
	
	public int[][] getAdjacencyMatrix(){
		setNumbers();
		int n = g.numberOfVertices();
		int [][] ad = new int[n][n];
		boolean directed = g.isDirected();
		Iterator<Edge<E>> it = g.edges();
		while ( it.hasNext()) {
			Vertex<V>[] endPts = g.endVertices(it.next()); 
			int i = (int) endPts[0].get(NUMBER);
			int k = (int) endPts[1].get(NUMBER);
			ad[i][k]=1;
			if (! directed) ad[k][i]=1;
		}
		return ad;
	}
	
	@Algorithm
	public void kruskal(){
		// gives the Object MSF to each
		// edge belonging to an minimal spanning forest
		
		// create clusters, put the vertex in it  
		// and assign them to the vertice
		Iterator<Vertex<V>> it = g.vertices();
		while(it.hasNext()){
			Vertex<V> v = it.next();
			ArrayList<Vertex<V>> cluster = new ArrayList<>();
			cluster.add(v);
			v.set(CLUSTER,cluster);
		}
		PriorityQueue<Double,Edge<E>> pq = new MyPriorityQueue<>();
		Iterator<Edge<E>> eIt = g.edges();
		while(eIt.hasNext()){
			Edge<E> e = eIt.next();
			double weight = 1.0;
			if (e.has(WEIGHT)) weight = (Double)e.get(WEIGHT);
			pq.insert(weight,e);
		}
		while ( ! pq.isEmpty()){
			Edge<E> e = pq.removeMin().element();
			Vertex<V>[] endPts = g.endVertices(e);
			ArrayList<Vertex<V>> cluster1 = (ArrayList<Vertex<V>>)endPts[0].get(CLUSTER);
			ArrayList<Vertex<V>> cluster2 = (ArrayList<Vertex<V>>)endPts[1].get(CLUSTER);
			if (cluster1 != cluster2){
				e.set(MSF,null);
				// merge the two clusters
				// make sure that cluster2 is the smaller
				if (cluster1.size() > cluster2.size()){
					for (Vertex<V> v: cluster2){
						v.set(CLUSTER,cluster1);
						cluster1.add(v);
					}
				}
				else{
					for (Vertex<V> v: cluster1){
						v.set(CLUSTER,cluster2);
						cluster2.add(v);
					}
				}
			}			
		}
	}
	
	@Algorithm(vertex=true)
	public void dijkstra(Vertex<V> s){
		// sets the attribute 's' of each vertex 'u' from wich 
		// we can reach 's' to 'g' where 'g' is the gateway
		// of 'u' on the shortest path from 'u' to 's' 
		MyPriorityQueue<Double, Vertex<V>> pq = new MyPriorityQueue<>();
		Iterator<Vertex<V>> it = g.vertices();
		// put all vertices to pq and give them
		// an attribute DISTANCE and PQLOCATOR
		while(it.hasNext()){
			Vertex<V> v = it.next();
			v.set(DISTANCE,Double.POSITIVE_INFINITY);
			Locator<Double,Vertex<V>> loc = pq.insert(Double.POSITIVE_INFINITY,v);
			v.set(PQLOCATOR,loc);
		}
		// correct the attributes for s
		s.set(DISTANCE,0.0);
		pq.replaceKey((Locator<Double,Vertex<V>>)s.get(PQLOCATOR),0.0);
		while( ! pq.isEmpty()){
			Vertex<V> u = pq.removeMin().element();
			// now make the relaxation step for all 
			// neighbours:
			Iterator<Edge<E>> eIt;
			if (g.isDirected()) eIt = g.incidentInEdges(u); // backwards!
			else eIt = g.incidentEdges(u);
			while (eIt.hasNext()){
				Edge<E> e = eIt.next();
				double weight = 1.0; // default weight
				if (e.has(WEIGHT)) weight = (Double)e.get(WEIGHT);
				Vertex<V> z = g.opposite(e, u);
				//Relaxation
				double newDist = (Double) u.get(DISTANCE) + weight;
				if (newDist < (Double) z.get(DISTANCE)) {
					z.set(DISTANCE,newDist); // new distance of z (can be changed later!)
					z.set(s, u); // gateway (will eventually be changed later!)
					pq.replaceKey((Locator<Double,Vertex<V>>) z.get(PQLOCATOR), newDist);
				}
			}
		}

	}
	
	public int[] shortestPath(int[][] ad, int from, int to){
		// returns the vertex numbers of the shortest path 
		// (hopping distance) fromn 'from' to 'to' or 'null'
		// if no path exists
		int n = ad.length;
		int [] visitedFrom = new int[n];
		Arrays.fill(visitedFrom,-1);
		visitedFrom[to] = to;
		LinkedList<Integer> q = new LinkedList<>();
		q.addLast(to); // we start at to (for directed graphs!)
		boolean found=false;
		while ( ! q.isEmpty() && ! found){
			int p = q.removeFirst();
			for (int i= 0;i<n;i++){
				// we take backwards direction!
				if (ad[i][p]==1 && visitedFrom[i] == -1 ){
					visitedFrom[i] = p;
					q.addLast(i);
					if (i==from) found = true;
				}
			}
		}
		
		if (visitedFrom[from] == -1) return null;
		int len = 2;
		int p = from;
		// get the length of the path
		while (visitedFrom[p] != to) {
			len++;
			p=visitedFrom[p];
		}
		// now we construct the path
		int [] path = new int[len];
		for (int i=0; i<len; i++) {
			path[i] = from;
			from = visitedFrom[from];
		}
		return path;
	}
	
	public boolean isConnected(int ad[][]){
		int n = ad.length;
		boolean [] visited = new boolean[n];
		visitDFS(ad,0,visited);
		for (boolean v:visited) if ( ! v) return false;
		return true;
	}

	private void visitDFS(int [][] ad, int p, boolean[] visited) {
		visited[p] = true;
		for (int i = 0; i < ad.length; i++){
			if (ad[p][i]==1 && ! visited[i]) visitDFS(ad, i, visited);
		}
	}


	/**
	 * @return the number of connected components of 'g'
	 */
	private int numberOfConnectedComponents() {
		int cnt = 1;
		Vertex<V> v = g.aVertex();
		// visit all vertices which can be reached from v
		visitDFS(v);
		// check wether all vertices have the 
		// VISITED attribute (and remove it)
		Iterator<Vertex<V>> it = g.vertices();
		while (it.hasNext()){
			Vertex<V> w = it.next();
			if (! w.has(VISITED)){
				cnt++;
				visitDFS(w);			
			}
			w.destroy(VISITED);
		}

		return  cnt;
	}

	/**
	 * @return true if 'g' is connected
	 */
	private boolean isConnected() {
		Vertex<V> v = g.aVertex();
		// visit all vertices which can be reached from v
		visitDFS(v);
		// check wether all vertices have the 
		// VISITED attribute (and remove it)
		Iterator<Vertex<V>> it = g.vertices();
		int cnt = 0;
		while (it.hasNext()){
			Vertex<V> w = it.next();
			if (w.has(VISITED)){
				cnt++;
				w.destroy(VISITED); // clean w
			}
		}
		return g.numberOfVertices() == cnt;
	}

	
	/**
	 * vists all vertices starting with 'v' which
	 * can be reached
	 * @param v
	 */
	private void visitDFS(Vertex<V> v) {
		v.set(VISITED,null);
		Iterator<Edge<E>> it = g.incidentEdges(v);
		while (it.hasNext()){
			Vertex<V> w = g.opposite(it.next(), v);
			if ( ! w.has(VISITED)) visitDFS(w);
		}
	}


	
	
	/**
	 * @param from
	 * @param to
	 * @return the shortest path from vertex 'from' to vertex 'to'
	 *  or null if no path exists
	 */
	public Vertex<V>[] shortestPath(Vertex<V> from, Vertex<V> to ){
		ArrayList<Vertex<V>> al = new ArrayList<Vertex<V>>();
		if (from.has(to)){
			while (from != to){
				al.add(from);
				from = (Vertex<V>)from.get(to);
			}
			al.add(to);
			return al.toArray(new Vertex[0]);
		}
		else return null;
	}
	
	
	
	private void setGW(Vertex<V> s){
		// sets for all (reachable) vertices the attribute 's' to the value 
		// 'g' where 'g' is the first vertex on the shortest 
		// path to 's' (considering 'hopping' distance)
		s.set(s, s);
		LinkedList<Vertex<V>> q = new LinkedList<>(); 
		q.addLast(s);
		while( ! q.isEmpty()){
			Vertex<V> v = q.removeFirst();
			// now find all neighbours 'w'
			Iterator<Edge<E>> it = g.incidentEdges(v);
			while (it.hasNext()){
				Vertex<V> w = g.opposite(it.next(), v);
				if ( ! w.has(s)){
					w.set(s,v);
					q.addLast(w);
				}
				
			}
 		}
	}
	

	public void setGateways(){
		// sets for all nodes 'v' the attribute 'w' with value 'g'. 
		// which means that the first node on the shortest path
		// from 'v' to 'w' is 'g'
		Iterator<Vertex<V>> it = g.vertices();
		while (it.hasNext()) dijkstra(it.next());
	}
	
	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		// make an undirected graph
		IncidenceListGraph<String,String> g = 
			new IncidenceListGraph<>(false);
		GraphExamples<String,String> ge = new GraphExamples<>(g);
		Vertex vA = g.insertVertex("A");
		Vertex vB = g.insertVertex("B");
		Vertex vC = g.insertVertex("C");
		Vertex vD = g.insertVertex("D");
		Vertex vE = g.insertVertex("E");
		Vertex vF = g.insertVertex("F");
		Vertex vG = g.insertVertex("G");
		Edge e_a = g.insertEdge(vA,vB,"AB");
		Edge e_b = g.insertEdge(vD,vC,"DC");
		Edge e_c = g.insertEdge(vD,vB,"DB");
		Edge e_d = g.insertEdge(vC,vB,"CB");
		Edge e_e = g.insertEdge(vC,vE,"CE");
		e_e.set(WEIGHT,2.0);
		Edge e_f = g.insertEdge(vB,vE,"BE"); 
		e_f.set(WEIGHT, 7.0); 
		Edge e_g = g.insertEdge(vD,vE,"DE");
		Edge e_h = g.insertEdge(vE,vG,"EG");
		e_h.set(WEIGHT,3.0);
		Edge e_i = g.insertEdge(vG,vF,"GF");
		Edge e_j = g.insertEdge(vF,vE,"FE");
		//System.out.println(g);
		ge.setGateways();
		
		new GraphTool(ge);
		
//		System.out.print("Path: ");
//		Vertex<String> [] path = ge.shortestPath(vA,vG);
//		if (path == null) System.out.println("no path");
//		else {
//			for(Vertex<String>v:path) System.out.print(v);
//		}
//		System.out.println();
//		ge.setNumbers();
//		int [][] ad = ge.getAdjacencyMatrix();
//		System.out.println(ge.isConnected(ad));
//		int [] spath = ge.shortestPath(ad,(int)vC.get(NUMBER),(int)vF.get(NUMBER));
//		if (spath == null) System.out.println("no path");
//		else {
//			for (int i=0;i<spath.length;i++){
//				System.out.println(ge.vertexArray[spath[i]]);
//			}
//		}

		// definition to serialize an object.
//		try {
//			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("o.ser")));
//		    oos.writeObject(g);
//		    oos.close();
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		ObjectInputStream ois;
//		try {
//			ois = new ObjectInputStream(                                 
//			        new FileInputStream(  new File("o.ser")) );
//			try {
//				g = (IncidenceListGraph<String,String>) ois.readObject();
//			} catch (ClassNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}	
//
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		int [][] gw = ge.getGatewayMatrix(ad);
//		int n = gw.length;
//		for (int i=0;i<n;i++) System.out.println(ge.vertexArray[i]+", "+i);
//		for (int i=0;i<n;i++){
//			System.out.println();
//			for (int k=0;k<n;k++){
//				System.out.print(gw[i][k]+", ");
//			}
//		}
//		System.out.println();
//		ge.kruskal();
//		Iterator<Edge<String>> eIt = g.edges();
//		while(eIt.hasNext()){
//			Edge<String> e = eIt.next();
//			if (e.has(MSF))
// 			System.out.println(e);
//		}
//		
//	A__B     F
//	  /|\   /|
//	 / | \ / |
//	C__D__E__G   
//	\     /
//	 \___/
//      
		}


}

