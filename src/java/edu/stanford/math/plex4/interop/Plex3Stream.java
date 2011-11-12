package edu.stanford.math.plex4.interop;

import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.math.plex.SimplexStream;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.homology.filtration.FiltrationUtility;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.collections.utility.AdaptedIterator;
import gnu.trove.TObjectIntHashMap;

/**
 * This class is a wrapper around a plex 3 SimplexStream that implements the AbstractFilteredStream interface
 * of plex 4. 
 * 
 * @author Andrew Tausz
 *
 */
public class Plex3Stream implements AbstractFilteredStream<edu.stanford.math.plex4.homology.chain_basis.Simplex> {
	private final SimplexStream plex3Stream;
	private final TObjectIntHashMap<Simplex> filtrationIndexMap = new TObjectIntHashMap<Simplex>();
	private final Plex3ToPlex4SimplexAdapter simplexAdapter;
	private final int maxFiltrationIndex;
	
	public Plex3Stream(SimplexStream plex3Stream) {
		this.simplexAdapter = Plex3ToPlex4SimplexAdapter.getInstance();
		this.plex3Stream = plex3Stream;
		int tempMaxFiltrationIndex = Integer.MIN_VALUE;
		
		for (Iterator<edu.stanford.math.plex.Simplex> iterator = plex3Stream.iterator(); iterator.hasNext(); ) {
			edu.stanford.math.plex.Simplex plex3Simplex = iterator.next();
			this.filtrationIndexMap.put(this.simplexAdapter.evaluate(plex3Simplex), plex3Simplex.findex());
			tempMaxFiltrationIndex = Math.max(tempMaxFiltrationIndex, plex3Simplex.findex());
		}
		
		this.maxFiltrationIndex = tempMaxFiltrationIndex;
	}
	
	public Plex3Stream(SimplexStream plex3Stream, int[] vertexMapping) {
		this.simplexAdapter = Plex3ToPlex4SimplexAdapter.getInstance(vertexMapping);
		this.plex3Stream = plex3Stream;
		int tempMaxFiltrationIndex = Integer.MIN_VALUE;
		
		for (Iterator<edu.stanford.math.plex.Simplex> iterator = plex3Stream.iterator(); iterator.hasNext(); ) {
			edu.stanford.math.plex.Simplex plex3Simplex = iterator.next();
			this.filtrationIndexMap.put(this.simplexAdapter.evaluate(plex3Simplex), plex3Simplex.findex());
			tempMaxFiltrationIndex = Math.max(tempMaxFiltrationIndex, plex3Simplex.findex());
		}
		
		this.maxFiltrationIndex = tempMaxFiltrationIndex;
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
	
	public double getFiltrationValue(Simplex basisElement) {
		return this.plex3Stream.convert_filtration_index(this.getFiltrationIndex(basisElement));
	}

	public int getMaximumFiltrationIndex() {
		return this.maxFiltrationIndex;
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

	public Comparator<Simplex> getBasisComparator() {
		throw new UnsupportedOperationException();
	}

	public <G> PersistenceInvariantDescriptor<Interval<Double>, G> transform(PersistenceInvariantDescriptor<Interval<Integer>, G> barcodeCollection) {
		FiltrationConverter converter = new FiltrationConverter() {

			@Override
			public double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2) {
				return Math.max(filtrationValue1, filtrationValue2);
			}

			@Override
			public int getFiltrationIndex(double filtrationValue) {
				return 0;
			}

			@Override
			public double getFiltrationValue(int filtrationIndex) {
				return plex3Stream.convert_filtration_index(filtrationIndex);
			}

			@Override
			public double getInitialFiltrationValue() {
				return 0;
			}
			
		};
		
		return FiltrationUtility.transform(barcodeCollection, converter);
	}
}
