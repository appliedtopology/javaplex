package edu.stanford.math.plex4.streams.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.math.plex4.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.filtration.ExternalConverter;
import edu.stanford.math.plex4.homology.filtration.IncreasingLinearConverter;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.autogen.pair.IntDoublePair;
import edu.stanford.math.primitivelib.utility.Infinity;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;
import gnu.trove.TIntObjectHashMap;

/**
 * This class implements the lazy witness complex described in the paper
 * "Topological estimation using witness complexes", by Vin de Silva and Gunnar
 * Carlsson. The details of the construction are described in this paper. Note
 * that a lazy witness complex is fully described by its 1-skeleton, therefore
 * we simply derive from the FlagComplexStream class.
 * 
 * @author Andrew Tausz
 * 
 * @param <T>
 *            the type of the underlying metric space
 */

public class LazyWitnessStream<T> extends FlagComplexStream {

	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;

	/**
	 * This is the selection of landmark points
	 */
	protected final LandmarkSelector<T> landmarkSelector;

	/**
	 * This is the nu value described in the paper. Note that we use the default
	 * value of 2.
	 */
	protected final int nu;

	/**
	 * The maximum distance allowed between two connected vertices.
	 */
	protected final double maxDistance;

	protected double[][] D = null;
	protected double[] m = null;

	/**
	 * If (i, sigma) is in witnessSimplexMap, then it means that point i is a a
	 * witness for sigma
	 */
	private final TIntObjectHashMap<List<Simplex>> witnessSimplexMap = new TIntObjectHashMap<List<Simplex>>();

	protected final int N;
	protected final int L;

	protected final double epsilon = 1e-8;

