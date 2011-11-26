package edu.stanford.math.plex4.bottleneck;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;

public class BottleneckDistanceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	//@Test
	public void test1() {
		BarcodeCollection<Double> collection1 = new BarcodeCollection<Double>();
		collection1.addInterval(0, 1.0, 2.0);
		
		BarcodeCollection<Double> collection2 = new BarcodeCollection<Double>();
		collection2.addInterval(0, 2.0, 3.0);
		collection2.addInterval(0, 0.1, 0.2);
		
		double distance = BottleneckDistance.computeBottleneckDistance(collection1.getIntervalsAtDimension(0), collection2.getIntervalsAtDimension(0));
		System.out.println(distance);
	}
	
	@Test
	public void test2() {
		double maxFiltrationValue = 0.2;
		int n = 100;
		
		double[][] points1 = PointCloudExamples.getRandomCirclePoints(n);
		double[][] points2 = PointCloudExamples.getRandomCirclePoints(n/2);
		
		VietorisRipsStream<double[]> vr_complex1 = Plex4.createVietorisRipsStream(points1, 1, maxFiltrationValue, 1000);
		VietorisRipsStream<double[]> vr_complex2 = Plex4.createVietorisRipsStream(points2, 1, maxFiltrationValue, 1000);
		
		AbstractPersistenceAlgorithm<Simplex> peristenceAlgorithm = Plex4.getDefaultSimplicialAlgorithm(1);
		
		BarcodeCollection<Double> barcodeCollection1 = peristenceAlgorithm.computeIntervals(vr_complex1);
		BarcodeCollection<Double> barcodeCollection2 = peristenceAlgorithm.computeIntervals(vr_complex2);
		
		List<Interval<Double>> collection1 = barcodeCollection1.getIntervalsAtDimension(0);
		List<Interval<Double>> collection2 = barcodeCollection2.getIntervalsAtDimension(0);
		
		collection1 = BottleneckDistance.truncate(collection1, 0, maxFiltrationValue);
		collection2 = BottleneckDistance.truncate(collection2, 0, maxFiltrationValue);
		
		double bottleneckDistance = BottleneckDistance.computeBottleneckDistance(collection1, collection2);
		
		System.out.println(bottleneckDistance);
	}
}
