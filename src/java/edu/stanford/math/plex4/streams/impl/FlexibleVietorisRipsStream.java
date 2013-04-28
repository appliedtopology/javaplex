/**
 * 
 */
package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.streams.storage_structures.StreamStorageStructure;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;

/**
 * <p>
 * This class implements the Vietoris-Rips filtered simplicial complex. A
 * simplex [v_0, ..., v_k] is in VR(r) if and only if the pairwise distances
 * d(v_i, v_j) are less than r for all 0 <= i, j <= k. Thus a Vietoris-Rips
 * complex is fully defined by its 1-skeleton, in that it is the maximal
 * simplicial complex given such a 1-skeleton. For this reason, we simply derive
 * from the FlagComplexStream abstract class, and implement the pairwise
 * thresholding in order to generate the 1-skeleton.
 * </p>
 * 
 * @author Andrew Tausz
 * @author Mikael Vejdemo-Johansson
 * 
 * @param <T>
 *            the base type of the underlying metric space
 */
public class FlexibleVietorisRipsStream<T> extends FlagComplexStream {

	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;

	/**
	 * The maximum distance allowed between two connected vertices.
	 */
	protected final double maxDistance;

	/**
	 * Constructor which initializes the complex with a metric space.
	 * 
	 * @param metricSpace
	 *            the metric space to use in the construction of the complex
	 * @param maxDistance
	 *            the maximum allowable distance
	 * @param maxDimension
	 *            the maximum dimension of the complex
	 * @param converter
	 *            the Filtration Converter to be used
	 */
	public FlexibleVietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension, FiltrationConverter converter) {
		super(maxDimension, converter);
		ExceptionUtility.verifyNonNull(metricSpace);
		this.metricSpace = metricSpace;
		this.maxDistance = maxDistance;
	}

	public FlexibleVietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension, FiltrationConverter converter,
			StreamStorageStructure<Simplex> storageStructure) {
		super(maxDimension, converter, storageStructure);
		ExceptionUtility.verifyNonNull(metricSpace);
		this.metricSpace = metricSpace;
		this.maxDistance = maxDistance;
	}

	public FlexibleVietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension, FiltrationConverter converter,
			int[] indices) {
		super(maxDimension, converter, indices);
		ExceptionUtility.verifyNonNull(metricSpace);
		this.metricSpace = metricSpace;
		this.maxDistance = maxDistance;
	}

	@Override
	protected UndirectedWeightedListGraph constructEdges() {
		int n = this.metricSpace.size();
		TIntHashSet neighborhood = null;

		UndirectedWeightedListGraph graph = new UndirectedWeightedListGraph(n);

		for (int i = 0; i < n; i++) {
			// obtain the neighborhood of the i-th point
			neighborhood = this.metricSpace.getClosedNeighborhood(metricSpace.getPoint(i), this.maxDistance);

			// get the pairwise distances of the points and store them
			TIntIterator iterator = neighborhood.iterator();
			while (iterator.hasNext()) {
				int j = iterator.next();

				if (i == j) {
					continue;
				}

				double distance = this.metricSpace.distance(i, j);
				graph.addEdge(i, j, distance);
			}
		}

		return graph;
	}
}
