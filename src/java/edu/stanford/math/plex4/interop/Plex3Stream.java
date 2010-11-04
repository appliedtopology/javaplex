package edu.stanford.math.plex4.interop;

import java.util.Iterator;

import edu.stanford.math.plex.SimplexStream;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.collections.utility.AdaptedIterator;
import gnu.trove.TObjectIntHashMap;

/**
 * This class is a wrapper around a plex 3 SimplexStrea that implements the AbstractFilteredStream interface
 * of plex 4. 
 * 
 * @author Andrew Tausz
 *
 */
public class Plex3Stream implements AbstractFilteredStream<edu.stanford.math.plex4.homology.chain_basis.Simplex> {
	private final SimplexStream plex3Stream;
	private final TObjectIntHashMap<Simplex> filtrationIndexMap = new TObjectIntHashMap<Simplex>();
	private final Plex3ToPlex4SimplexAdapter simplexAdapter = Plex3ToPlex4SimplexAdapter.getInstance();
	
	public Plex3Stream(SimplexStream plex3Stream) {
		this.plex3Stream = plex3Stream;
		
		for (Iterator<edu.stanford.math.plex.Simplex> iterator = plex3Stream.iterator(); iterator.hasNext(); ) {
			edu.stanford.math.plex.Simplex plex3Simplex = iterator.next();
			this.filtrationIndexMap.put(this.simplexAdapter.evaluate(plex3Simplex), plex3Simplex.findex());
		}
	}
	
	public void finalizeStream() { }

	public Simplex[] getBoundary(Simplex basisElement) {
		return basisElement.getBoundaryArray();
	}

	public int[] getBoundaryCoefficients(Simplex basisElement) {
		return basisElement.getBoundaryCoefficients();
	}

	public int getDimension(Simplex basisElement) {
		return basisElement.getDimension();
	}

	public int getFiltrationIndex(Simplex basisElement) {
		return this.filtrationIndexMap.get(basisElement);
	}

	public int getMaximumFiltrationIndex() {
		throw new UnsupportedOperationException();
	}

	public int getSize() {
		return this.plex3Stream.size();
	}

	public boolean isFinalized() {
		return true;
	}

	public Iterator<edu.stanford.math.plex4.homology.chain_basis.Simplex> iterator() {
		return new AdaptedIterator<edu.stanford.math.plex.Simplex, Simplex>(this.plex3Stream.iterator(), simplexAdapter);
	}
}
