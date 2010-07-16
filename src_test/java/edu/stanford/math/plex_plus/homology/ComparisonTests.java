package edu.stanford.math.plex_plus.homology;


import cern.colt.Arrays;
import cern.colt.Timer;
import edu.stanford.math.plex.EuclideanArrayData;
import edu.stanford.math.plex.PersistenceInterval;
import edu.stanford.math.plex.Plex;
import edu.stanford.math.plex.WitnessStream;
import edu.stanford.math.plex.PersistenceInterval.Float;
import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntegerField;
import edu.stanford.math.plex_plus.examples.MetricStreamExamples;
import edu.stanford.math.plex_plus.examples.PointCloudExamples;
import edu.stanford.math.plex_plus.examples.SimplexStreamExamples;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_plus.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex_plus.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex_plus.math.metric.landmark.RandomLandmarkSelector;

public class ComparisonTests {
	public static void main(String[] args) {
		Timer timer = new Timer();

		timer.start();
		sphereTestVersion3();
		timer.stop();
		timer.display();
		timer.reset();


		timer.start();
		sphereTestVersion4();
		timer.stop();
		timer.display();
		timer.reset();
	}

	public static void torusTestVersion4() {
		int n = 1000;
		int landmarkPoints = 300;
		double r = 1;
		double R = 2;
		double maxFiltrationValue = 0.4;
		double[][] torusPoints = PointCloudExamples.getRandomTorusPoints(n, r, R);

		SearchableFiniteMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(torusPoints);

		//double diameterEstimate = MetricUtility.estimateDiameter(metricSpace);

		LandmarkSelector<double[]> selector = new RandomLandmarkSelector<double[]>(metricSpace, landmarkPoints);
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, selector, 3, maxFiltrationValue);
		stream.finalizeStream();

		ClassicalPersistentHomology<Simplex> homology = new ClassicalPersistentHomology<Simplex>(ModularIntField.getInstance(13), SimplexComparator.getInstance());
		BarcodeCollection barcodes = homology.computeIntervals(stream, 2);
		System.out.println(barcodes);

		//GenericPersistentHomology<Fraction, Simplex> homology = new GenericPersistentHomology<Fraction, Simplex>(RationalField.getInstance(), SimplexComparator.getInstance(), 3);
		//AugmentedBarcodeCollection<GenericFormalSum<Fraction, Simplex>> barcodes = homology.computeIntervals(stream);
		//System.out.println(barcodes.getBettiNumbers(100));
	}

	public static void torusTestVersion3() {
		int n = 1000;
		int landmarkPoints = 300;
		double r = 1;
		double R = 2;
		double maxFiltrationValue = 0.4;
		double[][] torusPoints = PointCloudExamples.getRandomTorusPoints(n, r, R);

		EuclideanArrayData pData = Plex.EuclideanArrayData(torusPoints);
		int[] L = WitnessStream.makeRandomLandmarks(pData, landmarkPoints);

		WitnessStream wit = Plex.WitnessStream(0.001, 3, maxFiltrationValue, L, pData);
		Float[] intervals = Plex.Persistence().computeIntervals(wit);
		System.out.println(Plex.FilterInfinite(intervals));
	}

	public static void compareHomologyComputations(AbstractFilteredStream<Simplex> stream, int d) {
		computeClassicalHomology(stream, d);
		computeDualityHomology(stream, d);
		computeDualityCohomology(stream, d);
	}

	public static void computeClassicalHomology(AbstractFilteredStream<Simplex> stream, int d) {
		System.out.println("Classical");
		ClassicalPersistentHomology<Simplex> classicalHomology = new ClassicalPersistentHomology<Simplex>(ModularIntField.getInstance(13), SimplexComparator.getInstance());
		BarcodeCollection barcodes = classicalHomology.computeIntervals(stream, d + 1);
		System.out.println(barcodes);
	}


	public static void computeDualityHomology(AbstractFilteredStream<Simplex> stream, int d) {
		System.out.println("Duality Homology");
		GenericPersistenceAlgorithm<Integer, Simplex> homology = new GenericAbsoluteHomology<Integer, Simplex>(ModularIntegerField.getInstance(13), SimplexComparator.getInstance(), d);
		//AugmentedBarcodeCollection<AbstractGenericFormalSum<Integer, Simplex>> barcodes = homology.computeAugmentedIntervals(stream);
		BarcodeCollection barcodes = homology.computeIntervals(stream);
		System.out.println(barcodes);
	}

	public static void computeDualityCohomology(AbstractFilteredStream<Simplex> stream, int d) {
		System.out.println("Duality Cohomology");
		GenericPersistenceAlgorithm<Integer, Simplex> homology = new GenericAbsoluteCohomology<Integer, Simplex>(ModularIntegerField.getInstance(13), SimplexComparator.getInstance(), d);
		//AugmentedBarcodeCollection<AbstractGenericFormalSum<Integer, Simplex>> barcodes = homology.computeAugmentedIntervals(stream);
		BarcodeCollection barcodes = homology.computeIntervals(stream);
		System.out.println(barcodes);
	}




	public static void sphereTestVersion4() {
		int n = 1000;
		int landmarkPoints = 40;
		int d = 2;
		double maxFiltrationValue = 0.4;
		double[][] points = PointCloudExamples.getRandomSpherePoints(n, d);

		SearchableFiniteMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points);

		//double diameterEstimate = MetricUtility.estimateDiameter(metricSpace);
		//System.out.println("Diameter estimate: " + diameterEstimate);

		LandmarkSelector<double[]> selector = new RandomLandmarkSelector<double[]>(metricSpace, landmarkPoints);
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, selector, d + 1, maxFiltrationValue);
		stream.finalizeStream();

		//compareHomologyComputations(MetricStreamExamples.getLazyWitnessSphere(n, landmarkPoints, d, maxFiltrationValue), d);
		compareHomologyComputations(stream, d);
	}

	public static void sphereTestVersion3() {
		int n = 1000;
		int landmarkPoints = 30;
		int d = 1;
		double maxFiltrationValue = 0.4;
		double[][] points = PointCloudExamples.getRandomSpherePoints(n, d);

		EuclideanArrayData pData = Plex.EuclideanArrayData(points);
		int[] L = WitnessStream.makeRandomLandmarks(pData, landmarkPoints);

		WitnessStream wit = Plex.WitnessStream(0.0001, d + 1, maxFiltrationValue, L, pData);

		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(wit);
		System.out.println(Arrays.toString(intervals));

	}
}
