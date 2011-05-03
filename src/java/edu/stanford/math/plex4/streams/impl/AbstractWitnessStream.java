package edu.stanford.math.plex4.streams.impl;

import java.util.Arrays;

import edu.stanford.math.plex4.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.filtration.IncreasingLinearConverter;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.autogen.pair.IntDoublePair;
import edu.stanford.math.primitivelib.utility.Infinity;
import gnu.trove.TIntIntHashMap;

public abstract class AbstractWitnessStream<T> extends ConditionalFlagComplexStream {

	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;

	/**
	 * This is the selection of landmark points
	 */
	protected final LandmarkSelector<T> landmarkSelector;

	/**
	 * This is the nu value described in the paper. Note that we use the
	 * default value of 2.
	 */
	protected final int nu;

	/**
	 * The maximum distance allowed between two connected vertices.
	 */
	protected final double maxDistance;
	
	protected double[][] D = null;
	protected double[] m = null;
	
	protected final TIntIntHashMap witnesses = new TIntIntHashMap();
	protected final int N;
	protected final int L;
	
	/**
	 * Constructor which initializes the complex with a metric space.
	 * 
	 * @param metricSpace the metric space to use in the construction of the complex
	 * @param maxDistance the maximum allowable distance
	 * @param maxDimension the maximum dimension of the complex
	 */
	public AbstractWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int nu, int numDivisions) {
		super(maxDimension, new IncreasingLinearConverter(numDivisions, maxDistance));
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyNonNegative(nu);
		ExceptionUtility.verifyLessThan(nu, landmarkSelector.size());
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.nu = nu;
		this.maxDistance = maxDistance;
		this.N = this.metricSpace.size();
		this.L = this.landmarkSelector.size();
	}

	public AbstractWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int numDivisions) {
		this(metricSpace, landmarkSelector, maxDimension, maxDistance, 2, numDivisions);
	}
	
	public AbstractWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int[] indices) {
		super(maxDimension, new IncreasingLinearConverter(20, maxDistance), indices);
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.nu = 2;
		this.maxDistance = maxDistance;
		this.N = this.metricSpace.size();
		this.L = this.landmarkSelector.size();
	}

	public static int getDefaultNuValue() {
		return 2;
	}
	
	public int getWitnessIndex(Simplex simplex) {
		return this.getWitnessIndex(simplex.getVertices());
	}
	
	public int getWitnessIndex(int... indices) {
		int h = hashIndices(indices);
		return this.witnesses.get(h);
	}
	
	protected int hashIndices(int... indices) {
		
		int[] copy = Arrays.copyOf(indices, indices.length);
		Arrays.sort(copy);
		
		return polyEvalReversed(copy, L);
	}
	
	public static int polyEvalReversed(final int[] coefficients, final int x) {
		int n = coefficients.length;
		
		int accumulator = coefficients[0];
		
		for (int i = 1; i < n; i++) {
			accumulator = (accumulator * x) + coefficients[i];
		}
		
		return accumulator;
	}
	
	@Override
	protected UndirectedWeightedListGraph constructEdges() {

		UndirectedWeightedListGraph graph = new UndirectedWeightedListGraph(L);

		/*
		 * Let N be the number of points in the metric space, and n the number of 
		 * landmark points. Let D be the L x N matrix of distances between the set
		 * of landmark points, and the set of all points in the metric space.
		 * 
		 * The definition of the 1-skeleton of the lazy witness complex is as follows:
		 * 
		 * - If nu = 0, then define m_i = 0, otherwise define m_i to be the nu-th smallest entry
		 * in the i-th column of D.
		 * - The edge [ab] belongs to W(D, R, nu) iff there exists as witness i in {1, ..., N} such 
		 * that max(D(a, i), D(b, i)) <= R + m_i
		 * 
		 */

		/**
		 * Key difference between Plex3 and Plex4:
		 * - if landmarks[l] == n, Plex3 sets the distance to infinity
		 * - in Plex4, the distance is just set to 0
		 */		
		
		D = null;
		m = DoubleArrayUtility.createArray(N);
		
		try {
			D = DoubleArrayUtility.createMatrix(L, N);
			for (int l = 0; l < L; l++) {
				for (int n = 0; n < N; n++) {
					D[l][n] = this.metricSpace.distance(this.landmarkSelector.getLandmarkIndex(l), n);
				}
			}
		} catch (OutOfMemoryError error) {
			D = null;
		} finally {
		}
		
		if (this.nu > 0) {
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
		}
		
		int edge_count = 0;
		
		{
			double e_ij;
			int n_star = 0;
			
			for (int i = 0; i < L; i++) {
				for (int j = i + 1; j < L; j++) {
					
					IntDoublePair witnessAndDistance = this.getWitnessAndDistance(i, j);
					n_star = witnessAndDistance.getFirst();
					e_ij = witnessAndDistance.getSecond();
					
					if (e_ij <= this.maxDistance) {
						int h = hashIndices(i, j);
						this.witnesses.put(h, n_star);
						graph.addEdge(i, j, e_ij);
						edge_count++;
					}
				}
			}
		}
		
		return graph;
	}
	
	protected IntDoublePair getWitnessAndDistance(int... indices) {
		double e_ij;
		double d[] = new double[indices.length];
		e_ij = Infinity.Double.getPositiveInfinity();
		int n_star = -1;
		
		for (int n = 0; n < N; n++) {
			double d_max = Infinity.Double.getNegativeInfinity();
			if (D == null) {
				for (int k = 0; k < indices.length; k++) {
					d[k] = this.metricSpace.distance(this.landmarkSelector.getLandmarkIndex(indices[k]), n);
					if (k == 0 || d[k] > d_max) {
						d_max = d[k];
					}
				}
			} else {
				for (int k = 0; k < indices.length; k++) {
					d[k] = D[indices[k]][n];
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
			
			if (d_max < e_ij) {
				e_ij = d_max;
				n_star = n;
			}
		}
		
		return new IntDoublePair(n_star, e_ij);
	}
}
