package edu.stanford.math.plex_plus.homology.simplex_streams;

import edu.stanford.math.plex_plus.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex_plus.math.metric.interfaces.FiniteMetricSpace;
import edu.stanford.math.plex_plus.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class LazyWitnessStream<T> extends MaximalStream {

	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final FiniteMetricSpace<T> metricSpace;
	
	protected final LandmarkSelector<T> landmarkSelector;
	
	/**
	 * Constructor which initializes the complex with a metric space.
	 * 
	 * @param metricSpace the metric space to use in the construction of the complex
	 * @param maxDistance the maximum allowable distance
	 * @param maxDimension the maximum dimension of the complex
	 */
	public LazyWitnessStream(FiniteMetricSpace<T> metricSpace, int maxDimension, LandmarkSelector<T> landmarkSelector) {
		super(maxDimension);
		ExceptionUtility.verifyNonNull(metricSpace);
		this.metricSpace = metricSpace;
		this.landmarkSelector = landmarkSelector;
	}

	@Override
	protected UndirectedWeightedListGraph constructEdges() {
		int n = this.metricSpace.size();
		int m = this.landmarkSelector.size();
				
		UndirectedWeightedListGraph graph = new UndirectedWeightedListGraph(n);
		
		/*
		 * Let n be the number of points in the metric space, and m the number of 
		 * landmark points. Let D be the m x n matrix of distances between the set
		 * of landmark points, and the set of all points in the metric space.
		 * 
		 * To construct the 1-skeleton we use the following definition:
		 * 
		 * [ab] belongs to the Lazy Witness complex iff there is a data point 0 <= i < n
		 * such that D(a, i) and D(b, i) are the two smallest entries in the i-th column
		 * of the distance matrix D
		 * 
		 */
		
		/**
		 * This is will hold the i-th column in the m x n distance matrix.
		 */
		double[] distanceMatrixColumn = new double[m];
		
		// iterate through the columns
		for (int i = 0; i < n; i++) {
			// form the i-th column of the distance matrix;
			for (int k = 0; k < m; k++) {
				distanceMatrixColumn[k] = this.metricSpace.distance(i, this.landmarkSelector.getLandmarkIndex(k));
			}
			
			// get the minimum indices within the set of landmark points
			int[] minimumIndices = ArrayUtility.getMinimumIndices(distanceMatrixColumn, 2);
			
			// convert the landmark indices to indices within metric space
			int a = this.landmarkSelector.getLandmarkIndex(minimumIndices[0]);
			int b = this.landmarkSelector.getLandmarkIndex(minimumIndices[1]);
			double distance = this.metricSpace.distance(a, b);
			
			graph.addEdge(a, b, distance);
		}
		
		return graph;
	}

}
