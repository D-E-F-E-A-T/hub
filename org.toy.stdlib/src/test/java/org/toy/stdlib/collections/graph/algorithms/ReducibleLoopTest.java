package org.toy.stdlib.collections.graph.algorithms;

import java.io.IOException;

import org.toy.stdlib.collections.graph.AbstractFastGraphTest;
import org.toy.stdlib.collections.graph.GraphUtils;
import org.toy.stdlib.collections.graph.directed.FakeFastDirectedGraph;

public class ReducibleLoopTest extends AbstractFastGraphTest {

	public ReducibleLoopTest() {
		super(true);
	}

	public void testIrreducible1() throws IOException {
		FakeFastDirectedGraph g = new FakeFastDirectedGraph();
		g.addVertex(node(1));
		g.addVertex(node(2));
		g.addVertex(node(3));
		
		g.addEdge(edge(1, 2));
		g.addEdge(edge(1, 3));
		g.addEdge(edge(2, 3));
		g.addEdge(edge(3, 2));
		
		assertFalse(GraphUtils.isReducibleGraph(g, node(1)));
	}
}
