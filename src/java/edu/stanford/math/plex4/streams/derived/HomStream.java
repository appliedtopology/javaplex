package edu.stanford.math.plex4.streams.derived;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.StreamUtility;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

/**
 * This implements the hom-complex from two filtered chain complexes. For the definition
 * of the hom-complex, one is invited to look at the book "Homology" by Saunders Maclane.
 * For the case of vector-space components, one can express the hom-complex in terms of the
 * tensor product of the dualized first complex with the second. Elements of the hom-complex
 * are of the form a* tensor b, for a in the first chain complex and b in the second chain
 * complex. Thus in practice, we represent these as elements of type ObjectObjectPair<T, U>.
 * 
 * @see edu.stanford.math.plex4.streams.derived.TensorStream
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type of the first complex
 * @param <U> the underlying basis type of the second complex
 */
public class HomStream<T, U> extends TensorStream<T, U> {
	
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
	
	public <R> List<IntSparseFormalSum<ObjectObjectPair<T, U>>> getHomotopies() {
		return StreamUtility.getBoundaryMatrixColumns(this, 1);
	}
	
	/**
	 * This function returns the set of chain homotopies of between the domain and codomain chain complexes.
	 * 
	 * @param <R> the underlying type of the ring in which to compute in
	 * @param chainModule the free module over pairs of type (T, U) with coefficients in R
	 * @return a list containing the set of chain homotopies
	 */
	public <R> List<ObjectSparseFormalSum<R, ObjectObjectPair<T, U>>> getHomotopies(ObjectAlgebraicFreeModule<R, ObjectObjectPair<T, U>> chainModule) {
		return StreamUtility.getBoundaryMatrixColumns(this, 1, chainModule);
	}
	
	/**
	 * This function returns the set of chain homotopies between the domain and codomain chain complexes as formal
	 * sums over pairs with double coefficients.
	 * 
	 * @param <R> the underlying type of the ring in which to compute in
	 * @param chainModule the free module over pairs of type (T, U) with coefficients in R
	 * @return a list containing the set of chain homotopies
	 */
	public <R extends Number> List<DoubleSparseFormalSum<ObjectObjectPair<T, U>>> getHomotopiesAsDouble(ObjectAlgebraicFreeModule<R, ObjectObjectPair<T, U>> chainModule) {
		return toDoubleFormalSum(StreamUtility.getBoundaryMatrixColumns(this, 1, chainModule));
	}
	
	/**
	 * This function converts an ObjectSparseFormalSum to a DoubleSparseFormalSum object.
	 * 
	 * @param <R> the underlying type of the original coefficient field (must extend Number)
	 * @param <M> the type of the basis elements in the formal sumssum
	 * @param objectFormalSum the ObjectSparseFormalSum to convert
	 * @return a DoubleSparseFormalSum with the same objects as the one passed in, but with double coefficients
	 */
	public static <R extends Number, M> DoubleSparseFormalSum<M> toDoubleFormalSum(ObjectSparseFormalSum<R, M> objectFormalSum) {
		DoubleSparseFormalSum<M> sum = new DoubleSparseFormalSum<M>();
		
		for (Iterator<Map.Entry<M, R>> iterator = objectFormalSum.iterator(); iterator.hasNext(); ) {
			Map.Entry<M, R> entry = iterator.next();
			sum.put(entry.getValue().doubleValue(), entry.getKey());
		}
		
		return sum;
	}
	
	/**
	 * This function converts a list of ObjectSparseFormalSum objects to DoubleSparseFormalSum objects.
	 * 
	 * @param <R> the underlying type of the original coefficient field (must extend Number)
	 * @param <M> the type of the basis elements in the formal sums
	 * @param objectFormalSumList the list of ObjectSparseFormalSum objects to convert
	 * @return the list of converted sums
	 */
	public static <R extends Number, M> List<DoubleSparseFormalSum<M>> toDoubleFormalSum(List<ObjectSparseFormalSum<R, M>> objectFormalSumList) {
		List<DoubleSparseFormalSum<M>> doubleFormalSumList = new ArrayList<DoubleSparseFormalSum<M>>();
		
		for (ObjectSparseFormalSum<R, M> objectSum: objectFormalSumList) {
			doubleFormalSumList.add(toDoubleFormalSum(objectSum));
		}
		
		return doubleFormalSumList;
	}
}
