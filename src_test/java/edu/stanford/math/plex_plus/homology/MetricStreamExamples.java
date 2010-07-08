package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex_plus.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex_plus.math.metric.landmark.MaxMinLandmarkSelector;

public class MetricStreamExamples {
	
	public static AbstractFilteredStream<Simplex> getLazyWitnessTorus(int points, int landmarkPoints, double r, double R, double maxFiltrationValue) {
		SearchableFiniteMetricSpace<double[]> metricSpace = EuclideanMetricSpaceExamples.getRandomTorusPoints(points, r, R);
		
		LandmarkSelector<double[]> selector = new MaxMinLandmarkSelector<double[]>(metricSpace, landmarkPoints);
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, selector, 3, maxFiltrationValue);
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Simplex> getLazyWitnessTorus() {
		return getLazyWitnessTorus(1000, 300, 1, 2, 0.4);
	}
	
	public static AbstractFilteredStream<Simplex> getLazyWitnessSphere(int points, int landmarkPoints, int dimension, double maxFiltrationValue) {		
		SearchableFiniteMetricSpace<double[]> metricSpace = EuclideanMetricSpaceExamples.getRandomSpherePoints(points, dimension + 1);
		
		LandmarkSelector<double[]> selector = new MaxMinLandmarkSelector<double[]>(metricSpace, landmarkPoints);
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, selector, dimension + 1, maxFiltrationValue);
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Simplex> getLazyWitness2Sphere() {
		return getLazyWitnessSphere(1000, 100, 2, 0.2);
	}
}
