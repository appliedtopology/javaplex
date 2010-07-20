package edu.stanford.math.plex4.free_module;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericRing;

public class UnorderedGenericFreeModule<R, M> extends AbstractGenericFreeModule<R, M> {

	public UnorderedGenericFreeModule(GenericRing<R> ring) {
		super(ring);
	}

	@Override
	public AbstractGenericFormalSum<R, M> createNewSum() {
		return new UnorderedGenericFormalSum<R, M>();
	}

	@Override
	public AbstractGenericFormalSum<R, M> createNewSum(R coefficient, M object) {
		return new UnorderedGenericFormalSum<R, M>(coefficient, object);
	}

	@Override
	public AbstractGenericFormalSum<R, M> createNewSum(AbstractGenericFormalSum<R, M> contents) {
		return new UnorderedGenericFormalSum<R, M>(contents);
	}

}
