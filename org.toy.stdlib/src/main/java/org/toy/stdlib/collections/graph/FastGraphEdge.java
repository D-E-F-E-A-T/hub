package org.toy.stdlib.collections.graph;

public interface FastGraphEdge<N extends FastGraphVertex> {
	N src();

	N dst();
}
