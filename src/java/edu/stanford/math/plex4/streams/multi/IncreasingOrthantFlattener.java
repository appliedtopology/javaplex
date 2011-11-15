package edu.stanford.math.plex4.streams.multi;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.utility.Infinity;

public class IncreasingOrthantFlattener<T extends PrimitiveBasisElement> implements AbstractStreamFlattener<T> {

	private final double[] principalDirection;

	public IncreasingOrthantFlattener(double[] prinicpalDirection) {
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
		int maxMultiple = Infinity.Int.getNegativeInfinity();

		for (int i = 0; i < filtrationValue.length; i++) {
			if (this.principalDirection[i] == 0) {
				continue;
			}
			int multiple = (int) Math.ceil(filtrationValue[i] / this.principalDirection[i]);
			if (multiple > maxMultiple) {
				maxMultiple = multiple;
			}
		}

		return maxMultiple;
	}
}
