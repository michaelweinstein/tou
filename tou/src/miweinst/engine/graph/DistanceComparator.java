package miweinst.engine.graph;

import java.util.Comparator;


public class DistanceComparator<E> implements Comparator<GraphNode<E>> {

	@Override
	public int compare(GraphNode<E> n1, GraphNode<E> n2) {
		if (n1.getDist() < n2.getDist()) {
			return -1;
		}
		if (n1.getDist() > n2.getDist()) {
			return 1;
		}
		return 0;
	}

}
