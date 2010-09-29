package edu.stanford.math.plex4.unit_tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.LazyWitnessSpecifier;
import edu.stanford.math.plex4.homology.PersistenceAlgorithmResult;
import edu.stanford.math.plex4.homology.PersistenceAlgorithmTester;
import edu.stanford.math.plex4.homology.PersistenceCalculationData.PersistenceAlgorithmType;
import edu.stanford.math.plex4.homology.VietorisRipsSpecifier;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.utility.RandomUtility;
import gnu.trove.THashMap;




public class PersistenceAlgorithmTest {

	// 2-D point cloud examples
	//private List<double[][]> pointCloudExamples = new ArrayList<double[][]>();
	
	private List<VietorisRipsSpecifier> vietorisRipsExamples = new ArrayList<VietorisRipsSpecifier>();
	private List<LazyWitnessSpecifier> lazyWitnessExamples = new ArrayList<LazyWitnessSpecifier>();
	
	private List<GenericPair<ExplicitStream<Simplex>, int[]>> explicitStreamExamples = new ArrayList<GenericPair<ExplicitStream<Simplex>, int[]>>();

	private List<PersistenceAlgorithmType> persistenceAlgorithms = new ArrayList<PersistenceAlgorithmType>();
	
	@Before
	public void setUp() {
		int n = 200;
		int d = 3;
		int l = 50;
		
		RandomUtility.initializeWithSeed(3);
		
		//vietorisRipsExamples.add(new VietorisRipsSpecifier(PointCloudExamples.getEquispacedCirclePoints(4), 3, 2, 100));
		//vietorisRipsExamples.add(new VietorisRipsSpecifier(PointCloudExamples.getEquispacedCirclePoints(n), 2, 0.3, 10));
		//vietorisRipsExamples.add(new VietorisRipsSpecifier(PointCloudExamples.getHouseExample(), 2, 2, 10));
		vietorisRipsExamples.add(new VietorisRipsSpecifier(PointCloudExamples.getSquare(), 2, 2, 10));
		//vietorisRipsExamples.add(new VietorisRipsSpecifier(PointCloudExamples.getGaussianPoints(n, d), d, 0.4, 10));
		//vietorisRipsExamples.add(new VietorisRipsSpecifier(PointCloudExamples.getRandomSpherePoints(n, d), d, 0.4, 10));
		
		/*
		lazyWitnessExamples.add(new LazyWitnessSpecifier(new MaxMinLandmarkSelector<double[]>(new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(n)), l), 2, 0.5, 10));
		lazyWitnessExamples.add(new LazyWitnessSpecifier(new MaxMinLandmarkSelector<double[]>(new EuclideanMetricSpace(PointCloudExamples.getGaussianPoints(n, d)), l), d, 0.5, 10));
		lazyWitnessExamples.add(new LazyWitnessSpecifier(new MaxMinLandmarkSelector<double[]>(new EuclideanMetricSpace(PointCloudExamples.getRandomFigure8Points(n)), l), 2, 0.5, 10));
		lazyWitnessExamples.add(new LazyWitnessSpecifier(new MaxMinLandmarkSelector<double[]>(new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(n, d)), l), 2, 0.5, 10));
		lazyWitnessExamples.add(new LazyWitnessSpecifier(new MaxMinLandmarkSelector<double[]>(new EuclideanMetricSpace(PointCloudExamples.getRandomTorusPoints(n, 1, 2)), l), 2, 0.5, 10));	
		*/
		/*
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getTriangle(), new int[]{1, 1}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getFilteredTriangle(), new int[]{1, 0}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getSimplicialSphere(4), new int[]{1, 0, 0, 0, 1}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getZomorodianCarlssonExample(), new int[]{1, 0}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getTorus(), new int[]{1, 2, 1}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getCircle(n), new int[]{1, 1}));
		
		*/
		persistenceAlgorithms.add(PersistenceAlgorithmType.Plex3Homology);
		//persistenceAlgorithms.add(PersistenceAlgorithmType.GenericClassicalHomology);
		//persistenceAlgorithms.add(PersistenceAlgorithmType.IntAbsoluteHomology);
		//persistenceAlgorithms.add(PersistenceAlgorithmType.GenericAbsoluteHomology);
		persistenceAlgorithms.add(PersistenceAlgorithmType.GenericAbsoluteCohomology);
		
	}

