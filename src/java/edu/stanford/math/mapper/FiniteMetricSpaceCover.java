package edu.stanford.math.mapper;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;
import edu.stanford.math.plex4.streams.filter.IntFilterFunction;
import gnu.trove.TIntHashSet;

import java.util.Iterator;

public class FiniteMetricSpaceCover implements Iterable<TIntHashSet> {
	private IntFilterFunction filter;
	private AbstractIntMetricSpace metricSpace;

	public FiniteMetricSpaceCover(IntFilterFunction filter, AbstractIntMetricSpace metricSpace) {
		super();
		this.filter = filter;
		this.metricSpace = metricSpace;
	}

	public Iterator<TIntHashSet> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
