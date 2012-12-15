package edu.stanford.math.plex4.streams.impl;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
//import java.util.Collections;

//import org.apache.commons.lang.ArrayUtils;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
//import edu.stanford.math.plex4.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.homology.filtration.FiltrationUtility;
import edu.stanford.math.plex4.homology.filtration.IncreasingLinearConverter;
//import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.interfaces.PrimitiveStream;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
//import edu.stanford.math.primitivelib.autogen.pair.IntDoublePair;
//import edu.stanford.math.primitivelib.utility.Infinity;
//import gnu.trove.TIntHashSet;
//import gnu.trove.TIntIterator;
//import gnu.trove.TIntObjectHashMap;

public class WitnessStream<T> extends PrimitiveStream<Simplex> {
	/**
	 * The maximum allowable dimension of the complex.
	 */
	protected final int maxAllowableDimension;

	/**
	 * This converts between filtration indices and values
	 */
	protected FiltrationConverter converter;

	protected int[] indices = null;

	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;

	/**
	 * This is the selection of landmark points
	 */
	protected final LandmarkSelector<T> landmarkSelector;

	/**
	 * The maximum distance allowed between two connected vertices.
	 */
	protected final double maxDistance;

	protected double[][] D = null;
	protected double[][] m = null;

	protected final int N;
	protected final int L;
	protected final int maxLen;

	protected boolean plex3Compatible = true;

	/*
	 * Storage of all associated simplices for a witness is costly, so we do not
	 * save this information by default. It is only needed for witness
	 * bicomplexes.
	 */
	protected boolean saveAssociatedSimplices = false;

	List<List<Simplex>> associatedSimplices;

	/**
	 * Constructor which initializes the complex with a metric space.
	 * 
	 * @param metricSpace
	 *            the metric space to use in the construction of the complex
	 * @param maxDistance
	 *            the maximum allowable distance
	 * @param maxDimension
	 *            the maximum dimension of the complex
	 */

