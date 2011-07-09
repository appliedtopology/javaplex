package edu.stanford.math.plex4.homology.zigzag.bootstrap;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.zigzag.IntervalTracker;
import edu.stanford.math.plex4.homology.zigzag.SimpleHomologyBasisTracker;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.utility.ArrayUtility;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;

public class VietorisRipsBootstrapper {
	private final double[][] points;
	private final List<int[]> indexSelections;
	IntAbstractField intField = ModularIntField.getInstance(2);
	protected final IntAlgebraicFreeModule<Simplex> chainModule = new IntAlgebraicFreeModule<Simplex>(this.intField);

	private final double maxDistance;
	private final int maxDimension;

	public VietorisRipsBootstrapper(double[][] points, double maxDistance, int maxDimension, List<int[]> indexSelections) {
		this.points = points;
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;

		this.indexSelections = new ArrayList<int[]>();
		for (int i = 0; i < indexSelections.size(); i++) {
			int[] selection = indexSelections.get(i);
			this.indexSelections.add(ArrayUtility.makeMonotone(selection));
		}
	}

	public VietorisRipsBootstrapper(double[][] points, double maxDistance, int maxDimension, int numSelections, int selectionSize) {
		this.points = points;
		this.indexSelections = new ArrayList<int[]>();
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;

		selectionSize = Math.min(points.length, selectionSize);

		for (int selection = 0; selection < numSelections; selection++) {
			int[] set = RandomUtility.randomSubset(selectionSize, points.length).toArray();
			set = ArrayUtility.makeMonotone(set);
			this.indexSelections.add(set);
		}
	}

	public int[] getSubset(int index) {
		return this.indexSelections.get(index);
	}

	public BarcodeCollection<Integer> performBootstrap() {
		int[] i_indices = this.indexSelections.get(0);
		VietorisRipsStream<double[]> X_stream = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, i_indices)), maxDistance, maxDimension + 1, i_indices);
		X_stream.finalizeStream();

		VietorisRipsStream<double[]> Y_stream;

		IntervalTracker<Integer, Integer, IntSparseFormalSum<Simplex>> result = null;

		SimpleHomologyBasisTracker<Simplex> ZTracker = null;
		SimpleHomologyBasisTracker<Simplex> XTracker = null;
		SimpleHomologyBasisTracker<Simplex> YTracker = null;

		for (int j = 1; j < this.indexSelections.size(); j++) {
			if (XTracker == null) {
				XTracker = new SimpleHomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance(), 0, this.maxDimension);
				for (Simplex x: X_stream) {
					XTracker.add(x, X_stream.getFiltrationIndex(x));
				}

				System.out.println("Barcodes for X_" + (j - 1));
				AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<Simplex>> XBarcodes = XTracker.getAnnotatedBarcodes();
				System.out.println(XBarcodes.toString());
			}

			int[] j_indices = this.indexSelections.get(j);
			Y_stream = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, j_indices)), maxDistance, maxDimension + 1, j_indices);
			Y_stream.finalizeStream();

			YTracker = new SimpleHomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance(), 0, this.maxDimension);
			for (Simplex y: Y_stream) {
				YTracker.add(y, Y_stream.getFiltrationIndex(y));
			}

			

			int[] ij_indices = ArrayUtility.union(i_indices, j_indices);

			VietorisRipsStream<double[]> Z_stream = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, ij_indices)), maxDistance, maxDimension + 1, ij_indices);
			Z_stream.finalizeStream();

			ZTracker = new SimpleHomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance(), 0, this.maxDimension);
			for (Simplex z: Z_stream) {
				ZTracker.add(z, Z_stream.getFiltrationIndex(z));
			}

			
			System.out.println("Barcodes for X_" + (j-1) + "," + (j));
			AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<Simplex>> ZBarcodes = ZTracker.getAnnotatedBarcodes();
			System.out.println(ZBarcodes.toString());
			
			System.out.println("Barcodes for X_" + (j));
			AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<Simplex>> YBarcodes = YTracker.getAnnotatedBarcodes();
			System.out.println(YBarcodes.toString());

			
			if (result == null) {
				result = XTracker.getStateWithoutFiniteBarcodes(j - 1);
				result.setUseLeftClosedIntervals(true);
				result.setUseRightClosedIntervals(true);
			}
			
			result = InducedHomologyMappingUtility.include(XTracker, ZTracker, YTracker, result, chainModule, (j - 1), j);
			
			X_stream = Y_stream;
			XTracker = YTracker;
			i_indices = j_indices;
		}

		result.endAllIntervals(this.indexSelections.size() - 1);

		return BarcodeCollection.forgetGeneratorType(result.getAnnotatedBarcodes().filterByMaxDimension(maxDimension));
	}
}
