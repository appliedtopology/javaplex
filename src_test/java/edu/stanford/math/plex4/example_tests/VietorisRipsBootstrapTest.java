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
	public void testVdSExample() throws IOException {
		double[][] points = PointCloudExamples.getEquispacedCirclePoints(1000);
		double maxDistance = 0.2;
		int maxDimension = 1;
		int numSelections = 4;
		int selectionSize = 10;
		
		RandomUtility.initializeWithSeed(0);	
	
		VietorisRipsBootstrapper bootstrapper = new VietorisRipsBootstrapper(points, maxDistance, maxDimension, numSelections, selectionSize);
		BarcodeCollection<Integer> barcodes = bootstrapper.performBootstrap();
		
		System.out.println("Zigzag barcodes");
		System.out.println(barcodes);
	}
}