	protected boolean plex3Compatible = true;

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

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int nu,
			int numDivisions) {
		super(maxDimension, new IncreasingLinearConverter(numDivisions, maxDistance));
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyNonNegative(nu);
		ExceptionUtility.verifyLessThan(nu, 3);
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.nu = nu;
		this.maxDistance = maxDistance;
		this.N = this.metricSpace.size();
		this.L = this.landmarkSelector.size();
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance,
			int numDivisions) {
		this(metricSpace, landmarkSelector, maxDimension, maxDistance, getDefaultNuValue(), numDivisions);
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int nu,
			int[] indices) {
		super(maxDimension, new IncreasingLinearConverter(20, maxDistance), indices);
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyNonNegative(nu);
		ExceptionUtility.verifyLessThan(nu, 3);
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.nu = nu;
		this.maxDistance = maxDistance;
		this.N = this.metricSpace.size();
		this.L = this.landmarkSelector.size();
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance,
			int[] indices) {
		this(metricSpace, landmarkSelector, maxDimension, maxDistance, getDefaultNuValue(), indices);
	}
	
	
	
	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double[] filtrationValues, int nu) {
		super(maxDimension, new ExternalConverter(filtrationValues));
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyNonNegative(nu);
		ExceptionUtility.verifyLessThan(nu, 3);
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.nu = nu;
		this.maxDistance = DoubleArrayMath.max(filtrationValues);
		this.N = this.metricSpace.size();
		this.L = this.landmarkSelector.size();
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double[] filtrationValues) {
		this(metricSpace, landmarkSelector, maxDimension, filtrationValues, getDefaultNuValue());
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double[] filtrationValues, int nu,
			int[] indices) {
		super(maxDimension, new ExternalConverter(filtrationValues), indices);
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyNonNegative(nu);
		ExceptionUtility.verifyLessThan(nu, 3);
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.nu = nu;
		this.maxDistance = DoubleArrayMath.max(filtrationValues);
		this.N = this.metricSpace.size();
		this.L = this.landmarkSelector.size();
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double[] filtrationValues,
			int[] indices) {
		this(metricSpace, landmarkSelector, maxDimension, filtrationValues, getDefaultNuValue(), indices);
	}

	public void setPlex3Compatbility(boolean value) {
		this.plex3Compatible = value;
	}

	public static int getDefaultNuValue() {
		return 2;
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
		List<Simplex> temp = this.witnessSimplexMap.get(witness);

		if (temp == null) {
			return null;
		}

		List<Simplex> result = new ArrayList<Simplex>();
		result.addAll(temp);
		return result;
	}

	@Override
	protected UndirectedWeightedListGraph constructEdges() {

		this.indices = this.landmarkSelector.getLandmarkPoints();

		UndirectedWeightedListGraph graph = new UndirectedWeightedListGraph(L);

		/*
		 * Let N be the number of points in the metric space, and n the number
		 * of landmark points. Let D be the L x N matrix of distances between
		 * the set of landmark points, and the set of all points in the metric
		 * space.
		 * 
		 * The definition of the 1-skeleton of the lazy witness complex is as
		 * follows:
		 * 
		 * - If nu = 0, then define m_i = 0, otherwise define m_i to be the
		 * nu-th smallest entry in the i-th column of D. - The edge [ab] belongs
		 * to W(D, R, nu) iff there exists as witness i in {1, ..., N} such that
		 * max(D(a, i), D(b, i)) <= R + m_i
		 */

		/**
		 * Key difference between Plex3 and Plex4: - if landmarks[l] == n, Plex3
		 * sets the distance to infinity - in Plex4, the distance is just set to
		 * 0
		 * 
		 * !not true anymore!
		 */

		D = null;
		m = DoubleArrayUtility.createArray(N);

		try {
			D = DoubleArrayUtility.createMatrix(L, N);
			for (int l = 0; l < L; l++) {
				for (int n = 0; n < N; n++) {
					// if (n == this.indices[l]) {
					// D[l][n] = Infinity.Double.getPositiveInfinity();
					// } else {
					D[l][n] = this.metricSpace.distance(this.landmarkSelector.getLandmarkIndex(l), n);
					// }
				}
			}
		} catch (OutOfMemoryError error) {
			D = null;
		} finally {
		}

		double[] m_temp = new double[L + 1];
		for (int n = 0; n < N; n++) {
			for (int l = 0; l < L; l++) {
				if (D == null) {
					m_temp[l + 1] = this.metricSpace.distance(this.landmarkSelector.getLandmarkIndex(l), n);
				} else {
					m_temp[l + 1] = D[l][n];
				}
			}
			Arrays.sort(m_temp);
			assert (m_temp[0] == 0.0);
			m[n] = m_temp[this.nu];
			assert (m[n] > 0.0);
		}

		// int edge_count = 0;

		{
			double e_ij;
			int n_star = 0;

			for (int i = 0; i < L; i++) {
				for (int j = i + 1; j < L; j++) {

					IntDoublePair witnessAndDistance = this.getWitnessAndDistance(i, j);
					n_star = witnessAndDistance.getFirst();
					e_ij = witnessAndDistance.getSecond();

					if (e_ij <= this.maxDistance) {
						this.updateWitnessInformationInternalIndices(n_star, e_ij, i, j);
						graph.addEdge(i, j, e_ij);
						// edge_count++;
					}
				}
			}
		}

		return graph;
	}

	protected IntDoublePair getWitnessAndDistance(int... landmarkIndices) {

		double e_ij;
		double d;
		e_ij = Infinity.Double.getPositiveInfinity();
		int n_star = -1;

		int[] externalIndices = HomologyUtility.convertIndices(landmarkIndices, this.landmarkSelector.getLandmarkPoints());

		int l = landmarkIndices.length - 1;
		// An empty simplex has no witnesses.
		if (l < 0) {
			return new IntDoublePair(n_star, e_ij);
		}

		for (int n = 0; n < N; n++) {
			if (contains(this.indices, n) && !this.plex3Compatible) {
				continue;
			}

			double d_max = Infinity.Double.getNegativeInfinity();
			if (D == null) {
				for (int k = 0; k < landmarkIndices.length; k++) {
					d = this.metricSpace.distance(externalIndices[k], n);
					if (k == 0 || d > d_max) {
						d_max = d;
					}
				}
			} else {
				for (int k = 0; k < landmarkIndices.length; k++) {
					d = D[landmarkIndices[k]][n];
					if (k == 0 || d > d_max) {
						d_max = d;
					}
				}
			}
			if (d_max < m[n]) {
				d_max = 0.0;
			} else {
				d_max -= m[n];
			}

			if (d_max < e_ij) {
				e_ij = d_max;
				n_star = n;
			}

		}

		return new IntDoublePair(n_star, e_ij);
	}

	protected static boolean contains(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return true;
			}
		}

		return false;
	}

	protected void updateWitnessInformationInternalIndices(int n_star, double e_ij, int... internalIndices) {
		updateWitnessInformation(n_star, e_ij, HomologyUtility.convertIndices(internalIndices, this.indices));
	}

	protected void updateWitnessInformation(int n_star, double e_ij, int... externalIndices) {

		TIntHashSet witnesses = this.getAllWitnesses(e_ij, externalIndices);

		for (TIntIterator iterator = witnesses.iterator(); iterator.hasNext();) {
			int witnessIndex = iterator.next();
			if (!this.witnessSimplexMap.contains(witnessIndex)) {
				this.witnessSimplexMap.put(witnessIndex, new ArrayList<Simplex>());
			}
			this.witnessSimplexMap.get(witnessIndex).add(Simplex.makeSimplex(externalIndices));
		}
	}

	protected boolean isWitness(int x, Simplex simplex) {
		return this.isWitness(x, simplex.getVertices());
	}

	protected boolean isWitness(int x, int[] externalIndices) {
		if (!this.witnessSimplexMap.contains(x)) {
			return false;
		}

		return this.witnessSimplexMap.get(x).contains(Simplex.makeSimplex(externalIndices));
	}

	protected TIntHashSet getAllWitnesses(final double e_ij, final int... externalIndices) {
		double d[] = new double[externalIndices.length];
		TIntHashSet witnesses = new TIntHashSet();

		int[] landmarkIndices = HomologyUtility.deconvertIndices(externalIndices, this.indices);
		// landmarkIndices = externalIndices;

		for (int n = 0; n < N; n++) {
			if (contains(this.indices, n) && !this.plex3Compatible) {
				continue;
			}

			double d_max = Infinity.Double.getNegativeInfinity();
			if (D == null) {
				for (int k = 0; k < landmarkIndices.length; k++) {
					d[k] = this.metricSpace.distance(externalIndices[k], n);
					if (k == 0 || d[k] > d_max) {
						d_max = d[k];
					}
				}
			} else {
				for (int k = 0; k < landmarkIndices.length; k++) {
					d[k] = D[landmarkIndices[k]][n];
					if (k == 0 || d[k] > d_max) {
						d_max = d[k];
					}
				}
			}
			if (d_max < m[n]) {
				d_max = 0.0;
			} else {
				d_max -= m[n];
			}

			if (Math.abs(d_max - e_ij) <= this.epsilon) {
				witnesses.add(n);
			}
		}

		if (externalIndices.length == 1 && contains(this.indices, externalIndices[0])) {
			witnesses.add(externalIndices[0]);
		}

		return witnesses;
	}
}
