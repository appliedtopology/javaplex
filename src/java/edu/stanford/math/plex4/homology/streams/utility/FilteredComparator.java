package edu.stanford.math.plex4.homology.streams.utility;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;

public class FilteredComparator<M> implements Comparator<M> {
	private final AbstractFilteredStream<M> stream;
	private final Comparator<M> basisComparator;
	
	public FilteredComparator(AbstractFilteredStream<M> stream, Comparator<M> basisComparator) {
		this.stream = stream;
		this.basisComparator = basisComparator;
	}
	
	public int compare(M arg0, M arg1) {
		double filtration0 = stream.getFiltrationValue(arg0);
		double filtration1 = stream.getFiltrationValue(arg1);
		
		if (filtration0 < filtration1) {
			return -1;
		} else if (filtration0 > filtration1) {
			return 1;
		} else {
			return this.basisComparator.compare(arg0, arg1);
		}
	}
}
