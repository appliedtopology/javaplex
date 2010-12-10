package edu.stanford.math.plex4.interop;

import edu.stanford.math.plex.Persistence;
import edu.stanford.math.plex.PersistenceInterval;
import edu.stanford.math.plex.SimplexStream;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;

/**
 * This class encapsulates the functionality of the original version of javaPlex. It implements the
 * <code>AbstractPersistenceAlgorithm<Simplex></code> interface for easy interoperability and testing.
 * Essentially, it computes the persistent homology of a plex 4 stream using the plex 3 algorithm.
 * 
 * @author Andrew Tausz
 *
 */
public class Plex3PersistenceAlgorithm implements AbstractPersistenceAlgorithm<edu.stanford.math.plex4.homology.chain_basis.Simplex> {
	private final int maxDimension;
	
	/**
	 * This constructor initializes the class with the maximum dimension to compute persistence for.
	 * 
	 * @param maxDimension the maximum dimension
	 */
	public Plex3PersistenceAlgorithm(int maxDimension) {
		this.maxDimension = maxDimension;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.new_version.AbstractPersistenceAlgorithm#computeIntervals(edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream)
	 */
	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<edu.stanford.math.plex4.homology.chain_basis.Simplex> plex4Stream) {
		
		SimplexStream.Stack stack = new SimplexStream.Stack(plex4Stream.getMaximumFiltrationIndex() + 1, this.maxDimension);
		
		for (edu.stanford.math.plex4.homology.chain_basis.Simplex plex4Simplex: plex4Stream) {
			stack.push(edu.stanford.math.plex.Simplex.getSimplex(IntArrayMath.scalarAdd(plex4Simplex.getVertices(), 1), plex4Stream.getFiltrationIndex(plex4Simplex)));
		}
		
		Persistence persistenceInstance = new Persistence();
		PersistenceInterval[] rawIntervals = persistenceInstance.computeRawIntervals(stack, false, Persistence.baseModulus());

		return Plex3ToPlex4BarcodeAdapter.getInstance().evaluate(rawIntervals);
	}
}