	public WitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance,
			int numDivisions) {
		super(SimplexComparator.getInstance());
		this.maxAllowableDimension = maxDimension;
		this.converter = new IncreasingLinearConverter(numDivisions, maxDistance);
		ExceptionUtility.verifyNonNull(metricSpace);
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.maxDistance = maxDistance;
		this.N = this.metricSpace.size();
		this.L = this.landmarkSelector.size();

		this.maxLen = (maxDimension < 0 || maxDimension >= L) ? L : maxDimension + 1;
	}

	public WitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int[] indices) {
		super(SimplexComparator.getInstance());
		this.maxAllowableDimension = maxDimension;
		this.converter = new IncreasingLinearConverter(20, maxDistance);
		this.indices = indices;
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.maxDistance = maxDistance;
		this.N = this.metricSpace.size();
		this.L = this.landmarkSelector.size();

		this.maxLen = (maxDimension < 0 || maxDimension >= L) ? L : maxDimension + 1;
	}

	public double getFiltrationValue(Simplex simplex) {
		return this.converter.getFiltrationValue(this.getFiltrationIndex(simplex));
	}

	/**
	 * This function transforms the given collection of filtration index
	 * barcodes into filtration value barcodes.
	 * 
	 * @param <G>
	 * @param barcodeCollection
	 *            the set of filtration index barcodes
	 * @return the barcodes transformed into filtration value form
	 */
	public <G> PersistenceInvariantDescriptor<Interval<Double>, G> transform(PersistenceInvariantDescriptor<Interval<Integer>, G> barcodeCollection) {
		return FiltrationUtility.transform(barcodeCollection, this.converter);
	}

	public void setPlex3Compatbility(boolean value) {
		/*
		 * Daniel: I don't know what would make this implementation not
		 * compatible to Plex3. Hence, if someone requests
		 * setPlex3Compatbility(false), we raise an error.
		 */
		if (!value) {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * This function returns the list of simplices such that they have the given
	 * point as their witness. If there are no such points, this function
	 * returns null;
	 * 
	 * @param witness
	 *            the witness point
	 * @return a list of simplices with the given point as their witness
	 */
	public List<Simplex> getAssociatedSimplices(int witness) {
		if (!this.saveAssociatedSimplices) {
			throw new UnsupportedOperationException(); // TODO: use a better
														// exception
		}
		return this.associatedSimplices.get(witness);
	}

	@Override
	protected void constructComplex() {

		this.indices = this.landmarkSelector.getLandmarkPoints(); // TODO Is
																	// this
																	// correct?

		// Get all distances between landmark points and data points.
		D = DoubleArrayUtility.createMatrix(L, N);
		for (int ll = 0; ll < L; ll++) {
			for (int nn = 0; nn < N; nn++) {
				D[ll][nn] = this.metricSpace.distance(this.landmarkSelector.getLandmarkIndex(ll), nn);
			}
		}
		// Find the max_len nearest landmarks for every data point.
		double tmp[] = new double[L];
		m = DoubleArrayUtility.createMatrix(maxLen, N);
		for (int nn = 0; nn < N; ++nn) {
			for (int ll = 0; ll < L; ++ll) {
				tmp[ll] = D[ll][nn];
			}
			// Daniel: I want partial sort (the smallest maxLen elements in
			// sorted order)
			// but I don't know what function to use.
			Arrays.sort(tmp);
			for (int ll = 0; ll < maxLen; ++ll) {
				m[ll][nn] = tmp[ll];
			}
		}

		if (this.saveAssociatedSimplices) {
			this.associatedSimplices = new ArrayList<List<Simplex>>();
			for (int ll = 0; ll < L; ++ll) {
				this.associatedSimplices.add(new ArrayList<Simplex>());
			}
		}

		Deque<Integer> lower_vertices = new ArrayDeque<Integer>();
		// Recursively add vertices and all their cofaces.
		for (int ll = 0; ll < L; ++ll) {
			if (this.addCofaces_(ll, new int[] {}, null, lower_vertices)) {
				lower_vertices.add(ll);
			}
		}
	}

	/*
	 * Add the simplex (index[i0], s1[0], ...) if all conditions are met.
	 */
	boolean addCofaces_(int i0, int[] s1, double[] Dm1, Deque<Integer> lower_vertices) {
		int dim = s1.length;
		int filtrationIndex;
		double[] Dm = new double[N];
		int[] snew;

		switch (dim) {
		case 0: {
			for (int nn = 0; nn < N; ++nn) {
				Dm[nn] = D[i0][nn];
			}
			filtrationIndex = this.converter.getFiltrationIndex(0.0);
			snew = new int[] { this.indices[i0] };
			break;
		}
		case 1: {
			for (int nn = 0; nn < N; ++nn) {
				Dm[nn] = Math.max(Dm1[nn], D[i0][nn]);
			}
			snew = new int[] { this.indices[i0], s1[0] };

			double filtrationValue = Double.POSITIVE_INFINITY;
			for (int nn = 0; nn < N; ++nn) {
				filtrationValue = Math.min(filtrationValue, Dm[nn] - m[dim][nn]);
			}
			if (filtrationValue > this.maxDistance) {
				return false;
			}
			filtrationIndex = this.converter.getFiltrationIndex(filtrationValue);
			break;
		}
		default: {
			snew = new int[dim + 1];
			snew[0] = this.indices[i0];
			System.arraycopy(s1, 0, snew, 1, dim);

			filtrationIndex = 0;
			for (Simplex face : new Simplex(snew).getBoundaryArray()) {
				if (!this.containsElement(face)) {
					return false;
				}
				filtrationIndex = Math.max(filtrationIndex, this.getFiltrationIndex(face));
			}

			for (int nn = 0; nn < N; ++nn) {
				Dm[nn] = Math.max(Dm1[nn], D[i0][nn]);
			}

			double filtrationValue = Double.POSITIVE_INFINITY;
			for (int nn = 0; nn < N; ++nn) {
				filtrationValue = Math.min(filtrationValue, Dm[nn] - m[dim][nn]);
			}
			if (filtrationValue > this.maxDistance) {
				return false;
			}
			filtrationIndex = Math.max(filtrationIndex, this.converter.getFiltrationIndex(filtrationValue));
		}
		}

		// Add the simplex to the complex.
		Simplex Snew = new Simplex(snew);
		this.storageStructure.addElement(Snew, filtrationIndex);
		if (this.saveAssociatedSimplices) {
			for (int nn = 0; nn < N; ++nn) {
				if (Dm[nn] <= m[dim][nn]) {
					this.associatedSimplices.get(nn).add(Snew);
				}
			}
		}

		// Recursively add cofaces.
		if (dim < this.maxAllowableDimension) {
			Deque<Integer> new_lower_vertices = new ArrayDeque<Integer>();
			for (int ll : lower_vertices) {
				if (this.addCofaces_(ll, snew, Dm, new_lower_vertices)) {
					new_lower_vertices.add(ll);
				}
			}
		}
		return true;
	}

}
