package org.toy.stdlib.collections.graph.undirected;

import org.toy.stdlib.collections.graph.FastUndirectedGraph;
import org.toy.stdlib.collections.graph.util.FakeFastEdge;
import org.toy.stdlib.collections.graph.util.FakeFastVertex;

public class FakeFastUndirectedGraph extends FastUndirectedGraph<FakeFastVertex, FakeFastEdge> {

	public FakeFastEdge pubGetSisterEdge(FakeFastEdge e) {
		try {
			return super.getSisterEdge(e);
		} catch(Throwable ex) {
			return null;
		}
	}
	
	@Override
	public FakeFastEdge clone(FakeFastEdge e, FakeFastVertex src, FakeFastVertex dst) {
		return new FakeFastEdge(src, dst, false);
	}
	
	@Override
	public String toString() {
		return makeDotGraph().toString();
	}
}
