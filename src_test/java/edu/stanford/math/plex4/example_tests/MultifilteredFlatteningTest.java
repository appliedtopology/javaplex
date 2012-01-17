package edu.stanford.math.plex4.example_tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.streams.filter.FilterFunction;
import edu.stanford.math.plex4.streams.filter.IntFilterFunction;
import edu.stanford.math.plex4.streams.filter.KernelDensityFilterFunction;
import edu.stanford.math.plex4.streams.filter.MaxSimplicialFilterFunction;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.multi.AbstractMultifilteredStream;
import edu.stanford.math.plex4.streams.multi.BifilteredMetricStream;
import edu.stanford.math.plex4.streams.multi.IncreasingOrthantFlattener;

public class MultifilteredFlatteningTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		// initialize constants
		int n = 100;
		int maxDimension = 1;
		double maxFiltrationValue = 0.4;
		double sigma = 0.4;

		// set direction of sets
		double[] principalDirection = new double[] { 0.05, 0.01 };

		// create a new metric space from random points on a circle
		double[][] points = PointCloudExamples.getRandomCirclePoints(n);
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);

		// create a vietoris rips complex from the points
		VietorisRipsStream<double[]> stream = Plex4.createVietorisRipsStream(metricSpace, maxDimension + 1, maxFiltrationValue);
		stream.finalizeStream();

		// initialize the kernel density function
		IntFilterFunction intFilterFunction = new KernelDensityFilterFunction(metricSpace, sigma);
		FilterFunction<Simplex> simplexFilterFunction = new MaxSimplicialFilterFunction(intFilterFunction);

		// create the bifiltered stream
		AbstractMultifilteredStream<Simplex> multifilteredStream = new BifilteredMetricStream<Simplex>(stream, simplexFilterFunction);

		// create a "flattened" version of the stream by considering increasing
		// subsets
		IncreasingOrthantFlattener<Simplex> flattener = new IncreasingOrthantFlattener<Simplex>(principalDirection);
		AbstractFilteredStream<Simplex> flattenedStream = flattener.collapse(multifilteredStream);

		// compute the persistent homology of the flattened complex, and print
		// the result
		AbstractPersistenceAlgorithm<Simplex> persistenceAlgorithm = Plex4.getDefaultSimplicialAlgorithm(maxDimension + 1);
		BarcodeCollection<Double> barcodes = persistenceAlgorithm.computeIntervals(flattenedStream);
		System.out.println(barcodes.toString());
	}
}
