/**
 * 
 */
package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.filtration.ExternalConverter;
import edu.stanford.math.plex4.homology.filtration.IncreasingLinearConverter;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.streams.storage_structures.StreamStorageStructure;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;


/**
 * <p>This class implements the Vietoris-Rips filtered simplicial complex.
 * A simplex [v_0, ..., v_k] is in VR(r) if and only if the pairwise distances
 * d(v_i, v_j) are less than r for all 0 <= i, j <= k. Thus a Vietoris-Rips
 * complex is fully defined by its 1-skeleton, in that it is the maximal 
 * simplicial complex given such a 1-skeleton. For this reason, we simply
 * derive from the FlagComplexStream abstract class, and implement the pairwise
 * thresholding in order to generate the 1-skeleton.</p>
 * 
 * @author Andrew Tausz
 * @author Mikael Vejdemo-Johansson
 *
 * @param <T> the base type of the underlying metric space
 */
public class VietorisRipsStream<T> extends FlexibleVietorisRipsStream<T> {
	
	/**
	 * Constructor which initializes the complex with a metric space.
	 * 
	 * @param metricSpace the metric space to use in the construction of the complex
	 * @param maxDistance the maximum allowable distance
	 * @param maxDimension the maximum dimension of the complex
	 */
	public VietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension) {
		this(metricSpace, maxDistance, maxDimension, 20);
	}
	
	public VietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension, int numDivisions) {
	    super(metricSpace, maxDistance, maxDimension, new IncreasingLinearConverter(numDivisions, maxDistance));
	}
	
	public VietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension, int numDivisions, StreamStorageStructure<Simplex> storageStructure) {
	    super(metricSpace, maxDistance, maxDimension, new IncreasingLinearConverter(numDivisions, maxDistance), storageStructure);
	}

	public VietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension, int[] indices) {
	    super(metricSpace, maxDistance, maxDimension, new IncreasingLinearConverter(20, maxDistance), indices);
	}
	
	public VietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, double[] filtrationValues, int maxDimension) {
		super(metricSpace, DoubleArrayMath.max(filtrationValues), maxDimension, new ExternalConverter(filtrationValues));
	}
}
