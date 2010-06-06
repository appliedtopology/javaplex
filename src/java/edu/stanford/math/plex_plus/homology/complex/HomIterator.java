package edu.stanford.math.plex_plus.homology.complex;

import java.util.Iterator;

import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;

public class HomIterator<M> implements Iterator<GenericPair<M, M>> {
	private final IntHomComplex<M> homComplex;
	
	private int n = 0;
	private Iterator<M> CIterator;
	private Iterator<M> DIterator;
	
	public HomIterator(IntHomComplex<M> homComplex) {
		this.homComplex = homComplex;
		this.n = 0;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GenericPair<M, M> next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
}
