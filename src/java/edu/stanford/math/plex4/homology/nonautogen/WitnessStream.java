package edu.stanford.math.plex4.homology.nonautogen;

import java.util.Arrays;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.homology.filtration.IncreasingLinearConverter;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.interfaces.PrimitiveStream;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.utility.Infinity;

public class WitnessStream<T> extends PrimitiveStream<Simplex> {
	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;

	/**
	 * This is the selection of landmark points
	 */
	protected final LandmarkSelector<T> landmarkSelector;

	protected final double maxDistance;
	protected final int maxDimension;
	
	/**
	 * This converts between filtration indices and values
	 */
	protected FiltrationConverter converter;

	public WitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int numDivisions) {
		super(SimplexComparator.getInstance());
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;
		
		this.converter = new IncreasingLinearConverter(numDivisions, maxDistance);
	}

	@Override
	protected void constructComplex() {
		int N = this.metricSpace.size();
		int L = this.landmarkSelector.size();

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
		
		// add 0-skeleton
		for (int l = 0; l < L; l++) {
			this.storageStructure.addElement(Simplex.makeSimplex(l), 0);
		}
		
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
		
		int nu = 2;
		
		if (nu > 0) {
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
				m[n] = m_temp[nu];
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
						//graph.addEdge(i, j, e_ij);
						this.storageStructure.addElement(Simplex.makeSimplex(i, j), this.converter.getFiltrationIndex(e_ij));
						edge_count++;
					}
				}
			}
		}
	}

	/*
	@Override
	protected void constructComplex() {
		int N = this.metricSpace.size();
		int L = this.landmarkSelector.size();

		// add 0-skeleton
		for (int l = 0; l < L; l++) {
			this.storageStructure.addElement(Simplex.makeSimplex(l), 0);
		}

		// form L x N matrix
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

		int nu = 0;
		if (nu > 0) {
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
				m[n] = m_temp[nu];
				assert (m[n] > 0.0);
			}
		}
		
		for (int i = 0; i < N; i++) {
			final double[] column_i = new double[L];
			for (int l = 0; l < L; l++) {
				column_i[l] = D[l][i];
			}

			int[] indices = IntArrayGeneration.range(0, L);

			cern.colt.Sorting.quickSort(indices, 0, L, new IntComparator(){
				public int compare(int arg0, int arg1) {
					if (column_i[arg0] > column_i[arg1]) {
						return 1;
					} else if (column_i[arg0] < column_i[arg1]) {
						return -1;
					} else {
						return 0;
					}
				}});
			
			assert (column_i[indices[0]] == 0.0);
			
			double distance = column_i[indices[1]];
			int dimension = 1;
			List<Integer> vertices = new ArrayList<Integer>();
			
			while (distance <= this.maxDistance && dimension <= this.maxDimension + 1) {
				vertices.add(indices[dimension]);
				
				if (vertices.size() > 1) {
					int[] vertex_array = new int[vertices.size()];
					for (int c = 0; c < vertices.size(); c++) {
						vertex_array[c] = vertices.get(c);
					}
					
					this.storageStructure.addElement(Simplex.makeSimplex(vertex_array), this.converter.getFiltrationIndex(distance));
				}
				
				dimension++;
				distance = column_i[indices[dimension]];
			}
		}
	}*/
}
