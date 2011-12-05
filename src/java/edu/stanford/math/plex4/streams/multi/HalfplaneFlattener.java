package edu.stanford.math.plex4.streams.multi;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;

/**
 * This flattener maps a filtration value vector, x, to the smallest integer k such that <p, x> <= k
 * where p is the specified principal direction.
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public class HalfplaneFlattener<T extends PrimitiveBasisElement> implements AbstractStreamFlattener<T> {

	private final double[] principalDirection;
	
	public HalfplaneFlattener(double[] prinicpalDirection) {
		this.principalDirection = prinicpalDirection;
	}

	public AbstractFilteredStream<T> collapse(AbstractMultifilteredStream<T> multifilteredStream) {
		ExplicitStream<T> stream = new ExplicitStream<T>(multifilteredStream.getBasisComparator());

		for (T element : multifilteredStream) {
			double[] filtrationValue = multifilteredStream.getFiltrationValue(element);
			int collapsedIndex = this.getCollapsedIndex(filtrationValue);
			stream.addElement(element, collapsedIndex);
		}

		stream.finalizeStream();
		
		return stream;
	}
	
	private int getCollapsedIndex(double[] filtrationValue) {
		// we want to find minimum integer k such that <p, f> <= k
		// where p is the principal direction, and f is the filtration value
		
		double innerProduct = DoubleArrayMath.innerProduct(filtrationValue, this.principalDirection);
		return (int) Math.ceil(innerProduct);
	}
	
}
