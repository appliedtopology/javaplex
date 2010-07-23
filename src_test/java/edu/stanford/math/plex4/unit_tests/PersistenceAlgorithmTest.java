package edu.stanford.math.plex4.unit_tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.colt.Arrays;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.PersistenceAlgorithmTester;
import edu.stanford.math.plex4.homology.PointCloudTestSpecifier;
import edu.stanford.math.plex4.homology.FiltrationSpecifier.FiltrationType;
import edu.stanford.math.plex4.homology.PersistenceCalculationData.PersistenceAlgorithmType;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import gnu.trove.map.hash.THashMap;


public class PersistenceAlgorithmTest {

	// 2-D point cloud examples
	//private List<double[][]> pointCloudExamples = new ArrayList<double[][]>();
	
	private List<PointCloudTestSpecifier> vietorisRipsExamples = new ArrayList<PointCloudTestSpecifier>();
	private List<GenericPair<ExplicitStream<Simplex>, int[]>> explicitStreamExamples = new ArrayList<GenericPair<ExplicitStream<Simplex>, int[]>>();

	private List<PersistenceAlgorithmType> persistenceAlgorithms = new ArrayList<PersistenceAlgorithmType>();
	
	@Before
	public void setUp() {
		int n = 80;
		int d = 4;
		
		vietorisRipsExamples.add(new PointCloudTestSpecifier(PointCloudExamples.getHouseExample(), FiltrationType.VietorisRips, 2, 2, 10));
		vietorisRipsExamples.add(new PointCloudTestSpecifier(PointCloudExamples.getSquare(), FiltrationType.VietorisRips, 2, 2, 10));
		vietorisRipsExamples.add(new PointCloudTestSpecifier(PointCloudExamples.getEquispacedCirclePoints(n), FiltrationType.VietorisRips, 2, 0.3, 10));
		vietorisRipsExamples.add(new PointCloudTestSpecifier(PointCloudExamples.getGaussianPoints(n, d), FiltrationType.VietorisRips, 2, 0.4, 10));
		vietorisRipsExamples.add(new PointCloudTestSpecifier(PointCloudExamples.getRandomSpherePoints(n, d), FiltrationType.VietorisRips, 2, 0.4, 10));
		
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getTriangle(), new int[]{1, 1}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getFilteredTriangle(), new int[]{1, 0}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getSimplicialSphere(4), new int[]{1, 0, 0, 0, 1}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getZomorodianCarlssonExample(), new int[]{1, 0}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getTorus(), new int[]{1, 2, 1}));
		explicitStreamExamples.add(new GenericPair<ExplicitStream<Simplex>, int[]>(SimplexStreamExamples.getCircle(n), new int[]{1, 1}));
		
		
		persistenceAlgorithms.add(PersistenceAlgorithmType.GenericClassicalHomology);
		persistenceAlgorithms.add(PersistenceAlgorithmType.GenericDualityHomology);
		persistenceAlgorithms.add(PersistenceAlgorithmType.GenericDualityCohomology);
		persistenceAlgorithms.add(PersistenceAlgorithmType.Plex3Homology);
	}

	@After
	public void tearDown() {
		vietorisRipsExamples = null;
	}
	
	//@Test
	public void testVietorisRipsExamples() {
		for (PointCloudTestSpecifier example: this.vietorisRipsExamples) {
			THashMap<PersistenceAlgorithmType, BarcodeCollection> barcodeResults = new THashMap<PersistenceAlgorithmType, BarcodeCollection>();
			
			for (PersistenceAlgorithmType type: this.persistenceAlgorithms) {
				BarcodeCollection barcodeCollection = PersistenceAlgorithmTester.testVietorisRipsStream(example.getPointCloud(), example.getMaxDimension(), example.getMaxFiltrationValue(), example.getNumDivisions(), type);
				barcodeResults.put(type, barcodeCollection);
			}
			
			System.out.println("================");
			
			this.printBarcodeComparisons(barcodeResults);
		}
	}
	
	@Test
	public void testExplicitExamples() {
		for (GenericPair<ExplicitStream<Simplex>, int[]> pair: this.explicitStreamExamples) {
			ExplicitStream<Simplex> stream = pair.getFirst();
			THashMap<PersistenceAlgorithmType, BarcodeCollection> barcodeResults = new THashMap<PersistenceAlgorithmType, BarcodeCollection>();
			
			System.out.println("actual: " + Arrays.toString(pair.getSecond()));
			
			for (PersistenceAlgorithmType type: this.persistenceAlgorithms) {
				BarcodeCollection barcodeCollection = PersistenceAlgorithmTester.testExplicitStream(stream, 5, type);
				barcodeResults.put(type, barcodeCollection);
				
				int[] computedBettiSequence = barcodeCollection.getInfiniteIntervals().getBettiSequence();
				System.out.println(type + ": " + Arrays.toString(computedBettiSequence));
				
				assertTrue(HomologyUtility.compareIntArrays(computedBettiSequence, pair.getSecond()) == 0);
			}
			
			System.out.println("================");
		}
	}
	
	private void printBarcodeComparisons(THashMap<PersistenceAlgorithmType, BarcodeCollection> barcodeResults) {
		int numFailures = 0;
		
		for (PersistenceAlgorithmType algorithm_type_1: this.persistenceAlgorithms) {
			BarcodeCollection bc_1 = barcodeResults.get(algorithm_type_1);
			for (PersistenceAlgorithmType algorithm_type_2: this.persistenceAlgorithms) {
				if (algorithm_type_1 == algorithm_type_2) {
					break;
				}
				
				BarcodeCollection bc_2 = barcodeResults.get(algorithm_type_2);
				
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
