package edu.stanford.math.plex_plus.free_module;

import java.util.Comparator;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericRing;


public class OrderedGenericFreeModule<R, M> extends AbstractGenericFreeModule<R, M> {
	private final Comparator<M> comparator;
	
	public OrderedGenericFreeModule(GenericRing<R> ring, Comparator<M> comparator) {
		super(ring);
		this.comparator = comparator;
	}

	@Override
	public AbstractGenericFormalSum<R, M> createNewSum() {
		return new OrderedGenericFormalSum<R, M>(this.comparator);
	}

	@Override
	public AbstractGenericFormalSum<R, M> createNewSum(R coefficient, M object) {
		return new OrderedGenericFormalSum<R, M>(coefficient, object, this.comparator);
	}

	@Override
	public AbstractGenericFormalSum<R, M> createNewSum(AbstractGenericFormalSum<R, M> contents) {
		return new OrderedGenericFormalSum<R, M>(contents, this.comparator);
	}

}
