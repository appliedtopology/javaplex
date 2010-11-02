package edu.stanford.math.plex4.homology.streams.derived;

import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

/**
 * This implements the hom-complex from two filtered chain complexes. For the definition
 * of the hom-complex, one is invited to look at the book "Homology" by Saunders Maclane.
 * For the case of vector-space components, one can express the hom-complex in terms of the
 * tensor product of the reversed first complex with the second. Elements of the hom-complex
 * are of the form a^* tensor b, for a in the first chain complex and b in the second chain
 * complex. Thus in practice, we represent these as elements of type ObjectObjectPair<T, U>.
 * 
 * @see TensorStream<T, U>
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type of the first complex
 * @param <U> the underlying basis type of the second complex
 */
public class HomStream<T, U> extends TensorStream<T, U>{
	
	/**
	 * This constructor initializes the hom-complexes from two existing chain complexes.
	 * 
	 * @param stream1 the domain complex
	 * @param stream2 the codomain complex
	 */
	public HomStream(AbstractFilteredStream<T> stream1, AbstractFilteredStream<U> stream2) {
		super(new DualStream<T>(stream1), stream2);
	}
	
	@Override
	public int getDimension(ObjectObjectPair<T, U> element) {
		return (this.stream2.getDimension(element.getSecond()) - this.stream1.getDimension(element.getFirst()));
	}
}
