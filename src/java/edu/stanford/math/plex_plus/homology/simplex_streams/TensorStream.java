/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.util.Collections;
import java.util.Comparator;

import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPair;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPairComparator;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex.TensorComparator;
import edu.stanford.math.plex_plus.homology.simplex.TensorProductPair;


/**
 * 
 * @author Andrew Tausz
 *
 */
public class TensorStream<T extends ChainBasisElement, U extends ChainBasisElement> extends BasicStream<TensorProductPair<T, U>> {
	private final SimplexStream<T> stream1;
	private final SimplexStream<U> stream2;
	
	
	public TensorStream(SimplexStream<T> stream1, SimplexStream<U> stream2, Comparator<T> TComparator, Comparator<U> UComparator) {
		super(new TensorComparator<T, U>(TComparator, UComparator));
		this.stream1 = stream1;
		this.stream2 = stream2;
	}

	@Override
	public void finalizeStream() {
		if (!stream1.isFinalized()) {
			stream1.finalizeStream();
		}
		
		if (!stream2.isFinalized()) {
			stream2.finalizeStream();
		}
		
		this.constructComplex();
		
		this.isFinalized = true;
	}

	private void constructComplex() {
		for (T a: this.stream1) {
			double a_filtration = this.stream1.getFiltrationValue(a);
			for (U b: this.stream2) {
				double b_filtration = this.stream2.getFiltrationValue(b);
				this.addSimplexInternal(new TensorProductPair<T, U>(a, b), Math.max(a_filtration, b_filtration));
			}
		}
		
		Comparator<DoubleGenericPair<TensorProductPair<T, U>>> filteredComparator = new DoubleGenericPairComparator<TensorProductPair<T, U>>(this.basisComparator);
		
		// sort simplices by filtration order
		Collections.sort(this.simplices, filteredComparator);
	}
	
}
