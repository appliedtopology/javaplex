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
		int filtration0 = stream.getFiltrationIndex(arg0);
		int filtration1 = stream.getFiltrationIndex(arg1);
		
		if (filtration0 < filtration1) {
			return -1;
		} else if (filtration0 > filtration1) {
			return 1;
		} else {
			return this.basisComparator.compare(arg0, arg1);
			//return 0;
		}
	}
}
