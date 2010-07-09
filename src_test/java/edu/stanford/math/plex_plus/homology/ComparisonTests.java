package edu.stanford.math.plex_plus.homology;


import org.apache.commons.math.fraction.Fraction;

import cern.colt.Timer;
import edu.stanford.math.plex.EuclideanArrayData;
import edu.stanford.math.plex.Plex;
import edu.stanford.math.plex.WitnessStream;
import edu.stanford.math.plex.PersistenceInterval.Float;
import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex_plus.examples.PointCloudExamples;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_plus.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex_plus.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex_plus.math.metric.landmark.RandomLandmarkSelector;
import edu.stanford.math.plex_plus.math.metric.utility.MetricUtility;

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
	
	public static void sphereTestVersion4() {
		int n = 100;
		int landmarkPoints = 50;
		int d = 1;
		double maxFiltrationValue = 0.8;
		double[][] points = PointCloudExamples.getRandomSpherePoints(n, d);
		
		SearchableFiniteMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points);
		
		double diameterEstimate = MetricUtility.estimateDiameter(metricSpace);
		System.out.println("Diameter estimate: " + diameterEstimate);
		
		LandmarkSelector<double[]> selector = new RandomLandmarkSelector<double[]>(metricSpace, landmarkPoints);
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, selector, d + 1, maxFiltrationValue);
		stream.finalizeStream();
		
		GenericPersistenceAlgorithm<Fraction, Simplex> homology = new GenericAbsoluteCohomology<Fraction, Simplex>(RationalField.getInstance(), SimplexComparator.getInstance(), d + 1);
		BarcodeCollection barcodes = homology.computeIntervals(stream);
		System.out.println(barcodes.getInfiniteIntervals());
		
		System.out.println("Classical");
		
		ClassicalPersistentHomology<Simplex> classicalHomology = new ClassicalPersistentHomology<Simplex>(ModularIntField.getInstance(13), SimplexComparator.getInstance());
		BarcodeCollection classicalBarcodes = classicalHomology.computeIntervals(stream, d + 1);
		System.out.println(classicalBarcodes.getInfiniteIntervals());
	}
	
	public static void sphereTestVersion3() {
		int n = 100;
		int landmarkPoints = 50;
		int d = 1;
		double maxFiltrationValue = 0.8;
		double[][] points = PointCloudExamples.getRandomSpherePoints(n, d);
		
		EuclideanArrayData pData = Plex.EuclideanArrayData(points);
		int[] L = WitnessStream.makeRandomLandmarks(pData, landmarkPoints);
		
		WitnessStream wit = Plex.WitnessStream(0.1, d + 1, maxFiltrationValue, L, pData);
		
		
		Float[] intervals = Plex.Persistence().computeIntervals(wit);
		System.out.println(Plex.FilterInfinite(intervals));
		
	}
}
