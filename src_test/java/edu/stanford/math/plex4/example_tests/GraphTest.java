package edu.stanford.math.plex4.example_tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.UndirectedListGraph;

public class GraphTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGraph() {
		AbstractUndirectedGraph graph = new UndirectedListGraph(4);
		
		graph.addEdge(0, 1);
		assertTrue("numEdges does not work", graph.getNumEdges() == 1);
		graph.addEdge(0, 1);
		assertTrue("numEdges does not work", graph.getNumEdges() == 1);
		
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 0);
		assertTrue("numEdges does not work", graph.getNumEdges() == 4);
	}
	
	@Test
	public void testCompleteGraph() {
		int n = 1000;
		AbstractUndirectedGraph graph = new UndirectedListGraph(n);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				graph.addEdge(i, j);
			}
		}

		assertTrue("numEdges does not work", graph.getNumEdges() == n * (n - 1) / 2);
	}
}