	@After
	public void tearDown() {
		//vietorisRipsExamples = null;
	}
	
	@Test
	public void testVietorisRipsExamples() {
		for (VietorisRipsSpecifier example: this.vietorisRipsExamples) {
			THashMap<PersistenceAlgorithmType, PersistenceAlgorithmResult> persistenceResults = new THashMap<PersistenceAlgorithmType, PersistenceAlgorithmResult>();
			
			for (PersistenceAlgorithmType type: this.persistenceAlgorithms) {
				PersistenceAlgorithmResult result = PersistenceAlgorithmTester.testVietorisRipsStream(example.getPointCloud(), example.getMaxDimension(), example.getMaxFiltrationValue(), example.getNumDivisions(), type);
				persistenceResults.put(type, result);
				System.out.println(result);
				
				System.out.println(result.getBarcodeCollection());
			}
			
			System.out.println("================");
			
			this.printBarcodeComparisons(persistenceResults);
		}
	}
	
	@Test
	public void testLazyWitnessExamples() {
		for (LazyWitnessSpecifier example: this.lazyWitnessExamples) {
			THashMap<PersistenceAlgorithmType, PersistenceAlgorithmResult> persistenceResults = new THashMap<PersistenceAlgorithmType, PersistenceAlgorithmResult>();
			
			for (PersistenceAlgorithmType type: this.persistenceAlgorithms) {
				PersistenceAlgorithmResult result = PersistenceAlgorithmTester.testLazyWitnessStream(example.getSelector(), example.getMaxDimension(), example.getMaxFiltrationValue(), example.getNumDivisions(), type);
				persistenceResults.put(type, result);
				System.out.println(result);
			}
			
			System.out.println("================");
			
			this.printBarcodeComparisons(persistenceResults);
		}
	}
	
	//@Test
	private void testExplicitExamples() {
		for (GenericPair<ExplicitStream<Simplex>, int[]> pair: this.explicitStreamExamples) {
			ExplicitStream<Simplex> stream = pair.getFirst();
			THashMap<PersistenceAlgorithmType, PersistenceAlgorithmResult> persistenceResults = new THashMap<PersistenceAlgorithmType, PersistenceAlgorithmResult>();
			
			System.out.println("actual: " + Arrays.toString(pair.getSecond()));
			
			for (PersistenceAlgorithmType type: this.persistenceAlgorithms) {
				PersistenceAlgorithmResult result = PersistenceAlgorithmTester.testExplicitStream(stream, 5, type);
				persistenceResults.put(type, result);
				
				int[] computedBettiSequence = result.getBarcodeCollection().getInfiniteIntervals().getBettiSequence();
				System.out.println(type + ": " + Arrays.toString(computedBettiSequence));
				
				assertTrue(HomologyUtility.compareIntArrays(computedBettiSequence, pair.getSecond()) == 0);
			}
			
			System.out.println("================");
		}
	}
	
	private void printBarcodeComparisons(THashMap<PersistenceAlgorithmType, PersistenceAlgorithmResult> persistenceResults) {
		int numFailures = 0;
		
		for (PersistenceAlgorithmType algorithm_type_1: this.persistenceAlgorithms) {
			BarcodeCollection bc_1 = persistenceResults.get(algorithm_type_1).getBarcodeCollection();
			for (PersistenceAlgorithmType algorithm_type_2: this.persistenceAlgorithms) {
				if (algorithm_type_1 == algorithm_type_2) {
					break;
				}
				
				BarcodeCollection bc_2 = persistenceResults.get(algorithm_type_2).getBarcodeCollection();
				
				System.out.print(algorithm_type_1.toString() + "-" + algorithm_type_2.toString());
				
				if (bc_1.equals(bc_2)) {
					System.out.println(": pass");
				} else {
					System.out.println(": fail");
					numFailures++;
				}
			}
		}
		
		System.out.println("================");
		
		//assertTrue(numFailures == 0);
	}
}
