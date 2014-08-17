package edu.stanford.math.mapper;

import java.io.IOException;
import java.util.List;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.graph.AbstractWeightedUndirectedGraph;
import edu.stanford.math.plex4.graph.io.GraphDotWriter;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.streams.filter.RandomProjectionFilterFunction;
import gnu.trove.TIntHashSet;

public class MapperTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 1000;

		double[][] points = PointCloudExamples.getRandomCirclePoints(n);

		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);

		RandomProjectionFilterFunction filter = new RandomProjectionFilterFunction(points);

		MapperSpecifier specifier = MapperSpecifier.create().numIntervals(6).overlap(0.4).numHistogramBuckets(6);

		List<TIntHashSet> partialClustering = MapperPipeline.producePartialClustering(filter, metricSpace, specifier);
		
		AbstractWeightedUndirectedGraph graph = MapperPipeline.intersectionGraph(partialClustering);

		System.out.println(graph);
		
		GraphDotWriter writer = new GraphDotWriter();
		
		try {
			writer.writeToFile(graph, "out.dot");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
