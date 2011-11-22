package edu.stanford.math.plex4.bottleneck;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;

public class BottleneckDistanceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test1() {
		BarcodeCollection<Double> collection1 = new BarcodeCollection<Double>();
		collection1.addInterval(0, 1.0, 2.0);
		
		BarcodeCollection<Double> collection2 = new BarcodeCollection<Double>();
		collection2.addInterval(0, 2.0, 3.0);
		collection2.addInterval(0, 0.1, 0.2);
		
		double distance = BottleneckDistance.computeBottleneckDistance(collection1.getIntervalsAtDimension(0), collection2.getIntervalsAtDimension(0));
		System.out.println(distance);
	}
}
