/**
 * 
 */
package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.streams.storage_structures.StreamStorageStructure;
import edu.stanford.math.primitivelib.autogen.pair.BooleanDoublePair;

/**
 * <p>This class implements a simplex stream with the property that
 * given its 1-skeleton, its higher skeletons are maximal in the 
 * following sense. A k-simplex [v_0, ..., v_k] is a member of the
 * complex iff all of its edges [v_i, v_j] are in the complex.</p>
 * 
 * <p>This class is abstract in that it does not provide a mechanism 
 * for constructing the 1-skeleton, and only constructs the higher
 * order skeletons inductively. The reason is that there are different
 * choices for definitions of the 1-skeletons, the main ones being the
 * lazy witness complex, the Vietoris-Rips complex, and the clique complex. 
 * Thus a child class must provide the implementation of the 1-skeleton.</p>
 * 
 * <p>Also note that the FlagComplexStream construction can be used with 
 * any finite metric space. For information on the implementation,
 * consult the paper "Fast Construction of the Vietoris-Rips Complex",
 * by Afra Zomorodian. This implementation uses the incremental
 * algorithm described in the above paper.</p>
 * 
 * @author Andrew Tausz
 *
 */
public abstract class FlagComplexStream extends ConditionalFlagComplexStream {

	public FlagComplexStream(int maxAllowableDimension, FiltrationConverter converter, int[] indices) {
		super(maxAllowableDimension, converter, indices);
	}

	public FlagComplexStream(int maxAllowableDimension, FiltrationConverter converter, StreamStorageStructure<Simplex> storageStructure) {
		super(maxAllowableDimension, converter, storageStructure);
	}

	public FlagComplexStream(int maxAllowableDimension, FiltrationConverter converter) {
		super(maxAllowableDimension, converter);
	}	
	
	protected BooleanDoublePair isMember(Simplex simplex) {
		return new BooleanDoublePair(true, 0.0);
	}
}
