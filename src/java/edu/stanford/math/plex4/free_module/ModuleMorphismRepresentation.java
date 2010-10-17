package edu.stanford.math.plex4.free_module;

import java.util.Map;

import edu.stanford.math.plex4.array_utility.ArrayCreation;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.THashMap;

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
public class ModuleMorphismRepresentation<R, M, N> {
	protected final FreeModuleRepresentation<R, M> domainRepresentation;
	protected final FreeModuleRepresentation<R, N> codomainRepresentation;
	
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
	
	
	
	public UnorderedGenericFormalSum<R, GenericPair<M, N>> toBasisMapping(R[][] matrix) {
		UnorderedGenericFormalSum<R, GenericPair<M, N>> sum = new UnorderedGenericFormalSum<R, GenericPair<M, N>>();
		int rows = matrix.length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				sum.put(matrix[i][j], new GenericPair<M, N>(this.domainRepresentation.getBasisElement(j), this.codomainRepresentation.getBasisElement(i)));
			}
		}
		return sum;
	}
	
	public DoubleFormalSum<GenericPair<M, N>> toBasisMapping(double[][] matrix) {
		DoubleFormalSum<GenericPair<M, N>> sum = new DoubleFormalSum<GenericPair<M, N>>();
		int rows = matrix.length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				sum.put(matrix[i][j], new GenericPair<M, N>(this.domainRepresentation.getBasisElement(j), this.codomainRepresentation.getBasisElement(i)));
			}
		}
		return sum;
	}
	
	public R[][] toMatrix(THashMap<M, AbstractGenericFormalSum<R, N>> columnList, R initializer) {
		R[][] matrix = ArrayCreation.newGenericMatrix(this.codomainRepresentation.getDimension(), this.domainRepresentation.getDimension(), initializer);
		
		for (int i = 0; i < this.codomainRepresentation.getDimension(); i++)
			for (int j = 0; j < this.domainRepresentation.getDimension(); j++)
				matrix[i][j] = initializer;
		
		for (M key: columnList.keySet()) {
			AbstractGenericFormalSum<R, N> column = columnList.get(key);
			int columnIndex = this.domainRepresentation.getIndex(key);
			for (Map.Entry<N, R> entry: column) {
				int rowIndex = this.codomainRepresentation.getIndex(entry.getKey());
				matrix[rowIndex][columnIndex] = entry.getValue();
			}
		}
		
		return matrix;
	}
}
