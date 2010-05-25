/**
 * 
 */
package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.complex.IntSimplicialComplex;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.homology.simplex.SimplexComparator;
import edu.stanford.math.plex_plus.homology.simplex_streams.ExplicitStream;
import edu.stanford.math.plex_plus.homology.simplex_streams.VietorisRipsStream;
import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_plus.math.structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.RandomUtility;

/**
 * @author atausz
 *
 */
public class PersistentHomologyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testTriangle();
	}

	/**
	 * This implements the example in Figure 1 of the paper
	 * "Computing Persistent Homology" by Zomorodian and Carlsson.
	 * 
	 */
	public static void testZomorodianCarlssonExample() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addSimplex(new Simplex(new int[]{0}), 0);
		stream.addSimplex(new Simplex(new int[]{1}), 0);

		stream.addSimplex(new Simplex(new int[]{2}), 1);
		stream.addSimplex(new Simplex(new int[]{3}), 1);
		stream.addSimplex(new Simplex(new int[]{0, 1}), 1);
		stream.addSimplex(new Simplex(new int[]{1, 2}), 1);

		stream.addSimplex(new Simplex(new int[]{2, 3}), 2);
		stream.addSimplex(new Simplex(new int[]{3, 0}), 2);

		stream.addSimplex(new Simplex(new int[]{0, 2}), 3);

		stream.addSimplex(new Simplex(new int[]{0, 1, 2}), 4);

		stream.addSimplex(new Simplex(new int[]{0, 2, 3}), 5);

		stream.finalizeStream();
		
		System.out.println(stream);
		
		PersistentHomology<Simplex> homology = new PersistentHomology<Simplex>(ModularIntField.getInstance(7), SimplexComparator.getInstance());
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
	
	/**
	 * A simple triangle.
	 */
	public static void testTriangle() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addSimplex(new Simplex(new int[]{0}), 0);
		stream.addSimplex(new Simplex(new int[]{1}), 0);
		stream.addSimplex(new Simplex(new int[]{2}), 0);
		stream.addSimplex(new Simplex(new int[]{0, 1}), 0);
		stream.addSimplex(new Simplex(new int[]{1, 2}), 0);
		stream.addSimplex(new Simplex(new int[]{2, 0}), 0);
		//stream.addSimplex(new Simplex(new int[]{0, 1, 2}), 3);

		stream.finalizeStream();
		
		System.out.println(stream);
		
		PersistentHomology<Simplex> homology = new PersistentHomology<Simplex>(ModularIntField.getInstance(7), SimplexComparator.getInstance());
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
		
		IntSimplicialComplex<Simplex> staticComplex = new IntSimplicialComplex<Simplex>(stream, SimplexComparator.getInstance());
		
		System.out.println(staticComplex.getSkeleton(0));
		System.out.println(staticComplex.getSkeleton(1));
		System.out.println(staticComplex.getSkeleton(2));
		
		int[][] boundary = staticComplex.getDenseBoundaryMatrix(1);
		System.out.println(ArrayUtility.toString(boundary));
	}
	
	public static void testTetrahedron() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addSimplex(new Simplex(new int[]{0}), 0);
		stream.addSimplex(new Simplex(new int[]{1}), 0);
		stream.addSimplex(new Simplex(new int[]{2}), 0);
		stream.addSimplex(new Simplex(new int[]{3}), 0);
		stream.addSimplex(new Simplex(new int[]{0, 1}), 0);
		stream.addSimplex(new Simplex(new int[]{1, 2}), 0);
		stream.addSimplex(new Simplex(new int[]{0, 2}), 0);
		stream.addSimplex(new Simplex(new int[]{0, 3}), 0);
		stream.addSimplex(new Simplex(new int[]{1, 3}), 0);
		stream.addSimplex(new Simplex(new int[]{2, 3}), 0);
		stream.addSimplex(new Simplex(new int[]{0, 1, 2}), 0);
		stream.addSimplex(new Simplex(new int[]{0, 1, 3}), 0);
		stream.addSimplex(new Simplex(new int[]{0, 2, 3}), 0);
		stream.addSimplex(new Simplex(new int[]{1, 2, 3}), 0);

		System.out.println(stream.validate());
		
		stream.finalizeStream();
		
		System.out.println(stream);
		
		PersistentHomology<Simplex> homology = new PersistentHomology<Simplex>(ModularIntField.getInstance(7), SimplexComparator.getInstance());
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
	
	/**
	 * The 2-torus.
	 */
	public static void testTorus() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addSimplex(new Simplex(new int[] {1}), 0);
		stream.addSimplex(new Simplex(new int[] {2}), 0);
		stream.addSimplex(new Simplex(new int[] {3}), 0);

		stream.addSimplex(new Simplex(new int[] {4}), 0);
		stream.addSimplex(new Simplex(new int[] {5}), 0);
		stream.addSimplex(new Simplex(new int[] {6}), 0);

		stream.addSimplex(new Simplex(new int[] {7}), 0);
		stream.addSimplex(new Simplex(new int[] {8}), 0);
		stream.addSimplex(new Simplex(new int[] {9}), 0);

		stream.addSimplex(new Simplex(new int[] {1, 2}), 0);
		stream.addSimplex(new Simplex(new int[] {1, 3}), 0);
		stream.addSimplex(new Simplex(new int[] {2, 3}), 0);

		stream.addSimplex(new Simplex(new int[] {1, 4}), 0);
		stream.addSimplex(new Simplex(new int[] {2, 5}), 0);
		stream.addSimplex(new Simplex(new int[] {3, 6}), 0);

		stream.addSimplex(new Simplex(new int[] {1, 6}), 0);
		stream.addSimplex(new Simplex(new int[] {2, 4}), 0);
		stream.addSimplex(new Simplex(new int[] {3, 5}), 0);

		stream.addSimplex(new Simplex(new int[] {4, 5}), 0);
		stream.addSimplex(new Simplex(new int[] {4, 6}), 0);
		stream.addSimplex(new Simplex(new int[] {5, 6}), 0);
		stream.addSimplex(new Simplex(new int[] {4, 7}), 0);
		stream.addSimplex(new Simplex(new int[] {4, 9}), 0);
		stream.addSimplex(new Simplex(new int[] {5, 7}), 0);
		stream.addSimplex(new Simplex(new int[] {5, 8}), 0);
		stream.addSimplex(new Simplex(new int[] {6, 8}), 0);
		stream.addSimplex(new Simplex(new int[] {6, 9}), 0);

		stream.addSimplex(new Simplex(new int[] {7, 9}), 0);
		stream.addSimplex(new Simplex(new int[] {8, 9}), 0);
		stream.addSimplex(new Simplex(new int[] {7, 1}), 0);
		stream.addSimplex(new Simplex(new int[] {7, 3}), 0);
		stream.addSimplex(new Simplex(new int[] {7, 8}), 0);
		stream.addSimplex(new Simplex(new int[] {8, 1}), 0);
		stream.addSimplex(new Simplex(new int[] {8, 2}), 0);
		stream.addSimplex(new Simplex(new int[] {9, 2}), 0);
		stream.addSimplex(new Simplex(new int[] {9, 3}), 0);

		stream.addSimplex(new Simplex(new int[] {1, 2, 4}), 0);
		stream.addSimplex(new Simplex(new int[] {2, 4, 5}), 0);
		stream.addSimplex(new Simplex(new int[] {2, 3, 5}), 0);
		stream.addSimplex(new Simplex(new int[] {3, 5, 6}), 0);
		stream.addSimplex(new Simplex(new int[] {1, 4, 6}), 0);
		stream.addSimplex(new Simplex(new int[] {1, 3, 6}), 0);

		stream.addSimplex(new Simplex(new int[] {4, 5, 7}), 0);
		stream.addSimplex(new Simplex(new int[] {5, 7, 8}), 0);
		stream.addSimplex(new Simplex(new int[] {5, 6, 8}), 0);
		stream.addSimplex(new Simplex(new int[] {6, 8, 9}), 0);
		stream.addSimplex(new Simplex(new int[] {4, 7, 9}), 0);
		stream.addSimplex(new Simplex(new int[] {4, 6, 9}), 0);

		stream.addSimplex(new Simplex(new int[] {7, 8, 1}), 0);
		stream.addSimplex(new Simplex(new int[] {8, 1, 2}), 0);
		stream.addSimplex(new Simplex(new int[] {8, 9, 2}), 0);
		stream.addSimplex(new Simplex(new int[] {9, 2, 3}), 0);
		stream.addSimplex(new Simplex(new int[] {7, 1, 3}), 0);
		stream.addSimplex(new Simplex(new int[] {7, 9, 3}), 0);

		stream.finalizeStream();
		
		System.out.println(stream);
		
		PersistentHomology<Simplex> homology = new PersistentHomology<Simplex>(ModularIntField.getInstance(2), SimplexComparator.getInstance());
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
	
	/**
	 * House example in the plex tutorial by Henry Adams.
	 */
	public static void testHouseExample() {
		double[][] points = new double[][]{new double[]{0, 3}, 
				new double[]{1, 2},
				new double[]{1, 0},
				new double[]{-1, 0},
				new double[]{-1, 2}};
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, 10, 3);
		stream.finalizeStream();
		
		System.out.println(stream.toString());
		
		PersistentHomology<Simplex> homology = new PersistentHomology<Simplex>(ModularIntField.getInstance(7), SimplexComparator.getInstance());
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
	
	public static void testVietorisRipsSquare() {
		double[][] points = new double[][]{new double[]{0, 0}, 
				new double[]{0, 1},
				new double[]{1, 0},
				new double[]{1, 1}};
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, 10, 3);
		stream.finalizeStream();
		
		System.out.println(stream.toString());
		
		PersistentHomology<Simplex> homology = new PersistentHomology<Simplex>(ModularIntField.getInstance(7), SimplexComparator.getInstance());
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
	
	public static void testVietorisRipsSphere() {
		int n = 50000;
		int d = 3;
		double epsilon = 0.1;
		
		double[][] points = new double[n][d];
		double denom = 0;
		
		for (int i = 0; i < n; i++) {
			points[i] = RandomUtility.normalArray(d);
			
			for (int j = 0; j < d; j++) {
				denom += points[i][j] * points[i][j];
			}
			denom = Math.sqrt(denom);
			for (int j = 0; j < d; j++) {
				points[i][j] /= denom;;
			}
		}
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, epsilon, 3);
		stream.finalizeStream();
		
		PersistentHomology<Simplex> homology = new PersistentHomology<Simplex>(ModularIntField.getInstance(7), SimplexComparator.getInstance());
		BarcodeCollection barcodes = homology.computeIntervals(stream, 2);
		System.out.println(barcodes);
	}
}
