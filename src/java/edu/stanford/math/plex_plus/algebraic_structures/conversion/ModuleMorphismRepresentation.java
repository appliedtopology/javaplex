package edu.stanford.math.plex_plus.algebraic_structures.conversion;

import java.util.Map.Entry;

import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.utility.ArrayUtility2;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class computes matrix representations of module homomorphisms
 * T: F(M) -> F(N), where F(M) and F(N) are free modules on sets with 
 * underlying type M and N.
 * 
 * @author Andrew Tausz
 *
 * @param <R> the type of the underlying commutative ring
 * @param <M> the type of the generating set of the domain
 * @param <N> the type of the generating set of the codomain
 */
public class ModuleMorphismRepresentation<R extends Number, M, N> {
	private final FreeModuleRepresentation<R, M> domainRepresentation;
	private final FreeModuleRepresentation<R, N> codomainRepresentation;
	
	/**
	 * This constructor initializes the object with a basis for the domain and codomain.
	 * 
	 * @param domainBasis a collection consisting of basis elements for the domain
	 * @param codomainBasis a collection consisting of basis elements for the codomain
	 */
	public ModuleMorphismRepresentation(Iterable<M> domainBasis, Iterable<N> codomainBasis) {
		this(new FreeModuleRepresentation<R, M>(domainBasis), new FreeModuleRepresentation<R, N>(codomainBasis));
	}
	
	public ModuleMorphismRepresentation(FreeModuleRepresentation<R, M> domainRepresentation, FreeModuleRepresentation<R, N> codomainRepresentation) {
		ExceptionUtility.verifyNonNull(domainRepresentation);
		ExceptionUtility.verifyNonNull(codomainRepresentation);
		this.domainRepresentation = domainRepresentation;
		this.codomainRepresentation = codomainRepresentation;
	}
	
	public FreeModuleRepresentation<R, M> getDomainRepresentation() {
		return this.domainRepresentation;
	}
	
	public FreeModuleRepresentation<R, N> getCodomainRepresentation() {
		return this.codomainRepresentation;
		
	}
	
	public double[][] toDoubleMatrix(GenericFormalSum<R, GenericPair<M, N>> basisMapping) {
		double[][] matrix = ArrayUtility2.newDoubleMatrix(this.codomainRepresentation.getDimension(), this.domainRepresentation.getDimension());
		
		for (Entry<GenericPair<M, N>, R> entry: basisMapping) {
			int row = this.codomainRepresentation.getIndex(entry.getKey().getSecond());
			int column = this.domainRepresentation.getIndex(entry.getKey().getFirst());
			matrix[row][column] = entry.getValue().doubleValue();
		}
		return matrix;
	}
	
	public GenericFormalSum<R, GenericPair<M, N>> toBasisMapping(R[][] matrix) {
		GenericFormalSum<R, GenericPair<M, N>> sum = new GenericFormalSum<R, GenericPair<M, N>>();
		int rows = matrix.length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				sum.put(matrix[i][j], new GenericPair<M, N>(this.domainRepresentation.getBasisElement(j), this.codomainRepresentation.getBasisElement(i)));
			}
		}
		return sum;
	}
}
