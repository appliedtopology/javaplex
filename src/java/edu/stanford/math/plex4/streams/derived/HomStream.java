package edu.stanford.math.plex4.streams.derived;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.StreamUtility;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPairComparator;
import edu.stanford.math.primitivelib.collections.utility.ReversedComparator;

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
	 * @param domainStream the domain complex
	 * @param codomainStream the codomain complex
	 */
	public HomStream(AbstractFilteredStream<T> domainStream, AbstractFilteredStream<U> codomainStream) {
		super(new DualStream<T>(domainStream), codomainStream);
	}
	
	@Override
	public int getDimension(ObjectObjectPair<T, U> element) {
		return (this.stream2.getDimension(element.getSecond()) - this.stream1.getDimension(element.getFirst()));
	}
	
	public Comparator<ObjectObjectPair<T, U>> getInducedComparator(Comparator<T> domainComparator, Comparator<U> codomainComparator) {
		return new ObjectObjectPairComparator<T, U>(new ReversedComparator<T>(domainComparator), codomainComparator);
	}
	
	public <R> List<ObjectSparseFormalSum<R, ObjectObjectPair<T, U>>> getHomotopies(ObjectAlgebraicFreeModule<R, ObjectObjectPair<T, U>> chainModule) {
		return StreamUtility.getBoundaryMatrixColumns(this, 1, chainModule);
	}
	
	public <R extends Number> List<DoubleSparseFormalSum<ObjectObjectPair<T, U>>> getHomotopiesAsDouble(ObjectAlgebraicFreeModule<R, ObjectObjectPair<T, U>> chainModule) {
		return toDoubleFormalSum(StreamUtility.getBoundaryMatrixColumns(this, 1, chainModule));
	}
	
	public static <R extends Number, M> DoubleSparseFormalSum<M> toDoubleFormalSum(ObjectSparseFormalSum<R, M> objectFormalSum) {
		DoubleSparseFormalSum<M> sum = new DoubleSparseFormalSum<M>();
		
		for (Iterator<Map.Entry<M, R>> iterator = objectFormalSum.iterator(); iterator.hasNext(); ) {
			Map.Entry<M, R> entry = iterator.next();
			sum.put(entry.getValue().doubleValue(), entry.getKey());
		}
		
		return sum;
	}
	
	public static <R extends Number, M> List<DoubleSparseFormalSum<M>> toDoubleFormalSum(List<ObjectSparseFormalSum<R, M>> objectFormalSumList) {
		List<DoubleSparseFormalSum<M>> doubleFormalSumList = new ArrayList<DoubleSparseFormalSum<M>>();
		
		for (ObjectSparseFormalSum<R, M> objectSum: objectFormalSumList) {
			doubleFormalSumList.add(toDoubleFormalSum(objectSum));
		}
		
		return doubleFormalSumList;
	}
}
