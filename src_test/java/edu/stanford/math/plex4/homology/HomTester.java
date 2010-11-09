package edu.stanford.math.plex4.homology;

import static org.junit.Assert.assertTrue;

import java.util.List;

import edu.stanford.math.plex4.streams.derived.HomStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.StreamUtility;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntMatrixConverter;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntPrimitiveFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.matrix.IntSparseMatrix;
import edu.stanford.math.primitivelib.autogen.matrix.IntSparseVector;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;


public class HomTester {
	/**
	 * TODO: fix
	 * 
	 * @param <T>
	 * @param <U>
	 * @param domainStream
	 * @param codomainStream
	 */
	public static <T, U> void verifyHomotopies(AbstractFilteredStream<T> domainStream, AbstractFilteredStream<U> codomainStream) {
		HomStream<T, U> homStream = new HomStream<T, U>(domainStream, codomainStream);
		List<IntSparseFormalSum<ObjectObjectPair<T, U>>> homotopies = homStream.getHomotopies();

		IntPrimitiveFreeModule<T> domainChainModule = new IntPrimitiveFreeModule<T>();
		IntMatrixConverter<T, U> matrixConverter = new IntMatrixConverter<T, U>(domainStream, codomainStream);
		IntMatrixConverter<T, T> domainMatrixConverter = new IntMatrixConverter<T, T>(domainStream, domainStream);
		IntMatrixConverter<U, U> codomainMatrixConverter = new IntMatrixConverter<U, U>(codomainStream, codomainStream);

		IntSparseFormalSum<ObjectObjectPair<T, T>> domainBoundaryMap = StreamUtility.createBoundaryMatrixAsSum(domainStream);
		IntSparseFormalSum<ObjectObjectPair<U, U>> codomainBoundaryMap = StreamUtility.createBoundaryMatrixAsSum(codomainStream);
		
		IntSparseMatrix domainBoundaryMatrix = domainMatrixConverter.toSparseMatrix(domainBoundaryMap);
		IntSparseMatrix codomainBoundaryMatrix = codomainMatrixConverter.toSparseMatrix(codomainBoundaryMap);
		
		
		// We must verify that d(f(s)) = f(d(s)) where d is the boundary operator, and f is the homotopy, and s ranges
		// over the domain complex.
		for (IntSparseFormalSum<ObjectObjectPair<T, U>> homotopy: homotopies) {
			IntSparseMatrix homotopyMatrix = matrixConverter.toSparseMatrix(homotopy);
			
			for (T domainElement: domainStream) {
				IntSparseVector domainElementVector = matrixConverter.getDomainRepresentation().toSparseVector(domainChainModule.createNewSum(domainElement));
				
				IntSparseVector domainElementBoundary = domainBoundaryMatrix.multiply(domainElementVector);
				
				IntSparseVector RHS = homotopyMatrix.multiply(domainElementBoundary);
				IntSparseVector LHS = codomainBoundaryMatrix.multiply(homotopyMatrix.multiply(domainElementVector));
				IntSparseVector difference = RHS.subtract(LHS);
				if (!difference.isEmpty()) {
					assertTrue(difference.isEmpty());
				}
			}
		}
	}
}
