package edu.stanford.math.plex4.homology.nonautogen;

import java.util.Arrays;
import java.util.Comparator;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.homology.filtration.IncreasingLinearConverter;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.interfaces.PrimitiveStream;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import edu.stanford.math.primitivelib.utility.Infinity;

public class WitnessBicomplex<T> extends PrimitiveStream<SimplexPair> {
	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;

	/**
	 * This is the selection of landmark points
	 */
	protected final LandmarkSelector<T> selectorL;
	protected final LandmarkSelector<T> selectorM;

	protected final int maxDimension;
	protected final double maxDistance;

	protected final int nu = 2;

	protected final FiltrationConverter converter;

	public WitnessBicomplex(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> L, LandmarkSelector<T> M, int maxDimension, double maxDistance, Comparator<SimplexPair> comparator) {
		super(comparator);
		this.metricSpace = metricSpace;
		this.selectorL = L;
		this.selectorM = M;

		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;

		this.converter = new IncreasingLinearConverter(20, maxDistance);
	}

	@Override
	protected void constructComplex() {
		int N = metricSpace.size();
		int L = this.selectorL.size();
		int M = this.selectorM.size();

		ObjectObjectPair<double[][], double[]> pair = null;

		pair = this.computeMatrices(selectorL);
		double[][] D1 = pair.getFirst();
		double[] m1 = pair.getSecond();

		pair = this.computeMatrices(selectorM);
		double[][] D2 = pair.getFirst();
		double[] m2 = pair.getSecond();

		for (int l = 0; l < L; l++) {
			for (int m = 0; m < M; m++) {
				this.storageStructure.addElement(SimplexPair.createPair(l, m), 0);
			}
		}


		// form ([a, b], c)
		{
			double d_ijn, e_ij;
			double d_kln, e_kl;

			for (int i = 0; i < L; i++) {
				for (int j = i + 1; j < L; j++) {
					e_ij = Infinity.Double.getPositiveInfinity();
					for (int k = 0; k < M; k++) {
						e_kl = Infinity.Double.getPositiveInfinity();
						for (int n = 0; n < N; n++) {
							if (D1 == null) {
								double d_in = this.metricSpace.distance(this.selectorL.getLandmarkIndex(i), n);
								double d_jn = this.metricSpace.distance(this.selectorL.getLandmarkIndex(j), n);
								d_ijn = Math.max(d_in, d_jn);
							} else {
								d_ijn = Math.max(D1[i][n], D1[j][n]);
							}

							if (D2 == null) {
								double d_kn = this.metricSpace.distance(this.selectorL.getLandmarkIndex(k), n);
								d_kln = d_kn;
							} else {
								d_kln = D2[k][n];
							}

							if (d_ijn < m1[n]) {
								d_ijn = 0.0;
							} else {
								d_ijn -= m1[n];
							}

							if (d_kln < m2[n]) {
								d_kln = 0.0;
							} else {
								d_kln -= m2[n];
							}

							e_ij = Math.min(e_ij, d_ijn);
							e_kl = Math.min(e_kl, d_kln);
						}

						if (Math.max(e_ij, e_kl) <= this.maxDistance) {
							this.storageStructure.addElement(SimplexPair.createPair(Simplex.makeSimplex(i, j), Simplex.makeSimplex(k)), this.converter.getFiltrationIndex(Math.max(e_ij, e_kl)));
						}
					}
				}
			}
		}

		{
			double d_ijn, e_ij;
			double d_kln, e_kl;

			for (int i = 0; i < L; i++) {
				e_ij = Infinity.Double.getPositiveInfinity();
				for (int k = 0; k < M; k++) {
					for (int l = k + 1; l < M; l++) {
						e_kl = Infinity.Double.getPositiveInfinity();
						for (int n = 0; n < N; n++) {
							if (D1 == null) {
								double d_in = this.metricSpace.distance(this.selectorL.getLandmarkIndex(i), n);
								d_ijn = d_in;
							} else {
								d_ijn = D1[i][n];
							}

							if (D2 == null) {
								double d_kn = this.metricSpace.distance(this.selectorL.getLandmarkIndex(k), n);
								double d_ln = this.metricSpace.distance(this.selectorL.getLandmarkIndex(l), n);
								d_kln = Math.max(d_kn, d_ln);
							} else {
								d_kln = Math.max(D2[k][n], D2[l][n]);
							}

							if (d_ijn < m1[n]) {
								d_ijn = 0.0;
							} else {
								d_ijn -= m1[n];
							}

							if (d_kln < m2[n]) {
								d_kln = 0.0;
							} else {
								d_kln -= m2[n];
							}

							e_ij = Math.min(e_ij, d_ijn);
							e_kl = Math.min(e_kl, d_kln);
						}

						if (Math.max(e_ij, e_kl) <= this.maxDistance) {
							this.storageStructure.addElement(SimplexPair.createPair(Simplex.makeSimplex(i), Simplex.makeSimplex(k, l)), this.converter.getFiltrationIndex(Math.max(e_ij, e_kl)));
						}
					}
				}
			}
		}




		/*


		{
			double d_ijn, e_ij;
			double d_kln, e_kl;

			for (int i = 0; i < L; i++) {
				for (int j = i + 1; j < L; j++) {
					e_ij = Infinity.Double.getPositiveInfinity();
					for (int k = 0; k < M; k++) {
						for (int l = k + 1; l < M; l++) {
							e_kl = Infinity.Double.getPositiveInfinity();
							for (int n = 0; n < N; n++) {
								if (D1 == null) {
									double d_in = this.metricSpace.distance(this.selectorL.getLandmarkIndex(i), n);
									double d_jn = this.metricSpace.distance(this.selectorL.getLandmarkIndex(j), n);
									d_ijn = Math.max(d_in, d_jn);
								} else {
									d_ijn = Math.max(D1[i][n], D1[j][n]);
								}

								if (D2 == null) {
									double d_kn = this.metricSpace.distance(this.selectorL.getLandmarkIndex(k), n);
									double d_ln = this.metricSpace.distance(this.selectorL.getLandmarkIndex(l), n);
									d_kln = Math.max(d_kn, d_ln);
								} else {
									d_kln = Math.max(D2[k][n], D2[l][n]);
								}

								if (d_ijn < m1[n]) {
									d_ijn = 0.0;
								} else {
									d_ijn -= m1[n];
								}

								if (d_kln < m2[n]) {
									d_kln = 0.0;
								} else {
									d_kln -= m2[n];
								}

								e_ij = Math.min(e_ij, d_ijn);
								e_kl = Math.min(e_kl, d_kln);
							}

							if (Math.max(e_ij, e_kl) <= this.maxDistance) {
								this.storageStructure.addElement(SimplexPair.createPair(i, j), this.converter.getFiltrationIndex(Math.max(e_ij, e_kl)));
							}
						}
					}
				}
			}
		}
		
		*/
	}

	protected ObjectObjectPair<double[][], double[]> computeMatrices(LandmarkSelector<T> landmarkSelector) {
		int N = metricSpace.size();
		int L = landmarkSelector.size();

		double[][] D = null;
		double[] m = DoubleArrayUtility.createArray(N);

		try {
			D = DoubleArrayUtility.createMatrix(L, N);
			for (int l = 0; l < L; l++) {
				for (int n = 0; n < N; n++) {
					D[l][n] = this.metricSpace.distance(landmarkSelector.getLandmarkIndex(l), n);
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
						m_temp[l + 1] = this.metricSpace.distance(landmarkSelector.getLandmarkIndex(l), n);
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

		return new ObjectObjectPair<double[][], double[]>(D, m);
	}
}
