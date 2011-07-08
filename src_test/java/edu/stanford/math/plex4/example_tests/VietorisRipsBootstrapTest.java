package edu.stanford.math.plex4.example_tests;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.zigzag.bootstrap.VietorisRipsBootstrapper;
import edu.stanford.math.plex4.utility.RandomUtility;

public class VietorisRipsBootstrapTest {
	@Before
	public void setUp() {}

	@After
	public void tearDown() {}
	
	@Test
	public void testCircle() throws IOException {
		RandomUtility.initializeWithSeed(0);
		
		double[][] points = PointCloudExamples.getEquispacedCirclePoints(10000);
		double maxDistance = 1.3;
		int maxDimension = 1;
		int numSelections = 10;
		int selectionSize = 20;
		
	
		VietorisRipsBootstrapper bootstrapper = new VietorisRipsBootstrapper(points, maxDistance, maxDimension, numSelections, selectionSize);
		BarcodeCollection<Integer> barcodes = bootstrapper.performBootstrap();
		
		System.out.println("Zigzag barcodes");
		System.out.println(barcodes);
	}
}
