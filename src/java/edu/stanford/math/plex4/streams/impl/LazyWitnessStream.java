package edu.stanford.math.plex4.streams.impl;

import java.util.Arrays;

import edu.stanford.math.plex4.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex4.homology.filtration.IncreasingLinearConverter;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.utility.Infinity;

/**
 * This class implements the lazy witness complex described in the paper
 * "Topological estimation using witness complexes", by Vin de Silva and
 * Gunnar Carlsson. The details of the construction are described in this
 * paper. Note that a lazy witness complex is fully described by its 
 * 1-skeleton, therefore we simply derive from the FlagComplexStream class.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the type of the underlying metric space
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
	 * This is the nu value described in the paper. Note that we use the
	 * default value of 2.
	 */
	protected final int nu;

	/**
	 * The maximum distance allowed between two connected vertices.
	 */
	protected final double maxDistance;
	
	/**
	 * Constructor which initializes the complex with a metric space.
	 * 
	 * @param metricSpace the metric space to use in the construction of the complex
	 * @param maxDistance the maximum allowable distance
	 * @param maxDimension the maximum dimension of the complex
	 */
	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int nu, int numDivisions) {
		super(maxDimension, new IncreasingLinearConverter(numDivisions, maxDistance));
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyNonNegative(nu);
		ExceptionUtility.verifyLessThan(nu, landmarkSelector.size());
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.nu = nu;
		this.maxDistance = maxDistance;
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int numDivisions) {
		this(metricSpace, landmarkSelector, maxDimension, maxDistance, 2, numDivisions);
	}

	public static int getDefaultNuValue() {
		return 2;
	}
	
	@Override
	protected UndirectedWeightedListGraph constructEdges() {
		int N = this.metricSpace.size();
		int L = this.landmarkSelector.size();

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
		
		double[][] D = null;
		double[] m = DoubleArrayUtility.createArray(N);
		
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
			double d_ijn, e_ij;
			
			for (int i = 0; i < L; i++) {
				for (int j = i + 1; j < L; j++) {
					e_ij = Infinity.Double.getPositiveInfinity();
					for (int n = 0; n < N; n++) {
						if (D == null) {
							double d_in = this.metricSpace.distance(this.landmarkSelector.getLandmarkIndex(i), n);
							double d_jn = this.metricSpace.distance(this.landmarkSelector.getLandmarkIndex(j), n);
							d_ijn = Math.max(d_in, d_jn);
						} else {
							d_ijn = Math.max(D[i][n], D[j][n]);
						}
						if (d_ijn < m[n]) {
							d_ijn = 0.0;
						} else {
							d_ijn -= m[n];
						}
						e_ij = Math.min(e_ij, d_ijn);
					}
					
					if (e_ij <= this.maxDistance) {
						graph.addEdge(i, j, e_ij);
						edge_count++;
					}
				}
			}
		}
		
		return graph;
	}
}
