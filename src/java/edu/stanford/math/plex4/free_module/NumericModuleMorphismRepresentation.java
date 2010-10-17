package edu.stanford.math.plex4.free_module;

import java.util.Map.Entry;

import edu.stanford.math.plex4.array_utility.ArrayCreation;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;

public class NumericModuleMorphismRepresentation<R extends Number, M, N> extends ModuleMorphismRepresentation<R, M, N> {

	public NumericModuleMorphismRepresentation(Iterable<M> domainBasis, Iterable<N> codomainBasis) {
		super(domainBasis, codomainBasis);
	}

	public double[][] toDoubleMatrix(AbstractGenericFormalSum<R, GenericPair<M, N>> basisMapping) {
		double[][] matrix = ArrayCreation.newDoubleMatrix(this.codomainRepresentation.getDimension(), this.domainRepresentation.getDimension());
		
		for (Entry<GenericPair<M, N>, R> entry: basisMapping) {
			int row = this.codomainRepresentation.getIndex(entry.getKey().getSecond());
			int column = this.domainRepresentation.getIndex(entry.getKey().getFirst());
			matrix[row][column] = entry.getValue().doubleValue();
		}
		return matrix;
	}
}
