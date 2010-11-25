package edu.stanford.math.plex4.homology;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.autogen.homology.ObjectAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.derived.HomStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.StreamUtility;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntMatrixConverter;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntPrimitiveFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.matrix.IntSparseMatrix;
import edu.stanford.math.primitivelib.autogen.matrix.IntSparseVector;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;


public class HomTester {
	
	public static void dumpHomInformation(AbstractFilteredStream<Simplex> domainStream, AbstractFilteredStream<Simplex> codomainStream) {
		HomStream<Simplex, Simplex> homStream = new HomStream<Simplex, Simplex>(domainStream, codomainStream);
		List<IntSparseFormalSum<ObjectObjectPair<Simplex, Simplex>>> homotopies = homStream.getHomotopies();
		
		System.out.println("Homotopies:");
		for (IntSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> homotopy: homotopies) {
			System.out.println("$$" + homotopy + "$$");
		}
		
		ObjectPersistenceAlgorithm<Fraction, ObjectObjectPair<Simplex, Simplex>> persistence = Plex4.getRationalHomAlgorithm();
		ObjectAlgebraicFreeModule<Fraction, ObjectObjectPair<Simplex, Simplex>> chain_module = persistence.getChainModule();
		
		IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>>> barcode_collection = persistence.computeAnnotatedIntervals(homStream);

	    List<ObjectObjectPair<IntHalfOpenInterval, ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>>>> intervals = barcode_collection.getBarcode(0).getIntervals();

	    ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>> cycle_sum = chain_module.createNewSum();

	    Iterator<ObjectObjectPair<IntHalfOpenInterval, ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>>>> iterator = intervals.iterator();
	    ObjectObjectPair<IntHalfOpenInterval, ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>>> interval_generator_pair;

	    System.out.println("Generating Cycles:");
	    
	    while (iterator.hasNext()) {
	        interval_generator_pair = iterator.next();
	        IntHalfOpenInterval interval = interval_generator_pair.getFirst();
	        ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>> generator = interval_generator_pair.getSecond();
	        if (interval.isInfinite()) {
	            chain_module.accumulate(cycle_sum, generator);
	            System.out.println("$$" + generator + "$$");
	        }
	    }
	    
	    System.out.println("Cycle Sum:");
	    System.out.println("$$" + cycle_sum + "$$");
	    
	    ObjectAbsoluteHomology<Fraction, Simplex> basic_persistence = Plex4.getRationalSimplicialAlgorithm(2);
	    
	    System.out.println(basic_persistence.computeAnnotatedIntervals(domainStream));
	    System.out.println(basic_persistence.computeAnnotatedIntervals(codomainStream));
	}
	
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
