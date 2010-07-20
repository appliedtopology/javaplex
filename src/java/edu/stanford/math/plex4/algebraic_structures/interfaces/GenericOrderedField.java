package edu.stanford.math.plex4.algebraic_structures.interfaces;

import java.util.Comparator;

public abstract class GenericOrderedField<F> extends GenericField<F> implements Comparator<F> {
	public abstract F abs(F a);
}
