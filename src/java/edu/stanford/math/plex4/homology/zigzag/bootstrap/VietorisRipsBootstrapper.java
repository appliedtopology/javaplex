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

	/*
	public BarcodeCollection<Integer> performProjectionBootstrap(int[] expectedBettiNumbers) {
		VietorisRipsStream<double[]> X_stream = new VietorisRipsStream<T>(this.metricSpace, indexSelections.get(0), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
		X_stream.setPlex3Compatbility(false);
		X_stream.finalizeStream();

		WitnessStream<T> Y_stream = null;

		IntervalTracker<Integer, Integer, IntSparseFormalSum<Simplex>> result = null;

		HomologyBasisTracker<SimplexPair> ZTracker = null;
		HomologyBasisTracker<Simplex> XTracker = null;
		HomologyBasisTracker<Simplex> YTracker = null;

		for (int j = 1; j < this.indexSelections.size(); j++) {
			if (XTracker == null) {
				XTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
				XTracker.getIntervalTracker().setUseRightClosedIntervals(false);
				XTracker.getIntervalTracker().setMaxDimension(maxDimension);
				for (Simplex x: X_stream) {
					XTracker.add(x, j - 1);
				}

				System.out.println("Barcodes for X_" + (j - 1));
				AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<Simplex>> XBarcodes = XTracker.getAnnotatedBarcodes();

				if (expectedBettiNumbers != null && !Arrays.equals(XBarcodes.getBettiSequence(), expectedBettiNumbers)) {
					this.indexSelections.set(0, new RandomLandmarkSelector<T>(metricSpace, this.indexSelections.get(0).getLandmarkPoints().length));
					X_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(0), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
					j--;
					continue;
				}
				System.out.println(XBarcodes.toString());
			}

			Y_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(j), maxDimension + 1, maxDistance, indexSelections.get(j).getLandmarkPoints());
			Y_stream.setPlex3Compatbility(false);
			Y_stream.finalizeStream();

			YTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
			YTracker.getIntervalTracker().setUseRightClosedIntervals(false);
			YTracker.getIntervalTracker().setMaxDimension(maxDimension);
			for (Simplex y: Y_stream) {
				YTracker.add(y, j);
			}

			System.out.println("Barcodes for X_" + (j));
			AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<Simplex>> YBarcodes = YTracker.getAnnotatedBarcodes();

			if (expectedBettiNumbers != null && !Arrays.equals(YBarcodes.getBettiSequence(), expectedBettiNumbers)) {
				this.indexSelections.set(j, new RandomLandmarkSelector<T>(metricSpace, this.indexSelections.get(j).getLandmarkPoints().length));
				j--;
				continue;
			}

			System.out.println(YBarcodes.toString());

			WitnessBicomplex<T> Z_stream = new WitnessBicomplex<T>(X_stream, Y_stream, maxDimension);
			Z_stream.finalizeStream();
			Z_stream.ensureAllFaces();

			ZTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());
			ZTracker.getIntervalTracker().setUseRightClosedIntervals(false);
			ZTracker.getIntervalTracker().setMaxDimension(maxDimension);
			for (SimplexPair z: Z_stream) {
				ZTracker.add(z, j);
			}

			if (result == null) {
				result = InducedHomologyMappingUtility.project(XTracker, ZTracker, YTracker, XTracker.getStateWithoutFiniteBarcodes(), chainModule, Z_chainModule, (j - 1), j);
			} else {
				result = InducedHomologyMappingUtility.project(XTracker, ZTracker, YTracker, result, chainModule, Z_chainModule, (j - 1), j);
			}

			X_stream = Y_stream;
			XTracker = YTracker;
		}

		result.endAllIntervals(this.indexSelections.size() - 1);

		return BarcodeCollection.forgetGeneratorType(result.getAnnotatedBarcodes().filterByMaxDimension(maxDimension));
	}
	 */

	/*
	public BarcodeCollection<Integer> performBootstrap() {
		HomologyBasisTracker<Simplex> basisTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		int increment = 1;

		int[] i_indices = this.indexSelections.get(0);
		VietorisRipsStream<double[]> X_i = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, i_indices)), maxDistance, maxDimension + 1, i_indices);
		X_i.finalizeStream();

		List<Simplex> X_i_set = CollectionUtility.dump(X_i);
		Collections.sort(X_i_set, SimplexComparator.getInstance());

		basisTracker.getIntervalTracker().setUseRightClosedIntervals(false);

		for (Simplex simplex: X_i_set) {
			basisTracker.add(simplex, 0);
		}

		for (int j = 1; j < this.indexSelections.size(); j++) {
			int[] j_indices = this.indexSelections.get(j);
			int[] ij_indices = ArrayUtility.union(i_indices, j_indices);

			VietorisRipsStream<double[]> X_j = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, j_indices)), maxDistance, maxDimension + 1, j_indices);
			VietorisRipsStream<double[]> X_ij = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, ij_indices)), maxDistance, maxDimension + 1, ij_indices);
			X_j.finalizeStream();
			X_ij.finalizeStream();

			//List<Simplex> X_j_set = dump(X_j);
			//Collections.sort(X_j_set, SimplexComparator.getInstance());

			//List<Simplex> X_ij_set = dump(X_ij);
			//Collections.sort(X_ij_set, SimplexComparator.getInstance());


			//basisTracker.setBasisComparator(new FilteredComparator<Simplex>(X_ij, SimplexComparator.getInstance()));

			List<Simplex> X_ij_sub_X_i = CollectionUtility.getDifference(X_ij, X_i);
			List<Simplex> X_ij_sub_X_j = CollectionUtility.getDifference(X_ij, X_j);

			Collections.sort(X_ij_sub_X_i, SimplexComparator.getInstance());
			Collections.sort(X_ij_sub_X_j, SimplexComparator.getInstance());
			Collections.reverse(X_ij_sub_X_j);

			for (Simplex simplex: X_ij_sub_X_i) {
				basisTracker.add(simplex, increment * j);
			}

			for (Simplex simplex: X_ij_sub_X_j) {
				basisTracker.remove(simplex, increment * j);
			}

			X_i = X_j;
			i_indices = j_indices;
		}

		IntervalTracker<Integer, Integer, IntSparseFormalSum<Simplex>> result = basisTracker.getIntervalTracker();
		result.setUseRightClosedIntervals(true);
		result.endAllIntervals(this.indexSelections.size() - 1);

		return BarcodeCollection.forgetGeneratorType(result.getAnnotatedBarcodes().filterByMaxDimension(maxDimension));
	}
	 */
}
