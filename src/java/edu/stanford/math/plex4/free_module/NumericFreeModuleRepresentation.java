package edu.stanford.math.plex4.free_module;

import java.util.Map;

public class NumericFreeModuleRepresentation<R extends Number, M> extends FreeModuleRepresentation<R, M> {

	public NumericFreeModuleRepresentation(Iterable<M> stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	public double[] toDoubleArray(AbstractGenericFormalSum<R, M> formalSum) {
		double[] array = new double[this.dimension];
		
		for (Map.Entry<M, R> entry: formalSum) {
			int index = this.getIndex(entry.getKey());
			array[index] = entry.getValue().doubleValue();
		}
		
		return array;
	}
}
