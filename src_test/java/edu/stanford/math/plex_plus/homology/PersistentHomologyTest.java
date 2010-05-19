/**
 * 
 */
package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex_streams.ExplicitStream;
import edu.stanford.math.plex_plus.homology.simplex_streams.VietorisRipsStream;
import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_plus.math.structures.impl.ModularIntField;

/**
 * @author atausz
 *
 */
public class PersistentHomologyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testVietorisRips2();
	}

	/**
	 * This implements the example in Figure 1 of the paper
	 * "Computing Persistent Homology" by Zomorodian and Carlsson.
	 * 
	 */
	public static void testZomorodianCarlssonExample() {
		ExplicitStream stream = new ExplicitStream();

		stream.addSimplex(new int[]{0}, 0);
		stream.addSimplex(new int[]{1}, 0);

		stream.addSimplex(new int[]{2}, 1);
		stream.addSimplex(new int[]{3}, 1);
		stream.addSimplex(new int[]{0, 1}, 1);
		stream.addSimplex(new int[]{1, 2}, 1);

		stream.addSimplex(new int[]{2, 3}, 2);
		stream.addSimplex(new int[]{3, 0}, 2);

		stream.addSimplex(new int[]{0, 2}, 3);

		stream.addSimplex(new int[]{0, 1, 2}, 4);

		stream.addSimplex(new int[]{0, 2, 3}, 5);

		System.out.println(stream);
		
		PersistentHomology homology = new PersistentHomology(ModularIntField.getInstance(7));
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
	
	public static void testTriangle() {
		ExplicitStream stream = new ExplicitStream();

		stream.addSimplex(new int[]{0}, 0);
		stream.addSimplex(new int[]{1}, 0);
		stream.addSimplex(new int[]{2}, 0);
		stream.addSimplex(new int[]{0, 1}, 1);
		stream.addSimplex(new int[]{1, 2}, 1);
		stream.addSimplex(new int[]{2, 0}, 1);
		stream.addSimplex(new int[]{0, 1, 2}, 10);

		System.out.println(stream);
		
		PersistentHomology homology = new PersistentHomology(ModularIntField.getInstance(7));
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
	
	public static void testVietorisRips1() {
		double[][] points = new double[][]{new double[]{0, 0}, 
				new double[]{0, 1},
				new double[]{1, 0},
				new double[]{1, 1}};
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, 10, 4);
		
		System.out.println(stream.toString());
		
		PersistentHomology homology = new PersistentHomology(ModularIntField.getInstance(7));
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
	
	public static void testVietorisRips2() {
		int n = 7;
		double[][] points = new double[n][2];
		
		for (int i = 0; i < n; i++) {
			points[i][0] = Math.cos(2 * Math.PI * i / n);
			points[i][1] = Math.sin(2 * Math.PI * i / n);
		}
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, 10, 4);
		
		System.out.println(stream.toString());
		
		PersistentHomology homology = new PersistentHomology(ModularIntField.getInstance(7));
		BarcodeCollection barcodes = homology.computeIntervals(stream, 3);
		System.out.println(barcodes);
	}
}
