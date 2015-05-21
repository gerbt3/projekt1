package examples;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import domain.Algorithm;
import domain.Attribut;
import domain.GraphTool;


public class GraphExamples<V,E> {

	private Vertex<V> [] vertexArray;

	public void setNumbers(Graph<V,E> g){
		vertexArray = new  Vertex[g.numberOfVertices()];
		Iterator<Vertex<V>> it = g.vertices();
		int num = 0;
		while(it.hasNext()) {
			vertexArray[num]=it.next();
			vertexArray[num].set(Attribut.NUMBER, num++);
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



	public int[][] getAdjacencyMatrix(Graph<V,E> g){
		setNumbers(g);
		int n = g.numberOfVertices();
		int [][] ad = new int[n][n];
		boolean directed = g.isDirected();
		Iterator<Edge<E>> it = g.edges();
		while ( it.hasNext()) {
			Vertex<V>[] endPts = g.endVertices(it.next()); 
			int i = (int) endPts[0].get(Attribut.NUMBER);
			int k = (int) endPts[1].get(Attribut.NUMBER);
			ad[i][k]=1;
			if (! directed) ad[k][i]=1;
		}
		return ad;
	}

	@Algorithm
	public void kruskal(Graph<V,E> g, GraphTool t){
//-----------------------------------------------------------------
		t.serializeAlgoGraph(g);
//-----------------------------------------------------------------
		
		// gives the Object MSF to each
		// edge belonging to an minimal spanning forest

		// create clusters, put the vertex in it  
		// and assign them to the vertices
		Iterator<Vertex<V>> it = g.vertices();
		while(it.hasNext()){
			Vertex<V> v = it.next();
			ArrayList<Vertex<V>> cluster = new ArrayList<>();
			cluster.add(v);
			v.set(Attribut.CLUSTER,cluster);
		}
		PriorityQueue<Double,Edge<E>> pq = new MyPriorityQueue<>();
		Iterator<Edge<E>> eIt = g.edges();
		while(eIt.hasNext()){
			Edge<E> e = eIt.next();
			double weight = 1.0;
//-----------------------------------------------------------------
			//Original version: if (e.has(Attribut.weight)) weight = (Double)e.get(Attribut.weight);
			
			//Whether the graph comes from graphexamples or not it has to be casted differently
			if (e.has(Attribut.weight)) {
				
				if (e.get(Attribut.weight) instanceof String) {
					
					try {
						weight = Double.parseDouble((String) e.get(Attribut.weight));
					} catch (NumberFormatException ex) {
						//If the string cannot be parsed, set the weight to 0
						System.out.println("@GraphExamples: Failed to parse a string to a double");
						weight = 0;
					}
				}
					
				//For graphs from graphexamples
				else weight = (Double)e.get(Attribut.weight);
			}
//-----------------------------------------------------------------
			pq.insert(weight,e);
		}
		while ( ! pq.isEmpty()){
			Edge<E> e = pq.removeMin().element();
			Vertex<V>[] endPts = g.endVertices(e);
			ArrayList<Vertex<V>> cluster1 = (ArrayList<Vertex<V>>)endPts[0].get(Attribut.CLUSTER);
			ArrayList<Vertex<V>> cluster2 = (ArrayList<Vertex<V>>)endPts[1].get(Attribut.CLUSTER);
			if (cluster1 != cluster2){
				e.set(Attribut.MSF,null);
//-----------------------------------------------------------------
				e.set(Attribut.color, Color.red);
				t.serializeAlgoGraph(g);
//-----------------------------------------------------------------
				// merge the two clusters
				// make sure that cluster2 is the smaller
				if (cluster1.size() > cluster2.size()){
					for (Vertex<V> v: cluster2){
						v.set(Attribut.CLUSTER,cluster1);
//-----------------------------------------------------------------
						v.set(Attribut.color, Color.red);
						t.serializeAlgoGraph(g);
//-----------------------------------------------------------------
						cluster1.add(v);
					}
				}
				else{
					for (Vertex<V> v: cluster1){
						v.set(Attribut.CLUSTER,cluster2);
//-----------------------------------------------------------------
						v.set(Attribut.color, Color.red);
						t.serializeAlgoGraph(g);
//-----------------------------------------------------------------
						cluster2.add(v);
					}
				}
			}            
		}
	}

	@Algorithm(vertex=true)
	public void dijkstra(Graph<V,E> g,Vertex<V> s, GraphTool t){
//-----------------------------------------------------------------
		t.serializeAlgoGraph(g);
//-----------------------------------------------------------------
		
		// sets the attribute 's' of each vertex 'u' from wich 
		// we can reach 's' to 'g' where 'g' is the gateway
		// of 'u' on the shortest path from 'u' to 's' 
		MyPriorityQueue<Double, Vertex<V>> pq = new MyPriorityQueue<>();
		Iterator<Vertex<V>> it = g.vertices();
		// put all vertices to pq and give them
		// an attribute Attribut.DISTANCE and PQLOCATOR
		while(it.hasNext()){
			Vertex<V> v = it.next();
			v.set(Attribut.DISTANCE,Double.POSITIVE_INFINITY);
			//-------
			v.set(Attribut.string,"inf");
			//-------
			Locator<Double,Vertex<V>> loc = pq.insert(Double.POSITIVE_INFINITY,v);
			v.set(Attribut.PQLOCATOR,loc);
//-----------------------------------------------------------------
			v.set(Attribut.color, Color.red);
			t.serializeAlgoGraph(g);
//-----------------------------------------------------------------
		}
		// correct the attributes for s
		//-------
		s.set(Attribut.string,"0");
		//-------
		s.set(Attribut.DISTANCE,0.0);
//-->	pq.replaceKey((Locator<Double,Vertex<V>>)s.get(Attribut.PQLOCATOR),0.0);
		while( ! pq.isEmpty()){
			Vertex<V> u = pq.removeMin().element();    
			///-------
			u.set(Attribut.color,Color.RED);
			if (u.has(Attribut.DISCOVERY)){
				Edge<E> e = (Edge<E>)u.get(Attribut.DISCOVERY);
//-----------------------------------------------------------------
				e.set(Attribut.color,Color.RED);
				t.serializeAlgoGraph(g);
//-----------------------------------------------------------------
			}

			// now make the relaxation step for all 
			// neighbours:
			Iterator<Edge<E>> eIt;
			if (g.isDirected()) eIt = g.incidentInEdges(u); // backwards!
			else eIt = g.incidentEdges(u);
			while (eIt.hasNext()){
				Edge<E> e = eIt.next();
				double weight = 1.0; // default weight
//-----------------------------------------------------------------
				//Original version: if (e.has(Attribut.weight)) weight = (Double)e.get(Attribut.weight);
				
				//Whether the graph comes from graphexamples or not it has to be casted differently
				if (e.has(Attribut.weight)) {
					
					if (e.get(Attribut.weight) instanceof String) {
						
						try {
							weight = Double.parseDouble((String) e.get(Attribut.weight));
						} catch (NumberFormatException ex) {
							//If the string cannot be parsed, set the weight to 0
							System.out.println("@GraphExamples: Failed to parse a string to a double");
							weight = 0;
						}
					}
						
					//For graphs from graphexamples
					else weight = (Double)e.get(Attribut.weight);
				}
//-----------------------------------------------------------------				
				Vertex<V> z = g.opposite(e, u);
				//Relaxation
				double newDist = (Double) u.get(Attribut.DISTANCE) + weight;
				if (newDist < (Double) z.get(Attribut.DISTANCE)) {
					z.set(Attribut.DISTANCE,newDist); // new distance of z (can be changed later!)
					z.set(Attribut.string,""+newDist);
					z.set(Attribut.DISCOVERY,e);
					z.set(s, u); // gateway (will eventually be changed later!)
//-->				pq.replaceKey((Locator<Double,Vertex<V>>) z.get(Attribut.PQLOCATOR), newDist);
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
	private int numberOfConnectedComponents(Graph<V,E> g) {
		int cnt = 1;
		Vertex<V> v = g.aVertex();
		// visit all vertices which can be reached from v
		visitDFS(g,v);
		// check whether all vertices have the 
		// VISITED attribute (and remove it)
		Iterator<Vertex<V>> it = g.vertices();
		while (it.hasNext()){
			Vertex<V> w = it.next();
			if (! w.has(Attribut.VISITED)){
				cnt++;
				visitDFS(g,w);            
			}
			w.destroy(Attribut.VISITED);
		}

		return  cnt;
	}

	/**
	 * @return true if 'g' is connected
	 */
	private boolean isConnected(Graph<V,E> g) {
		Vertex<V> v = g.aVertex();
		// visit all vertices which can be reached from v
		visitDFS(g,v);
		// check whether all vertices have the 
		// VISITED attribute (and remove it)
		Iterator<Vertex<V>> it = g.vertices();
		int cnt = 0;
		while (it.hasNext()){
			Vertex<V> w = it.next();
			if (w.has(Attribut.VISITED)){
				cnt++;
				w.destroy(Attribut.VISITED); // clean w
			}
		}
		return g.numberOfVertices() == cnt;
	}


	/**
	 * vists all vertices starting with 'v' which
	 * can be reached
	 * @param v
	 */
	@Algorithm
	private void visitDFS(Graph<V,E> g,Vertex<V> v) {
		v.set(Attribut.VISITED,null);
		Iterator<Edge<E>> it = g.incidentEdges(v);
		while (it.hasNext()){
			Vertex<V> w = g.opposite(it.next(), v);
			if ( ! w.has(Attribut.VISITED)) visitDFS(g,w);
		}
	}




	/**
	 * @param from
	 * @param to
	 * @return the shortest path from vertex 'from' to vertex 'to'
	 *  or null if no path exists
	 */
	public Vertex<V>[] shortestPath(Vertex<V> from, Vertex<V> to){
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



	private void setGW(Graph<V,E> g, Vertex<V> s){
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


	public void setGateways(Graph g){
		// sets for all nodes 'v' the attribute 'w' with value 'g'. 
		// which means that the first node on the shortest path
		// from 'v' to 'w' is 'g'
		Iterator<Vertex<V>> it = g.vertices();
		while (it.hasNext()) dijkstra(g,it.next(), null);
	}

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		
		// make an undirected graph
		IncidenceListGraph<String,String> g = 
				new IncidenceListGraph<>(false);
		GraphExamples<String,String> ge = new GraphExamples<>();
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
		e_e.set(Attribut.weight,2.0);
		Edge e_f = g.insertEdge(vB,vE,"BE"); 
		e_f.set(Attribut.weight, 7.0); 
		Edge e_g = g.insertEdge(vD,vE,"DE");
		Edge e_h = g.insertEdge(vE,vG,"EG");
		e_h.set(Attribut.weight,3.0);
		Edge e_i = g.insertEdge(vG,vF,"GF");
		Edge e_j = g.insertEdge(vF,vE,"FE");

		GraphTool t = new GraphTool(g, ge);
		
		//    A__B     F
		//      /|\   /|
		//     / | \ / |
		//    C__D__E__G   
		//    \     /
		//     \___/
		//      
	}


}
