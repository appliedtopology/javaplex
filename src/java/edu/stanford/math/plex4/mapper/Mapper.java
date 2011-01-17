package edu.stanford.math.plex4.mapper;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.UndirectedListGraph;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;

public class Mapper {
	AbstractUndirectedGraph compute(double[][] points, FilterFunction filter, int numIntervals, double overlap) {
		int n = points.length;
		AbstractUndirectedGraph graph = new UndirectedListGraph(n);
		
		double[] filterValues = filter.evaluate(points);
		double minFilterValue = DoubleArrayMath.min(filterValues);
		double maxFilterValue = DoubleArrayMath.max(filterValues);
		
		double intervalLength = (maxFilterValue - minFilterValue) / (1 + (numIntervals - 1) * (1 - overlap));
		
		TIntObjectHashMap<TIntHashSet> intervalPreimages = new TIntObjectHashMap<TIntHashSet>();
		
		for (int i = 0; i < numIntervals; i++) {
			double start = minFilterValue + i * (1 - overlap) * intervalLength;
			double end = start + intervalLength;
			
			for (int pointIndex = 0; pointIndex < filterValues.length; pointIndex++) {
				if (filterValues[pointIndex] >= start && filterValues[pointIndex] <= end) {
					
				}
			}
		}
		
		return graph;
	}
}


