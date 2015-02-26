/**
 * 
 */
package examples;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * @author ps
 * Implements an array-heap based priority-queue with Locators
 *
 */
public class MyPriorityQueue<K extends Comparable<? super K>, E> implements
		PriorityQueue<K, E>, Serializable {

	
	class PQLocator<K1 extends Comparable<? super K1>,E1> implements Locator<K1, E1>, Serializable {

		K1 key;
		E1 elem;
		Object creator = MyPriorityQueue.this;
		int pos; // position in the heap-array
		
		/* (non-Javadoc)
		 * @see examples.Position#element()
		 */
		@Override
		public E1 element() {
			return elem;
		}

		/* (non-Javoadoc)
		 * @see examples.Locator#key()
		 */
		@Override
		public K1 key() {
			return key;
		}
		
	}
	
	
	private PQLocator<K,E>[] locs = (PQLocator<K,E>[]) new PQLocator[256]; 
	private int size=1; // we start at 1 because the navigation is simpler
	
	/* (non-Javadoc)
	 * @see examples.PriorityQueue#showMin()
	 */
	@Override
	public Locator<K, E> showMin() {
		return locs[1];
	}

	/* (non-Javadoc)
	 * @see examples.PriorityQueue#removeMin()
	 */
	@Override
	public Locator<K, E> removeMin() {
		Locator<K,E> ret = showMin();
		remove(ret);
		return ret;
	}

	/* (non-Javadoc)
	 * @see examples.PriorityQueue#insert(java.lang.Comparable, java.lang.Object)
	 */
	@Override
	public Locator<K, E> insert(K key, E element) {
		PQLocator<K,E> n = new PQLocator<>();
		n.key=key;
		n.elem=element;
		if (size==locs.length) expand();
		locs[size] = n;
		n.pos=size;
		upHeap(size);
		size++;
		return n;
	}

	/**
	 * 
	 */
	private void expand() {
		locs = Arrays.copyOf(locs,locs.length*2);
	}

	/**
	 * @param pos 
	 */
	private void upHeap(int i) {
		while (i > 1 && locs[i].key.compareTo(locs[i/2].key) < 0){
			swap(i,i/2);
			i=i/2;
		}
	}

	/**
	 * @param i
	 */
	private void downHeap(int i) {
		int left = i*2;
		while (left < size){
			int right = left+1;
			int cand = left;
			if (right < size && 
					locs[left].key.compareTo(locs[right].key) > 0) cand = right;
			if (locs[i].key.compareTo(locs[cand].key) <= 0) break;
			swap(i,cand);
			i=cand;
			left=i*2;
		}
		
	}
	

	
/**
	 * @param i
	 * @param j
	 */
	private void swap(int i, int k) {
		PQLocator<K,E> tmp = locs[i];
		locs[i]=locs[k];
		locs[k]=tmp;
		// do'nt forget the 'pos' values:
		locs[i].pos=i;
		locs[k].pos=k;		
	}

	/* (non-Javadoc)
	 * @see examples.PriorityQueue#remove(examples.Locator)
	 */
	@Override
	public void remove(Locator<K, E> loc) {
		PQLocator<K,E> l = (PQLocator<K,E>) loc;
		if (l.creator != this) throw new RuntimeException("invalid locator");
		int pos = l.pos;
		swap(pos,--size);
		l.creator = null;
		upHeap(pos);
		downHeap(pos);
	}

	/* (non-Javadoc)
	 * @see examples.PriorityQueue#replaceKey(examples.Locator, java.lang.Comparable)
	 */
	@Override
	public void replaceKey(Locator<K, E> loc, K newKey) {
		PQLocator<K,E> l = (PQLocator<K,E>) loc;
		if (l.creator != this) throw new RuntimeException("invalid locator"+loc.element());
		int comp = l.key.compareTo(newKey);
		l.key = newKey;
		if (comp<0) downHeap(l.pos);
		else if (comp>0) upHeap(l.pos);
	}

	/* (non-Javadoc)
	 * @see examples.PriorityQueue#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return size==1;
	}

	/* (non-Javado
	 * @see examples.PriorityQueue#size()
	 */
	@Override
	public int size() {
		return size;
	}

	private boolean isHeap(){
		for (int i=2; i<size;i++){
			if (locs[i].key.compareTo(locs[i/2].key)<0) return false;
		}
		return true;
	}
	static public void main(String[] argv){
		int N=10000000;
		HashMap<Double,String> hm = new HashMap<>();
		java.util.PriorityQueue<Double> s = new java.util.PriorityQueue<Double>();
		MyPriorityQueue<Double,String> pq = new MyPriorityQueue<>();
		Locator<Double,String>[] locs = new Locator[N];
		Random ra = new Random(63465);
		for(int i=0;i<N/2;i++) locs[i]=pq.insert(ra.nextDouble(),null);
		for(int i=0;i<N/2;i++) locs[i+N/2]=pq.insert(ra.nextDouble(),null);
		for(int i=0;i<N;i++) pq.removeMin();
		System.out.println(pq.isHeap());	
	}
}
