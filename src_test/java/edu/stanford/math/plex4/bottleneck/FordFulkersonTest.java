package edu.stanford.math.plex4.bottleneck;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FordFulkersonTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void test1() {
		FlowNetwork network = new FlowNetwork();
		network.addEdge(1, 2, 3);
		network.addEdge(1, 3, 3);
		network.addEdge(2, 3, 2);
		network.addEdge(2, 4, 3);
		network.addEdge(3, 5, 2);
		network.addEdge(4, 5, 4);
		network.addEdge(4, 6, 2);
		network.addEdge(5, 6, 3);

		int flow = network.maxFlow(1, 6);
		
		System.out.println("Computed flow: " + flow);
		
		assertTrue("Incorrect flow computed", flow == 5);
		
	}
	
	@Test
	public void test2() {
		FlowNetwork network = new FlowNetwork();
		network.addEdge(1, 2, 1000);
		network.addEdge(1, 3, 1000);
		network.addEdge(2, 3, 1);
		network.addEdge(2, 4, 1000);
		network.addEdge(3, 4, 1000);

		int flow = network.maxFlow(1, 4);
		
		System.out.println("Computed flow: " + flow);
		
		assertTrue("Incorrect flow computed", flow == 2000);
		
	}
}
